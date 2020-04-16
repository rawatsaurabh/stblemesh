/**
 ******************************************************************************
 * @file    Model.java
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

package com.st.bluenrgmesh.models.meshdata;

import java.io.Serializable;
import java.util.ArrayList;


public class Model implements Serializable {


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    private String modelName;

    private String modelId;

    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    private ArrayList<String> subscribe;

    public ArrayList<String> getSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(ArrayList<String> subscribe) {
        this.subscribe = subscribe;
    }

    private Publish publish;

    public Publish getPublish() {
        return this.publish;
    }

    public void setPublish(Publish publish) {
        this.publish = publish;
    }

    public ArrayList<Publish> getPublishingAddressList() {
        return publishingAddressList;
    }

    public void setPublishingAddressList(ArrayList<Publish> publishingAddressList) {
        this.publishingAddressList = publishingAddressList;
    }

    public ArrayList<Publish> publishingAddressList;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public ArrayList<Integer> getBind() {
        return bind;
    }

    public void setBind(ArrayList<Integer> bind) {
        this.bind = bind;
    }

    private ArrayList<Integer> bind;

    public int getActiveKeyIndex() {
        return activeKeyIndex;
    }

    public void setActiveKeyIndex(int activeKeyIndex) {
        this.activeKeyIndex = activeKeyIndex;
    }

    private int activeKeyIndex;

}
