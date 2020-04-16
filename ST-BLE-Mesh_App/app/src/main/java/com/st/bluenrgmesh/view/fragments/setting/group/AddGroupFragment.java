/**
 * *****************************************************************************
 *
 * @file AddGroupFragment.java
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.ProvisionedRecyclerAdapter;
import com.st.bluenrgmesh.adapter.SubListForGroupAdapter;
import com.st.bluenrgmesh.adapter.group.SubGrpSettingAdapter;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import java.util.ArrayList;
import java.util.Collections;



public class AddGroupFragment extends BaseFragment {

    private View view;
    private EditText edtName;
    private RecyclerView subscriptionRecycler;
    private Button butAddGroup;
    private int count;
    public static mobleAddress addr;
    private String grpName;
    public static ArrayList<Element> eleSubscriptionList = new ArrayList<>();
    private SubListForGroupAdapter subListForGroupAdapter;
    private SubGrpSettingAdapter subGrpSettingAdapter;
    private int selectedPosition;
    private AppDialogLoader loader;
    private static ArrayList<Nodes> nodes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProvisionedRecyclerAdapter provisionedRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addgroup, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        loader.show();
        Utils.updateActionBarForFeatures(getActivity(), new AddGroupFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        Utils.setSettingType(getActivity(),getString(R.string.add_group));
        eleSubscriptionList.clear();
        edtName = (EditText) view.findViewById(R.id.edtName);
        butAddGroup = (Button) view.findViewById(R.id.butAddGroup);
        butAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.checkGrpHasSubElements(getActivity(), eleSubscriptionList, addr, getString(R.string.str_grpaddedsuccessfully_label));
            }
        });
        subscriptionRecycler = (RecyclerView) view.findViewById(R.id.subscriptionRecycler);
        updateElementSubRecyclerForSubList();

        updateUi();


        /****************************/
        //setProvisionAdapter(null,false);

    }

    private void setProvisionAdapter(String selected_element_address,boolean is_command_error) {

        getNodesData();

        try {
            if (nodes != null) {
                Collections.sort(nodes);
            }
        } catch (Exception e) {

        }

        recyclerView = (RecyclerView) view.findViewById(R.id.nodeRecycler);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        itemAnimator.setAddDuration(100);
        itemAnimator.setChangeDuration(10);
        itemAnimator.setRemoveDuration(100);
        recyclerView.setItemAnimator(itemAnimator);
        provisionedRecyclerAdapter = new ProvisionedRecyclerAdapter(getActivity(), new ProvisionedTabFragment().getClass().getName(), nodes,selected_element_address,is_command_error, new ProvisionedRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, String item, String mAutoAddress, boolean isSelected) {
                recyclerView.stopScroll();
                provisionedRecyclerAdapter.sortData();
                provisionedRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyAdapter(String selected_element_address, boolean is_command_error) {
                getNodesData();
                setProvisionAdapter(selected_element_address,is_command_error);
            }

            @Override
            public void notifyPosition(int elementPosition, Nodes node) {
                // get element position w.r.t any touch event
            }
        });
        recyclerView.setAdapter(provisionedRecyclerAdapter);

        loader.hide();
    }

    private void getNodesData() {

        //meshRootClass = Utils.getBLEMeshDataInstance(getActivity());

        if(nodes!=null)  nodes.clear();
        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(provisionedRecyclerAdapter!=null)  provisionedRecyclerAdapter.notifyDataSetChanged();
                }
            });

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
        }catch (Exception e){}
    }








    /*********************************************************************/

    private void updateUi() {

        count = Utils.getNextGroupCount(((MainActivity)getActivity()).meshRootClass);
        addr = mobleAddress.groupAddress(mobleAddress.GROUP_HEADER + count);
        //int nextGrpAddress = Integer.parseInt(Utils.getProvisionerGroupLowAddressRange(AddGroupActivity.this)) + count;
        //addr = mobleAddress.groupAddress(nextGrpAddress);
        Utils.DEBUG("New Group Address : " + String.valueOf(addr));
        if (count > 9) {
            loader.dismiss();
            Utils.showAlertForCount(getActivity(),count);

        } else {
            grpName = "Group " + count;
            edtName.setText(grpName);
            edtName.setSelection(edtName.getText().length());

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    eleSubscriptionList = getElementsListData();
                    Utils.addNewGroupToJson(getActivity(), addr, grpName); //remove if no elements are checked
                    ((MainActivity)getActivity()).updateJsonData();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subGrpSettingAdapter.notifyDataSetChanged();
                            loader.hide();
                        }
                    });
                }
            });
        }
    }

    private ArrayList<Element> getElementsListData() {

        eleSubscriptionList.clear();
        try {
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
                        element.setChecked(false);
                        element.setParentNodeAddress(((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress());
                        element.setParentNodeName(((MainActivity)getActivity()).meshRootClass.getNodes().get(i).getName());
                        eleSubscriptionList.add(element);
                    }
                }
            }
        } catch (Exception e) {
            Utils.DEBUG(">> Method Error : getElementsListData()");
        }

        return eleSubscriptionList;
    }

    private void updateElementSubRecyclerForSubList() {

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        subscriptionRecycler.setLayoutManager(gridLayoutManager);
        subGrpSettingAdapter = new SubGrpSettingAdapter(getActivity(), eleSubscriptionList, String.valueOf(addr), ((MainActivity)getActivity()).meshRootClass,
                true, new SubGrpSettingAdapter.IRecyclerViewHolderClicks() {

            @Override
            public void onClickRecyclerItem(View v, int position, Element element, String mAutoAddress, boolean isChecked) {
                Utils.DEBUG("DEBUG Clicked >> Subscription : " +
                        " address : " + mAutoAddress + " subaddrss : " + element.getUnicastAddress());

                Utils.DEBUG("DEBUG Clicked >> Subscription : " + " address : " + mAutoAddress + " subaddrss : " + element.getUnicastAddress());

                if (isChecked) {
                    //on checked
                    int count = getCheckedElement();
                    UserApplication.trace("count " + count);
                    /*String replaceModelName = modelSelected.getModelName().replace(" ", "_");
                    ModelRepository modelRepository = new ModelRepository();
                    ApplicationParameters.GenericModelID modelSelected =  modelRepository.getModelSelected(replaceModelName);
                    if (count <= 9) {
                       loader.show();
                        try {
                            selectedPosition = position;
                            if (eleSubscriptionList.get(position).isChecked()) {
                                //Utils.removeElementFromGroup(AddGroupActivity.this, addr, mAutoAddress, item);
                                Utils.DEBUG(">>>Group Remove : " + " addr : " + addr + " mAutoAddress :" + mAutoAddress + " Unicast : " + element.getUnicastAddress());
                                Utils.removeElementFromGroup(getActivity(), addr, element, modelSelected);
                            } else {
                                //Utils.addElementToGroup(AddGroupActivity.this, addr, mAutoAddress, item);
                                Utils.DEBUG(">>>Group Add : " + " addr : " + addr + " mAutoAddress :" + mAutoAddress + " Unicast : " + element.getUnicastAddress());
                                Utils.addElementToGroup(getActivity(), null, addr, element);
                            }

                        } catch (Exception e) {
                            loader.hide();
                        }

                    } else {
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

    private int getCheckedElement() {
        count = 0;
        for (Element eleemnt : eleSubscriptionList) {
            if (eleemnt.isChecked()) {
                count++;
            }
        }
        return count;
    }


    public void updateFragmentUi(String bt_addr, int i, ApplicationParameters.Status status) {

        if(i == ((MainActivity)getActivity()).SUBSCRIPTION_CASE)
        {
            if (status == ApplicationParameters.Status.SUCCESS) {
                eleSubscriptionList.get(selectedPosition).setChecked(eleSubscriptionList.get(selectedPosition).isChecked() ? false : true);
                json_updateSubscriptionGroupInModel(getActivity(), String.valueOf(addr), eleSubscriptionList.get(selectedPosition), ((MainActivity)getActivity()).meshRootClass);

            } else {
                updateSubscriptionData("Error");
                Utils.showToast(getActivity(), "Subscription Failed");
            }
        }
        else if(i == ((MainActivity)getActivity()).PUBLICATION_CASE)
        {
            //updateUi_PublicationForGroup(ApplicationParameters.Status.SUCCESS, bt_addr);
        }
    }

    /**
     * Method used to update group data whenever any element is added or removed in subscription list of model
     *
     * @param context
     * @param groupAddress
     * @param meshRootClass
     */
    public void json_updateSubscriptionGroupInModel(final Context context, final String groupAddress, final Element element, final MeshRootClass meshRootClass) {

        // According to note 1 : All models data will be same
        String strMsg = "";
        try {
            for (int i = 0; i < meshRootClass.getNodes().size(); i++) {
                int addrs = Integer.parseInt(element.getParentNodeAddress());
                if (String.valueOf(addrs).equalsIgnoreCase(meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress())) {
                    for (int j = 0; j < meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        if (meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                            for (int k = 0; k < meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {

                                if (element.isChecked()) {
                                    strMsg = context.getString(R.string.str_subscribed_success_label);
                                    meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().add(groupAddress);
                                } else {
                                    if (meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().contains(groupAddress)) {
                                        meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getSubscribe().remove(groupAddress);
                                        strMsg = context.getString(R.string.str_unsubscribed_label);
                                        // /Utils.showPopUpForMessage(context, "Unsubscribed Successfully",Utils.getDialogInstance(context));
                                    }
                                }

                            }
                            break;
                        }
                    }
                }
            }
            Utils.setBLEMeshDataToLocal(context, ParseManager.getInstance().toJSON(meshRootClass));
            Utils.DEBUG("Group Updated Successfully in Subscription List For Model: " + groupAddress);
        }catch (Exception e){Utils.DEBUG("Exception in method : json_updateSubscriptionGroupInModel");}

        updateSubscriptionData(strMsg.equals("") ? context.getString(R.string.str_subscribed_success_label) : strMsg);
    }

    public void updateSubscriptionData(String msg)
    {
        loader.hide();
        loader.dismiss();
        Utils.showPopUpForMessage(getActivity(), msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).updateJsonData();
            }
        }, 500);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subGrpSettingAdapter.notifyDataSetChanged();
            }
        });
    }
}
