/**
 * *****************************************************************************
 *
 * @file LoadConfigFeaturesFragment.java
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

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.SensorModelClient;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.DeviceEntry;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.config.ConfigElementRecyclerAdapter;
import com.st.bluenrgmesh.adapter.elementsettings.AppKeyBindListAdapter;
import com.st.bluenrgmesh.callbacks.appkeymanagement.quickbinding.ShareAppKeyCallback;
import com.st.bluenrgmesh.callbacks.configuration.SubscriptionCallback;
import com.st.bluenrgmesh.models.compositiondata.ModelInfo;
import com.st.bluenrgmesh.models.compositiondata.ModelsData;
import com.st.bluenrgmesh.models.compositiondata.ModelsDataByElement;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Features;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.AppSingletonDialog;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.AddGroupFragment;
import com.st.bluenrgmesh.view.fragments.setting.group.GroupSettingFragment;
import com.st.bluenrgmesh.view.fragments.setting.node.ElementSettingFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ModelTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import static com.msi.moble.ApplicationParameters.Status.NOT_A_SUBSCRIBE_MODEL;

public class LoadConfigFeaturesFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private Nodes nodeData;
    private ConfigElementRecyclerAdapter configElementRecyclerAdapter;
    private RecyclerView recyclerView;
    private int type;
    private DeviceEntry mAutoDevice;
    private ConfigurationModelClient mConfigModel;
    Boolean vendorModelPresent = false;
    ArrayList<String> vendor_models = new ArrayList<String>();
    public String mCid = "";
    public String mPid = "";
    public String mVid = "";
    public String mCrpl = "";
    private AppKey appKeyAssign;
    private SubscriptionThread subscriptionThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_configuration, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new LoadConfigFeaturesFragment().getClass().getName());
        initUi();
        checkNodeFeaturesCommand();

        return view;
    }


    @Override
    public void onDestroyView() {
        Utils.updateActionBarForFeatures(getActivity(), new ProvisionedTabFragment().getClass().getName());
        super.onDestroyView();
    }

    private void checkNodeFeaturesCommand() {
        try {
            ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Loading Device Composition Data...", true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAutoDevice = new DeviceEntry(nodeData.getName(), mobleAddress.deviceAddress(Integer.parseInt(nodeData.getElements().get(0).getUnicastAddress())));
                    mConfigModel.getDeviceCompositionData(new ApplicationParameters.Address(mAutoDevice.getAddress().mValue), ApplicationParameters.Page.PAGE0, deviceCompositionDataStatus_callback);
                }
            }, 1000);
        } catch (Exception e) {

        }


    }

    private void initUi() {

        nodeData = (Nodes) getArguments().getSerializable(getString(R.string.key_serializable));
        if (nodeData == null) {
            return;
        }
        if (nodeData.getElements().size() == 1) {
            nodeData.getElements().get(0).setConfigured(false);
        } else {
            for (int i = 0; i < nodeData.getElements().size(); i++) {
                nodeData.getElements().get(i).setConfigured(false);
            }
        }

        ((MainActivity) getActivity()).currentNode = nodeData;
        mConfigModel = mobleNetwork.getConfigurationModelClient();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        configElementRecyclerAdapter = new ConfigElementRecyclerAdapter(getActivity(), nodeData, new ConfigElementRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void notifyAdapter(int position) {
                Utils.moveToFragment(getActivity(), new ElementSettingFragment(), nodeData.getElements().get(position), 0);
            }
        });
        recyclerView.setAdapter(configElementRecyclerAdapter);

        view.findViewById(R.id.butConfigAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Utils.showToast(getActivity(), "Setting Default Configurations !" + nodeData.getName());
                        //addConfiguration();
                    }
                }, 1000);
            }
        });

        view.findViewById(R.id.butQuickconfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity)getActivity()).isQuickBinding = true;

                if(getResources().getBoolean(R.bool.bool_isAutoProvisioning) && Utils.isAutoProvisioning(getActivity()))
                {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            AppKey appKey = ((MainActivity) getActivity()).meshRootClass.getAppKeys().get(0);
                            new ShareAppKeyCallback(getActivity(), appKey, nodeData, true).shareAppKey();
                        }
                    });
                }
                else {
                    showPopUpForKeys(getActivity());
                }

            }
        });

        view.findViewById(R.id.butDetailConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

    }

    ConfigurationModelClient.DeviceCompositionDataStatusCallback deviceCompositionDataStatus_callback =
            new ConfigurationModelClient.DeviceCompositionDataStatusCallback() {
                @Override
                public void onDeviceCompositionDataStatus(boolean timeout, ApplicationParameters.DeviceCompositionData block) {
                    try {
                        if (((MainActivity) getActivity()) == null)
                            return;

                        if (timeout == true) {
                            Log.i("DeviceCompositionData", "Timeout");
                            //Utils.showToast(getActivity(), "Timeout Detected");
                            ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), getString(R.string.ERROR_CONFIG_TIMEOUT), false);
                            ((MainActivity) getActivity()).onBackPressed();
                        } else {

                            ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Getting Device Features....", true);
                            //Utils.showToast(getActivity(), "Loading Device Features !");
                            Collection<ApplicationParameters.DeviceCompositionDataElement> mElements;
                            Utils.DEBUG("mCID = " + block.getCompanyID());
                            Utils.DEBUG("mPID = " + block.getProductID());
                            Utils.DEBUG("mVID = " + block.getVersionID());
                            Utils.DEBUG("mCRPL = " + block.getReplayProtectionListSize());
                            Utils.DEBUG("Features = " + block.getFeatures());
                            mCid = block.getCompanyID() + "";
                            MainActivity.mCid = Integer.parseInt(mCid);
                            mPid = block.getProductID() + "";
                            mVid = block.getVersionID() + "";
                            mCrpl = block.getReplayProtectionListSize() + "";

                            int ff = block.getFeatures();

                            UserApplication.trace("deviceCompositionDataStatus_callback Features RELAY  = " + ((ff & 1) == 1)); // RELAY  = ((ff & 1) == 1
                            boolean is_node_relay = ((ff & 1) == 1);
                            ff >>= 1;
                            UserApplication.trace("deviceCompositionDataStatus_callback Features Proxy  = " + ((ff & 1) == 1)); // Proxy  = ((ff & 1) == 1
                            boolean is_node_proxy = ((ff & 1) == 1);
                            ff >>= 1;
                            boolean is_node_friend = ((ff & 1) == 1);
                            UserApplication.trace("deviceCompositionDataStatus_callback Features Friend  = " + ((ff & 1) == 1)); // Friend  = ((ff & 1) == 1
                            ff >>= 1;
                            boolean is_node_low_power = ((ff & 1) == 1);
                            UserApplication.trace("deviceCompositionDataStatus_callback Features Low Power  = " + ((ff & 1) == 1)); // Low Power  = ((ff & 1) == 1


                            Features features = new Features();
                            features.setProxy(is_node_proxy ? 1 : 0);
                            features.setRelay(is_node_relay ? 1 : 0);
                            features.setFriend(is_node_friend ? 1 : 0);
                            features.setLowPower(is_node_low_power ? 1 : 0);
                            Utils.setNodeFeatures(getActivity(), ParseManager.getInstance().toJSON(features));

                            mElements = block.getElements();
                            //modelInfos.clear();
                            int count = 1;

                            ArrayList<ModelsData> modelDataByElement = new ArrayList<>();
                            for (ApplicationParameters.DeviceCompositionDataElement e : mElements) {
                                ArrayList<ModelInfo> modelInfos = new ArrayList<>();
                                //if(count == 1)
                                {
                                    int locaId = e.getLocationID();
                                    Collection<ApplicationParameters.ModelID> sigMids = e.getModels();
                                    for (ApplicationParameters.ModelID m : sigMids) {

                                        if (m != null) {
                                            UserApplication.trace("deviceCompositionDataStatus_callback SIG Model = " + m.toString());

                                            ModelInfo modelInfo = new ModelInfo();
                                            String[] split = m.toString().split(" ");
                                            modelInfo.setModelName(split[0].replace("_", " "));
                                            modelInfo.setModelID(split[1]);
                                            modelInfos.add(modelInfo);
                                        }

                                    }

                                    Collection<ApplicationParameters.VendorModelID> vMids = e.getVendorModels();
                                    for (ApplicationParameters.VendorModelID v : vMids) {
                                        UserApplication.trace("deviceCompositionDataStatus_callback Vendor Model = " + v.toString());
                                        vendorModelPresent = true;
                                        vendor_models.add(v.toString());
                                        int modelID = v.getModelID();
                                        int vendorID = v.getVendorID();
                                        String vendorModelId = "" + Utils.intToHexForFourDigit(modelID) + Utils.intToHexForFourDigit(vendorID);
                                        ModelInfo modelInfo = new ModelInfo();
                                        String[] split = v.toString().split("[.]");
                                        //modelInfo.setModelName(getString(R.string.MODEL_SUB_NAME_VENDOR_MODEL));
                                        modelInfo.setModelName(getString(R.string.MODEL_SUB_NAME_ST_VENDOR_SERVER_MODEL));
                                        ////modelInfo.setModelID(split[1]+split[0]);
                                        ////modelInfo.setModelID("00300001");
                                        modelInfo.setModelID(vendorModelId);
                                        modelInfos.add(modelInfo);
                                    }

                                    //save single element models data
                                    if (count == 1) {
                                        count++;
                                        ModelsData modelsData = new ModelsData();
                                        modelsData.setModelInfos(modelInfos);
                                        String strModelsInfo = ParseManager.getInstance().toJSON(modelsData);
                                        Utils.saveModelInfo(getActivity(), strModelsInfo);
                                    }

                                    //Models data represent single element data
                                    ModelsData modelsData = new ModelsData();
                                    modelsData.setModelInfos(modelInfos);
                                    modelDataByElement.add(modelsData);
                                }
                            }

                            //save multi element models data
                            final ModelsDataByElement modelsDataByElem = new ModelsDataByElement();
                            modelsDataByElem.setModelDataByElement(modelDataByElement);
                            String strModelsInfoByElement = ParseManager.getInstance().toJSON(modelsDataByElem);
                            Utils.saveModelInfoOfMultiElement(getActivity(), strModelsInfoByElement);
                            updateFeaturesAndModels(modelsDataByElem);

                            for (int i = 0; i < nodeData.getElements().size(); i++) {
                                //mobleAddress addr = mobleAddress.groupAddress(Integer.parseInt("FFFF", 16));
                                mobleAddress addr = mobleAddress.groupAddressFFFF(Integer.parseInt("FFFF", 16));
                                Utils.DEBUG(">> Subscription Start FFFF >>>>>>>>>>>> " + nodeData.getElements().get(i).getParentNodeAddress());
                                Utils.DEBUG(">> Subscription Start FFFF >>>>>>>>>>>> " + nodeData.getElements().get(i).getUnicastAddress());
                                subscriptionThread = new SubscriptionThread(addr, nodeData.getElements().get(i));
                                subscriptionThread.start();

                                if (i == nodeData.getElements().size() - 1) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (subscriptionThread != null && subscriptionThread.isAlive()) {
                                                subscriptionThread.stop();
                                            }
                                            ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Ready for Configuration.", false);

                                            if (getResources().getBoolean(R.bool.bool_isAutoProvisioning) && Utils.isAutoProvisioning(getActivity())) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AppKey appKey = ((MainActivity) getActivity()).meshRootClass.getAppKeys().get(0);
                                                        new ShareAppKeyCallback(getActivity(), appKey, nodeData, true).shareAppKey();
                                                    }
                                                }, 800);
                                            }
                                        }
                                    }, 800);
                                }
                            }
                        }

                    }catch (Exception e){

                    }
                }
            };


    public class SubscriptionThread extends Thread {
        private mobleAddress addr;
        private Element element;

        public SubscriptionThread(mobleAddress addr, Element element) {
            this.addr = addr;
            this.element = element;
        }

        public void run() {
            try {
                Utils.DEBUG(">> Subscription Running >>>>>>>>>>>> " + element.getUnicastAddress());
                try {
                    Utils.addElementToGroup(getActivity(), ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER, addr, element);
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
        }
    }


    public void updateFeaturesAndModels(ModelsDataByElement modelsDataByElem) {
        try {
            MeshRootClass meshRootClass = (MeshRootClass) ((MainActivity) getActivity()).meshRootClass.clone();

            String nodeFeatures = Utils.getNodeFeatures(getActivity()).replaceAll("\\\\", "");
            nodeFeatures = nodeFeatures.replaceAll("\"", "");

            nodeData.setCid(mCid);
            try {
                Features features = ParseManager.getInstance().fromJSON(new JSONObject(nodeFeatures), Features.class);
                nodeData.setFeatures(features);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<Element> updatedElementsByModel = Utils.updateAllElementsByModel(getActivity(), nodeData.getElements(), modelsDataByElem);
            nodeData.setElements(updatedElementsByModel);

            /*for (int j = 0; j < nodeData.getElements().size(); j++) {
                Element element = nodeData.getElements().get(j);
                ArrayList<Model> completeModelsForCurrentElement = Utils.getCompleteModelsForCurrentElement(getActivity(), element, meshRootClass, false);
                nodeData.getElements().get(j).setModels(completeModelsForCurrentElement);
            }*/

            // update node in json
            nodeData.setConfigured("true");
            nodeData.setConfigComplete(true);
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(nodeData.getElements().get(0).getUnicastAddress())) {
                        meshRootClass.getNodes().set(i, nodeData);
                        break;
                    }
                }
            }



            String s = ParseManager.getInstance().toJSON(meshRootClass);
            Utils.setBLEMeshDataToLocal(getActivity(), s);
            ((MainActivity) getActivity()).updateJsonData();
            ((MainActivity) getActivity()).updateProvisionedTab(null, 0);
            ((MainActivity) getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);
            ((MainActivity) getActivity()).fragmentCommunication(new ModelTabFragment().getClass().getName(), null, 1, null, true, null);
        } catch (Exception e) {
        }
    }

    public void showPopUpForKeys(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_appkeybind);
        dialog.findViewById(R.id.lytSlideLeftMsg).setVisibility(View.GONE);
        TextView txt = (TextView) dialog.findViewById(R.id.txtBindNow);
        RecyclerView recyclerAppKeyBind = (RecyclerView) dialog.findViewById(R.id.recyclerAppKeyBind);
        popUpRecyclerAppKeyBind(recyclerAppKeyBind, dialog);
        if (!dialog.isShowing()) {
            dialog.show();
        }

        TextView txtGenerateNewKey = (TextView) dialog.findViewById(R.id.txtMakeNewKey);
        txtGenerateNewKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                MainActivity.isCustomAppKeyShare = true;
                //Utils.moveToFragment(getActivity(), new AddAppKeyFragment(), element, 0);
            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void popUpRecyclerAppKeyBind(RecyclerView recyclerAppKeyBind, final Dialog dialog) {

        ArrayList<AppKey> appKeyList = ((MainActivity) getActivity()).meshRootClass.getAppKeys();
        if (appKeyList == null) {
            return;
        }
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerAppKeyBind.setLayoutManager(gridLayoutManager);
        AppKeyBindListAdapter appKeyBindListAdapter = new AppKeyBindListAdapter(getActivity(), null, true, appKeyList, null, new AppKeyBindListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, final AppKey appkeySelected, boolean isChecked) {

                if (isChecked) {
                    //update nodeaData object with selected key index
                    //for all elements share appkey and bind the key
                    dialog.dismiss();
                    appKeyAssign = appkeySelected;
                    ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Sharing App Key..", true);

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new ShareAppKeyCallback(getActivity(), appkeySelected, nodeData, true).shareAppKey();
                        }
                    });
                    //new BindAppKeyCallbackOld(getActivity(),appkeySelected.getKey(),element,appkeySelected.getIndex(), false);
                } else {
                    // call unbind  api
                    // update in json
                    Utils.showToast(getActivity(), "Key Already Assigned.");

                }

            }

            @Override
            public void onUnbindRecyclerItem(View v, int position, AppKey data) {

            }
        }, "loadconfigpopup");
        recyclerAppKeyBind.setAdapter(appKeyBindListAdapter);
        //Utils.calculateHeight(models.size(), recyclerAppKey);
    }

    /*public void updateConfiguration() {

        subAddress = Utils.getSubscriptionGroupAddressOnProvsioning(getActivity());
        pubAddress = Utils.getPublicationAddressOnProvsioning(getActivity());

        ((MainActivity)getActivity()).showPopUpForProxy(getActivity(), "Initializing Configuration...", true);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).adviseCallbacks();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                totalElements = nodeData.getElements().size();
                elementsCount = 0;
                mobleAddress nodeAddress = mobleAddress.deviceAddress(Integer.parseInt(nodeData.getElements().get(0).getUnicastAddress()));
                //startSubscription(elementsCount, nodeAddress, nodeAddress,subAddress, pubAddress);
            }
        }, 3000);

    }*/

}
