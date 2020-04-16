package com.st.BlueSTSDK.Utils.advertise;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.Nullable;

import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

public class LeScanCallback implements BluetoothAdapter.LeScanCallback {

    private Manager mBleManager;
    private List<AdvertiseFilter> mAdvFilters;


    public LeScanCallback(Manager bleManager, List<AdvertiseFilter> advFilters) {
        this.mBleManager = bleManager;
        this.mAdvFilters = advFilters;
    }

    private @Nullable
    BleAdvertiseInfo matchAdvertise(byte[] advertise){
        for (AdvertiseFilter filter : mAdvFilters){
            BleAdvertiseInfo res = filter.filter(advertise);
            if (res!=null)
                return res;
        }
        return null;
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] advetiseData) {
        BleAdvertiseInfo info = matchAdvertise(advetiseData);
        /*if(bluetoothDevice.getAddress().equalsIgnoreCase("EF:D4:5D:D5:A3:E9"))
        {
            String s = array2string(advetiseData);
            System.out.println("Log Data : " + s);
        }*/
        if(info == null){
            return;
        } // else
        Node node = new Node(bluetoothDevice,rssi,info);
        mBleManager.addNode(node);

    }

    public static String array2string(byte[] data) {
        char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer buffer = new StringBuffer();
        if (data != null) {
            for (int i = 0; i < data.length; ++i) {
                buffer.append(hex[(data[i] >> 4) & 0x0F]);
                buffer.append(hex[(data[i] >> 0) & 0x0F]);
            }
        }
        return buffer.toString();
    }
}
