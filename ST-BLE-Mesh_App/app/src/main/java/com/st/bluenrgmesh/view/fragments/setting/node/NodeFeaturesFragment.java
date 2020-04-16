/**
 * *****************************************************************************
 *
 * @file NodesFeaturesFragment.java
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

package com.st.bluenrgmesh.view.fragments.setting.node;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;


public class NodeFeaturesFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private NodeSettings nodeSettings;
    private TextView tvProxy;
    private TextView tvRelay;
    private TextView tvFriend;
    private TextView tvLowPower;
    private ImageButton refreshProxy;
    private ImageButton refreshRelay;
    private ImageButton refreshFriend;
    private Switch switchProxy;
    private Switch switchRelay;
    private Switch switchFriend;
    private String nodesUnicastAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nodefeatures, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new NodeFeaturesFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {
        nodeSettings = (NodeSettings) getArguments().getSerializable(getString(R.string.key_serializable));
        Utils.DEBUG(nodeSettings.getNodesName() + " " + nodeSettings.getNodesUnicastAddress() + " >>>>  " + nodeSettings.getNodesMacAddress());

        tvProxy = (TextView) view.findViewById(R.id.tvProxy);
        tvRelay = (TextView) view.findViewById(R.id.tvRelay);
        tvFriend = (TextView) view.findViewById(R.id.tvFriend);
        tvLowPower = (TextView) view.findViewById(R.id.tvLowPower);
        refreshProxy = (ImageButton) view.findViewById(R.id.refreshProxy);
        refreshRelay = (ImageButton) view.findViewById(R.id.refreshRelay);
        refreshFriend = (ImageButton) view.findViewById(R.id.refreshFriend);
        switchProxy = (Switch) view.findViewById(R.id.switchProxy);
        switchRelay = (Switch) view.findViewById(R.id.switchRelay);
        switchFriend = (Switch) view.findViewById(R.id.switchFriend);

        nodesUnicastAddress = nodeSettings.getNodesUnicastAddress();

        switchProxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loader.show();
                if (switchProxy.isChecked()) {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setProxy(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Proxy.RUNNING, mProxyStatusCallback);
                } else {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setProxy(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Proxy.NOT_RUNNING, mProxyStatusCallback);
                }
            }

        });
        switchRelay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loader.show();
                if (switchRelay.isChecked()) {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setRelay(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Relay.RELAYING, ApplicationParameters.RelayRetransmitCount.DEFAULT, ApplicationParameters.RelayRetransmitIntervalSteps.DEFAULT, mConfigRelayStatusCallback);
                } else {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setRelay(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Relay.NOT_RELAYING, ApplicationParameters.RelayRetransmitCount.DEFAULT, ApplicationParameters.RelayRetransmitIntervalSteps.DEFAULT, mConfigRelayStatusCallback);
                }
            }
        });
        switchFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loader.show();
                if (switchFriend.isChecked()) {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setFriend(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Friend.RUNNING, mConfigFriendStatusCallback);

                } else {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().setFriend(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), ApplicationParameters.Friend.STOPPED, mConfigFriendStatusCallback);

                }
            }

        });

        refreshProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().getProxy(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), mProxyStatusCallback);
            }
        });

        refreshRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().getRelay(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), mConfigRelayStatusCallback);
            }
        });

        refreshFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getConfigurationModelClient().getFriend(new ApplicationParameters.Address(Integer.parseInt(nodesUnicastAddress, 16)), mConfigFriendStatusCallback);
            }
        });

        checkForPRFLP();
    }


    private void checkForPRFLP() {

        if (nodeSettings.isProxy()) {
            switchProxy.setVisibility(View.VISIBLE);
            refreshProxy.setVisibility(View.VISIBLE);
        } else {
            tvProxy.setVisibility(View.VISIBLE);
        }
        if (nodeSettings.isRelay()) {
            switchRelay.setVisibility(View.VISIBLE);
            refreshRelay.setVisibility(View.VISIBLE);
        } else {
            tvRelay.setVisibility(View.VISIBLE);
        }
        if (nodeSettings.isFriend()) {
            switchFriend.setVisibility(View.VISIBLE);
            refreshFriend.setVisibility(View.VISIBLE);
        } else {
            tvFriend.setVisibility(View.VISIBLE);
        }
        if (nodeSettings.isLowPower()) {
            tvLowPower.setText("Supported");
        } else {
            tvLowPower.setText("Not Supported");
        }

    }

    public final ConfigurationModelClient.ConfigProxyStatusCallback mProxyStatusCallback = new ConfigurationModelClient.ConfigProxyStatusCallback() {


        @Override
        public void onProxyStatus(boolean Timeoutstatus , ApplicationParameters.Proxy state) {

            loader.hide();
            if (Timeoutstatus) {
                UserApplication.trace("Proxy Status : fail");

            } else {
                if(state== ApplicationParameters.Proxy.RUNNING)
                {
                    switchProxy.setChecked(true);
                }
                else{
                    switchProxy.setChecked(false);
                }


            }
        }
    };

    ConfigurationModelClient.ConfigFriendStatusCallback mConfigFriendStatusCallback = new ConfigurationModelClient.ConfigFriendStatusCallback() {
        @Override
        public void onFriendStatus(boolean b, ApplicationParameters.Friend friend) {
            loader.hide();
            if (b) {
                UserApplication.trace(" Friend Status : fail");

            } else {
                if(friend== ApplicationParameters.Friend.RUNNING){
                    switchFriend.setChecked(true);
                }
                else{
                    switchFriend.setChecked(false);
                }

            }
        }
    };

    public ConfigurationModelClient.ConfigRelayStatusCallback mConfigRelayStatusCallback = new ConfigurationModelClient.ConfigRelayStatusCallback() {
        @Override
        public void onRelayStatus(boolean timeout, ApplicationParameters.Relay relay, ApplicationParameters.RelayRetransmitCount relayRetransmitCount, ApplicationParameters.RelayRetransmitIntervalSteps relayRetransmitIntervalSteps) {
            loader.hide();
            if (timeout) {
                UserApplication.trace(" Relay Status :fail");

            } else {
                UserApplication.trace(" Relay Status : Success" );
                UserApplication.trace(" Relay Status : relayRetransmitCount => " + relayRetransmitCount );
                UserApplication.trace(" Relay Status : relayRetransmitIntervalSteps => " + relayRetransmitIntervalSteps );


                if(relay== ApplicationParameters.Relay.RELAYING){
                    switchRelay.setChecked(true);
                }
                else {
                    switchRelay.setChecked(false);
                }

            }
        }
    };


}
