/**
 ******************************************************************************
 * @file    progress.java
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class progress {
    private ProgressDialog mDialog;
    private static Context mContext;
    private onEventListener mListener;
    private int countAttempts = 0;
    /**
     * The Start time.
     */
    public long startTime;
    private String mMessage = "Please wait...";
    /**
     * The Progress min delay.
     */
    public static final long PROGRESS_MIN_DELAY = 1000;

    /**
     * The interface On event listener.
     */
    public interface onEventListener {
        /**
         * On show.
         */
        void onShow();

        /**
         * On hide.
         */
        void onHide();

        /**
         * On cancel.
         */
        void onCancel();
    }

    /**
     * Instantiates a new Progress.
     *
     * @param context  the context
     * @param listener the listener
     */
    public progress(Context context, onEventListener listener) {
        mContext = context;
        mDialog = null;
        mListener = listener;
    }


    /**
     * Show.
     *
     * @param enable_progress the enable progress
     */
    public void show(Context context,boolean enable_progress) {
        show(context, mMessage, enable_progress);
    }

    /**
     * Show.
     *
     * @param text            the text
     * @param enable_progress the enable progress
     */
    public void show(Context mContext, String text, boolean enable_progress) {
        countAttempts++;
        if(countAttempts==1) {
            if (mListener != null) mListener.onShow();
            startTime = System.currentTimeMillis();
            mDialog = new ProgressDialog(mContext);
            mDialog.setCanceledOnTouchOutside(false);
            if(enable_progress) {
                mDialog.setMessage(text);
                mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDialog.setMax(10);
                mDialog.setIndeterminate(false);
                mDialog.setProgress(0);
            } else {
                mDialog.setIndeterminate(true);
                mDialog.setMessage(text);
            }
            mDialog.setOnCancelListener(new CancelListener(mListener));
            mDialog.show();
        }
    }

    /**
     * The type Cancel listener.
     */
    public static class CancelListener implements DialogInterface.OnCancelListener {
        private onEventListener mEventListener;

        /**
         * Instantiates a new Cancel listener.
         *
         * @param evnt the evnt
         */
        CancelListener(onEventListener evnt) {
            mEventListener = evnt;
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mEventListener != null) mEventListener.onCancel();
            mEventListener = null;
        }
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    void setProgress(int progress) {
        if (mDialog == null) return;
        mDialog.setProgress(progress);
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     * @param message  the message
     */
    public void setProgress(int progress, String message) {
        if (mDialog == null) return;
        mDialog.setProgress(progress);
        if(message != null) {
            mDialog.setMessage(message);
        }
    }

    /**
     * Hide.
     */
    public void hide() {
        if (mDialog == null) return;
        countAttempts--;
        if(countAttempts==0) {
            mDialog.setOnCancelListener(null);
            mDialog.dismiss();
            mDialog = null;
            startTime = -1;
            if (mListener != null) mListener.onHide();
        }
    }


}
