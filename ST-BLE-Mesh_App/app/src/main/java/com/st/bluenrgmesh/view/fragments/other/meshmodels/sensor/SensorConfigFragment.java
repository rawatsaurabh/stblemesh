/**
 * *****************************************************************************
 *
 * @file SensorConfigFragment.java
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

package com.st.bluenrgmesh.view.fragments.other.meshmodels.sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.msi.moble.ApplicationParameters;
import com.msi.moble.SensorModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.adapter.models.SensorModelRecyclerAdapter;
import com.st.bluenrgmesh.callbacks.SensorModelCallbacks;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.meshdata.Element;
import com.st.bluenrgmesh.models.meshdata.Model;
import com.st.bluenrgmesh.models.sensor.CompleteSensorData;
import com.st.bluenrgmesh.models.sensor.SensorData;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class SensorConfigFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private Element elementData;
    private RecyclerView recyclerView;
    private SensorModelRecyclerAdapter sensorModelRecyclerAdapter;
    ArrayList<Model> requiredModelList = new ArrayList<>();
    private String modelType =  "fromSensorConfigScreen";
    private FloatingActionButton fab;
    private CompleteSensorData completeSensorData;
    private ArrayList<SensorData> sensorList = null;
    private LinearLayout txtRefresh;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_elementsensor_model, container, false);
        localBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("sensor_data_broadcast"));
        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new SensorConfigFragment().getClass().getName());
        loader.show();
        initUi();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null && mMessageReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            if (loader != null) {
                loader.dismiss();
            }
        }
    };

    private void initUi() {

        try{
            elementData = (Element) getArguments().getSerializable(getString(R.string.key_serializable));
            ((MainActivity)getActivity()).elementUnicast_Model = elementData.getUnicastAddress();
        }catch (Exception e){}

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.show();
                updateSensorCallback();
            }
        });
        fab.setVisibility(View.GONE);

        txtRefresh = (LinearLayout) view.findViewById(R.id.txtRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        initRecyclerAdapter();
        //loader.dismiss();
        //updateSensorCallback();

    }

    public void updateFragmentUi(String sensorData)
    {
        if(view == null)
            return;

        if(sensorData != null)
        {
            Utils.DEBUG("Complete Sensor Data : " + sensorData);

            if(completeSensorData == null)
            {
                try {
                    completeSensorData = ParseManager.getInstance().fromJSON(new JSONObject(sensorData), CompleteSensorData.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sensorList = completeSensorData.getSensorList();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerAdapter();
                        txtRefresh.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loader.dismiss();
                    }
                });
            }
            else
            {
                //update current list
                try {
                    completeSensorData = ParseManager.getInstance().fromJSON(new JSONObject(sensorData), CompleteSensorData.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < completeSensorData.getSensorList().size(); i++) {
                    for (int j = 0; j < sensorList.size(); j++) {
                        if(sensorList.get(j).getPropertyId() == completeSensorData.getSensorList().get(i).getPropertyId())
                        {
                            sensorList.set(j, completeSensorData.getSensorList().get(i));
                        }
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerAdapter();
                        txtRefresh.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loader.dismiss();
                    }
                });
            }

            loader.dismiss();
        }
    }

    public void updateSensorCallback() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SensorModelClient mSensorModelClient = MainActivity.network.getSensorModel();
                ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(((MainActivity)getActivity()).elementUnicast_Model));
                ((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("GetSensor command sent for==>"+elementAddress, LoggerConstants.TYPE_SEND);
                UserApplication.trace("Send getSensor Command Refresh icon on the Extended Sensor Model TAB >>");
                mSensorModelClient.getSensor(elementAddress,null, SensorModelCallbacks.mGetSensorStatusCallback);
            }
        });
    }

    private void initRecyclerAdapter() {

        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.supportsPredictiveItemAnimations();
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        sensorModelRecyclerAdapter = new SensorModelRecyclerAdapter(getActivity(), sensorList, modelType, new SensorModelRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(final ApplicationParameters.SensorDataPropertyId propertyID, int position) {
                //loader.show();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        if(completeSensorData.getSensorList().size() != 9)
                        {
                            Utils.DEBUG("GetSensor command sent");
                            Utils.setClassName(getActivity(), new SensorConfigFragment().getClass().getName());
                            SensorModelClient mSensorModelClient = ((MainActivity)getActivity()).network.getSensorModel();
                            ApplicationParameters.Address elementAddress = new ApplicationParameters.Address(Integer.parseInt(elementData.getUnicastAddress()));
                            ((MainActivity)getActivity()).mUserDataRepository.getNewDataFromRemote("GetSensor command sent for==>"+elementAddress, LoggerConstants.TYPE_SEND);
                            UserApplication.trace("Send getSensor Command Individual Click>>");
                          boolean is_timeout =  mSensorModelClient.getSensor(elementAddress,propertyID, SensorModelCallbacks.mGetSensorStatusCallback);
                        }

                    }
                });
            }

        });
        recyclerView.setAdapter(sensorModelRecyclerAdapter);
    }
}
