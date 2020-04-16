
/**
 * *****************************************************************************
 *
 * @file ConfigurationFragment.java
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
 */package com.st.bluenrgmesh.view.fragments.setting.configuration;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAssignedNumbers;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.config.PublicationListForProvisionAdapter;
import com.st.bluenrgmesh.adapter.config.SubscriptionListForProvisoningAdapter;
import com.st.bluenrgmesh.callbacks.configuration.PublicationCallback;
import com.st.bluenrgmesh.callbacks.configuration.SubscriptionCallback;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Group;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.AppSingletonDialog;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;

public class ConfigurationFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private static final int RESULT_CODE = 200;
    private RecyclerView publishingRV, subscriptionRV;
    private Button addconfigBT;
    private SubscriptionListForProvisoningAdapter subListForGroupAdapter;
    private PublicationListForProvisionAdapter publicationRecyclerAdapter;
    private ConfigurationModelClient mConfigModel;
    private Nodes nodeData ;
    public String mCid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_provision_add_configuration, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUI();
        setAdapterForSubscriptionList();
        setAdapterForPublicationList();

        return view;
    }

    private void initUI() {

        try {
            nodeData = (Nodes) getArguments().getSerializable(getString(R.string.key_serializable));
            ((MainActivity)getActivity()).currentNode = nodeData;
            Utils.DEBUG(">> Json Data : " + ParseManager.getInstance().toJSON(((MainActivity)getActivity()).meshRootClass));
        }catch (Exception e){}
        mConfigModel = mobleNetwork.getConfigurationModelClient();
        subscriptionRV = (RecyclerView) view.findViewById(R.id.subscriptionRV);
        publishingRV = (RecyclerView) view.findViewById(R.id.publishingRV);
        addconfigBT = (Button) view.findViewById(R.id.addconfigBT);
        addconfigBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                * The nodeData object will be treated as separately for parallel operation of
                * subscription and publication. After the completion of both the process asynchronously
                * the nodeData object will be updated as one single object by merging pub and sub status.
                * */
                final AppSingletonDialog appSingletonDialog = new AppSingletonDialog(getActivity(), nodeData);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SubscriptionCallback subscriptionCallback = new SubscriptionCallback(getActivity(), nodeData, appSingletonDialog);
                        subscriptionCallback.startSubscription();
                    }
                }, 100);
            }
        });

        if(getResources().getBoolean(R.bool.bool_isAutoProvisioning) && Utils.isAutoProvisioning(getActivity()))
        {
            final AppSingletonDialog appSingletonDialog = new AppSingletonDialog(getActivity(), nodeData);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SubscriptionCallback subscriptionCallback = new SubscriptionCallback(getActivity(), nodeData, appSingletonDialog);
                    subscriptionCallback.startSubscription();
                }
            }, 100);
        }
    }

    private void setAdapterForSubscriptionList() {
        ArrayList<Group> final_subscriptionList = new ArrayList<>();
        if (((MainActivity)getActivity()).meshRootClass != null && ((MainActivity)getActivity()).meshRootClass.getGroups() != null && ((MainActivity)getActivity()).meshRootClass.getGroups().size() > 0) {
            final_subscriptionList = ((MainActivity)getActivity()).meshRootClass.getGroups();
        }
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        subscriptionRV.setLayoutManager(gridLayoutManager);
        subListForGroupAdapter = new SubscriptionListForProvisoningAdapter(getActivity(), final_subscriptionList, new SubscriptionListForProvisoningAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void notifyAdapter(int position) {
                subscriptionRV.stopScroll();
                subListForGroupAdapter.notifyDataSetChanged();

            }
        });
        subscriptionRV.setAdapter(subListForGroupAdapter);
        //Utils.calculateHeight(meshRootClass.getGroups().size(), subscriptionRV);
    }

    private void setAdapterForPublicationList() {
        ArrayList<Group> final_publicationNameList = new ArrayList<>();
        try {
            if (((MainActivity) getActivity()).meshRootClass != null) {

                if (((MainActivity) getActivity()).meshRootClass.getGroups() != null && ((MainActivity) getActivity()).meshRootClass.getGroups().size() > 0) {
                    for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getGroups().size(); i++) {

                        Group grp = new Group();
                        grp.setAddress(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getAddress());
                        grp.setName(((MainActivity) getActivity()).meshRootClass.getGroups().get(i).getName());

                        final_publicationNameList.add(grp);

                    }
                }

                if (((MainActivity) getActivity()).meshRootClass.getNodes() != null && ((MainActivity) getActivity()).meshRootClass.getNodes().size() > 0) {
                    for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {

                        for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().size(); j++) {

                            //avoid provisioner data from listing
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
                                Group grp = new Group();
                                grp.setAddress(element.getUnicastAddress());
                                String elementName =  element.getName() != null ? element.getName() : element.getUnicastAddress();
                                grp.setName(((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getName() + " / " + elementName);
                                final_publicationNameList.add(grp);
                            }

                        }
                    }
                }
            }

            NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
            publishingRV.setLayoutManager(gridLayoutManager);
            publicationRecyclerAdapter = new PublicationListForProvisionAdapter(getActivity(), final_publicationNameList, new PublicationListForProvisionAdapter.IRecyclerViewHolderClicks() {
                @Override
                public void notifyAdapter(int position) {
                    publishingRV.post(new Runnable()
                    {
                        @Override
                        public void run() {
                            publishingRV.stopScroll();
                            publicationRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

                }
            });
            publishingRV.setAdapter(publicationRecyclerAdapter);
        }catch (Exception e){}
        //  Utils.calculateHeight(subscriptionList.size(), publishingRecycler);

    }
}
