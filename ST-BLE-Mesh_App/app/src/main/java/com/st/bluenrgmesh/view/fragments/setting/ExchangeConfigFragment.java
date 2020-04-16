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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.cloud.SyncAndInviteUserFragment;
import com.st.bluenrgmesh.view.fragments.cloud.JoinAndRegisterNetworkFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import org.json.JSONObject;

import java.util.ArrayList;


public class ExchangeConfigFragment extends BaseFragment {

    public AppDialogLoader loader;
    Button via_cloud_button;
    Button import_config_button;
    Button export_config_button;
    Button delete_config_button;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.import_export_config_new, container, false);

        Utils.updateActionBarForFeatures(getActivity(), new ExchangeConfigFragment().getClass().getName());
        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
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

        import_config_button = (Button) view.findViewById(R.id.import_config_button);
        import_config_button.setOnClickListener(new View.OnClickListener() {
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
                        },500);
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
            @Override
            public void onClick(View view) {
                if (vibrator != null) vibrator.vibrate(40);

                ((MainActivity)getActivity()).onBackPressed();
                Utils.sendDataOverMail(getActivity());
            }
        });

        delete_config_button = (Button) view.findViewById(R.id.delete_config_button);
        delete_config_button.setOnClickListener(new View.OnClickListener() {
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
}
