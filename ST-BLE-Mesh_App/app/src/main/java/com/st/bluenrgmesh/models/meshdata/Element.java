/**
 * *****************************************************************************
 *
 * @file Element.java
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

import java.io.Serializable;
import java.util.ArrayList;


public class Element implements Serializable , Cloneable{

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setConfigured(boolean configured) {
        isConfigured = configured;
    }

    public boolean isConfigured;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private String parentNodeName;

    private String elementName;

    private String unicastAddress;

    public String getUnicastAddress() {
        return this.unicastAddress;
    }

    public void setUnicastAddress(String unicastAddress) {
        this.unicastAddress = unicastAddress;
    }

    private ArrayList<Model> models;

    public ArrayList<Model> getModels() {
        return this.models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    private int index;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public boolean isSubscribed;
    public boolean isPublished;

    private String parentNodeAddress;

    public String getParentNodeAddress() {
        return parentNodeAddress;
    }

    public void setParentNodeAddress(String parentNodeAddress) {
        this.parentNodeAddress = parentNodeAddress;
    }


    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getParentNodeName() {
        return parentNodeName;
    }

    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }

    public ArrayList<AppKey> getAppKeys() {
        return appKeys;
    }

    public void setAppKeys(ArrayList<AppKey> appKeys) {
        this.appKeys = appKeys;
    }

    private ArrayList<AppKey> appKeys;

    /*
    * Extra Params Used
    * */

    private String tempSensorValue;
    private String pressureSensorValue;
    private String accelerometerValue;
    private boolean showProgress;

    public String getCompleteSensorData() {
        return completeSensorData;
    }

    public void setCompleteSensorData(String completeSensorData) {
        this.completeSensorData = completeSensorData;
    }

    private String completeSensorData;

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public String getTempSensorValue() {
        return tempSensorValue;
    }

    public void setTempSensorValue(String tempSensorValue) {
        this.tempSensorValue = tempSensorValue;
    }

    public String getPressureSensorValue() {
        return pressureSensorValue;
    }

    public void setPressureSensorValue(String pressureSensorValue) {
        this.pressureSensorValue = pressureSensorValue;
    }

    public String getAccelerometerValue() {
        return accelerometerValue;
    }

    public void setAccelerometerValue(String accelerometerValue) {
        this.accelerometerValue = accelerometerValue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
//TODO to be checked
    private String location;
}
