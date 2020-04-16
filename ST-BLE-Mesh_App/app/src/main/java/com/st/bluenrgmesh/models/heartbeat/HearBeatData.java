/**
 ******************************************************************************
 * @file    Heartbeat.java
 * @author  BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
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
package com.st.bluenrgmesh.models.heartbeat;

import com.st.bluenrgmesh.Utils;

import java.io.Serializable;


public class HearBeatData implements Serializable {

    long date;
    String is_node_relay;
    String is_node_proxy;
    String is_node_friend;
    String is_node_low_power;
    String max_hop;

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    String ttl;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getIs_node_relay() {
        return is_node_relay;
    }

    public void setIs_node_relay(String is_node_relay) {
        this.is_node_relay = is_node_relay;
    }

    public String getIs_node_proxy() {
        return is_node_proxy;
    }

    public void setIs_node_proxy(String is_node_proxy) {
        this.is_node_proxy = is_node_proxy;
    }

    public String getIs_node_friend() {
        return is_node_friend;
    }

    public void setIs_node_friend(String is_node_friend) {
        this.is_node_friend = is_node_friend;
    }

    public String getIs_node_low_power() {
        return is_node_low_power;
    }

    public void setIs_node_low_power(String is_node_low_power) {
        this.is_node_low_power = is_node_low_power;
    }

    public String getMax_hop() {
        return max_hop;
    }

    public void setMax_hop(String max_hop) {
        this.max_hop = max_hop;
    }

    public String getMin_hop() {
        return min_hop;
    }

    public void setMin_hop(String min_hop) {
        this.min_hop = min_hop;
    }

    String min_hop;
}
