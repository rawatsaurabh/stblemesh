/**
 * *****************************************************************************
 *
 * @file Node.java
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
package com.st.bluenrgmesh.models.newmeshdata;

import java.io.Serializable;
import java.util.ArrayList;



public class Node implements Serializable {

    private String UUID;

    public String getUUID() { return this.UUID; }

    public void setUUID(String UUID) { this.UUID = UUID; }

    private String unicastAddress;

    public String getUnicastAddress() { return this.unicastAddress; }

    public void setUnicastAddress(String unicastAddress) { this.unicastAddress = unicastAddress; }

    private String deviceKey;

    public String getDeviceKey() { return this.deviceKey; }

    public void setDeviceKey(String deviceKey) { this.deviceKey = deviceKey; }

    private String security;

    public String getSecurity() { return this.security; }

    public void setSecurity(String security) { this.security = security; }

    private String Name;

    public String getName() { return this.Name; }

    public void setName(String Name) { this.Name = Name; }

    private String cid;

    public String getCid() { return this.cid; }

    public void setCid(String cid) { this.cid = cid; }

    private String pid;

    public String getPid() { return this.pid; }

    public void setPid(String pid) { this.pid = pid; }

    private String vid;

    public String getVid() { return this.vid; }

    public void setVid(String vid) { this.vid = vid; }

    private String crpl;

    public String getCrpl() { return this.crpl; }

    public void setCrpl(String crpl) { this.crpl = crpl; }

    private String features;

    public String getFeatures() { return this.features; }

    public void setFeatures(String features) { this.features = features; }

    private boolean secureNetworkBeacon;

    public boolean getSecureNetworkBeacon() { return this.secureNetworkBeacon; }

    public void setSecureNetworkBeacon(boolean secureNetworkBeacon) { this.secureNetworkBeacon = secureNetworkBeacon; }

    private int defaultTTL;

    public int getDefaultTTL() { return this.defaultTTL; }

    public void setDefaultTTL(int defaultTTL) { this.defaultTTL = defaultTTL; }

    private String networkTransmit;

    public String getNetworkTransmit() { return this.networkTransmit; }

    public void setNetworkTransmit(String networkTransmit) { this.networkTransmit = networkTransmit; }

    private String relayRetransmit;

    public String getRelayRetransmit() { return this.relayRetransmit; }

    public void setRelayRetransmit(String relayRetransmit) { this.relayRetransmit = relayRetransmit; }

    private boolean configComplete;

    public boolean getConfigComplete() { return this.configComplete; }

    public void setConfigComplete(boolean configComplete) { this.configComplete = configComplete; }

    private ArrayList<Element> elements;

    public ArrayList<Element> getElements() { return this.elements; }

    public void setElements(ArrayList<Element> elements) { this.elements = elements; }

    private String appKeys;

    public String getAppKeys() { return this.appKeys; }

    public void setAppKeys(String appKeys) { this.appKeys = appKeys; }

    private String netKeys;

    public String getNetKeys() { return this.netKeys; }

    public void setNetKeys(String netKeys) { this.netKeys = netKeys; }

    private boolean blacklisted;

    public boolean getBlacklisted() { return this.blacklisted; }

    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }


}
