/**
 * *****************************************************************************
 *
 * @file AppDialogLoader.java
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.WindowManager;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;

public class AppDialogLoader {
    private static AppDialogLoader loader = null;
    private static AppDialogLoader previousFragmentLoader = null;
    private static Context con;
    private ProgressDialog pDialog;

    public AppDialogLoader(Context context) {
        pDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);

        pDialog.setCancelable(false);

        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                /****cleanup code****/
                dismiss();
            }
        });
    }


    public AppDialogLoader(Context context, int progressDialogTheme) {
        pDialog = new ProgressDialog(context);
        pDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        pDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);

    }

    public AppDialogLoader(Context context, int progressDialogTheme, String msg) {
        pDialog = new ProgressDialog(context);
        pDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        pDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pDialog.setMessage(msg + "...");
        pDialog.setProgressDrawable(context.getDrawable(R.drawable.gifsimpleloader));
        pDialog.setCancelable(true);

    }

    public static AppDialogLoader getLoader(Context context) {
        con = context;

        {
            loader = new AppDialogLoader(context);
            previousFragmentLoader = loader;
        }

        return loader;
    }

    public static AppDialogLoader getProxyLoader(Context context, String msg) {
        con = context;

        if(loader == null)
        {
            loader = new AppDialogLoader(context,0, msg);
        }

        return loader;
    }

    public static AppDialogLoader getLoader(Context context, int progressDialogTheme) {
        con = context;
        {
            loader = new AppDialogLoader(context, progressDialogTheme);
            previousFragmentLoader = loader;
        }

        return loader;
    }

    public boolean CheckLoaderStatus() {

        if (pDialog.isShowing() || previousFragmentLoader != null) {
            return true;
        } else {
            return false;
        }
    }

    public int count = 0;
    public void show() {
        Utils.DEBUG("AppDialogLoader >> show() : " + pDialog + " Countp >> : " + count);
        count++;
        if(!pDialog.isShowing() && !((Activity) con).isFinishing())//use this condition to check, for any reason if the activity is destroyed then it will prevent from crash
        {
            try {
                pDialog.show();
            }catch (Exception e){}
        }
    }

    public void dismiss() {
        Utils.DEBUG("AppDialogLoader >> dismiss() > " + pDialog);
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void hide() {
        Utils.DEBUG("AppDialogLoader >> hide() > " + pDialog);
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }


}
