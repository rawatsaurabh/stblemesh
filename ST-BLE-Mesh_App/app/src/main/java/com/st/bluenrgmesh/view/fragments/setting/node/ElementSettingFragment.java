/**
 * *****************************************************************************
 *
 * @file ElementSettingFragment.java
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
package com.st.bluenrgmesh.view.fragments.setting.node;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.PublicationListAdapter;
import com.st.bluenrgmesh.adapter.RecyclerItemTouchHelper;
import com.st.bluenrgmesh.adapter.elementsettings.AppKeyBindListAdapter;
import com.st.bluenrgmesh.adapter.elementsettings.ModelListAdapter;
import com.st.bluenrgmesh.adapter.elementsettings.SubscriptionListAdapter;
import com.st.bluenrgmesh.utils.ModelRepository;
import com.st.bluenrgmesh.callbacks.appkeymanagement.UnbindAppKeyCallback;
import com.st.bluenrgmesh.callbacks.appkeymanagement.quickbinding.BindAppKeyCallback;
import com.st.bluenrgmesh.models.meshdata.AppKey;
import com.st.bluenrgmesh.models.meshdata.AppKeyList;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.meshdata.Publish;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.setting.managekey.AddAppKeyFragment;
import com.st.bluenrgmesh.view.fragments.tabs.GroupTabFragment;
import com.st.bluenrgmesh.view.fragments.tabs.ProvisionedTabFragment;

import java.util.ArrayList;
import java.util.List;



public class ElementSettingFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private View view;
    private AppDialogLoader loader;
    private EditText edtDeviceName;
    private EditText edtElementName;
    private EditText edtAddress;
    private RecyclerView subscriptionRecycler;
    private RecyclerView publishingRecycler;
    private PublicationListAdapter publicationListAdapter;
    private ArrayList<Publish> elePublicationList = new ArrayList<>();
    private ArrayList<Group> subscriptionList = new ArrayList<>();
    private ArrayList<Model> models = new ArrayList<>();
    private int selectedPosition;
    private Element element;
    private mobleAddress peerAddress;
    private Model modelSelected;
    private LinearLayout layModel;
    private RecyclerView recyclerModel;
    private TextView txtModelName;
    private TextView txtModelcount;
    private RecyclerView recyclerAppKey;
    private CardView cdAppKeyBind;
    private AppKeyList appKeyList;
    private TextView txtBindMoreKey;
    private TextView txtGenerateNewKey;
    private AppKey appKeyAssign;
    private AppKeyBindListAdapter appKeyBindListAdapter;
    private ArrayList<AppKey> bindedAppKeys = new ArrayList<>();
    private CardView cdModelSelection;
    private CardView subscriptionCardView;
    private CardView publicationCardView;
    private ModelListAdapter modelListAdapter;
    private MeshRootClass clonedMeshRoot;
    private AppKeyBindListAdapter popUpAppKeyBindListAdapter;
    private ArrayList<AppKey> popUpAppKeyList;
    private RecyclerItemTouchHelper itemTouchPopUpHelperCallback;
    private SubscriptionListAdapter subscriptionListAdapter;
    private LinearLayout lytModelSelection;
    private RecyclerView recyclerPopUpAppKeyBind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_elementsettings, container, false);

        Utils.updateActionBarForFeatures(getActivity(), new ElementSettingFragment().getClass().getName());
        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
    }

    private void initUi() {

        element = (Element) getArguments().getSerializable(getString(R.string.key_serializable));

        layModel = (LinearLayout) view.findViewById(R.id.layModel);
        layModel.setVisibility(View.GONE);
        edtDeviceName = (EditText) view.findViewById(R.id.edtDeviceName);
        edtElementName = (EditText) view.findViewById(R.id.edtElementName);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        subscriptionRecycler = (RecyclerView) view.findViewById(R.id.subscriptionRecycler);
        publishingRecycler = (RecyclerView) view.findViewById(R.id.publishingRecycler);
        recyclerAppKey = (RecyclerView) view.findViewById(R.id.recyclerAppKey);
        recyclerModel = (RecyclerView) view.findViewById(R.id.recyclerModel);
        recyclerModel.setVisibility(View.GONE);
        cdModelSelection = (CardView) view.findViewById(R.id.cdModelSelection);
        subscriptionCardView = (CardView) view.findViewById(R.id.subscriptionCardView);
        publicationCardView = (CardView) view.findViewById(R.id.publicationCardView);
        cdAppKeyBind = (CardView) view.findViewById(R.id.cdAppKeyBind);
        //cdAppKeyBind.setVisibility(View.GONE);
        txtBindMoreKey = (TextView) view.findViewById(R.id.txtBindMoreKey);
        txtBindMoreKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpForKeys(getActivity(),element);
            }
        });
        txtModelName = (TextView) view.findViewById(R.id.txtModelName);
        txtModelcount = (TextView) view.findViewById(R.id.txtModelcount);
        lytModelSelection = (LinearLayout) view.findViewById(R.id.lytModelSelection);
        lytModelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerModel.setVisibility(recyclerModel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getClonedMeshRoot();
                updateUI();
                Utils.setSettingType(getActivity(), getString(R.string.str_element_setting));
            }
        });
    }

    private void getClonedMeshRoot() {
        try {
            clonedMeshRoot = (MeshRootClass) ((MainActivity) getActivity()).meshRootClass.clone();
        } catch (CloneNotSupportedException e) {
        }
    }

    private void updateUI() {

        if (element != null) {
            try {
                peerAddress = mobleAddress.deviceAddress(Integer.parseInt(element.getParentNodeAddress()));
            }catch (Exception e){}
            Utils.DEBUG("Peer Address : " + element.getParentNodeAddress());
            edtDeviceName.setText(element.getParentNodeName());
            edtElementName.setText(element.getName());
            edtAddress.setText(element.getUnicastAddress());
            models.clear();
            //models = Utils.getModelsForCurrentElement3(this, ((MainActivity)getActivity()).meshRootClass, element.getParentNodeAddress(), element.getUnicastAddress());
            if (element.getModels() == null) {
                cdModelSelection.setVisibility(View.GONE);
                cdAppKeyBind.setVisibility(View.GONE);
                subscriptionCardView.setVisibility(View.GONE);
                publicationCardView.setVisibility(View.GONE);
                return;
            }
            models = element.getModels();

            ArrayList<Model> models_arr = (ArrayList<Model>) models.clone();

            for(Model model : models_arr){
                if("CONFIGURATION SERVER".equalsIgnoreCase(model.getModelName())){
                    models.remove(model);
                }
                if("HEALTH MODEL SERVER".equalsIgnoreCase(model.getModelName())){
                    models.remove(model);
                }

            }
            //Publisher and Subscriber remain same for all models in current release
            //so any one model is selected by default
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

            getSubscriptionData(modelSelected);
            getPublicationData(modelSelected);
            initRecyclerForModels();
            initRecyclerForBindKeys(modelSelected, recyclerAppKey);

            //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerAppKey);
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerAppKey);

        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        Utils.DEBUG(">> Delete Op");
        if(direction == ItemTouchHelper.LEFT)
        {
            new UnbindAppKeyCallback(getActivity(), element, appKeyAssign, modelSelected);
        }
        else
        {

        }
    }

    private void initRecyclerForBindKeys(final Model modelSelected, RecyclerView recyclerAppKey) {

        ArrayList<AppKey> activeKeyForModelSelected = getActiveKeyForModelSelected(modelSelected);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerAppKey.setLayoutManager(gridLayoutManager);
        appKeyBindListAdapter = new AppKeyBindListAdapter(getActivity(), element, false, activeKeyForModelSelected, modelSelected.getBind(), new AppKeyBindListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, final AppKey appKey, boolean isChecked) {

                //deselect all
                if (isChecked) {
                    appKeyAssign = appKey;
                    ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Activating Key at Index.." + appKey.getIndex(), true);
                    byte[] bytes = Utils.hexStringToByteArray(appKey.getKey());
                    MainActivity.network.setAppKey(bytes);
                    //Utils.setDefaultAppKeyIndex(getActivity(), appKey.getKey());
                    for (int i = 0; i < bindedAppKeys.size(); i++) {
                        bindedAppKeys.get(i).setChecked(false);
                    }
                    bindedAppKeys.get(position).setChecked(true);
                    setActiveKeyForModelSelected(appKey, true);
                    //Utils.setDefaultAppKeyIndex(getActivity(), appKeyAssign.getIndex());
                    //updateRecyclerAfterBindingKeys(modelSelected, appKey, false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Key Activated at Index.." + appKey.getIndex(), false);
                        }
                    }, 1000);
                } else {
                    //Utils.showToast(getActivity(), "Unbind Feature is in development.");
                }

            }

            @Override
            public void onUnbindRecyclerItem(View v, int position, AppKey appkey) {
                //unbind command
                appKeyAssign = appkey;
                //new UnbindAppKeyCallback(getActivity(),element, appKeyAssign, modelSelected);

            }
        },"elementSetting");
        recyclerAppKey.setAdapter(appKeyBindListAdapter);
        //Utils.calculateHeight(models.size(), recyclerAppKey);
        cdAppKeyBind.setVisibility(View.VISIBLE);
    }

    /**
     * unbind and bind case list update
     *
     * @param modelSelected
     * @param appKeyUsed
     * @param isBinding
     */
    private void updateRecyclerAfterBindingKeys(final Model modelSelected, final AppKey appKeyUsed, final boolean isBinding) {
        if (isBinding) {
            bindedAppKeys.add(appKeyUsed);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appKeyBindListAdapter.notifyItemInserted(bindedAppKeys.size());
                    //appKeyBindListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            int index = 0;
            //remove key from list
            for (int i = 0; i < bindedAppKeys.size(); i++) {
                if (bindedAppKeys.get(i).getIndex() == appKeyAssign.getIndex()) {
                    index = i;
                    bindedAppKeys.remove(i);
                    break;
                }
            }
            //setActiveKeyForModelSelected(appKeyAssign, false);
            Utils.updateActiveKeyForModelSelected(getActivity(), appKeyAssign, false, element, modelSelected);
            final int finalIndex = index;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(appKeyBindListAdapter != null)
                    {
                        recyclerAppKey.setAdapter(appKeyBindListAdapter);
                    }
                    //appKeyBindListAdapter.notifyItemMoved(finalIndex, appKeyBindListAdapter.getItemCount());
                    //appKeyBindListAdapter.notifyItemRemoved(finalIndex);
                    //appKeyBindListAdapter.onSwiped(appKeyBindListAdapter.holderView, ItemTouchHelper.RIGHT, finalIndex);
                }
            });
        }
    }

    private void setActiveKeyForModelSelected(AppKey appKeyUsed, boolean isSetKey) {
        Utils.updateActiveKeyForModelSelected(getActivity(), appKeyUsed, isSetKey, element, modelSelected);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //appKeyBindListAdapter.notifyItemInserted(bindedAppKeys.size());
                modelListAdapter.notifyDataSetChanged();
                appKeyBindListAdapter.notifyDataSetChanged();
                ((MainActivity)getActivity()).fragmentCommunication(new ProvisionedTabFragment().getClass().getName(), null, 0, null, false, null);
            }
        });
    }

    private ArrayList<AppKey> getActiveKeyForModelSelected(final Model modelSelected) {
        bindedAppKeys.clear();
        for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
            for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                    try {
                        boolean isDisplayed = false;
                        for (int k = 0; k < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                            //models
                            if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId().equalsIgnoreCase(modelSelected.getModelId())) {
                                for (int l = 0; l < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAppKeys().size(); l++) {
                                    try {
                                        for (int m = 0; m < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getBind().size(); m++) {
                                            if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getBind().get(m)
                                                    == ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAppKeys().get(l).getIndex()) {
                                                AppKey appKey = ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAppKeys().get(l);
                                                if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getModels().get(k).getActiveKeyIndex()
                                                        == appKey.getIndex() /*|| Utils.getDefaultAppKeyIndex(getActivity()) == appKey.getIndex()*/) {
                                                    appKey.setChecked(true);
                                                } else {
                                                    appKey.setChecked(false);
                                                }
                                                bindedAppKeys.add(appKey);
                                            }
                                        }
                                    } catch (Exception e) {
                                        if(!isDisplayed)
                                        {
                                            isDisplayed = true;
                                            //Utils.showPopUpForMessage(getActivity(), "Current model doesn't have any default key binded. Bind new keys or select any other available model.");
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        //detailed provisioning case
                    }
                }
            }
        }

        return bindedAppKeys;
    }

    /**
     * @param status
     * @param isAddKey True : False ? Binding Case : Unbind Case.
     */
    public void updateModelAfterBindingKey(ApplicationParameters.Status status, final boolean isAddKey) {
        if (status == ApplicationParameters.Status.SUCCESS) {
            if (appKeyAssign != null) {
                ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Updating Model.." + modelSelected.getModelName(), true);
                boolean isAlreadyAssigned = false;
                if(modelSelected.getBind() != null)
                {
                    for (int i = 0; i < modelSelected.getBind().size(); i++) {
                        if (modelSelected.getBind().get(i) == appKeyAssign.getIndex()) {
                            isAlreadyAssigned = true;
                            if (!isAddKey) {
                                //unbind case
                                modelSelected.getBind().remove(i);
                            }
                            break;
                        }
                    }
                }


                //adding appkey index in selected model
                if (!isAlreadyAssigned) {
                    if (isAddKey) {
                        if(modelSelected.getBind() == null)
                        {
                            ArrayList<Integer> bindKey = new ArrayList<>();
                            bindKey.add(appKeyAssign.getIndex());
                            modelSelected.setBind(bindKey);
                        }
                        else
                        {
                            modelSelected.getBind().add(appKeyAssign.getIndex());
                        }
                    }
                } else {
                    //removing for unbind case
                    //modelSelected.setActiveKeyIndex();
                }

                //updating selected model having new app key
                for (int i = 0; i < element.getModels().size(); i++) {
                    if (element.getModels().get(i).getModelId().equalsIgnoreCase(modelSelected.getModelId())) {
                        element.getModels().set(i, modelSelected);
                    }
                }

                // update in json inside model and in node
                for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                    int nodeIndex = -1;
                    //updating element having model with new app key
                    for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                        if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress()
                                .equalsIgnoreCase(element.getUnicastAddress())) {
                            //update inside model
                            nodeIndex = i;
                            ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().set(j, element);
                            break;
                        }
                    }
                }

                String s = ParseManager.getInstance().toJSON(((MainActivity) getActivity()).meshRootClass);
                Utils.setBLEMeshDataToLocal(getActivity(), s);
                ((MainActivity) getActivity()).updateJsonData();
                getClonedMeshRoot();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isAddKey) {
                            //unbind case
                            updateRecyclerAfterBindingKeys(modelSelected, appKeyAssign, false);
                        } else {
                            updateRecyclerAfterBindingKeys(modelSelected, appKeyAssign, true);
                        }

                        ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Updating Model.." + modelSelected.getModelName(), false);
                    }
                }, 500);
            }
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(appKeyBindListAdapter != null) {
                        recyclerAppKey.setAdapter(appKeyBindListAdapter);
                    }
                }
            });
            Utils.showToast(getActivity(), "Invalid binding for model : " + modelSelected.getModelName());
        }

    }

    public void updateNodeAfterDeletingKey(ApplicationParameters.Status status, final boolean isDelete) {
        if (status == ApplicationParameters.Status.SUCCESS) {
            getClonedMeshRoot();
            //get the respective key and index
            //remove from local model object
            //remove from node
            //remove from all models inside element w.r.t node having index key assigned

            if(modelSelected.getBind() != null)
            {
                for (int i = 0; i < modelSelected.getBind().size(); i++) {
                    if (modelSelected.getBind().get(i) == appKeyAssign.getIndex()) {
                        modelSelected.getBind().remove(i);
                        break;
                    }
                }
            }


            for (int i = 0; i < clonedMeshRoot.getNodes().size(); i++) {
                //find the node
                String addr = "";
                if(element.getParentNodeAddress().contains("000"))
                {
                    addr = element.getParentNodeAddress().replace("000", "");
                }
                else
                {
                    addr = element.getParentNodeAddress();
                }

                if (clonedMeshRoot.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(addr)) {
                    //remove from node
                    for (int k = 0; k < clonedMeshRoot.getNodes().get(i).getAppKeys().size(); k++) {
                        if (clonedMeshRoot.getNodes().get(i).getAppKeys().get(k).getIndex() == appKeyAssign.getIndex()) {
                            clonedMeshRoot.getNodes().get(i).getAppKeys().remove(k);
                            break;
                        }
                    }
                    //remove from all models

                    for (int k = 0; k < clonedMeshRoot.getNodes().get(i).getElements().size(); k++) {
                        for (int l = 0; l < clonedMeshRoot.getNodes().get(i).getElements().get(k).getModels().size(); l++) {
                            if (clonedMeshRoot.getNodes().get(i).getElements().get(k).getModels().get(l).getBind() != null) {
                                for (int m = 0; m < clonedMeshRoot.getNodes().get(i).getElements().get(k).getModels().get(l).getBind().size(); m++) {
                                    if (clonedMeshRoot.getNodes().get(i).getElements().get(k).getModels().get(l).getBind().get(m) == appKeyAssign.getIndex()) {
                                        clonedMeshRoot.getNodes().get(i).getElements().get(k).getModels().get(l).getBind().remove(m);
                                        break;
                                    }
                                }
                            }
                        }

                        if(clonedMeshRoot.getNodes().get(i).getElements().get(k).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress()))
                        {
                            //overriding element data
                            element = clonedMeshRoot.getNodes().get(i).getElements().get(k);
                        }
                    }

                }
            }

            String s = ParseManager.getInstance().toJSON(clonedMeshRoot);
            Utils.setBLEMeshDataToLocal(getActivity(), s);
            ((MainActivity) getActivity()).updateJsonData();
            getClonedMeshRoot();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (popUpAppKeyBindListAdapter != null) {
                        for (int i = 0; i < popUpAppKeyList.size(); i++) {
                            if (popUpAppKeyList.get(i).getIndex() == appKeyAssign.getIndex()) {
                                popUpAppKeyList.remove(i);
                                //popUpAppKeyBindListAdapter.notifyItemRemoved(i);
                                if(popUpAppKeyBindListAdapter != null) {
                                    recyclerPopUpAppKeyBind.setAdapter(popUpAppKeyBindListAdapter);
                                }
                                break;
                            }
                        }
                        //popUpAppKeyBindListAdapter.notifyDataSetChanged();
                    }
                    updateRecyclerAfterBindingKeys(modelSelected, appKeyAssign, false);
                    Utils.showToast(getActivity(), "AppKey deletion success for node.");

                }
            }, 500);

        } else {
            Utils.showToast(getActivity(), "AppKey deletion failed for node.");
        }

    }

    private void popUpRecyclerAppKeyBind(RecyclerView recyclerAppKeyBind, final Dialog dialog) {

        //ArrayList<AppKey> appKeyList = ((MainActivity) getActivity()).meshRootClass.getAppKeys();
        popUpAppKeyList = getAppKeysAsPerNode();
        if (popUpAppKeyList == null) {
            return;
        }
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerAppKeyBind.setLayoutManager(gridLayoutManager);
        popUpAppKeyBindListAdapter = new AppKeyBindListAdapter(getActivity(), element, true, popUpAppKeyList, this.modelSelected.getBind(), new AppKeyBindListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, AppKey appkeySelected, boolean isChecked) {

                if (isChecked) {
                    //call bind api
                    //disable this dialog
                    //update in json
                    //update app key in library before toggling
                    dialog.dismiss();
                    appKeyAssign = appkeySelected;
                    ((MainActivity) getActivity()).showPopUpForProxy(getActivity(), "Binding App key for Index.." + appkeySelected.getIndex(), true);
                    //new BindAppKeyCallbackOld(getActivity(),appkeySelected.getKey(),element,appkeySelected.getIndex(), false);
                    //MainActivity.isCustomModelBinding = true;
                    Nodes nodeWithRequirModel = getNodeWithSelectedModel();
                    new BindAppKeyCallback(getActivity(), appKeyAssign, nodeWithRequirModel, false);
                } else {
                    // call unbind  api
                    // update in json
                    Utils.showToast(getActivity(), "Key Already Assigned.");

                }

            }

            @Override
            public void onUnbindRecyclerItem(View v, int position, AppKey appkey) {

                appKeyAssign = appkey;
                //new UnbindAppKeyCallback(getActivity(),element, appKeyAssign, modelSelected);
            }
        },"elementpopup");
        recyclerAppKeyBind.setAdapter(popUpAppKeyBindListAdapter);
        itemTouchPopUpHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, popUpAppKeyBindListAdapter);
        new ItemTouchHelper(itemTouchPopUpHelperCallback).attachToRecyclerView(recyclerAppKeyBind);
        //Utils.calculateHeight(models.size(), recyclerAppKey);
    }

    private ArrayList<AppKey> getAppKeysAsPerNode() {

        if (modelSelected == null)
            return null;

        getClonedMeshRoot();
        ArrayList<AppKey> appKeyList = new ArrayList<>();
        for (int i = 0; i < clonedMeshRoot.getNodes().size(); i++) {
            for (int j = 0; j < clonedMeshRoot.getNodes().get(i).getElements().size(); j++) {
                if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                    //retreive models w.r.t element
                    //keys wrt node
                    ArrayList<AppKey> appKeysAddedBefore = clonedMeshRoot.getNodes().get(i).getAppKeys();
                    try {
                        for (int k = 0; k < clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().size(); k++) {
                            if (clonedMeshRoot.getNodes().get(i).getElements().get(j).getModels().get(k).getModelId().equals(modelSelected.getModelId())) {
                                for (int l = 0; l < appKeysAddedBefore.size(); l++) {
                                    appKeysAddedBefore.get(l).setChecked(false); //default
                                    for (int m = 0; m < modelSelected.getBind().size(); m++) {
                                        if(appKeysAddedBefore.get(l).getIndex() == modelSelected.getBind().get(m))
                                        {
                                            appKeysAddedBefore.get(l).setChecked(true);
                                        }
                                    }
                                }
                                appKeyList = new ArrayList<>(appKeysAddedBefore);
                            }
                        }
                    } catch (Exception e) {
                        if(appKeysAddedBefore != null && !appKeysAddedBefore.isEmpty())
                        {
                            Utils.showToast(getActivity(), "Bind Some New Keys.");
                            appKeyList = new ArrayList<>(appKeysAddedBefore);
                        }
                        else
                        {
                            Utils.showToast(getActivity(), "Add New Keys.");
                        }
                    }
                }
            }
        }
        return appKeyList;
    }

    private Nodes getNodeWithSelectedModel() {

        Element elementBySelectedModel = new Element();
        ArrayList<Model> models = new ArrayList<>();
        models.add(modelSelected);
        elementBySelectedModel.setModels(models);
        elementBySelectedModel.setParentNodeName(element.getParentNodeName());
        elementBySelectedModel.setParentNodeAddress(element.getParentNodeAddress());
        elementBySelectedModel.setUnicastAddress(element.getUnicastAddress());
        elementBySelectedModel.setChecked(element.isChecked());
        elementBySelectedModel.setConfigured(element.isConfigured);
        elementBySelectedModel.setAppKeys(element.getAppKeys());
        //elementBySelectedModel.

        Nodes node = new Nodes(0);
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(elementBySelectedModel);
        node.setElements(elements);

        return node;
    }

    private void initRecyclerForModels() {
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerModel.setLayoutManager(gridLayoutManager);
        modelListAdapter = new ModelListAdapter(getActivity(), models/*, modelSelected.getBind()*/, new ModelListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, Model model) {

                modelSelected = model;
                modelSelected.setChecked(true);
                getSubscriptionData(model);
                getPublicationData(model);
                initRecyclerForBindKeys(model, recyclerAppKey);
                txtModelName.setText(model.getModelName());
                recyclerModel.setVisibility(View.GONE);
            }
        });
        recyclerModel.setAdapter(modelListAdapter);
        Utils.calculateHeight(models.size(), recyclerModel);
    }

    private void getSubscriptionData(Model modelSelected) {

        subscriptionList.clear();
        try {
            List<String> subscriptionData = new ArrayList<>();
            if(modelSelected.getSubscribe() != null && modelSelected.getSubscribe().size() > 0)
            {
                for (int l = 0; l < modelSelected.getSubscribe().size(); l++) {
                    if ("c000".equalsIgnoreCase(String.valueOf(modelSelected.getSubscribe().get(l)))) {
                        subscriptionData.add("c000");
                    } else {
                        subscriptionData.add(String.valueOf(modelSelected.getSubscribe().get(l)));
                    }

                }
            }

            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getGroups().size(); i++) {
                Group grp = new Group();
                grp.setName(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getName());
                grp.setAddress(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getAddress());
                grp.setChecked(false);
                for (int j = 0; j < subscriptionData.size(); j++) {
                    if (((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getAddress().equalsIgnoreCase(subscriptionData.get(j))) {
                        grp.setChecked(true);
                        break;
                    }
                }
                subscriptionList.add(grp);
            }
        } catch (Exception e) {
        }

        initRecyclerForSubecribers();
    }

    private void initRecyclerForSubecribers() {
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        subscriptionRecycler.setLayoutManager(gridLayoutManager);
        subscriptionListAdapter = new SubscriptionListAdapter(getActivity(), subscriptionList, new SubscriptionListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, Group grp) {

                Utils.setSettingType(getActivity(), getString(R.string.str_element_setting));
                selectedPosition = position;
                mobleAddress addr = mobleAddress.groupAddress(Integer.parseInt(grp.getAddress(), 16));
                String replaceModelName = modelSelected.getModelName().replace(" ", "_");
                ApplicationParameters.GenericModelID modelSelected =  ModelRepository.getInstance().getModelSelected(replaceModelName);

                if(subscriptionList.get(position).isChecked())
                {
                    //unsubscribe case
                    //Utils.DEBUG(">>>Group Remove : " + " addr : " + addr + " mAutoAddress :" + mAutoAddress + " Unicast : " + element.getUnicastAddress());
                    Utils.removeElementFromGroup(getActivity(), addr, element, modelSelected);
                }
                else
                {
                    //subscribe case
                    Utils.addElementToGroup(getActivity(), modelSelected, addr, element);
                }

            }
        });
        subscriptionRecycler.setAdapter(subscriptionListAdapter);
        Utils.calculateHeight(subscriptionList.size(), subscriptionRecycler);
    }

    public void updateSubscribedData(ApplicationParameters.Status status)
    {
        if (status == ApplicationParameters.Status.SUCCESS) {
            //update data
            if(modelSelected.getSubscribe()!= null && modelSelected.getSubscribe().size() > 0)
            {
                if(subscriptionList.get(selectedPosition).isChecked())
                {
                    boolean isPresent = false;
                    int position = 0;
                    for (int i = 0; i < modelSelected.getSubscribe().size(); i++) {
                        if(modelSelected.getSubscribe().get(i).equalsIgnoreCase(subscriptionList.get(selectedPosition).getAddress()))
                        {
                            isPresent = true;
                            position = i;
                            break;
                        }
                    }

                    Utils.showPopUpForMessage(getActivity(), "Un-Subscribed Successfully.");
                    modelSelected.getSubscribe().remove(position);
                }
                else
                {
                    Utils.showPopUpForMessage(getActivity(), "Subscribed Successfully.");
                    modelSelected.getSubscribe().add(subscriptionList.get(selectedPosition).getAddress());
                }
            }
            else
            {
                Utils.showPopUpForMessage(getActivity(), "Subscribed Successfully.");

                ArrayList<String> grpAddress = new ArrayList<>();
                grpAddress.add(subscriptionList.get(selectedPosition).getAddress());
                modelSelected.setSubscribe(grpAddress);
            }

            if(element.getModels() != null)
            {
                for (int i = 0; i < element.getModels().size(); i++) {
                    if(element.getModels().get(i).getModelName().equalsIgnoreCase(modelSelected.getModelName()))
                    {
                        element.getModels().set(i, modelSelected);
                        break;
                    }
                }
            }

            // update in json inside model and in node
            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                int nodeIndex = -1;
                //updating element having model with new app key
                for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j).getUnicastAddress().equalsIgnoreCase(element.getUnicastAddress())) {
                        //update inside model
                        nodeIndex = i;
                        ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().set(j, element);
                        break;
                    }
                }
            }

            String s = ParseManager.getInstance().toJSON(((MainActivity) getActivity()).meshRootClass);
            Utils.setBLEMeshDataToLocal(getActivity(), s);
            ((MainActivity) getActivity()).updateJsonData();
            //update grouptab
            ((MainActivity)getActivity()).fragmentCommunication(new GroupTabFragment().getClass().getName(), null, 0, null, false, null);

            subscriptionList.get(selectedPosition).setChecked(subscriptionList.get(selectedPosition).isChecked() ? false : true);

        }
        else {
            Utils.showPopUpForMessage(getActivity(), "Operation failed.");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                subscriptionListAdapter.notifyDataSetChanged();
            }
        }, 500);

    }

    private void getPublicationData(Model modelSelected) {

        elePublicationList.clear();

        try {
            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {
                    boolean isProvisioner = false;
                    for (int k = 0; k < ((MainActivity) getActivity()).meshRootClass.getProvisioners().size(); k++) {
                        if (((MainActivity) getActivity()).meshRootClass.getProvisioners().get(k).getUUID().equalsIgnoreCase(((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getUUID())) {
                            isProvisioner = true;
                            break;
                        }
                    }
                    //if (!isProvisioner)
                    {
                        Element element = ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(j);

                        Publish publish = new Publish();
                        publish.setAddress(element.getUnicastAddress());
                        publish.setName(element.getName() != null ? element.getName() : ""+element.getUnicastAddress());
                        publish.setCurrentParentAddress(((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(0).getParentNodeAddress());
                        publish.setCurrentParentNodeName(((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getName());
                        publish.setTypeNode(true);
                        elePublicationList.add(publish);
                    }
                }
            }

            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getGroups().size(); i++) {
                Publish publish = new Publish();
                publish.setAddress(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getAddress());
                publish.setName(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getName());
                publish.setTypeNode(false);
                elePublicationList.add(publish);
            }

            if (elePublicationList.size() > 0) {
                for (int i = 0; i < elePublicationList.size(); i++) {

                    //Currently According to note 1 : as explained in utils
                    //In future here model will be looped according to note 2 becouse in future each model publish will be different.
                    if (modelSelected.getPublish().getAddress().equalsIgnoreCase(elePublicationList.get(i).getAddress())) {
                        elePublicationList.get(i).setChecked(true);
                    } else {
                        elePublicationList.get(i).setChecked(false);
                    }
                }
            }
        } catch (Exception e) {
        }
        initRecyclerForPublishers();
    }

    public void initRecyclerForPublishers() {
        if (elePublicationList.isEmpty()) {
            return;
        }

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        publishingRecycler.setLayoutManager(gridLayoutManager);
        publicationListAdapter = new PublicationListAdapter(getActivity(), elePublicationList, String.valueOf(peerAddress), "", null, new PublicationListAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View view, Publish item, String mAutoAddress, boolean isSelected, int position) {
                if (isSelected) {
                    selectedPosition = position;
                    Utils.DEBUG(">>>GNode : " + peerAddress + " elem" + element + " " + elePublicationList.get(selectedPosition).getAddress());
                    setPublicationForElement(peerAddress, element.getUnicastAddress(), selectedPosition, elePublicationList);
                }
            }
        });
        publishingRecycler.setAdapter(publicationListAdapter);
        Utils.calculateHeight(elePublicationList.size(), publishingRecycler);
    }

    private void setPublicationForElement(mobleAddress nodeAddress, String elementAddress, int selected_position, ArrayList<Publish> publicationList) {

        // Since subscription and publication are present inside model.
        // So from here it is needed to provide model object for this element.
        //Utils.contextU = AddGroupActivity.this;
        Utils.isPublicationStart = true;

        String replaceModelName = modelSelected.getModelName().replace(" ", "_");
        ApplicationParameters.GenericModelID modelSelected =  ModelRepository.getInstance().getModelSelected(replaceModelName);
        Utils.setPublicationForElement(getActivity(), modelSelected, nodeAddress, mobleAddress.deviceAddress(Integer.parseInt(element.getUnicastAddress())), selected_position, publicationList);

    }

    public void updateUi(ApplicationParameters.Status status) {

        if (status == ApplicationParameters.Status.SUCCESS) {
            //update model data in json
            elePublicationList.get(selectedPosition).setChecked(true);
            uncheckedOtherPublishers(selectedPosition);

            Publish currentPublisherSelected = new Publish();
            currentPublisherSelected.setCurrentParentNodeName(elePublicationList.get(selectedPosition).getCurrentParentNodeName());
            currentPublisherSelected.setChecked(elePublicationList.get(selectedPosition).isChecked());
            currentPublisherSelected.setAddress(elePublicationList.get(selectedPosition).getAddress());
            currentPublisherSelected.setTypeNode(elePublicationList.get(selectedPosition).isTypeNode());
            currentPublisherSelected.setName(elePublicationList.get(selectedPosition).getName());
            currentPublisherSelected.setCredentials(elePublicationList.get(selectedPosition).getCredentials());
            currentPublisherSelected.setIndex(elePublicationList.get(selectedPosition).getIndex());
            currentPublisherSelected.setPeriod(elePublicationList.get(selectedPosition).getPeriod());
            currentPublisherSelected.setRetransmit(elePublicationList.get(selectedPosition).getRetransmit());
            currentPublisherSelected.setTtl(elePublicationList.get(selectedPosition).getTtl());

            modelSelected.setPublish(currentPublisherSelected);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Utils.json_UpdatePublisherForCurrentModel(getActivity(), ((MainActivity) getActivity()).meshRootClass, String.valueOf(peerAddress), element.getUnicastAddress(), elePublicationList, modelSelected);
                    ((MainActivity) getActivity()).updateJsonData();
                    //sync meshRootObject for provisioned list
                    ((MainActivity)getActivity()).updateProvisionedTab(null, 0);

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    publicationListAdapter.notifyDataSetChanged();
                    Utils.showPopUpForMessage(getActivity(), "Published Successfully.");
                }
            }, 200);
        } else {
            //if publication failed then reset ui again
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    publicationListAdapter.notifyDataSetChanged();
                    Utils.showPopUpForMessage(getActivity(), "Publication Failed or Timeout.");
                }
            }, 100);
        }
    }

    private void uncheckedOtherPublishers(int position) {

        for (int i = 0; i < elePublicationList.size(); i++) {
            if (!elePublicationList.get(i).getAddress().equalsIgnoreCase(elePublicationList.get(position).getAddress())) {
                elePublicationList.get(i).setChecked(false);
            }
        }
    }

    public void openAppKeyDialog()
    {
        showPopUpForKeys(getActivity(),element);
    }

    public void showPopUpForKeys(final Context context, Element element) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_appkeybind);
        TextView txtNote = (TextView) dialog.findViewById(R.id.txtNote);
        txtNote.setText(getActivity().getString(R.string.str_label_keybindingnote) + " " + modelSelected.getModelName());
        TextView txt = (TextView) dialog.findViewById(R.id.txtBindNow);
        recyclerPopUpAppKeyBind = (RecyclerView) dialog.findViewById(R.id.recyclerAppKeyBind);
        popUpRecyclerAppKeyBind(recyclerPopUpAppKeyBind, dialog);
        if (!dialog.isShowing()) {
            dialog.show();
        }

        TextView txtGenerateNewKey = (TextView) dialog.findViewById(R.id.txtMakeNewKey);
        txtGenerateNewKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                MainActivity.isCustomAppKeyShare = true;
                Utils.moveToFragment(getActivity(), new AddAppKeyFragment(), ElementSettingFragment.this.element, 0);
            }
        });


        try {
            if (Integer.parseInt(element.getParentNodeAddress()) != Integer.parseInt(element.getUnicastAddress())) {
                //txtGenerateNewKey.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void updateElementName() {
         Utils.updateElementNameInJson(getActivity(), edtElementName.getText().toString(),element.getUnicastAddress());
        ((MainActivity)getActivity()).updateProvisionedTab(null, 0);
        ((MainActivity)getActivity()).onBackPressed();
    }
}
