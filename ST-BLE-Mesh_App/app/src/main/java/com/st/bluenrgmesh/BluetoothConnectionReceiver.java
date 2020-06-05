package com.st.bluenrgmesh;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.st.bluenrgmes.CustomUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class BluetoothConnectionReceiver  extends BroadcastReceiver {
    public BluetoothConnectionReceiver(){
        //No initialisation code needed
    }

    @Override
    public void onReceive(Context context, Intent intent){

       /* if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction()))
        {
            int a = 0;
        }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction()))
        {

            String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bluetooth/bluenrg-mesh_configuration.json";
            File source = new File(sourcePath);

            String descPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
            File desc = new File(descPath);
            try
            {
                moveFile(source,desc);
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
        }*/
    }

    public static void moveFile(File srcFileOrDirectory, File desFileOrDirectory) throws IOException {
        File newFile = new File(desFileOrDirectory, srcFileOrDirectory.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel();
             FileChannel inputChannel = new FileInputStream(srcFileOrDirectory).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            deleteRecursive(srcFileOrDirectory);
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);
        fileOrDirectory.delete();
    }
}
