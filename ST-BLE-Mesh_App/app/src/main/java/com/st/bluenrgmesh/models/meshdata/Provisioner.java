/**
 * *****************************************************************************
 *
 * @file Provisioner.java
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


public class Provisioner implements Serializable {

    private String provisionerName;

    public String getProvisionerName() { return this.provisionerName; }

    public void setProvisionerName(String provisionerName) { this.provisionerName = provisionerName; }

    private String UUID;

    public String getUUID() { return this.UUID; }

    public void setUUID(String UUID) { this.UUID = UUID; }

    private String unicastAddress;

    public String getUnicastAddress() {
        return this.unicastAddress;
    }

    public void setUnicastAddress(String unicastAddress) {
        this.unicastAddress = unicastAddress;
    }

    private String deviceKey;

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    private ArrayList<AllocatedGroupRange> allocatedGroupRange;

    public ArrayList<AllocatedGroupRange> getAllocatedGroupRange() {
        return this.allocatedGroupRange;
    }

    public void setAllocatedGroupRange(ArrayList<AllocatedGroupRange> allocatedGroupRange) {
        this.allocatedGroupRange = allocatedGroupRange;
    }

    private ArrayList<AllocatedUnicastRange> allocatedUnicastRange;

    public ArrayList<AllocatedUnicastRange> getAllocatedUnicastRange() {
        return this.allocatedUnicastRange;
    }

    public void setAllocatedUnicastRange(ArrayList<AllocatedUnicastRange> allocatedUnicastRange) {
        this.allocatedUnicastRange = allocatedUnicastRange;
    }

    private ArrayList<AllocatedSceneRange> allocatedSceneRange;

    public ArrayList<AllocatedSceneRange> getAllocatedSceneRange() {
        return this.allocatedSceneRange;
    }

    public void setAllocatedSceneRange(ArrayList<AllocatedSceneRange> allocatedSceneRange) {
        this.allocatedSceneRange = allocatedSceneRange;
    }


}
