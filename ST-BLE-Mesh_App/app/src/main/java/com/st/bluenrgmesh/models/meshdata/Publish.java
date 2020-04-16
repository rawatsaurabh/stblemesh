/**
 ******************************************************************************
 * @file    Publish.java
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


public class Publish implements Serializable{

    private String currentParentAddress;

    private String currentParentNodeName;

    private String address;

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public boolean isTypeNode() {
        return isTypeNode;
    }

    public void setTypeNode(boolean typeNode) {
        isTypeNode = typeNode;
    }

    private boolean isTypeNode;

    private int index;
    public int getIndex() { return this.index; }
    public void setIndex(int index) { this.index = index; }
    private int ttl;
    public int getTtl() { return this.ttl; }
    public void setTtl(int ttl) { this.ttl = ttl; }
    private int period;
    public int getPeriod() { return this.period; }
    public void setPeriod(int period) { this.period = period; }

    private Retransmit retransmit;
    public Retransmit getRetransmit() { return this.retransmit; }
    public void setRetransmit(Retransmit retransmit) { this.retransmit = retransmit; }

    private int credentials;
    public int getCredentials() { return this.credentials; }
    public void setCredentials(int credentials) { this.credentials = credentials; }

    public String getCurrentParentNodeName() {
        return currentParentNodeName;
    }

    public void setCurrentParentNodeName(String currentParentNodeName) {
        this.currentParentNodeName = currentParentNodeName;
    }

    public String getCurrentParentAddress() {
        return currentParentAddress;
    }

    public void setCurrentParentAddress(String currentParentAddress) {
        this.currentParentAddress = currentParentAddress;
    }
}
