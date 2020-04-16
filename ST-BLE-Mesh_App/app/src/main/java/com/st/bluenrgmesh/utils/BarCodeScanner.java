
/**
 ******************************************************************************
 * @file    BarCodeScanner.java
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
 */package com.st.bluenrgmesh.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.Utils;

import java.io.IOException;

import static com.st.bluenrgmesh.UserApplication.TAG;

public class BarCodeScanner {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static BarcodeDetector detector;
    private CameraSource cameraSource;

    public BarcodeDetector startBarCodeDetector(Context context)
    {
        if(detector == null)
        {
            detector = new BarcodeDetector.Builder(context)
                    .setBarcodeFormats(Barcode.ALL_FORMATS)
                    .build();
        }

        return detector;
    }

    public String getBarCodeData(Context context, TextView txtBarCode, Uri imageUri) {

        String value = "";
        try {
            Bitmap bitmap = Utils.decodeBitmapUri(context, imageUri);
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);
                for (int index = 0; index < barcodes.size(); index++) {
                    Barcode code = barcodes.valueAt(index);
                    txtBarCode.setText(code.displayValue + "\n");
                    value = code.displayValue.toString();
                    int type = barcodes.valueAt(index).valueFormat;
                    switch (type) {
                        case Barcode.CONTACT_INFO:
                            Log.i(TAG, code.contactInfo.title);
                            break;
                        case Barcode.EMAIL:
                            Log.i(TAG, code.displayValue);
                            break;
                        case Barcode.ISBN:
                            Log.i(TAG, code.rawValue);
                            break;
                        case Barcode.PHONE:
                            Log.i(TAG, code.phone.number);
                            break;
                        case Barcode.PRODUCT:
                            Log.i(TAG, code.rawValue);
                            break;
                        case Barcode.SMS:
                            Log.i(TAG, code.sms.message);
                            break;
                        case Barcode.TEXT:
                            Log.i(TAG, code.displayValue);
                            break;
                        case Barcode.URL:
                            Log.i(TAG, "url: " + code.displayValue);
                            break;
                        case Barcode.WIFI:
                            Log.i(TAG, code.wifi.ssid);
                            break;
                        case Barcode.GEO:
                            Log.i(TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                            break;
                        case Barcode.CALENDAR_EVENT:
                            Log.i(TAG, code.calendarEvent.description);
                            break;
                        case Barcode.DRIVER_LICENSE:
                            Log.i(TAG, code.driverLicense.licenseNumber);
                            break;
                        default:
                            Log.i(TAG, code.rawValue);
                            break;
                    }
                }
                if (barcodes.size() == 0) {
                    txtBarCode.setText("No barcode could be detected. Please try again.");
                }
            } else {
                txtBarCode.setText("Detector initialisation failed");
            }
        } catch (Exception e) {
            Toast.makeText(context, "Failed to load Image", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, e.toString());
        }

        return value;
    }

    public CameraSource initialiseDetectorsAndSources(Context context, BarcodeDetector detector, SurfaceView surfaceView, TextView txtBarCode) {

        //Toast.makeText(getActivity(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        cameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(1080, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraSource.start(surfaceView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions((MainActivity)context, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getActivity(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarCode.post(new Runnable() {

                        @Override
                        public void run() {

                            String intentData;
                            if (barcodes.valueAt(0).email != null) {
                                txtBarCode.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarCode.setText(intentData);
                                //isEmail = true;
                            } else {
                                //isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarCode.setText(intentData);

                            }
                        }
                    });

                }
            }
        });
         return cameraSource;
    }
}
