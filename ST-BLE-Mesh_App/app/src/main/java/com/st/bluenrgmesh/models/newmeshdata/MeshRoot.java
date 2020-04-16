/**
 * *****************************************************************************
 *
 * @file MeshRoot.java
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


public class MeshRoot implements Serializable {

    private String $schema;

    public String getSchema() { return this.$schema; }

    public void setSchema(String $schema) { this.$schema = $schema; }

    private String meshUUID;

    public String getMeshUUID() { return this.meshUUID; }

    public void setMeshUUID(String meshUUID) { this.meshUUID = meshUUID; }

    private String meshName;

    public String getMeshName() { return this.meshName; }

    public void setMeshName(String meshName) { this.meshName = meshName; }

    private String version;

    public String getVersion() { return this.version; }

    public void setVersion(String version) { this.version = version; }

    private String timestamp;

    public String getTimestamp() { return this.timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    private ArrayList<Provisioner> provisioners;

    public ArrayList<Provisioner> getProvisioners() { return this.provisioners; }

    public void setProvisioners(ArrayList<Provisioner> provisioners) { this.provisioners = provisioners; }

    private ArrayList<NetKey> netKeys;

    public ArrayList<NetKey> getNetKeys() { return this.netKeys; }

    public void setNetKeys(ArrayList<NetKey> netKeys) { this.netKeys = netKeys; }

    private ArrayList<AppKey> appKeys;

    public ArrayList<AppKey> getAppKeys() { return this.appKeys; }

    public void setAppKeys(ArrayList<AppKey> appKeys) { this.appKeys = appKeys; }

    private ArrayList<Node> nodes;

    public ArrayList<Node> getNodes() { return this.nodes; }

    public void setNodes(ArrayList<Node> nodes) { this.nodes = nodes; }

    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() { return this.groups; }

    public void setGroups(ArrayList<Group> groups) { this.groups = groups; }

    private String scenes;

    public String getScenes() { return this.scenes; }

    public void setScenes(String scenes) { this.scenes = scenes; }

}
