/**
 * *****************************************************************************
 *
 * @file HeartbeatInfoFragment.java
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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.callbacks.HeartBeatCallbacks;
import com.st.bluenrgmesh.models.heartbeat.HearBeatData;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HeartBeatInfoFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private TextView txtheartbeat_date,txtmaxhops,txtminhops,txtisrelay,txtisproxy,txtisfriend,txtislp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_heartbeatinfo, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new HeartBeatInfoFragment().getClass().getName());
        updateJsonData();
        initUi();

        return view;
    }

    private void initUi() {

        String click_address = (String) getArguments().getSerializable(getString(R.string.key_serializable));
        txtheartbeat_date = (TextView) view.findViewById(R.id.txtheartbeat_date);
        txtmaxhops = (TextView) view.findViewById(R.id.txtmaxhops);
        txtminhops = (TextView) view.findViewById(R.id.txtminhops);
        txtisrelay = (TextView) view.findViewById(R.id.txtisrelay);
        txtisproxy = (TextView) view.findViewById(R.id.txtisproxy);
        txtisfriend = (TextView) view.findViewById(R.id.txtisfriend);
        txtislp = (TextView) view.findViewById(R.id.txtislp);
        //String heartBeatdata = Utils.getHeartBeatData(getActivity());
        LinkedHashMap<String,ArrayList<HearBeatData>> heartBeatdata = HeartBeatCallbacks.getHeartBeatMap();
        ArrayList <HearBeatData> aa = null;
        if(heartBeatdata!=null &&heartBeatdata.size()>0) {
            try {

                for ( String key : heartBeatdata.keySet()) {
                    if(key.equalsIgnoreCase(click_address)) {
                        System.out.println(key);
                        aa = heartBeatdata.get(key);
                        break;
                    }
                }

                if(aa!=null&&aa.size()>0) {
                    // HearBeatData data = ParseManager.getInstance().fromJSON(new JSONObject(heartBeatdata), HearBeatData.class);
                    txtheartbeat_date.setText(Utils.getDate(aa.get(0).getDate(), "dd/MM/yyyy hh:mm:ss"));
                    txtisrelay.setText(aa.get(0).getIs_node_relay());
                    txtisproxy.setText(aa.get(0).getIs_node_proxy());
                    txtisfriend.setText(aa.get(0).getIs_node_friend());
                    txtislp.setText(aa.get(0).getIs_node_low_power());
                    txtmaxhops.setText(aa.get(0).getMax_hop());
                    txtminhops.setText(aa.get(0).getMin_hop());
                }else
                {
                    Toast.makeText(getActivity(),"No HeartBeat Received Yet for => "+ click_address,Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getActivity(),"No HeartBeat Received Yet.",Toast.LENGTH_LONG).show();
        }


    }

    private void updateJsonData() {

    }
}
