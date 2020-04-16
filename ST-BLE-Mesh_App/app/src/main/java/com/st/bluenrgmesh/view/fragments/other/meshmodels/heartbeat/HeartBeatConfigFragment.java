/**
 * *****************************************************************************
 *
 * @file HeartbeatConfigFragment.java
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
package com.st.bluenrgmesh.view.fragments.other.meshmodels.heartbeat;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleNetwork;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.defaultAppCallback;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.InputFilterMinMax;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;



public class HeartBeatConfigFragment extends BaseFragment {

    private View view;
    private String countLog;
    private AppDialogLoader loader;
    private EditText txtDestinationAddress;
    private Spinner txtCountLog;
    private EditText txtPriodLog;
    private EditText txtTTL;
    private Button publishBT;
    private ConfigurationModelClient mConfigModel;
    private NodeSettings nodeSettings;
    private ApplicationParameters.Address targetAddress;
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_heartbeatconfig, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mConfigModel = mobleNetwork.getConfigurationModelClient();
        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new HeartBeatConfigFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {
        nodeSettings = (NodeSettings) getArguments().getSerializable(getString(R.string.key_serializable));
       // targetAddress = new ApplicationParameters.Address(Utils.convertHexToInt(nodeSettings.getNodesUnicastAddress()));
        targetAddress = new ApplicationParameters.Address(Integer.parseInt(nodeSettings.getNodesUnicastAddress()));
        String[] countLogSpinner = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                                                    "11", "12", "13", "14", "15", "16", "17", "255"};

        txtDestinationAddress = (EditText) view.findViewById(R.id.txtDestinationAddress);
        txtCountLog = (Spinner) view.findViewById(R.id.txtCountLog);
        txtPriodLog = (EditText) view.findViewById(R.id.txtPriodLog);

//      txtCountLog.setFilters(new InputFilter[]{new InputFilterMinMax("1", "17")});//0,255,1-17
        txtPriodLog.setFilters(new InputFilter[]{new InputFilterMinMax("0", "17")});//

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countLogSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtCountLog.setAdapter(adapter);

        txtCountLog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countLog = txtCountLog.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                countLog = "";
            }
        });

        txtTTL = (EditText) view.findViewById(R.id.txtTTL);
        publishBT = (Button) view.findViewById(R.id.publishBT);
        publishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(countLog.toString().equalsIgnoreCase("")) {
//                    txtCountLog.setError("Please select a correct value.");

                if(txtPriodLog.getText().toString().equalsIgnoreCase("")){
                    txtPriodLog.setError("Please enter a correct value.");

                }else if(txtTTL.getText().toString().equalsIgnoreCase("")){
                    txtTTL.setError("Please enter a correct value.");

                }else {
                    final ApplicationParameters.CountLog mCountLog = new ApplicationParameters.CountLog(Integer.parseInt(countLog));
                    final ApplicationParameters.PeriodLog mPeriodLog = new ApplicationParameters.PeriodLog(Integer.parseInt(txtPriodLog.getText().toString()));
                    final ApplicationParameters.TTL mTTL = new ApplicationParameters.TTL(Integer.parseInt(txtTTL.getText().toString()));
                    final ApplicationParameters.Features mFeatures = new ApplicationParameters.Features(0x0002);
                    final ApplicationParameters.KeyIndex mKeyIndex = new ApplicationParameters.KeyIndex(0);
                    final ApplicationParameters.Address heartbeatReciever = new ApplicationParameters.Address(Integer.parseInt(txtDestinationAddress.getText().toString()));

                    mConfigModel.setPublicationHeartBeat(targetAddress, heartbeatReciever, mCountLog, mPeriodLog, mTTL, mFeatures, mKeyIndex,
                            configHeartBeatPublicationStatusCallback_callback);
                }
            }
        });


    }

    final ConfigurationModelClient.ConfigHeartBeatPublicationStatusCallback configHeartBeatPublicationStatusCallback_callback = new
            ConfigurationModelClient.ConfigHeartBeatPublicationStatusCallback() {
        @Override
        public void onHeartBeatPublicationStatus(boolean timeout, ApplicationParameters.Status status, ApplicationParameters.Address address,
                                                 ApplicationParameters.CountLog countLog, ApplicationParameters.PeriodLog periodLog,
                                                 ApplicationParameters.TTL ttl, ApplicationParameters.Features features,
                                                 ApplicationParameters.NetKeyIndex netKeyIndex) {
            if (timeout) {
                Toast.makeText(context,"Timeout Error Occured!",Toast.LENGTH_LONG).show();
                UserApplication.trace("HeartBeat timeout occurred");
            } else {
                Toast.makeText(context,"HeartBeat Published to =>" + address ,Toast.LENGTH_LONG).show();

                UserApplication.trace("HeartBeatSuccess==>"+status);

            }
        }
    };
}
