/**
 * *****************************************************************************
 *
 * @file ConnectionSetupFragment.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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
package com.st.bluenrgmesh.view.fragments.setting.configuration;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.msi.moble.Capabilities;
import com.msi.moble.CustomProvisioning;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.DeviceEntry;
import com.st.bluenrgmesh.GroupEntry;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.callbacks.provisioning.ConnectionCallbacks;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.capabilities.InformationOOB;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.AppSingletonOOBDialog;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import java.util.ArrayList;
import java.util.Collections;

public class ConnectionSetupFragment extends BaseFragment {

    public static final String DEFAULT_NAME = "New Node";
    private int STATE_INVITATION_0 = 0;
    private int STATE_MTU_PACKET_CHECK_1 = 1;
    private int STATE_CAPABILITIES_SELECTION_2 = 2;
    private int STATE_PUBLIC_KEY_EXCHANGE_3 = 3;
    private int STATE_AUTHENTICATION_4 = 4;
    private int STATE_DISTRIBUTION_5 = 5;
    private int STATE_DEVICE_AUTENTICATION_COMPLETE_6 = 6;
    private int STATE_DISCONNECTION_7 = 7;
    private int STATE_PROVISIONING_COMPLETE_8 = 8;
    private String mAutoName;
    private View view;
    private AppDialogLoader loader;
    private TextView txtNormalProvisioning;
    private Nodes nodeSelected;
    private Dialog provisioningDialog;
    private TextView txtMsgDialog;
    private TextView txtPercentage;
    private SeekBar progressHorizontal;
    private Button butOk;
    private DeviceEntry mAutoDevice;
    private int nodeNumber ;
    private String mAutoAddress = null;
    private Nodes currentNode;
    private ArrayList<Element> newElements;
    private TextView txtUUID,txtName,txtUnicastAddress;
    private TextView txtInputOOB;
    private TextView txtOutputOOB;
    private TextView txtStaticOOB;
    private Capabilities capabilities;
    private TextView txtEncription;
    private TextView txtCapabilitiesInfo;
    private CheckBox txtPublicKey;
    private Capabilities mainCapabilities;
    private Dialog dialog;
    private static int provisioningCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_provisioning_setup, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUI();

        return view;
    }

    private void initUI() {

        nodeSelected = (Nodes) getArguments().getSerializable(getString(R.string.key_serializable));
        txtUUID = (TextView) view.findViewById(R.id.txtUUID);
        txtUUID.setText(nodeSelected.getUUID()+"");
            txtUnicastAddress  = (TextView) view.findViewById(R.id.txtUnicastAddress);
        txtName = (TextView) view.findViewById(R.id.txtName);

        txtNormalProvisioning = (TextView) view.findViewById(R.id.txtNormalProvisioning);
        txtNormalProvisioning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startProvisioning(nodeSelected);
                if(provisioningDialog != null)
                {
                    provisioningDialog.show();
                }

                ((MainActivity)getActivity()).mIdentifyDialog.createDialog(((MainActivity)getActivity()).identifier);
                //((MainActivity)getActivity()).identifier.setIdentified(true, null);
            }
        });

        txtInputOOB = (TextView) view.findViewById(R.id.txtInputOOB);
        txtOutputOOB = (TextView) view.findViewById(R.id.txtOutputOOB);
        txtStaticOOB = (TextView) view.findViewById(R.id.txtStaticOOB);
        txtCapabilitiesInfo = (TextView) view.findViewById(R.id.txtCapabilitiesInfo);
        txtEncription = (TextView) view.findViewById(R.id.txtEncription);
        txtPublicKey = (CheckBox) view.findViewById(R.id.txtPublicKey);

        txtPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPublicKey.isChecked() && dialog == null && !MainActivity.isPublicKeyEnabled){
                    mainCapabilities.setOobTypeSelected(0);
                    showPopUp_OOB_PublicKey_Authentication(getActivity(), "",
                            nodeSelected.getmOOBInformation(), mainCapabilities);
                }
                else
                {
                    dialog = null;
                    MainActivity.isPublicKeyEnabled = false;
                }
            }
        });

        txtCapabilitiesInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.moveToFragment(getActivity(), new CapabilityInfoFragment(), capabilities, 0);
            }
        });

        txtInputOOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.isPublicKeyEnabled = false;
                mainCapabilities.setOobTypeSelected(1);
                Utils.moveToFragment(getActivity(), new CapabilitySelectionFragment(), mainCapabilities, 0);
            }
        });

        txtOutputOOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MainActivity.isPublicKeyEnabled = false;
                mainCapabilities.setOobTypeSelected(2);
                Utils.moveToFragment(getActivity(), new CapabilitySelectionFragment(), mainCapabilities, 0);
            }
        });

        txtStaticOOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            200);

                    return;
                }

                //MainActivity.isPublicKeyEnabled = false;
                mainCapabilities.setOobTypeSelected(3);
                Utils.moveToFragment(getActivity(), new CapabilitySelectionFragment(), mainCapabilities, 0);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startProvisioning(nodeSelected);
            }
        });
    }

    public void onGettingCapabilities()
    {
        if(getResources().getBoolean(R.bool.bool_isAutoProvisioning) && Utils.isAutoProvisioning(getActivity()))
        {
            capabilities = ((MainActivity) getActivity()).identifier.getCapabilities();
            updateCapabilityUI(capabilities);
            ((MainActivity)getActivity()).mIdentifyDialog.createDialog(((MainActivity)getActivity()).identifier);
        }
        else
        {
            if(provisioningDialog != null && provisioningDialog.isShowing())
            {
                provisioningDialog.hide();
            }

            if(((MainActivity)getActivity()).identifier != null)
            {
                //txtNoOOB.setVisibility(View.GONE);
                capabilities = ((MainActivity) getActivity()).identifier.getCapabilities();
                updateCapabilityUI(capabilities);
            }
            else
            {
                // txtNoOOB.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateCapabilityUI(Capabilities capabilities) {
try {
    mainCapabilities = capabilities;
    txtEncription.setText(capabilities.getAlgorithms() == 1 ? "FIPS P-256 Elliptic Curve" : "Reserved");
    txtStaticOOB.setVisibility(capabilities.getStaticOOBTypes() == 1 ? View.VISIBLE : View.GONE);
    txtInputOOB.setVisibility(capabilities.getInputOOBActions() != 0 ? View.VISIBLE : View.GONE);
    txtOutputOOB.setVisibility(capabilities.getOutputOOBActions() != 0 ? View.VISIBLE : View.GONE);
    txtPublicKey.setVisibility(capabilities.getPkTypes()[0] == 49 || capabilities.getPkTypes().length > 1 ? View.VISIBLE : View.GONE);
    txtPublicKey.setChecked(txtPublicKey.getVisibility() == View.VISIBLE ? (/*capabilities.getPkTypes().length > 1*/MainActivity.isPublicKeyEnabled ? true : false) : false);

    txtUnicastAddress.setText(((MainActivity) getActivity()).mAutoDevice.getAddress().toString());
    txtName.setText(((MainActivity) getActivity()).mAutoDevice.getName());
}catch (Exception e){

}
    }

    public boolean isOOBSupport()
    {
        if (capabilities.getPkTypes().length == 0 && capabilities.getInputOOBActions() == 0 && capabilities.getOutputOOBActions() == 0 && capabilities.getStaticOOBTypes() != 1)
            return true;

        return false;
    }

    public void startProvisioning(Nodes node) {
        provisioningCount = 0;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (((MainActivity)getActivity()).meshRootClass.getGroups() == null || ((MainActivity)getActivity()).meshRootClass.getGroups().size() == 0) {
                        Utils.addNewGroupToJson(getActivity(), mobleAddress.groupAddress(GroupEntry.LIGHTS_GROUP), getString(R.string.str_default_group_label));
                    }
                } catch (Exception e) { }
                ((MainActivity)getActivity()).updateJsonData();
            }
        });
        //((MainActivity)getActivity()).nodeSelected = node;
        //nodeSelected = node;
        boolean alreadyProvisioned = isAlreadyProvisioned();
        if(!alreadyProvisioned)
        {
            ((MainActivity)getActivity()).element_counter = 1;
            //((MainActivity)getActivity()).unAdviseCallbacks();
            Utils.setSubscriptionAddressOnProvsioning(getActivity(), "");
            Utils.setPublicationAddressOnProvsioning(getActivity(), "");
            configureDevice(node.getAddress());
        }
        else
        {
            Utils.showToast(getActivity(), "Already Connected. Configure your node.");
            ((MainActivity)getActivity()).onBackPressed();
        }
    }

    public void configureDevice(String bt_addr) {
        ((MainActivity)getActivity()).isProvisioningProcessLive = true;
        ((MainActivity)getActivity()).mAutoName = DEFAULT_NAME;
        mAutoName = DEFAULT_NAME;
        setAutoParameters(bt_addr);
        //mProgress.show(MainActivity.this, "Provisioning . . . : ", true);
        updateProgressBar(0);
        //((MainActivity)getActivity()).mHandler.sendEmptyMessage(((MainActivity)getActivity()).MSG_CONFIG);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                autoConfigure();
            }
        });
    }

    public void autoConfigure() {

        ((MainActivity)getActivity()).mSettings = MainActivity.network.getSettings();
        if (/*!Utils.checkConfiguration(this) ||*/ (null == ((MainActivity)getActivity()).mAutoAddress)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mProgress.hide();
                    provisioningDialog.hide();
                    Utils.showToast(getActivity(),"Network does not exist. Please create network");
                    UserApplication.trace("Network does not exists");
                }
            });
        } else {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)getActivity()).mProvisioningInProgress = true;
                    ((MainActivity)getActivity()).mcp = new CustomProvisioning(false);

                    if (getResources().getBoolean(R.bool.bool_isFasterProvisioningSupported)) {
                        ((MainActivity)getActivity()).mcp = new CustomProvisioning(((MainActivity)getActivity()).mCustomGroupId, true);
                    }
                    ((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("provision started==>" + ((MainActivity)getActivity()).mAutoAddress, LoggerConstants.TYPE_SEND);

                    ConnectionCallbacks.isCapabilitiesAssigned = false;
                    ((MainActivity)getActivity()).mSettings.provision(getActivity(),
                            ((MainActivity)getActivity()).mAutoAddress,
                            ((MainActivity)getActivity()).mAutoDevice.getAddress(),
                            ((MainActivity)getActivity()).PROVISIONING_IDENTIFY_DURATION,
                            ConnectionCallbacks.mProvisionCallback,
                            ConnectionCallbacks.mCapabilitiesLstnr,
                            ConnectionCallbacks.mProvisionerStateChanged,
                            ((MainActivity)getActivity()).COMPLETION_DELAY,
                            ((MainActivity)getActivity()).mcp);
                            /*((MainActivity)getActivity()).onOOB_Output*/
                }
            });
        }
    }

    private void setAutoParameters(final String addr) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DeviceEntry de = ((UserApplication) getActivity().getApplication()).mConfiguration.getDevice(addr);
                if (de != null) {
                    ((MainActivity)getActivity()).mAutoDevice = de;
                    mAutoDevice = de;
                } else {
                    ((MainActivity)getActivity()).updateJsonData();

                    try {
                        Collections.sort(((MainActivity)getActivity()).meshRootClass.getNodes());
                    } catch (Exception e) {
                    }
                    nodeNumber = Utils.getNextNodeNumber(((MainActivity)getActivity()).meshRootClass.getNodes(), ((MainActivity)getActivity()).meshRootClass);
                    ((MainActivity)getActivity()).nodeNumber = nodeNumber;
                    ((MainActivity)getActivity()).name = String.format("%s %02d", ((MainActivity)getActivity()).mAutoName, ((MainActivity)getActivity()).nodeNumber);
                    //here : (next node address) = (next node element number) = (missing/deleted node element number)
                    int min = Utils.getNextElementNumber(getActivity(), ((MainActivity)getActivity()).meshRootClass.getNodes());
                    if (min == 0) {
                        Utils.showToast(getActivity(),getString(R.string.network_is_full));
                        return;
                    }
                    mAutoDevice = new DeviceEntry(((MainActivity)getActivity()).name, mobleAddress.deviceAddress(min));
                    ((MainActivity)getActivity()).mAutoDevice = mAutoDevice;
                }
                ((MainActivity)getActivity()).mAutoAddress = addr;
                mAutoAddress = addr;
            }
        });

    }

    public boolean isAlreadyProvisioned() {
        boolean isAlreadyProvisioned = false;
        if(((MainActivity)getActivity()).meshRootClass != null && ((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0)
        {
            for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {
                try {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getAddress().equalsIgnoreCase(nodeSelected.getAddress())) {
                        isAlreadyProvisioned = true;
                    }
                }catch (Exception e){}
            }
        }

        return isAlreadyProvisioned;
    }

    @Override
    public void onPause() {
        if(provisioningDialog != null)
        {
            provisioningDialog.dismiss();
        }
        super.onPause();
    }

    private void initProvisioningDialog() {

        if(provisioningDialog == null) {
            provisioningDialog = new Dialog(getActivity());
            provisioningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            provisioningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            provisioningDialog.setCanceledOnTouchOutside(false);
            provisioningDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            provisioningDialog.setContentView(R.layout.dialog_provisioningsteps);
            txtMsgDialog = (TextView) provisioningDialog.findViewById(R.id.txtMsg);
            txtPercentage = (TextView) provisioningDialog.findViewById(R.id.txtPercentage);
            butOk = (Button) provisioningDialog.findViewById(R.id.butOk);
            butOk.setVisibility(View.GONE);
            butOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).isProvisioningProcessLive = false;
                    provisioningDialog.dismiss();
                    //check proxy connection
                    ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Loading Composition Data..", true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.moveToFragment(((MainActivity) getActivity()), new LoadConfigFeaturesFragment(), currentNode, 0);
                        }
                    }, 500);
                }
            });
        /*progressHorizontal = (ProgressBar) provisioningDialog.findViewById(R.id.progressHorizontal);
        txtMsgDialog.setText("");*/
            progressHorizontal = (SeekBar) provisioningDialog.findViewById(R.id.seekbar);
            progressHorizontal.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            if (!provisioningDialog.isShowing()) {
                if (((MainActivity) getActivity()) != null) {
                    ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            provisioningDialog.show();
                        }
                    });
                }
            }

            provisioningDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //terminateProvisioning("Provisioning terminated by user.");
                    provisioningDialog.dismiss();
                    ((MainActivity)getActivity()).onBackPressed();
                }
            });

        }
    }

    public void updateProgressBar(int percentage) {

        /*if(provisioningCount > percentage)
        {
            //when OOB get wrong // when provisioning get stuck
            txtPercentage.setText("Wrong Attempt.");
            txtMsgDialog.setText("Wrong user attempt or something went wrong.");
            terminateProvisioning("Wrong user attempt or something went wrong.");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    provisioningDialog.dismiss();
                    ((MainActivity)getActivity()).onBackPressed();
                }
            }, 3000);

        }*/

        provisioningCount = percentage;
        int percent = percentage*10;
        if(STATE_INVITATION_0 == percentage)
        {
            Utils.DEBUG(">>Provisioning States :: STATE_INVITATION_0");
            initProvisioningDialog();
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Invitation State...");
            progressHorizontal.setProgress(percent);
        }
        else if(STATE_MTU_PACKET_CHECK_1 == percentage)
        {
            Utils.DEBUG(">>Provisioning States :: STATE_MTU_PACKET_CHECK_1");
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("MTU Packet Check State...");
            progressHorizontal.setProgress(percent);
        }
        else if(STATE_CAPABILITIES_SELECTION_2 == percentage)
        {
            Utils.DEBUG(">>Provisioning States :: STATE_CAPABILITIES_SELECTION_2");
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Capabilities Selection State...");
            progressHorizontal.setProgress(percent);

        }
        else if(STATE_PUBLIC_KEY_EXCHANGE_3 == percentage)
        {
            //(new AppSingletonOOBDialog()).dissmissOOBDialog();
            MainActivity.isPublicKeyEnabled = false;
            Utils.DEBUG(">>Provisioning States :: STATE_PUBLIC_KEY_EXCHANGE_3");
            if(((MainActivity)getActivity()) != null)
            {
                if(provisioningDialog != null)
                {
                    provisioningDialog.show();
                }
                else
                {
                    initProvisioningDialog();
                }
            }
            progressHorizontal.setVisibility(View.VISIBLE);
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Public Key Exchange...");
            progressHorizontal.setProgress(percent);

        }
        else if(STATE_AUTHENTICATION_4 == percentage)
        {
            //(new AppSingletonOOBDialog()).dissmissOOBDialog();
            Utils.DEBUG(">>Provisioning States :: STATE_AUTHENTICATION_4");
            if(((MainActivity)getActivity()) != null)
            {
                if(provisioningDialog != null)
                {
                    provisioningDialog.show();
                }
                else
                {
                    initProvisioningDialog();
                }
            }
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Authentication State...");
            progressHorizontal.setProgress(percent);
        }
        else if(STATE_DISTRIBUTION_5 == percentage)
        {
            //ConnectionCallbacks.isCapabilitiesAssigned = false;
            (new AppSingletonOOBDialog()).dissmissOOBDialog();
            Utils.DEBUG(">>Provisioning States :: STATE_DISTRIBUTION_5");
            //OOB Authentication Completed. Hide the OOB Dialog
            //(new AppSingletonOOBDialog()).dissmissOOBDialog();
            if(((MainActivity)getActivity()) != null)
            {
                if(provisioningDialog != null)
                {
                    provisioningDialog.show();
                }
                else
                {
                    initProvisioningDialog();
                }
            }
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Distribution State...");
            progressHorizontal.setProgress(percent);
        }
        else if(STATE_DEVICE_AUTENTICATION_COMPLETE_6 == percentage)
        {
            Utils.DEBUG(">>Provisioning States :: STATE_DEVICE_AUTENTICATION_COMPLETE_6");
            percent = 80;
            txtPercentage.setText(String.valueOf(percent) + " %");
            txtMsgDialog.setText("Authentication Complete...");
            progressHorizontal.setProgress(percent);
        }
        else if(STATE_DISCONNECTION_7 == percentage)
        {
            MainActivity.isPublicKeyEnabled = false;
            Utils.DEBUG(">>Provisioning States :: STATE_DISCONNECTION_7");
            percent = 100;
            progressHorizontal.setProgress(percent);
            txtPercentage.setText(String.valueOf(percent) + " %");
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)getActivity()).isProvisioningProcessLive = false;
                    setCurrentNode();
                }
            });

            txtMsgDialog.setText("Provisioning Complete." + "\n" + getString(R.string.str_device_configuration_msg1));
            txtMsgDialog.setTextColor(getResources().getColor(R.color.ST_DARK_GREEN));
            txtMsgDialog.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blinking));
            loader.show();
            //butOk.setVisibility(View.VISIBLE);
            //provisioningDialog.dismiss();
            //settingCustomProxy();
        }
    }

    public void onProvisioningCompleted()
    {
        provisioningDialog.dismiss();
        loader.dismiss();

        MainActivity.isCustomAppKeyShare = false;
        Nodes clonnedNode = ((MainActivity) getActivity()).currentNode;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).onBackPressed();
                Utils.moveToFragment(((MainActivity) Utils.contextMainActivity), new LoadConfigFeaturesFragment(), clonnedNode, 0);
            }
        }, 200);

    }

    private void settingCustomProxy() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).isCustomProxy = true;
                ((MainActivity) getActivity()).showPopUpForProxy((MainActivity) getActivity(), "Connecting Proxy Device..", true);
                //MainActivity.mSettings.setCustomProxy(provisionedNodes.get(position).getAddress());
                ((MainActivity)getActivity()).mAutoAddress = mAutoAddress;
                //MainActivity.mSettings.stopCustomProxy();
            }
        });
    }

    public void setCurrentNode() {

        String uuid = "";
        if (nodeSelected != null) {
            if (nodeSelected.getAddress().equals(mAutoAddress)) {
                uuid = nodeSelected.getUUID();
            }
        }
        currentNode = new Nodes(nodeNumber);
        currentNode.setUUID(uuid + "");
        currentNode.setDeviceKey(Utils.array2string(mobleNetwork.getaDeviceKey()) + "");
        currentNode.setAddress(mAutoAddress + "");
        currentNode.setConfigured("false");
        currentNode.setConfigComplete(false);
        currentNode.setSubtitle("");
        if (mAutoDevice != null) {
            currentNode.setM_address(mAutoDevice.getAddress() + "");
            currentNode.setName(mAutoDevice.getName());
            currentNode.setTitle(mAutoDevice.getName());
        }
        currentNode.setType("0");

        /*AppKey appKey = new AppKey();
        appKey.setName("Default Key");
        appKey.setKey(Utils.getDefaultAppKeyIndex(getActivity()));
        appKey.setIndex(0);
        appKey.setUpdated(false);
        if(currentNode.getAppKeys() == null)
        {
            ArrayList<AppKey> appKeys = new ArrayList<>();
            appKeys.add(appKey);
            currentNode.setAppKeys(appKeys);
        }*/

        MeshRootClass meshRootClass = null;
        try {
            meshRootClass = (MeshRootClass)((MainActivity)getActivity()).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        currentNode.setNumberOfElements(((MainActivity)getActivity()).elementsSize);
        newElements = Utils.designElementsForCurrentNode(getActivity(), currentNode, meshRootClass);
        currentNode.setElements(newElements);
        Utils.setProvisionedNodeData(getActivity(), currentNode, meshRootClass);
        ((MainActivity)getActivity()).updateJsonData();
        ((MainActivity) getActivity()).currentNode = currentNode;
        Utils.setSelectedCapability(getActivity(), null);

        ((MainActivity)getActivity()).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, currentNode, false, null);
        ((MainActivity)getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
        ((MainActivity)getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, 1, null, true, null);
    }

    public void showPopUp_OOB_PublicKey_Authentication(final Context context, final String responseMessage, int oobInformation, Capabilities capabilities) {

        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_oob_publickeytype);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(!MainActivity.isPublicKeyEnabled)
                {
                    txtPublicKey.setChecked(false);
                }
                dialog.dismiss();
                dialog = null;
            }
        });

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        RadioButton rbElectronicURI = (RadioButton) dialog.findViewById(R.id.rbElectronicURI);
        RadioButton rbMachineReadable = (RadioButton) dialog.findViewById(R.id.rbMachineReadable);
        RadioButton rbBarCode = (RadioButton) dialog.findViewById(R.id.rbBarCode);
        RadioButton rbNFC = (RadioButton) dialog.findViewById(R.id.rbNFC);
        RadioButton rbNmber = (RadioButton) dialog.findViewById(R.id.rbNmber);
        RadioButton rbString = (RadioButton) dialog.findViewById(R.id.rbString);
        RadioButton rbOnBox = (RadioButton) dialog.findViewById(R.id.rbOnBox);
        RadioButton rbInsideBox = (RadioButton) dialog.findViewById(R.id.rbInsideBox);
        RadioButton rbOnPieceOfPaper = (RadioButton) dialog.findViewById(R.id.rbOnPieceOfPaper);
        RadioButton rbInsideManual = (RadioButton) dialog.findViewById(R.id.rbInsideManual);
        RadioButton rbOnDevice = (RadioButton) dialog.findViewById(R.id.rbOnDevice);

        InformationOOB inf = Utils.getTypeOfPublicKeyType_OOBInformation(oobInformation);
        rbElectronicURI.setEnabled(inf.isElectronicURI() ? false : false);
        rbMachineReadable.setEnabled(inf.isMachineReadableCode() ? true : false);
        rbBarCode.setEnabled(inf.isBarCode() ? true : false);
        rbNFC.setEnabled(inf.isNFC() ? false : false);
        rbNmber.setEnabled(inf.isNumber() ? true : true);
        rbString.setEnabled(inf.isString() ? true : true);
        rbOnBox.setEnabled(inf.isOnBox() ? false : false);
        rbInsideBox.setEnabled(inf.isInsideBox() ? false : false);
        rbOnPieceOfPaper.setEnabled(inf.isOnPieceOfPaper() ? false : false);
        rbInsideManual.setEnabled(inf.isInsideManual() ? false : false);
        rbOnDevice.setEnabled(inf.isOnDevice() ? false : false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                AppCompatRadioButton radioOutput = (AppCompatRadioButton)radioGroup.findViewById(i);
                if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_electronic_uri)))
                {
                    //capabilities.setOutputOOBActions(0);
                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_2D_machine_readable)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_bar_code)))
                {
                    Utils.moveToFragment(((MainActivity)context), new CapabilitySelectionFragment(), capabilities, context.getResources().getInteger(R.integer.STATIC_BAR_CODE));
                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_nfc)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_number)))
                {
                    //capabilities.setOobTypeSelected(3);
                    Utils.moveToFragment(((MainActivity)context), new CapabilitySelectionFragment(), capabilities, context.getResources().getInteger(R.integer.STATIC_NUMBER));

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_string)))
                {
                    //capabilities.setOobTypeSelected(3);
                    Utils.moveToFragment(((MainActivity)context), new CapabilitySelectionFragment(), capabilities, context.getResources().getInteger(R.integer.STATIC_STRING));
                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_on_box)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_inside_box)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_on_piece_paper)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_inside_manual)))
                {

                }
                else if(radioOutput.getText().toString().equalsIgnoreCase(context.getString(R.string.str_on_device)))
                {

                }

                dialog.dismiss();

            }
        });

        if(context != null)
        {
            dialog.show();
        }

    }
}
