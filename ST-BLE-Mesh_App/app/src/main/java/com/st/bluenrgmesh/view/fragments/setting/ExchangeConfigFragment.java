/**
 * *****************************************************************************
 *
 * @file ExchangeConfigFragment.java
 * @author BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief User Application file
 * *****************************************************************************
 * @attention <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3. Neither the name of STMicroelectronics nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 * <p>
 * *****************************************************************************
 */

package com.st.bluenrgmesh.view.fragments.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmes.CustomUtilities;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AllConstants;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.cloud.SyncAndInviteUserFragment;
import com.st.bluenrgmesh.view.fragments.cloud.JoinAndRegisterNetworkFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;
import com.st.bluenrgmesh.voicereceiver.BluetoothMeshTransferService;
import com.st.bluenrgmesh.voicereceiver.Constants;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import static com.st.BlueSTSDK.Utils.BlePermissionHelper.REQUEST_ENABLE_BT;


public class ExchangeConfigFragment extends BaseFragment {

    public AppDialogLoader loader;
    Button via_cloud_button;
    Button import_config_button;
    Button export_config_button;
    Button delete_config_button;
    Button discovery_enable_button;
    private View view;
    Context context;
    public BluetoothMeshTransferService mTransferService = null;
    BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    boolean isSend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.import_export_config_new, container, false);
        context = container.getContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Utils.updateActionBarForFeatures(getActivity(), new ExchangeConfigFragment().getClass().getName());
        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (mBluetoothAdapter == null) {
            return;
        }
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mTransferService == null) {
            setupChat();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!(AllConstants.deviceAddress.isEmpty())){
            connectDevice(AllConstants.deviceAddress,true);
        }
        else{
            if (mTransferService != null) {
                // Only if the state is STATE_NONE, do we know that we haven't started already
                if (mTransferService.getState() == BluetoothMeshTransferService.STATE_NONE) {
                    // Start the Bluetooth chat services
                    mTransferService.start();
                }
            }
        }
    }

    private void setupChat() {
        // Initialize the array adapter for the conversation thread
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        mTransferService = new BluetoothMeshTransferService(activity, mHandler);
    }

    @SuppressLint("MissingPermission")
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
        else{
            Toast.makeText(context,"Device is now discoverable",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessageString(String message) {
        // Check that we're actually connected before trying anything
        if (mTransferService.getState() != BluetoothMeshTransferService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), "Device not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            Log.d("Aman Saxena Connected",message);
            Log.d("Aman Saxena Connected",String.valueOf(send.length));
            mTransferService.write(send);
        }
    }

    private void initUi() {

        final Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        via_cloud_button = (Button) view.findViewById(R.id.via_cloud_button);
        via_cloud_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isUserLoggedIn(getActivity()))
                {
                    //Utils.moveToFragment(getActivity(), new CloudInteractionFragment(), null, 0);
                    ArrayList<Nodes> nodes = new ArrayList<>();
                    try {
                        if(((MainActivity)getActivity()).meshRootClass.getNodes() != null && ((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0)
                        {
                            for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {
                                boolean isNodeIsProvisioner = false;
                                for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getProvisioners().size(); j++) {
                                    if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity)getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                                        isNodeIsProvisioner = true;
                                        break;
                                    }
                                }

                                if (!isNodeIsProvisioner) {
                                    nodes.add(((MainActivity)getActivity()).meshRootClass.getNodes().get(i));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    if(Utils.isUserRegisteredToDownloadJson(getActivity()))
                    {
                        Utils.moveToFragment(getActivity(), new SyncAndInviteUserFragment(), null, 0);
                    }
                    else
                    {
                        call_getNetworks_API(Utils.getUserLoginKey(getActivity()));
                    }
                }
                else
                {
                    if(getResources().getBoolean(R.bool.bool_isCloudFunctionality))
                    {
                        Utils.showToast(getActivity(), "Kindly first login yourself.");
                        Utils.moveToFragment(getActivity(), new LoginDetailsFragment(), null, 0);
                    }
                    else
                    {
                        Utils.showPopUpForMessage(getActivity(),getString(R.string.str_error_Gatt_Not_Responding));
                    }
                }
            }
        });

        discovery_enable_button = (Button) view.findViewById(R.id.discovery_enable_button);
        discovery_enable_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ensureDiscoverable();
                isSend = false;
            }
        });

        import_config_button = (Button) view.findViewById(R.id.import_config_button);
        import_config_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (vibrator != null) vibrator.vibrate(40);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning!");
                builder.setMessage(R.string.warning);
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        loader.show();
                        Utils.showFileChooser(getActivity(), false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                loader.hide();
                                ((MainActivity)getActivity()).onBackPressed();
                            }
                        },1000);
                    }
                });
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        export_config_button = (Button) view.findViewById(R.id.export_config_button);
        export_config_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (vibrator != null) vibrator.vibrate(40);

                isSend = true;
                Intent serverIntent = new Intent(getActivity(), BluetoothConnectFragment.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

               /* ((MainActivity)getActivity()).onBackPressed();
                sendDataToOtherDevice();*/
               /* Utils.sendDataOverMail(getActivity());*/
            }
        });

        delete_config_button = (Button) view.findViewById(R.id.delete_config_button);
        delete_config_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (vibrator != null) vibrator.vibrate(40);

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_delete_config);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button Delete_Config = (Button) dialog.findViewById(R.id.Delete_Config);
                Delete_Config.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loader.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                removeNodes();
                                //meshRootClass.setNodes(null);

                                ((MainActivity)getActivity()).meshRootClass.setGroups(null);
                                Utils.setProxyNode(getActivity(), null);
                                Utils.setUserRegisteredToDownloadJson(getActivity(),"false");

                                String s = ParseManager.getInstance().toJSON(((MainActivity)getActivity()).meshRootClass);
                                Utils.setBLEMeshDataToLocal(getActivity(), s);
                                ((MainActivity)getActivity()).updateJsonData();
                                dialog.dismiss();
                                //update provisioned list
                                ((MainActivity)getActivity()).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
                                ((MainActivity)getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
                                loader.hide();
                                ((MainActivity)getActivity()).onBackPressed();

                            }
                        },500);
                    }
                });

                Button No_Delete_Config = (Button) dialog.findViewById(R.id.No_Delete_Config);
                No_Delete_Config.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

    }

    private void removeNodes() {

        try {

            ArrayList<Nodes> provisionedDevices = new ArrayList<>();

            for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {
                boolean isNodeIsProvisioner = false;
                for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getProvisioners().size(); j++) {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity)getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                        isNodeIsProvisioner = true;
                        break;
                    }
                }
                if (!isNodeIsProvisioner) {
                    //((MainActivity)getActivity()).meshRootClass.getNodes().remove(i);
                    provisionedDevices.add(((MainActivity)getActivity()).meshRootClass.getNodes().get(i));
                }
            }

            if(provisionedDevices.size() > 0)
            {
                for (int j = 0; j < provisionedDevices.size(); j++) {

                    try {
                        for (int k = 0; k < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); k++) {

                            if (provisionedDevices.get(j).getUUID().equalsIgnoreCase(((MainActivity) getActivity()).meshRootClass.getNodes().get(k).getUUID())) {

                                ((MainActivity) getActivity()).meshRootClass.getNodes().remove(k);
                                break;
                            }
                        }
                    }catch (Exception e){}
                }
            }
        }catch (Exception e){}
    }

    public void call_getNetworks_API(String userLoginKey) {

        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_getNetworks);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", userLoginKey);

        } catch (Exception e) {
            Utils.ERROR("Error while creating json request : " + e.toString());
        }
        MyJsonObjectRequest jsonObjReq = new MyJsonObjectRequest(
                false,
                getActivity(),
                Request.Method.POST,
                url,
                requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            return;
                        }
                        Utils.DEBUG("API Join_Network onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if(cloudResponseData.getStatusCode() == 0)
                            {
                                if(cloudResponseData.getResponseMessage() != null || !cloudResponseData.getResponseMessage().isEmpty())
                                {
                                    Utils.moveToFragment(getActivity(), new JoinAndRegisterNetworkFragment(), cloudResponseData.getResponseMessage(), 0);
                                }
                            }
                            else
                            {
                                //error : status code 110
                                Utils.showToast(getActivity(), cloudResponseData.getErrorMessage());
                            }

                        }catch (Exception e){}
                        loader.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ERROR("Error: " + error);
                //Utils.showToast(getActivity(), getString(R.string.string_common_error_message));
                loader.hide();
            }
        }
        );
        // Adding request to request queue
        UserApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void sendDataToOtherDevice(){
        CustomUtilities customUtilities = new CustomUtilities();
        customUtilities.createFileStorage(context,"");

        String filePath = context.getFilesDir().toString() + "/stblueNrg/" + context.getResources().getString(R.string.FILE_bluenrg_mesh_json);
        File file = new File(filePath);

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BLE Mesh Config File");
        //   intent.setComponent(ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTransferService != null) {
            mTransferService.stop();
            AllConstants.deviceAddress = "";
            AllConstants.meshedString = "";
        }
    }

    private void connectDevice(String address, boolean secure) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        AllConstants.deviceAddress = "";
        mTransferService.connect(device, secure);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothMeshTransferService.STATE_CONNECTED:
                            if(isSend){
                                String meshedStringData = Utils.filterJsonObject(context);
                                sendMessageString(meshedStringData);
                            }
                            break;
                        case BluetoothMeshTransferService.STATE_CONNECTING:
                            break;
                        case BluetoothMeshTransferService.STATE_LISTEN:
                        case BluetoothMeshTransferService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Log.d("Aman Saxena Write",String.valueOf(writeBuf.length));

                    Log.d("Aman Saxena Write",writeMessage);
                    Toast.makeText(context,"Mesh Config File Transfered",Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    Log.d("Aman Saxena Read",String.valueOf(readBuf.length));
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("Aman Saxena Read",readMessage);


                    String temp = AllConstants.meshedString;
                    AllConstants.meshedString = temp + readMessage;
                    readMessage = AllConstants.meshedString;

                    CustomUtilities customUtilities = new CustomUtilities();
                    customUtilities.createFileStorage(context,readMessage);

                    String filePath = context.getFilesDir().toString() + "/stblueNrg/" + context.getResources().getString(R.string.FILE_bluenrg_mesh_json);
                    File source = new File(filePath);

                    String descPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
                    File desc = new File(descPath);
                    try
                    {
                        moveFile(source,desc);
                        Toast.makeText(context, "Mesh Config File Received", Toast.LENGTH_SHORT).show();
                    }

                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public static void moveFile(File srcFileOrDirectory, File desFileOrDirectory) throws IOException {
        File newFile = new File(desFileOrDirectory, srcFileOrDirectory.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel();
             FileChannel inputChannel = new FileInputStream(srcFileOrDirectory).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            deleteRecursive(srcFileOrDirectory);
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);
        fileOrDirectory.delete();
    }
}
