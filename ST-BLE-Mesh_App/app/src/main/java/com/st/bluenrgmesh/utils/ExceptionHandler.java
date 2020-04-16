/**
 * *****************************************************************************
 *
 * @file ExceptionHandler.java
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
package com.st.bluenrgmesh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.st.bluenrgmesh.UserApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());
        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        //dumpCrashLogs(errorReport.toString());
        openMailClient(errorReport.toString());
    }

    private void openMailClient(String logs) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"stm.mobileapp@st.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Crash Report");
        intent.putExtra(Intent.EXTRA_TEXT, logs);
        UserApplication.getInstance().startActivity(Intent.createChooser(intent, ""));

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void dumpCrashLogs(String logs) {

        File file, crashFolder;
        FileOutputStream outputStream;
        crashFolder = new File(Environment.getExternalStorageDirectory(),
                "BlueNRG_CrashReport/");
        try {
            if (!crashFolder.exists())
                crashFolder.mkdirs();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss_yyyyMMdd");
            String currentTimeStamp = dateFormat.format(new Date());
            file = new File(Environment.getExternalStorageDirectory(), "BlueNRG_CrashReport/" + "BlueCrashReport_" + currentTimeStamp);

            outputStream = new FileOutputStream(file);
            outputStream.write(logs.getBytes());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
