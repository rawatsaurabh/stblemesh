/**
 * *****************************************************************************
 *
 * @file ProvisionedTabFragment.java
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

package com.st.bluenrgmesh.view.fragments.tabs;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.LightControlModeClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.ProvisionedRecyclerAdapter;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;


public class ProvisionedTabFragment extends BaseFragment {

    private static ProvisionedTabFragment fragment;
    private View view;
    private RecyclerView recyclerView;
    private AppDialogLoader loader;
    private ProvisionedRecyclerAdapter provisionedRecyclerAdapter;
    private SwipeRefreshLayout swiperefresh;
    private static ArrayList<Nodes> nodes = new ArrayList<>();
    private EditText setpropertyIDET;
    private LightControlModeClient mLightLCModeModelClient = null;
    private Animation animation;
    private LinearLayout lc_LL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_provisioned, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        initUi();

        return view;
    }

    private void initUi() {
        //Only for testing - LC Mode
        lc_LL  = view.findViewById(R.id.lc_LL);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        Button testlc = view.findViewById(R.id.testlc);
        Button testlcOn = view.findViewById(R.id.testlcOn);
        Button testlcOFF = view.findViewById(R.id.testlcOFF);
        Button occupancyON = view.findViewById(R.id.occupancyON);
        Button occupancyOFF = view.findViewById(R.id.occupancyOFF);
        Button testlcproperty = view.findViewById(R.id.testlcproperty);
        setpropertyIDET = view.findViewById(R.id.setpropertyIDET);
       

        occupancyON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOccupancy(1);

            }
        });

        occupancyOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOccupancy(2);

            }
        });
        testlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLCModeCommand();

            }
        });

        testlcOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLCOFFCommand();

            }
        });
        testlcOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLCONCommand();

            }
        });

        testlcproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLCProperty();

            }
        });
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.ST_primary_blue), getResources().getColor(R.color.ST_primary_blue), getResources().getColor(R.color.ST_primary_blue));
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateProvisioneRecycler(null, 0, null);
                    }
                }
        );



        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setProvisionAdapter(null, false);
            }
        });
    }

    private void showLcModeView() {
        if (getResources().getBoolean(R.bool.bool_isLCMODE)) {
            lc_LL.setVisibility(View.VISIBLE);
        } else {
            lc_LL.setVisibility(View.GONE);

        }
    }

    private void setProvisionAdapter(String selected_element_address, boolean is_command_error) {

        getNodesData();
        try {
            if (nodes != null) {
                Collections.sort(nodes);
            }
        } catch (Exception e) { }

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        /*RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        itemAnimator.setAddDuration(200);
        itemAnimator.setChangeDuration(20);
        itemAnimator.setRemoveDuration(100);
        recyclerView.setItemAnimator(itemAnimator);*/

        provisionedRecyclerAdapter = new ProvisionedRecyclerAdapter("node",getActivity(), new ProvisionedTabFragment().getClass().getName(), nodes, selected_element_address, is_command_error, new ProvisionedRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, String item, String mAutoAddress, boolean isSelected) {
                recyclerView.stopScroll();
                provisionedRecyclerAdapter.sortData();
                provisionedRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyAdapter(String selected_element_address, boolean is_command_error) {
                //getNodesData();
                setProvisionAdapter(selected_element_address, is_command_error);
            }

            @Override
            public void notifyPosition(int elementPosition, Nodes node) {
                // get element position w.r.t any touch event
            }
        });

        //Utils.runLayoutAnimation(recyclerView, provisionedRecyclerAdapter);
        recyclerView.setAdapter(provisionedRecyclerAdapter);


        loader.hide();
    }

    private void getNodesData() {

        if (nodes != null) nodes.clear();
        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (provisionedRecyclerAdapter != null)
                        provisionedRecyclerAdapter.notifyDataSetChanged();
                }
            });

            try {
                if (((MainActivity) getActivity()).meshRootClass.getNodes() != null && ((MainActivity) getActivity()).meshRootClass.getNodes().size() > 0) {
                    for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                        boolean isNodeIsProvisioner = false;
                        for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getProvisioners().size(); j++) {
                            if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity) getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                                isNodeIsProvisioner = true;
                                break;
                            }
                        }

                        if (!isNodeIsProvisioner) {
                            boolean isAddedAlready = false;
                            if (nodes != null && nodes.size() > 0) {
                                for (int j = 0; j < nodes.size(); j++) {
                                    if (nodes.get(j).getElements().get(0).getUnicastAddress().equalsIgnoreCase(((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress())) {
                                        isAddedAlready = true;
                                    }
                                }
                            }
                            if (!isAddedAlready)
                            {
                                nodes.add(((MainActivity) getActivity()).meshRootClass.getNodes().get(i));
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    /**
     * Method is used to update provisioned nodes views.
     *
     * @param strData          : Represent MacAddress, UUID of Nodes
     * @param intCaseValue     : Any Case Value
     * @param deviceDiscovered
     */
    public synchronized void updateProvisioneRecycler(final String strData, final int intCaseValue, final Nodes deviceDiscovered) {
        if (getActivity() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //loader.show();
                    try {
                        if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_PROXY_UPDATE)) {
                            //Proxy Changed becouse of removing, adding, disconnection.
                            //Case for enable and disable proxy
                            updateProxyNode(strData, deviceDiscovered);
                        } else if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_FEATURES_UPDATE)) {
                            getNodesData();
                            //updateNodeFeaturesData(strData);
                            updateNodeList(intCaseValue,/*uuid*/ strData);
                        } else if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_REMOVED)
                                || intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_ADDED)
                                || intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_REFRESH)) {
                            if (deviceDiscovered != null) {
                                //nodes.add(deviceDiscovered);
                                //updateProvisioneRecycler(null, 0, null);
                                getNodesData();
                            } else {
                                getNodesData();
                            }

                            updateNodeList(intCaseValue,/*uuid*/ strData);
                        } else if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_HEARTBEAT)) {
                            Log.e("heartbeat==>", "updateProvisioneRecycler address ==>>" + strData);
                            updateHeartBeat(strData);
                        } else {
                            getNodesData();
                            updateNodeList(intCaseValue,/*uuid*/ strData);
                        }
                    } catch (Exception e) {

                    }
                }
            });


        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(provisionedRecyclerAdapter != null)
                {
                    if(provisionedRecyclerAdapter.getItemCount() > 0) {
                        //if(deviceDiscovered != null)
                        {
                            int size = provisionedRecyclerAdapter.getItemCount();
                            if(nodes.size() == size)
                            {
                                //update item
                                provisionedRecyclerAdapter.notifyItemChanged(intCaseValue);
                            }
                            else {
                                //insert item
                                provisionedRecyclerAdapter.notifyItemInserted(size);
                            }
                            //recyclerView.smoothScrollToPosition(provisionedRecyclerAdapter.getItemCount());
                        }
                    }
                    else {
                        provisionedRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });*/

            if (swiperefresh != null) {
                swiperefresh.setRefreshing(false);
            }
        }
        //loader.hide();

    }

    private void updateNodeFeaturesData(String uuid) {

        try {
            if (((MainActivity) getActivity()).meshRootClass.getNodes() != null && ((MainActivity) getActivity()).meshRootClass.getNodes().size() > 0) {
                for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                    boolean isNodeIsProvisioner = false;
                    for (int j = 0; j < ((MainActivity) getActivity()).meshRootClass.getProvisioners().size(); j++) {
                        if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getUUID().equalsIgnoreCase(((MainActivity) getActivity()).meshRootClass.getProvisioners().get(j).getUUID())) {
                            isNodeIsProvisioner = true;
                            break;
                        }
                    }

                    if (!isNodeIsProvisioner) {
                        if (nodes.size() > 0) {
                            for (int j = 0; j < nodes.size(); j++) {
                                if (nodes.get(j).getUUID().equalsIgnoreCase(uuid)) {
                                    nodes.set(j, ((MainActivity) getActivity()).meshRootClass.getNodes().get(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void updateNodeList(int intCaseValue, String uuid) {

        if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_REMOVED)) {
            if (uuid != null) {
                try {
                    for (int i = 0; i < provisionedRecyclerAdapter.getItemCount(); i++) {
                        RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                        if (v.getTag().toString().equals(uuid)) {
                            final int finalI = i;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    provisionedRecyclerAdapter.notifyItemRemoved(finalI);
                                }
                            });
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }
        } else if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_NODE_ADDED)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //provisionedRecyclerAdapter.notifyDataSetChanged();
                    provisionedRecyclerAdapter.notifyItemInserted(nodes.size());
                }
            });
        } else if (intCaseValue == getActivity().getResources().getInteger(R.integer.PROVISIONED_FEATURES_UPDATE)) {
            if (uuid != null) {
                try {

                    for (int i = 0; i < provisionedRecyclerAdapter.getItemCount(); i++) {
                        RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                        if (v != null && v.getTag().toString().equals(uuid)) {
                            final int finalI = i;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    provisionedRecyclerAdapter.notifyItemChanged(finalI);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                }
            }
        } else {
            //refresh
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    provisionedRecyclerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private synchronized void updateHeartBeat(String strData) {

        String uuid = null;
        if (((MainActivity) getActivity()).meshRootClass.getNodes().size() > 0) {
            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                try {
                    if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getElements().get(0).getUnicastAddress().equalsIgnoreCase(strData)) {
                        uuid = ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getUUID();
                    }
                } catch (Exception e) {
                }
            }
        }

        /*if (uuid != null) {
            try {
                for (int i = 0; i < provisionedRecyclerAdapter.getItemCount(); i++) {
                    RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                    if (v.getTag().toString().equals(uuid)) {
                        final int finalI = i;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                provisionedRecyclerAdapter.notifyItemChanged(finalI);
                            }
                        });
                    }
                }
            } catch (Exception e) {
            }
        }*/

        if (uuid != null) {
            try {
                for (int i = 0; i < provisionedRecyclerAdapter.getItemCount(); i++) {
                    RelativeLayout v1 = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                    if (v1.getTag().toString().equals(uuid)) {
                        final int finalI = i;
                        RelativeLayout v2 = (RelativeLayout) v1.getChildAt(i);
                        RelativeLayout v3 = (RelativeLayout) v2.getChildAt(i);
                        RelativeLayout v4 = (RelativeLayout) v3.getChildAt(i);
                        ImageView heartImageView = (ImageView)v4.getChildAt(2);
                        Utils.DEBUG(">>> Heart Beat : " + i);
                        setHeartBeatAnimation(heartImageView);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void setHeartBeatAnimation(ImageView imgView)
    {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgView.startAnimation(animation);
                    }
                });



                /*ObjectAnimator scaleDown;
                scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                        imgView,
                        PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.2f));
                scaleDown.setDuration(310);
                scaleDown.setRepeatCount(ObjectAnimator.RESTART);
                scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
                scaleDown.setAutoCancel(true);
                scaleDown.start();*/
            }
        });
    }

    private synchronized void updateProxyNode(String strData, Nodes deviceDiscovered) {

        //strData : UUID

        String uuid = null;
        if (((MainActivity) getActivity()).meshRootClass.getNodes().size() > 0) {
            for (int i = 0; i < ((MainActivity) getActivity()).meshRootClass.getNodes().size(); i++) {
                try {
                    if (((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getAddress().equalsIgnoreCase(strData)) {
                        uuid = ((MainActivity) getActivity()).meshRootClass.getNodes().get(i).getUUID();
                    }
                } catch (Exception e) {
                }
            }
        }

        if (uuid != null) {
            try {
                for (int i = 0; i < provisionedRecyclerAdapter.getItemCount(); i++) {
                    RelativeLayout v = (RelativeLayout) recyclerView.getLayoutManager().findViewByPosition(i);
                    if (v.getTag().toString().equals(uuid)) {
                        final int finalI = i;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                provisionedRecyclerAdapter.notifyItemChanged(finalI);
                                recyclerView.scrollToPosition(finalI);
                                //provisionedRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static ProvisionedTabFragment newInstance() {

        if (fragment == null) {
            fragment = new ProvisionedTabFragment();
        }
        return fragment;
    }

    private void sendOccupancy(int i) {
        mLightLCModeModelClient = ((MainActivity) getActivity()).app.mConfiguration.getNetwork().getLightnessLCModel();
        ApplicationParameters.TID tid = new ApplicationParameters.TID(1);


        ApplicationParameters.Address address = new ApplicationParameters.Address(2);
        ApplicationParameters.Mode mode = new ApplicationParameters.Mode(i);

        mLightLCModeModelClient.setLightLCOM(false, address, mode, null);

    }

    LightControlModeClient.LightLCOMStatusCallback lightLCOMStatusCallback = new LightControlModeClient.LightLCOMStatusCallback() {
        @Override
        public void onLCOMStatus(boolean b, ApplicationParameters.Mode mode) {
            Log.d("onLCOMStatus", "occupancy =>" + mode);

        }
    };

    private void sendLCONCommand() {
        mLightLCModeModelClient = ((MainActivity) getActivity()).app.mConfiguration.getNetwork().getLightnessLCModel();


        ApplicationParameters.Address address = new ApplicationParameters.Address(2);
        ApplicationParameters.OnOff onOff = ApplicationParameters.OnOff.ENABLED;
        ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
        ApplicationParameters.Delay del = new ApplicationParameters.Delay(0);
        ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(getActivity()));
        mLightLCModeModelClient.setLightControlLightOnOff(false, address, onOff, tid, null, null, null);
    }

    private void sendLCOFFCommand() {
        mLightLCModeModelClient = ((MainActivity) getActivity()).app.mConfiguration.getNetwork().getLightnessLCModel();
        ApplicationParameters.TID tid = new ApplicationParameters.TID(Utils.getTIDValue(getActivity()));


        ApplicationParameters.Address address = new ApplicationParameters.Address(2);
        ApplicationParameters.OnOff onOff = ApplicationParameters.OnOff.DISABLED;
        ApplicationParameters.Time transitionTime = ApplicationParameters.Time.NONE;
        ApplicationParameters.Delay del = new ApplicationParameters.Delay(0);

        mLightLCModeModelClient.setLightControlLightOnOff(false, address, onOff, tid, null, null, null);
    }

    LightControlModeClient.LightLCOnOfflStatusCallback LightLCOnOfflStatusCallback = new LightControlModeClient.LightLCOnOfflStatusCallback() {
        @Override
        public void onLightLCOnOffStatus(boolean b, ApplicationParameters.OnOff onOff, ApplicationParameters.OnOff onOff1, ApplicationParameters.Time time) {
            Log.d("LC mode", "onoff=>" + onOff);

        }
    };

    private void sendLCModeCommand() {
        mLightLCModeModelClient = ((MainActivity) getActivity()).app.mConfiguration.getNetwork().getLightnessLCModel();


        ApplicationParameters.Address address = new ApplicationParameters.Address(2);

        ApplicationParameters.Mode mode = new ApplicationParameters.Mode(1);

        mLightLCModeModelClient.setLightLCMode(false, address, mode, null);

    }

    LightControlModeClient.LightLCModeStatusCallback lightLCModeStatusCallback = new LightControlModeClient.LightLCModeStatusCallback() {
        @Override
        public void onLCModeStatus(boolean b, ApplicationParameters.Mode mode) {
            Log.d("LC mode", "mode=>" + mode);
        }
    };


    private void sendLCProperty() {
        mLightLCModeModelClient = ((MainActivity) getActivity()).app.mConfiguration.getNetwork().getLightnessLCModel();


        ApplicationParameters.Address address = new ApplicationParameters.Address(2);

        ApplicationParameters.PropertyID propertyID = new ApplicationParameters.PropertyID(Integer.parseInt(setpropertyIDET.getText().toString()));

        mLightLCModeModelClient.setLightLCProperty(false, address, propertyID, null);

    }

    LightControlModeClient.LightLCPropertylStatusCallback lightLCPropertylStatusCallback = new LightControlModeClient.LightLCPropertylStatusCallback() {
        @Override
        public void onLightLCPropertyStatus(boolean b, ApplicationParameters.PropertyID propertyID) {
            Log.d("LC mode", "propertyID=>" + propertyID);

        }
    };
}
