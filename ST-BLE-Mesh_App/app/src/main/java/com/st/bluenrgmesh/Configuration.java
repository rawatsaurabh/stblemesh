/**
 ******************************************************************************
 * @file    Configuration.java
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

package com.st.bluenrgmesh;

import android.content.Context;

import com.msi.moble.Device;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * The type Configuration.
 */
public class Configuration {
    private Map<String, DeviceEntry> mDevices;
    private Map<mobleAddress, GroupEntry> mGroups;

    public void setmNetwork(mobleNetwork mNetwork) {
        this.mNetwork = mNetwork;
    }

    private mobleNetwork mNetwork;
    private List<mobleAddress> mSlaves;
    private boolean mIsSlave;
    private Map<String, String> mPublishingAddressMap;
    private int NO_OF_GROUPS_ALLOWED=11;

    /**
     * Create Configuration object for MoBLE network
     */
    public Configuration() {
        mDevices = new HashMap<String, DeviceEntry>();
        mGroups = new HashMap<mobleAddress, GroupEntry>();
        mSlaves = new ArrayList<mobleAddress>();
        mPublishingAddressMap=new HashMap<>();
        mIsSlave = false;

    }

    /**
     * Set publication.
     *
     * @param deviceAddress     the device address
     * @param publishingAddress the publishing address
     */
    public void setPublication(String deviceAddress,String publishingAddress){
        mPublishingAddressMap.put(deviceAddress,publishingAddress);

    }

    /**
     * Get publication string.
     *
     * @param deviceAddress the device address
     * @return the string
     */
    public String getPublication(String deviceAddress){
        String publishingAddress;
        UserApplication.trace("publish address map "+mPublishingAddressMap.toString());
        if(mPublishingAddressMap.containsKey(deviceAddress))
            publishingAddress=mPublishingAddressMap.get(deviceAddress);
        else
           publishingAddress=null;
        return publishingAddress;
    }

    /**
     * Check group boolean.
     *
     * @param address the address
     * @return the boolean
     */
    boolean checkGroup(mobleAddress address){
        if( mGroups.containsKey(address))
            return true;
        else
            return false;
    }

    /**
     * Instantiates a new Configuration.
     *
     * @param address the address
     */
    public Configuration(mobleAddress address){
        this();
        mNetwork = mobleNetwork.createNetwork(address);

    }

    /**
     * Gets network.
     *
     * @return the network
     */
    public mobleNetwork getNetwork() {
        return mNetwork;
    }

    private void saveDb(DataOutputStream ostmD) throws IOException {
        ostmD.writeInt(mDevices.size());
        for (Map.Entry<String, DeviceEntry> device : mDevices.entrySet()) {
            ostmD.writeUTF(device.getKey());
            device.getValue().save(ostmD);
        }

        ostmD.writeInt(mGroups.size());
        for (Map.Entry<mobleAddress, GroupEntry> group : mGroups.entrySet()) {
            group.getKey().save(ostmD);
            group.getValue().save(ostmD);
        }
        ostmD.writeInt(mPublishingAddressMap.size());
        for (Map.Entry<String,String> group : mPublishingAddressMap.entrySet()) {
            ostmD.writeUTF(group.getKey());
            ostmD.writeUTF(group.getValue());
        }
    }

    /**
     * Provide slave configuration.
     *
     * @param ostm the ostm
     * @throws IOException the io exception
     */
    void provideSlaveConfiguration(OutputStream ostm) throws IOException {
        DataOutputStream ostmD = new DataOutputStream(ostm);
        saveDb(ostmD);
        mobleAddress slaveAddress = mobleAddress.deviceAddress(getDeviceMinAvailableAddress());
        mNetwork.provideSlaveConfiguration(ostmD, slaveAddress);
        mSlaves.add(slaveAddress);
        ostmD.close();
    }

    /**
     * Save.
     *
     * @param ostm the ostm
     * @throws IOException the io exception
     */
    void save(OutputStream ostm) throws IOException {
        DataOutputStream ostmD = new DataOutputStream(ostm);
        saveDb(ostmD);
        mNetwork.backupNetwork(ostmD);
        ostmD.close();
    }

    /**
     * Save.
     *
     * @param filename the filename
     * @param context  the context
     * @throws IOException the io exception
     */
    void save(String filename, Context context) throws IOException {
//        FileOutputStream ofstm = context.openFileOutput(filename , 0);
        FileOutputStream ofstm = new FileOutputStream(new File(filename));
        save(ofstm);
        ofstm.close();
    }

    /**
     * Load configuration.
     *
     * @param istm the istm
     * @return the configuration
     * @throws IOException the io exception
     */
    static Configuration load(InputStream istm) throws IOException {
        DataInputStream istmD = new DataInputStream(istm);
        Configuration cfg = new Configuration();
        int count;
        count =istmD.readInt();
        for (int i=0; i<count; ++i) {
            String mac = istmD.readUTF();
            DeviceEntry entry = DeviceEntry.load(istmD);
            cfg.addDevice(mac, entry);

        }

        count = istmD.readInt();
        for (int i=0; i<count; ++i) {
            mobleAddress address = mobleAddress.load(istmD);
            GroupEntry entry = GroupEntry.load(istmD);
            cfg.addGroup(address, entry);
        }
                count = istmD.readInt();
        for (int i=0; i<count; ++i) {
            String deviceAdd=istmD.readUTF();
            String publishAdd= istmD.readUTF();
            cfg.setPublication(deviceAdd,publishAdd);
        }


        cfg.mNetwork = mobleNetwork.restoreNetwork(istmD);
        if (cfg.mNetwork != null && cfg.mNetwork.mAddress != null) {
            cfg.mIsSlave = cfg.mNetwork.mAddress.mValue != 1;
        } else {
            cfg.mIsSlave = false;
        }

        istmD.close();
        return cfg;
    }

    /**
     * Load configuration.
     *
     * @param context  the context
     * @param filename the filename
     * @return the configuration
     * @throws IOException the io exception
     */
    static Configuration load(Context context, String filename) throws IOException {
//        FileInputStream istream = context.openFileInput(filename);
        FileInputStream istream = new FileInputStream(new File(filename));
        Configuration cfg = load(istream);
        return cfg;
    }

    /**
     * Add device.
     *
     * @param mac   the mac
     * @param entry the entry
     */
    public void addDevice(String mac, DeviceEntry entry) {
        if (mac == null) {
            throw new IllegalArgumentException();
        }
        mDevices.put(mac, entry);
    }

    /**
     * Remove device.
     *
     * @param mac the mac
     */
    void removeDevice(String mac) {
        DeviceEntry entry = mDevices.remove(mac);
        if (entry == null) {

            throw new IllegalArgumentException();
        }
        for (mobleAddress address : mGroups.keySet()) {
            removeDeviceFromGroup(address, mac);
        }
    }

    /**
     * Remove devices.
     *
     * @param deviceCollection the device collection
     */
    public void removeDevices(Collection<Device> deviceCollection) {
        for(Device device : deviceCollection) {
            if(mDevices.containsKey(device.getAddress())) {
                removeDevice(device.getAddress());
            }
        }
    }

    /**
     * Gets device.
     *
     * @param mac the mac
     * @return the device
     */
    public DeviceEntry getDevice(String mac) {
        return mDevices.get(mac);
    }

    /**
     * Gets devices set.
     *
     * @return the devices set
     */
    public Set<String> getDevicesSet() {
        return mDevices.keySet();
    }

    /**
     * Gets devices count.
     *
     * @return the devices count
     */
    int getDevicesCount() {
        return mDevices.size();
    }

    /**
     * Gets device min available address.
     *
     * @return the device min available address
     */
    int getDeviceMinAvailableAddress() {
        NavigableSet<Short> used = new TreeSet<>();
        for (DeviceEntry e : mDevices.values()) {
            used.add(e.getAddress().mValue);
        }
        for (mobleAddress e : mSlaves) {
            used.add(e.mValue);
        }

        Short min = 1; // master phone fixed address is 1
        for (Iterator<Short> i = used.iterator(); i.hasNext(); ) {
            Short a = i.next();
            if (min + 1 != a.shortValue()) {
                break;
            }
            min = a;
        }
        if (min + 1 <= 0x3fff) {
            return min + 1;
        } else {
            return 0;
        }
    }

    int getDeviceAddressBasedOnLastElement(Integer minValue)
    {
        if (minValue + 1 <= 0x3fff) {
            return minValue + 1;
        } else {
            return 0;
        }
    }

    private Integer getGroupMaxAddress() {
        if (mGroups.size() == 0) {
            return null;
        }
        int max = 0;
        for(mobleAddress address : mGroups.keySet()) {
            int na = address.mValue & 0x3FFF;
            if (na > max)
                max = na;
        }
        return max;
    }

    /**
     * Gets group min available address.
     *
     * @return the group min available address
     */
    public int getGroupMinAvailableAddress() {
        Integer max = getGroupMaxAddress();
        if (max == null) {
            return 0;
        }

        for (int i = 0; i<max; i++) {
            boolean consists = false;
            for (mobleAddress address : mGroups.keySet()) {
                if (i == (address.mValue & 0x3FFF)) {
                    consists = true;
                    break;
                }
            }
            if (!consists) return i;
        }

        return max + 1;
    }

    /**
     * Add group int.
     *
     * @param address the address
     * @param entry   the entry
     * @return the int
     */
    public int addGroup(mobleAddress address, GroupEntry entry) {
        if (address == null) {
            throw new IllegalArgumentException();
        }
        if(mGroups.size()<=NO_OF_GROUPS_ALLOWED){
        mGroups.put(address, entry);
            UserApplication.trace("mGroups "+mGroups.toString());
        return 1;
        }
        return 0;
    }

    /**
     * Remove group.
     *
     * @param address the address
     */
    public void removeGroup(mobleAddress address) {
        GroupEntry entry = mGroups.remove(address);
        if (entry == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets group.
     *
     * @param address the address
     * @return the group
     */
    public GroupEntry getGroup(mobleAddress address) {
        return mGroups.get(address);
    }

    /**
     * Gets groups set.
     *
     * @return the groups set
     */
    public Set<mobleAddress> getGroupsSet() {
        return mGroups.keySet();
    }

    /**
     * Add device to group.
     *
     * @param address the address
     * @param mac     the mac
     */
    void addDeviceToGroup(mobleAddress address, String mac) {
        GroupEntry entry = mGroups.get(address);
        if (entry == null) {
            throw new IllegalArgumentException();
        }
        Collection<String> list = entry.getDevices();
        if (!list.contains(mac)) {
            list.add(mac);
            mGroups.put(address, new GroupEntry(entry.getName(), list));
        }
    }

    /**
     * Remove device from group.
     *
     * @param address the address
     * @param mac     the mac
     */
    void removeDeviceFromGroup(mobleAddress address, String mac) {
        GroupEntry entry = mGroups.get(address);
        if (entry == null) {
            throw new IllegalArgumentException();
        }
        Collection<String> list = entry.getDevices();
        if (list.contains(mac)) {
            list.remove(mac);
            mGroups.put(address, new GroupEntry(entry.getName(), list));
        }
    }

    /**
     * Gets groups count.
     *
     * @return the groups count
     */
    int getGroupsCount() {
        return mGroups.size();
    }

    /**
     * Clear all.
     */
    void clearAll() {
        mDevices.clear();
        mGroups.clear();
    }

    /**
     * Is slave boolean.
     *
     * @return the boolean
     */
    boolean isSlave() {
        return mIsSlave;
    }
}
