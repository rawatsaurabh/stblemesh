/**
 ******************************************************************************
 * @file    HeartBeatCallbacks.java
 * @author  BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
 * @brief   User Application file
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
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
 *
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 *
 ******************************************************************************
 */
package com.st.bluenrgmesh.callbacks;

import android.os.Handler;

import com.msi.moble.ApplicationParameters;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.defaultAppCallback;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.heartbeat.HearBeatData;
import com.st.bluenrgmesh.models.meshdata.Nodes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class HeartBeatCallbacks {

    public static LinkedHashMap<String, ArrayList<HearBeatData>> heartBeatMap = new LinkedHashMap<>();
    private static String heartbeat_address = "";
    private static int heartbeat_ttl, min_hop = 1, max_hop = 1;
    public static final defaultAppCallback onHeartBeatRecievedCallback = new defaultAppCallback() {

        @Override
        public void onHeartBeatRecievedCallback(final ApplicationParameters.Address address, final ApplicationParameters.TTL ttl, final ApplicationParameters.Features features) {
            super.onHeartBeatRecievedCallback(address, ttl, features);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    UserApplication.trace("Heartbeat Recieved from Node =>  " + address);
                    UserApplication.trace("Heartbeat params TTL  =>  " + ttl);
                    UserApplication.trace("Heartbeat features =>  " + features);

                    ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Heartbeat Recieved from Node => " + address, LoggerConstants.TYPE_RECEIVE);
                    ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Heartbeat params TTL => " + ttl, LoggerConstants.TYPE_RECEIVE);
                    ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("Heartbeat features => " + features, LoggerConstants.TYPE_RECEIVE);
                    try {

                        int ff = features.getValue();
                        UserApplication.trace("onHeartBeatRecievedCallback Features RELAY  = " + ((ff & 1) == 1)); // RELAY  = ((ff & 1) == 1
                        boolean is_node_relay = ((ff & 1) == 1);
                        ff >>= 1;
                        UserApplication.trace("onHeartBeatRecievedCallback Features Proxy  = " + ((ff & 1) == 1)); // Proxy  = ((ff & 1) == 1
                        boolean is_node_proxy = ((ff & 1) == 1);
                        ff >>= 1;
                        boolean is_node_friend = ((ff & 1) == 1);
                        UserApplication.trace("onHeartBeatRecievedCallback Features Friend  = " + ((ff & 1) == 1)); // Friend  = ((ff & 1) == 1
                        ff >>= 1;
                        boolean is_node_low_power = ((ff & 1) == 1);
                        UserApplication.trace("onHeartBeatRecievedCallback Features Low Power  = " + ((ff & 1) == 1)); // Low Power  = ((ff & 1) == 1

                        heartbeat_ttl = Integer.parseInt(String.valueOf(ttl.getValue()));
                /*if(min_hop>heartbeat_ttl){
                    min_hop = heartbeat_ttl;
                }
                if(max_hop<heartbeat_ttl){
                    max_hop=heartbeat_ttl;
                }*/


                        if (ttl.getValue() > min_hop && ttl.getValue() < max_hop) {

                        } else {
                            if (ttl.getValue() > max_hop) {
                                max_hop = ttl.getValue();
                            }

                            if (ttl.getValue() < min_hop)
                                min_hop = ttl.getValue();
                        }


                        UserApplication.trace("onHeartBeatRecievedCallback MIN HOP  = " + min_hop); // Low Power  = ((ff & 1) == 1
                        UserApplication.trace("onHeartBeatRecievedCallback MAX HOP  = " + max_hop); // Low Power  = ((ff & 1) == 1


                        ArrayList<HearBeatData> heartBeatList = new ArrayList<>();
                        HearBeatData data = new HearBeatData();
                        data.setDate(Calendar.getInstance().getTimeInMillis());
                        String unicastAdd = String.valueOf(Utils.convertHexToInt(String.valueOf(address)));
                        data.setAddress(unicastAdd);
                        data.setIs_node_friend(String.valueOf(is_node_friend));
                        data.setIs_node_friend(String.valueOf(is_node_friend));
                        data.setIs_node_relay(String.valueOf(is_node_relay));
                        data.setIs_node_proxy(String.valueOf(is_node_proxy));
                        data.setIs_node_low_power(String.valueOf(is_node_low_power));
                        data.setTtl(String.valueOf(ttl));
                        data.setMin_hop(String.valueOf(min_hop));
                        data.setMax_hop(String.valueOf(max_hop));
                        heartBeatList.add(data);
                        heartbeat_address = unicastAdd;
                        heartBeatMap.put(unicastAdd, heartBeatList);
                        /*Nodes nodes = new Nodes(0);
                        nodes.setAddress(unicastAdd);*/
                        ((MainActivity)Utils.contextMainActivity).updateProvisionedTab(unicastAdd, Utils.contextMainActivity.getResources().getInteger(R.integer.PROVISIONED_NODE_HEARTBEAT));
                        //fragmentCommunication(new ProvisionedTabFragment().getClass().getName(),unicastAdd, 6, nodes, false, null);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }

    };


    public static LinkedHashMap<String, ArrayList<HearBeatData>> getHeartBeatMap() {
        return heartBeatMap;
    }

    public static String getHeartBeatAddress() {
        return heartbeat_address;
    }
}
