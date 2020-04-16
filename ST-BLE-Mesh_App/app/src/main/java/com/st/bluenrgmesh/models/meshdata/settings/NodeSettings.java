/**
 * *****************************************************************************
 *
 * @file NodeSettings.java
 * @author BLE Mesh Team
 * @version V1.08.000
 * @date 15-October-2018
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

package com.st.bluenrgmesh.models.meshdata.settings;

import java.io.Serializable;



public class NodeSettings implements Serializable {

    private String nodesName;
    private String nodesUnicastAddress;
    private String nodesMacAddress;
    private String uuid;
    private boolean isProxy;
    private boolean isRelay;
    private boolean isFriend;
    private String mCid;
    private String mPid;
    private String mVid;
    private String mCrpl;

    public String getmCid() {
        return mCid;
    }

    public void setmCid(String mCid) {
        this.mCid = mCid;
    }

    public String getmPid() {
        return mPid;
    }

    public void setmPid(String mPid) {
        this.mPid = mPid;
    }

    public String getmVid() {
        return mVid;
    }

    public void setmVid(String mVid) {
        this.mVid = mVid;
    }

    public String getmCrpl() {
        return mCrpl;
    }

    public void setmCrpl(String mCrpl) {
        this.mCrpl = mCrpl;
    }

    public String getNodesName() {
        return nodesName;
    }

    public void setNodesName(String nodesName) {
        this.nodesName = nodesName;
    }

    public String getNodesUnicastAddress() {
        return nodesUnicastAddress;
    }

    public void setNodesUnicastAddress(String nodesUnicastAddress) {
        this.nodesUnicastAddress = nodesUnicastAddress;
    }

    public String getNodesMacAddress() {
        return nodesMacAddress;
    }

    public void setNodesMacAddress(String nodesMacAddress) {
        this.nodesMacAddress = nodesMacAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    public boolean isRelay() {
        return isRelay;
    }

    public void setRelay(boolean relay) {
        isRelay = relay;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isLowPower() {
        return isLowPower;
    }

    public void setLowPower(boolean lowPower) {
        isLowPower = lowPower;
    }

    private boolean isLowPower;

}
