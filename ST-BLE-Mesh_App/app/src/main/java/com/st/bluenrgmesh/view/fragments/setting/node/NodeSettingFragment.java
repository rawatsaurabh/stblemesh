/**
 * *****************************************************************************
 *
 * @file NodeSettingFragment.java
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.ConfigurationModelClient;
import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.datamap.Nucleo;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.health.HealthConfigFragment;
import com.st.bluenrgmesh.view.fragments.other.meshmodels.heartbeat.HeartBeatConfigFragment;



public class NodeSettingFragment extends BaseFragment {


    private View view;
    private AppDialogLoader loader;
    private RelativeLayout lytNodeFeatures;
    private RelativeLayout lytHeartBeatConfig;
    private RelativeLayout lytHealthConfig;
    private NodeSettings nodeSettings;
    private EditText edtName;
    private TextView txtUUID;
    private Button butRemoveNode;
    private mobleAddress peerAddress;
    private RelativeLayout lytNodeInformation;
    private Context context;


    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nodesetting, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new NodeSettingFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        Utils.setSettingType(getActivity(), getString(R.string.str_nodessettings_label));
        nodeSettings = (NodeSettings) getArguments().getSerializable(getString(R.string.key_serializable));
        //peerAddress = mobleAddress.deviceAddress(Integer.parseInt(nodeSettings.getNodesUnicastAddress(), 16));
        peerAddress = mobleAddress.deviceAddress(Integer.parseInt(nodeSettings.getNodesUnicastAddress()));
        Utils.DEBUG(nodeSettings.getNodesName() + " " + nodeSettings.getNodesUnicastAddress() + " >>>>  "+ nodeSettings.getNodesMacAddress());

        lytNodeInformation = (RelativeLayout) view.findViewById(R.id.lytNodeInformation);
        lytNodeFeatures = (RelativeLayout) view.findViewById(R.id.lytNodeFeatures);
        lytHeartBeatConfig = (RelativeLayout) view.findViewById(R.id.lytHeartBeatConfig);
        lytHealthConfig = (RelativeLayout) view.findViewById(R.id.lytHealthConfig);
        butRemoveNode = (Button) view.findViewById(R.id.butRemoveNode);

        edtName = (EditText) view.findViewById(R.id.edtName);
        txtUUID = (TextView) view.findViewById(R.id.txtUUID);
        edtName.setText(nodeSettings.getNodesName());
        txtUUID.setText(nodeSettings.getUuid());

        lytNodeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.moveToFragment(getActivity(), new NodeInfoFragment(), nodeSettings, 0);

            }
        });

        lytNodeFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.moveToFragment(getActivity(), new NodeFeaturesFragment(), nodeSettings, 0);

            }
        });

        lytHeartBeatConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.moveToFragment(getActivity(), new HeartBeatConfigFragment(), nodeSettings, 0);
               // Utils.showToast(getActivity(), "Updated soon in next release.");

            }
        });

        lytHealthConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.moveToFragment(getActivity(), new HealthConfigFragment(), nodeSettings, 0);
               // Utils.showToast(getActivity(), "Updated soon in next release.");
            }
        });

        butRemoveNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //button used for addition of group and removing of node and group
                removeNodePopUp();

            }
        });

       /* lytHeartBeatConfig.setClickable(false);
        lytHealthConfig.setClickable(false);*/

    }

    private void removeNodePopUp() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("This action will unprovision the node from the network. Do you want to continue?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (/*!g_model*/false) {
                    ((UserApplication) getActivity().getApplication()).mConfiguration.getNetwork().getApplication().setRemoteData(peerAddress, Nucleo.UNCONFIGURE, 1, new byte[]{}, false);

                } else {
                    UserApplication.trace("NODE unprovisioning from AddGroup _ 1 addr " + peerAddress);
                    loader.show();
                    Utils.saveModelInfo(context, null);
                    Utils.setNodeFeatures(context, null);
                    Utils.removeProvisionNodeFromJson(getActivity(), nodeSettings.getNodesUnicastAddress());
                    //((MainActivity)getActivity()).unAdviseCallbacks();
                    try {
                        MainActivity.network.getConfigurationModelClient().resetNode(new ApplicationParameters.Address(peerAddress.mValue), mNodeResetCallback);
                    }catch (Exception e){}
                }

                ((UserApplication) getActivity().getApplication()).save();
                dialog.cancel();
            }
        });

        builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public ConfigurationModelClient.NodeResetStatusCallback mNodeResetCallback = new ConfigurationModelClient.NodeResetStatusCallback() {
        @Override
        public void onNodeResetStatus(boolean Timeoutstatus) {

            if (Timeoutstatus == true) {
                loader.hide();
                UserApplication.trace("Node unprovisioned Failed");
                Utils.showToast(getActivity(), "Unprovisioned Failed");
            } else {
                UserApplication.trace("Node unprovisioned Successfully");
                Utils.showToast(getActivity(), "Unprovisioned Done");
                //((MainActivity)getActivity()).adviseCallbacks();
                ((MainActivity)getActivity()).clearUnprovisionList();
                ((MainActivity)getActivity()).updateProvisionedTab(nodeSettings.getUuid(), getResources().getInteger(R.integer.PROVISIONED_NODE_REMOVED));
                ((MainActivity) getActivity()).updateModelTab();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.hide();
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                });

            }
        }
    };


    public NodeSettings getNodeData(){
        nodeSettings.setNodesName(edtName.getText().toString());
        return nodeSettings;
    }
}
