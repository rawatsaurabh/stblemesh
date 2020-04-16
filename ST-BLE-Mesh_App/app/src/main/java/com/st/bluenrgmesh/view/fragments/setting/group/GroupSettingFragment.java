/**
 * *****************************************************************************
 *
 * @file GroupSettingFragment.java
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
package com.st.bluenrgmesh.view.fragments.setting.group;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.elementsettings.ModelListAdapter;
import com.st.bluenrgmesh.adapter.group.PubGrpSettingAdapter;
import com.st.bluenrgmesh.adapter.group.SubGrpSettingAdapter;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import java.util.ArrayList;
import java.util.List;


public class GroupSettingFragment extends BaseFragment {

    private View view;
    private EditText edtName;
    private EditText edtAddress;
    private RecyclerView subscriptionRecycler;
    private RecyclerView publishingRecycler;
    private Button butRemoveGroup;
    private ArrayList<Element> eleSubscriptionList = new ArrayList<>();
    private ArrayList<Element> elePublicationList = new ArrayList<>();
    private String new_groupAddress = "c000";
    private Group groupData = null;
    private mobleAddress groupAddress;
    private mobleAddress addr;
    private int count;
    private int selectedPosition;
    private SubGrpSettingAdapter subGrpSettingAdapter;
    private String parentNodeName = null;
    private PubGrpSettingAdapter pubGrpSettingAdapter;
    private AppDialogLoader loader;
    private TextView txtModelcount;
    private TextView txtModelName;
    private RecyclerView recyclerModel;
    private ModelListAdapter modelListAdapter;
    private Model modelSelected;
    private ArrayList<Model> models;
    private LinearLayout lytModelSelection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groupsettings, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        loader.show();
        Utils.updateActionBarForFeatures(getActivity(), new GroupSettingFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        ((MainActivity)getActivity()).updateJsonData();
        Utils.setSettingType(getActivity(), getString(R.string.str_group_setting));
        groupData = (Group) getArguments().getSerializable(getString(R.string.key_serializable));
        edtName = (EditText) view.findViewById(R.id.edtName);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        subscriptionRecycler = (RecyclerView) view.findViewById(R.id.subscriptionRecycler);
        publishingRecycler = (RecyclerView) view.findViewById(R.id.publishingRecycler);
        butRemoveGroup = (Button) view.findViewById(R.id.butRemoveNode);
        txtModelcount = (TextView) view.findViewById(R.id.txtModelcount);
        txtModelName = (TextView) view.findViewById(R.id.txtModelName);
        recyclerModel = (RecyclerView) view.findViewById(R.id.recyclerModel);
        lytModelSelection = (LinearLayout) view.findViewById(R.id.lytModelSelection);
        lytModelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerModel.setVisibility(recyclerModel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        edtAddress.setText(groupData.getAddress());
        int adr = Integer.parseInt(groupData.getAddress(), 16);
        mobleAddress addre = mobleAddress.groupAddress(adr);
        short adrs = addre.mValue;
        groupAddress = mobleAddress.groupAddress((adrs) & 0xFFFF);
        addr = groupAddress;
        edtName.setText(groupData.getName());

        butRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.checkGrpHasSubElements(getActivity(), eleSubscriptionList, addr, getString(R.string.str_groupfailremove_label));
            }
        });
        //updateSubscriptionRecycler();
        //updatePublicationRecycler();
        //setPublicationData();

        models = Utils.getAllAvailableModels(getActivity());
        ArrayList<Model> models_arr = (ArrayList<Model>) models.clone();

        try {
            for (Model model : models_arr) {

                if (model.getModelName().contains("CONFIGURATION") || model.getModelName().contains("HEALTH")) {
                    models.remove(model);
                }

            /*if("CONFIGURATION SERVER".equalsIgnoreCase(model.getModelName())){
                models.remove(model);
            }
            if("HEALTH MODEL SERVER".equalsIgnoreCase(model.getModelName())){
                models.remove(model);
            }*/

            }
        }catch (Exception e){}
        try {
            /*models.get(2).setChecked(true);
            modelSelected = models.get(2);
            txtModelName.setText(modelSelected.getModelName());
            txtModelcount.setText("Model Count : " + models.size());*/
            try {
                models.get(0).setChecked(true);
                modelSelected = models.get(0);
                if(modelSelected.getModelName().contains("CONFIGURATION") || modelSelected.getModelName().contains("HEALTH"))
                {
                    models.get(2).setChecked(true);
                    modelSelected = models.get(2);

                }
                txtModelName.setText(modelSelected.getModelName());
                txtModelcount.setText("Model Count : " + models.size());
            } catch (Exception e) {
            }
            eleSubscriptionList = Utils.getElemntsContainsModel(getActivity(), modelSelected, groupData);
            updateSubscriptionRecycler();
        } catch (Exception e) {
        }
        initRecyclerForModels();

        loader.hide();
    }

    private void initRecyclerForModels() {
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerModel.setLayoutManager(gridLayoutManager);


        modelListAdapter = new ModelListAdapter(getActivity(), models/*, modelSelected.getBind()*/  , new ModelListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, Model model) {

                modelSelected = model;
                modelSelected.setChecked(true);
                //getSubscriptionData(model);
                //initRecyclerForBindKeys(model, recyclerAppKey);
                txtModelName.setText(model.getModelName());
                recyclerModel.setVisibility(View.GONE);
                eleSubscriptionList = Utils.getElemntsContainsModel(getActivity(), model, groupData);
                //setSubscriptionData();
                updateSubscriptionRecycler();
            }
        });
        recyclerModel.setAdapter(modelListAdapter);
        Utils.calculateHeight(models.size(), recyclerModel);
    }

    private void setSubscriptionData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //eleSubscriptionList = getSubscriptionList();
                if(eleSubscriptionList.size() > 0)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subGrpSettingAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });

    }

    private void setPublicationData() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                elePublicationList = getPublicationData();
                if(elePublicationList.size() > 0)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubGrpSettingAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });
    }

    private void updateSubscriptionRecycler() {

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        subscriptionRecycler.setLayoutManager(gridLayoutManager);
        subGrpSettingAdapter = new SubGrpSettingAdapter(getActivity(), eleSubscriptionList, groupData.getAddress(), ((MainActivity)getActivity()).meshRootClass,
                 false, new SubGrpSettingAdapter.IRecyclerViewHolderClicks() {

            @Override
            public void onClickRecyclerItem(View v, int position, Element element, String mAutoAddress, boolean isChecked) {
                Utils.DEBUG("DEBUG Clicked >> Subscription : " +
                        " address : " + mAutoAddress + " subaddrss : " + element.getUnicastAddress());

                if (isChecked) {
                    //on checked
                    int count = getCheckedElement();
                    //int count = groupsInfo(mAutoAddress);
                    UserApplication.trace("count " + count);
                    //if (count <= 10)
                    {
                        loader.show();
                        try {
                            selectedPosition = position;
                            String replaceModelName = modelSelected.getModelName().replace(" ", "_");
                            ApplicationParameters.GenericModelID modelSelected =  ModelRepository.getInstance().getModelSelected(replaceModelName);
                            if (eleSubscriptionList.get(position).isChecked()) {
                                //Utils.removeElementFromGroup(AddGroupActivity.this, addr, mAutoAddress, item);
                                Utils.DEBUG(">>>Group Remove : " + " addr : " + addr + " mAutoAddress :" + mAutoAddress + " Unicast : " + element.getUnicastAddress());
                                Utils.removeElementFromGroup(getActivity(), addr, element, modelSelected);
                            } else {
                                Utils.DEBUG(">>>Group Setting : " + " addr : " + addr + " mAutoAddress :" + mAutoAddress + " Unicast : " + element.getUnicastAddress());
                                Utils.addElementToGroup(getActivity(), modelSelected, addr, element);
                            }

                        } catch (Exception e) {
                            loader.hide();
                        }

                    }
                    /*else {
                        //show maximum count status
                        Utils.showToast(getActivity(), "Maximum 10 elements limit for subscription.");
                    }*/
                }
            }

            @Override
            public void onClickNodeRecyclerItem(View v, int position, Nodes node, String address, boolean b) {

            }
        });
        subscriptionRecycler.setAdapter(subGrpSettingAdapter);
        //Utils.calculateHeight(eleSubscriptionList.size(), subscriptionRecycler);

    }

    private void updatePublicationRecycler() {

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        publishingRecycler.setLayoutManager(gridLayoutManager);
        //pubGrpSettingAdapter = new PublicationListForGroupSettingsAdapter(this, type,  elePublicationList, new PublicationListForGroupSettingsAdapter.IRecyclerViewHolderClicks() {
        pubGrpSettingAdapter = new PubGrpSettingAdapter(getActivity(), parentNodeName, groupData.getAddress(), null, elePublicationList, new PubGrpSettingAdapter.IRecyclerViewHolderClicks() {

            @Override
            public void notifyAdapter(final int position, final List<Element> final_publication_list, boolean isSelected) {
                if (isSelected) {
                    selectedPosition = position;
                    if (getResources().getBoolean(R.bool.bool_isElementFunctionality)) {
                        Utils.DEBUG(">>>GPNode Add" + elePublicationList.get(position).getParentNodeName() +
                                " Elem Add" + elePublicationList.get(position).getUnicastAddress());
                        Utils.addPublishToGroup(getActivity(), elePublicationList.get(position).getParentNodeAddress(), elePublicationList.get(position).getUnicastAddress(),  groupAddress.toString());
                    }
                }

            }
        });
        publishingRecycler.setAdapter(pubGrpSettingAdapter);
    }

    private ArrayList<Element> getPublicationData() {

        if (((MainActivity)getActivity()).meshRootClass != null) {
            elePublicationList.clear();
            if (((MainActivity)getActivity()).meshRootClass.getNodes() != null && ((MainActivity)getActivity()).meshRootClass.getNodes().size() > 0) {
                for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {

                    boolean isNodeIsProvisioner = false;
                    for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getProvisioners().size(); j++) {
                        if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity)getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                            isNodeIsProvisioner = true;
                            break;
                        }
                    }

                    if (!isNodeIsProvisioner) {
                        parentNodeName = ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getName();
                        for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                            ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).setParentNodeName(((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getName());
                            elePublicationList.add(((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(j));
                        }
                    }
                }
            }

        }
        return elePublicationList;
    }

    private int getCheckedElement() {
        count = 0;
        for (Element eleemnt : eleSubscriptionList) {
            if (eleemnt.isChecked()) {
                count++;
            }
        }
        return count;
    }

    private ArrayList<Element> getSubscriptionList() {

        //returmn all the elements
        eleSubscriptionList.clear();
        try{
            for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {

                boolean isNodeIsProvisioner = false;
                for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getProvisioners().size(); j++) {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity)getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                        isNodeIsProvisioner = true;
                        break;
                    }
                }

                if (!isNodeIsProvisioner) {
                    for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {

                        Element element = ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(j);
                        element.setParentNodeAddress(((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress());
                        boolean isGroupIsPresent_Element = Utils.isGroupIsPresentInElement(element, groupData.getAddress());

                        if (isGroupIsPresent_Element) {
                            element.setChecked(true);
                        } else {
                            element.setChecked(false);
                        }
                        eleSubscriptionList.add(element);
                    }
                }
            }
        }catch (Exception e){}

        return eleSubscriptionList;
    }

    public void updateUi_PublicationForGroup(ApplicationParameters.Status status, final String address) {
        loader.dismiss();
        if (status == ApplicationParameters.Status.SUCCESS) {
            for (int i = 0; i < elePublicationList.get(selectedPosition).getModels().size(); i++) {

                elePublicationList.get(selectedPosition).getModels().get(i).getPublish().setAddress(String.valueOf(addr));
            }

            for (int i = 0; i < ((MainActivity)getActivity()).meshRootClass.getNodes().size(); i++) {
                for (int j = 0; j < ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(elePublicationList.get(selectedPosition).getUnicastAddress())) {
                        ((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().set(j, elePublicationList.get(selectedPosition));
                    }
                }
            }
            Utils.setBLEMeshDataToLocal(getActivity(), ParseManager.getInstance().toJSON(((MainActivity)getActivity()).meshRootClass));
        }
        updatePublicationData(getActivity().getString(R.string.str_published_success_label));
    }

    public void updateFragmentUi(String bt_addr, int i, ApplicationParameters.Status status) {

        if(i == ((MainActivity)getActivity()).SUBSCRIPTION_CASE)
        {
            ((MainActivity)getActivity()).updateJsonData();
            if (status == ApplicationParameters.Status.SUCCESS) {
                eleSubscriptionList.get(selectedPosition).setChecked(eleSubscriptionList.get(selectedPosition).isChecked() ? false : true);
                json_updateSubscriptionGroupInModel(getActivity(), String.valueOf(addr), eleSubscriptionList.get(selectedPosition), modelSelected);

            } else {
                updateSubscriptionData(getActivity().getString(R.string.str_Subscription_Failed_label));
            }
        }
        else if(i == ((MainActivity)getActivity()).PUBLICATION_CASE)
        {
            ((MainActivity)getActivity()).updateJsonData();
            if (status == ApplicationParameters.Status.SUCCESS) {
                updateUi_PublicationForGroup(ApplicationParameters.Status.SUCCESS, bt_addr);

            } else {
                updatePublicationData(getActivity().getString(R.string.str_Publication_Failed_label));
            }

        }
    }

    /**
     * Method used to update group data whenever any element is added or removed in subscription list of model
     *
     *  @param context
     * @param groupAddress
     * @param modelSelected
     */
    public void json_updateSubscriptionGroupInModel(final Context context, final String groupAddress, final Element element, Model modelSelected) {

        // According to note 1 : All models data will be same
        MeshRootClass meshRootClass = null;

        try {
            meshRootClass = (MeshRootClass) ((MainActivity)context).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


        String strMsg = "";
        try {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                int addrs = Integer.parseInt(element.getParentNodeAddress());
                if (String.valueOf(addrs).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress())) {
                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        if (meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                            for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {

                                if(modelSelected.getModelName().equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelName())) {
                                    if (element.isChecked()) {
                                        strMsg = context.getString(R.string.str_subscribed_success_label);
                                        if(meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe() != null && meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().size() > 0)
                                        {
                                            meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().add(groupAddress);
                                        }
                                        else
                                        {
                                            ArrayList<String> subscribe = new ArrayList<>();
                                            subscribe.add(groupAddress);
                                            meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).setSubscribe(subscribe);
                                        }

                                    } else {
                                        if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().contains(groupAddress)) {
                                            meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().remove(groupAddress);
                                            strMsg = context.getString(R.string.str_unsubscribed_label);
                                            // /Utils.showPopUpForMessage(context, "Unsubscribed Successfully",Utils.getDialogInstance(context));
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(meshRootClass));
            ((MainActivity)context).updateJsonData();
            ((MainActivity)getActivity()).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
            Utils.DEBUG("Group Updated Successfully in Subscription List For Model: " + groupAddress);
        }catch (Exception e){Utils.DEBUG("Exception in method : json_updateSubscriptionGroupInModel");}

        updateSubscriptionData(strMsg.equals("") ? context.getString(R.string.str_subscribed_success_label) : strMsg);
    }

    public void updateSubscriptionData(String msg)
    {
        Utils.showPopUpForMessage(getActivity(), msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).updateJsonData();
                subGrpSettingAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    public void updatePublicationData(String msg)
    {
        Utils.showPopUpForMessage(getActivity(), msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).updateJsonData();
                pubGrpSettingAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    public Group getGroupData() {
        groupData.setName(edtName.getText().toString());
        return groupData;
    }
}
