/**
 * *****************************************************************************
 *
 * @file Nodes.java
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

package com.st.bluenrgmesh.models.meshdata;


import androidx.annotation.NonNull;

import com.st.bluenrgmesh.models.meshdata.nodecomparable.NodeData;

import java.io.Serializable;
import java.util.ArrayList;


public class Nodes implements Serializable, Comparable<Nodes> , Cloneable{

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public ArrayList<AppKey> getAppKeys() {
        return appKeys;
    }

    public void setAppKeys(ArrayList<AppKey> appKeys) {
        this.appKeys = appKeys;
    }

    private ArrayList<AppKey> appKeys;

    private String crpl;
    private String pid;
    private String vid;
    private String cid;
    private int publishAddress;

    public String getCrpl() {
        return crpl;
    }

    public void setCrpl(String crpl) {
        this.crpl = crpl;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getPublishAddress() {
        return publishAddress;
    }

    public void setPublishAddress(int publishAddress) {
        this.publishAddress = publishAddress;
    }

    public String getPid() { return this.pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getVid() { return this.vid; }
    public void setVid(String vid) { this.vid = vid; }




    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public Features features;

    public Nodes(int nodeNumber)
    {
        this.nodeNumber = nodeNumber;
    }

    @Override
    public int compareTo(@NonNull Nodes node) {

        return this.nodeNumber.compareTo(node.nodeNumber);
    }

    public Integer getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(Integer nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    private Integer nodeNumber;

    private boolean isChecked;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;


    private String UUID;

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    private boolean blacklisted;

    public boolean getBlacklisted() {
        return this.blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    private boolean configComplete;

    public boolean getConfigComplete() {
        return this.configComplete;
    }

    public void setConfigComplete(boolean configComplete) {
        this.configComplete = configComplete;
    }

    private String deviceKey;

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    private ArrayList<Element> elements;

    public ArrayList<Element> getElements() {
        return this.elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }


    /*
    * Extra Params Used to satisfy other dependencies.
    * Remember during transfer of json on cloud or through gmail please remove these variables from json.
    *
    * */
    private boolean showProgress;

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }





    private String unicastAddress;

    public String getUnicastAddress() { return this.unicastAddress; }

    public void setUnicastAddress(String unicastAddress) { this.unicastAddress = unicastAddress; }
    private String security;

    public String getSecurity() { return this.security; }

    public void setSecurity(String security) { this.security = security; }

    private int defaultTTL;

    public int getDefaultTTL() { return this.defaultTTL; }

    public void setDefaultTTL(int defaultTTL) { this.defaultTTL = defaultTTL; }

    private String networkTransmit;

    public String getNetworkTransmit() { return this.networkTransmit; }

    public void setNetworkTransmit(String networkTransmit) { this.networkTransmit = networkTransmit; }



    /*old params*/

    private String address;
    private String publish_address;
    private String configured;
    private String subtitle;
    private String known;
    private String m_address;
    private String in_range;
    private String title;
    private String rssi;
    private String type;
    private String subgroup;
    private Integer numberOfElements;

    public int getmOOBInformation() {
        return mOOBInformation;
    }

    public void setmOOBInformation(int mOOBInformation) {
        this.mOOBInformation = mOOBInformation;
    }

    private int mOOBInformation;


    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String group;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublish_address() {
        return publish_address;
    }

    public void setPublish_address(String publish_address) {
        this.publish_address = publish_address;
    }

    public String getConfigured() {
        return configured;
    }

    public void setConfigured(String configured) {
        this.configured = configured;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getKnown() {
        return known;
    }

    public void setKnown(String known) {
        this.known = known;
    }

    public String getM_address() {
        return m_address;
    }

    public void setM_address(String m_address) {
        this.m_address = m_address;
    }

    public String getIn_range() {
        return in_range;
    }

    public void setIn_range(String in_range) {
        this.in_range = in_range;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }



}
