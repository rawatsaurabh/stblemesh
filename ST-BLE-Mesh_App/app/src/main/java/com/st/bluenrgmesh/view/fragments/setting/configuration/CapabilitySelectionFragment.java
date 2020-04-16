/**
 * *****************************************************************************
 *
 * @file  CapabilitySelectionfaragment.java
 * @author BLE Mesh Team
 * @version V1.11.000
 * @date 20-October-2019
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
package com.st.bluenrgmesh.view.fragments.setting.configuration;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputLayout;
import com.msi.moble.Capabilities;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.capabilities.InputActions;
import com.st.bluenrgmesh.models.capabilities.OutputActions;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.BarCodeScanner;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;



public class CapabilitySelectionFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private TextView txtHeading;
    private AppCompatRadioButton radioBlink;
    private AppCompatRadioButton radioBeep;
    private AppCompatRadioButton radioVibrate;
    private AppCompatRadioButton radioOutputNumeric;
    private AppCompatRadioButton radioOutputAlphanumeric;
    private AppCompatRadioButton radioPush;
    private AppCompatRadioButton radioTwist;
    private AppCompatRadioButton radioInputNumeric;
    private AppCompatRadioButton radioInputAlphanumeric;
    private Capabilities capabilities;
    private LinearLayout lytOutputOOB;
    private LinearLayout lytInputOOB;
    private RadioGroup rgOutputOOB;
    private RadioGroup rgInputOOB;
    private LinearLayout lytStaticOOB;
    private EditText edtEnterAuthKeyOrPublicKey;
    private Button butStaticOOBAuthenticate;
    private BarcodeDetector detector;
    private TextView txtBarCode;
    private Uri imageUri;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CAMERA_REQUEST = 101;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private String intentData;
    private boolean isEmail;
    private BarCodeScanner barCodeScanner;
    private ImageView imgBarCode;
    private ImageView refreshData;
    private TextView txtAboutText;
    private TextView txtNordikAuth;
    private TextView txtSTPublicKey;
    private TextView txtStaticOOBHeading;
    private int publicKeyViewType;
    private LinearLayout lytBarCodeScanner;
    private TextInputLayout txtInputStaticOOBValue;
    private TextView txtAuthKey;
    private Button butAssignPublicKey;
    private View viewRedLine;
    private TranslateAnimation mAnimation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capability, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new CapabilitySelectionFragment().getClass().getName());

        initUi();

        return view;
    }

    private void initUi() {

        capabilities = (Capabilities) getArguments().getSerializable(getString(R.string.key_serializable));
        publicKeyViewType = getArguments().getInt(getString(R.string.key_view_type), 0);

        lytOutputOOB = (LinearLayout) view.findViewById(R.id.lytOutputOOB);
        radioBlink = (AppCompatRadioButton) view.findViewById(R.id.radioBlink);
        radioBeep = (AppCompatRadioButton) view.findViewById(R.id.radioBeep);
        radioVibrate = (AppCompatRadioButton) view.findViewById(R.id.radioVibrate);
        radioOutputNumeric = (AppCompatRadioButton) view.findViewById(R.id.radioOutputNumeric);
        radioOutputAlphanumeric = (AppCompatRadioButton) view.findViewById(R.id.radioOutputAlphanumeric);
        rgOutputOOB = (RadioGroup) view.findViewById(R.id.rgOutputOOB);

        lytInputOOB = (LinearLayout) view.findViewById(R.id.lytInputOOB);
        radioPush = (AppCompatRadioButton) view.findViewById(R.id.radioPush);
        radioTwist = (AppCompatRadioButton) view.findViewById(R.id.radioTwist);
        radioInputNumeric = (AppCompatRadioButton) view.findViewById(R.id.radioInputNumeric);
        radioInputAlphanumeric = (AppCompatRadioButton) view.findViewById(R.id.radioInputAlphanumeric);
        rgInputOOB = (RadioGroup) view.findViewById(R.id.rgInputOOB);

        txtInputStaticOOBValue = (TextInputLayout) view.findViewById(R.id.txtInputStaticOOBValue);
        txtStaticOOBHeading = (TextView) view.findViewById(R.id.txtStaticOOBHeading);
        lytStaticOOB = (LinearLayout) view.findViewById(R.id.lytStaticOOB);
        lytBarCodeScanner = (LinearLayout) view.findViewById(R.id.lytBarCodeScanner);
        edtEnterAuthKeyOrPublicKey = (EditText) view.findViewById(R.id.edtEnterAuthKeyOrPublicKey);
        butStaticOOBAuthenticate = (Button) view.findViewById(R.id.butStaticOOBAuthenticate);
        txtBarCode = (TextView) view.findViewById(R.id.txtBarCode);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        imgBarCode = (ImageView) view.findViewById(R.id.imgBarCode);
        refreshData = (ImageView) view.findViewById(R.id.refreshData);
        txtAboutText = (TextView) view.findViewById(R.id.txtAboutText);
        txtNordikAuth = (TextView) view.findViewById(R.id.txtNordikAuth);
        txtAuthKey = (TextView) view.findViewById(R.id.txtAuthKey);
        txtSTPublicKey = (TextView) view.findViewById(R.id.txtSTPublicKey);
        butAssignPublicKey = (Button) view.findViewById(R.id.butAssignPublicKey);
        viewRedLine = (View) view.findViewById(R.id.viewRedLine);

        txtBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEnterAuthKeyOrPublicKey.setText(txtBarCode.getText().toString());
            }
        });

        txtSTPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edtEnterAuthKeyOrPublicKey.setText(getString(R.string.str_oob_st_public_key));
                edtEnterAuthKeyOrPublicKey.setText(getString(R.string.str_oob_authkey_via_pk));
            }
        });

        txtAuthKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity().getResources().getBoolean(R.bool.bool_PTS))
                {
                    //edtEnterAuthKeyOrPublicKey.setText(MainActivity.isPublicKeyEnabled ? getString(R.string.str_oob_authkey_via_pk) : getString(R.string.str_oob_authkey_without_pk_PTS));
                    edtEnterAuthKeyOrPublicKey.setText(MainActivity.isPublicKeyEnabled ? getString(R.string.str_oob_authkey_without_pk_PTS) : getString(R.string.str_oob_authkey_without_pk_PTS));
                }
                else {
                    edtEnterAuthKeyOrPublicKey.setText(MainActivity.isPublicKeyEnabled ? getString(R.string.str_oob_authkey_via_pk) : getString(R.string.str_oob_authkey_without_pk));
                }
            }
        });

        txtNordikAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEnterAuthKeyOrPublicKey.setText("6E6F726469635F6578616D706C655F31");
            }
        });

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraSource != null) {
                    imgBarCode.setVisibility(View.GONE);
                    //initialiseDetectorsAndSources();
                    edtEnterAuthKeyOrPublicKey.setText("");
                    mAnimation = new TranslateAnimation(
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
                    mAnimation.setDuration(3000);
                    mAnimation.setRepeatCount(-1);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    viewRedLine.setAnimation(mAnimation);
                }
            }
        });



        if (capabilities.getOobTypeSelected() == 1) {
            //input oob
            txtAboutText.setText(getString(R.string.str_input_oob_attention));
            lytOutputOOB.setVisibility(View.GONE);
            lytStaticOOB.setVisibility(View.GONE);
            lytInputOOB.setVisibility(View.VISIBLE);
            int actionSize = capabilities.getInputOOBSize();
            InputActions inputActions = Utils.getInputTypeActions(getActivity(), actionSize, capabilities.getInputOOBActions());
            radioPush.setEnabled(inputActions.isPush() ? true : false);
            radioTwist.setEnabled(inputActions.isTwist() ? true : false);
            radioInputNumeric.setEnabled(inputActions.isInputNumeric() ? true : false);
            radioInputAlphanumeric.setEnabled(inputActions.isInputAlphanumeric() ? true : false);
            rgInputOOB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    AppCompatRadioButton radioOutput = (AppCompatRadioButton) radioGroup.findViewById(i);
                    if (radioOutput != null) {
                        if (radioOutput.getText().toString().equalsIgnoreCase("Push")) {
                            capabilities.setInputOOBActions(0);
                        } else if (radioOutput.getText().toString().equalsIgnoreCase("Twist")) {
                            capabilities.setInputOOBActions(1);
                        } else if (radioOutput.getText().toString().equalsIgnoreCase("Input Numeric")) {
                            capabilities.setInputOOBActions(2);
                        } else if (radioOutput.getText().toString().equalsIgnoreCase("Input Alphanumeric")) {
                            capabilities.setInputOOBActions(3);
                        }

                        ((MainActivity) getActivity()).identifier.setIdentified(true, capabilities);

                        //((MainActivity)getActivity()).onBackPressed();
                    }
                }
            });
        }
        else if (capabilities.getOobTypeSelected() == 2) {
            //output oob
            txtAboutText.setText(getString(R.string.str_output_oob_attention));
            lytOutputOOB.setVisibility(View.VISIBLE);
            lytInputOOB.setVisibility(View.GONE);
            lytStaticOOB.setVisibility(View.GONE);
            int actionSize = capabilities.getOutputOOBSize();
            OutputActions outputOOB = Utils.getOutputTypeAction(getActivity(), actionSize, capabilities.getOutputOOBActions());
            radioBlink.setEnabled(outputOOB.isBlink() ? true : false);
            radioBeep.setEnabled(outputOOB.isBeep() ? true : false);
            radioVibrate.setEnabled(outputOOB.isVibrate() ? true : false);
            radioOutputNumeric.setEnabled(outputOOB.isOutputNumeric() ? true : false);
            radioOutputAlphanumeric.setEnabled(outputOOB.isOutputAlphanumeric() ? true : false);
            rgOutputOOB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    AppCompatRadioButton radioOutput = (AppCompatRadioButton) radioGroup.findViewById(i);
                    if (radioOutput != null) {
                        if (radioOutput.getText().toString().equalsIgnoreCase(getString(R.string.str_blink_oob))) {
                            capabilities.setOutputOOBActions(0);
                            Utils.setSelectedCapability(getActivity(), getString(R.string.str_blink_oob));
                        } else if (radioOutput.getText().toString().equalsIgnoreCase(getString(R.string.str_beep_oob))) {
                            capabilities.setOutputOOBActions(1);
                            Utils.setSelectedCapability(getActivity(), getString(R.string.str_beep_oob));
                        } else if (radioOutput.getText().toString().equalsIgnoreCase(getString(R.string.str_Vibrate_oob))) {
                            capabilities.setOutputOOBActions(2);
                            Utils.setSelectedCapability(getActivity(), getString(R.string.str_Vibrate_oob));
                        } else if (radioOutput.getText().toString().equalsIgnoreCase(getString(R.string.str_output_numeric_oob))) {
                            capabilities.setOutputOOBActions(3);
                            Utils.setSelectedCapability(getActivity(), getString(R.string.str_output_numeric_oob));
                        } else if (radioOutput.getText().toString().equalsIgnoreCase(getString(R.string.str_output_Alphanumeric_oob))) {
                            capabilities.setOutputOOBActions(4);
                            Utils.setSelectedCapability(getActivity(), getString(R.string.str_output_Alphanumeric_oob));
                        }

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.DEBUG(">> Start Output OOB 1");
                                ((MainActivity) getActivity()).identifier.setIdentified(true, capabilities);
                            }
                        });

                        //((MainActivity)getActivity()).onBackPressed();
                    }
                }
            });
        }
        else if (capabilities.getOobTypeSelected() == 3) {
            //static oob
            barCodeScanner = new BarCodeScanner();
            detector = barCodeScanner.startBarCodeDetector(getActivity());

            if (!detector.isOperational()) {
                txtBarCode.setText("Barcode Detector initialisation failed");
                return;
            }
            if(MainActivity.isPublicKeyEnabled)
            {
                txtSTPublicKey.setVisibility(View.GONE);
                txtNordikAuth.setVisibility(View.GONE);
                txtAuthKey.setVisibility(View.VISIBLE);
                edtEnterAuthKeyOrPublicKey.setText(getString(R.string.str_oob_authkey_without_pk));
                txtAuthKey.setText(getString(R.string.str_oob_authkey_without_pk));
                butAssignPublicKey.setVisibility(View.GONE);
                butStaticOOBAuthenticate.setVisibility(View.VISIBLE);
            }
            txtAboutText.setText(getString(R.string.str_static_oob_attention));
            lytOutputOOB.setVisibility(View.GONE);
            lytStaticOOB.setVisibility(View.VISIBLE);
            lytInputOOB.setVisibility(View.GONE);
            butAssignPublicKey.setVisibility(View.GONE);
            butStaticOOBAuthenticate.setVisibility(View.VISIBLE);
            butStaticOOBAuthenticate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //byte[] bytes = Utils.arrayFromString(edtEnterStaticOOB.getText().toString());
                    byte[] authKey;
                    if (MainActivity.isPublicKeyEnabled) {
                        //Auth-Key via Public Key Process
                        //authKey = Utils.arrayFromString(getString(R.string.str_oob_authkey_via_pk));
                        authKey = Utils.arrayFromString(edtEnterAuthKeyOrPublicKey.getText().toString());
                    } else {
                        //Auth-Key without Public Key Process
                        //authKey = Utils.arrayFromString(getString(R.string.str_oob_authkey_without_pk));
                        authKey = Utils.arrayFromString(edtEnterAuthKeyOrPublicKey.getText().toString());
                        byte[] b = new byte[0];
                        capabilities.setPkTypes(b);
                    }

                    capabilities.setAuth(authKey);
                    //((MainActivity)getActivity()).identifier.setIdentified(true, capabilities);
                    //((MainActivity)getActivity()).capabilities = capabilities;
                    //((MainActivity)getActivity()).fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null, -4, null, false, null);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).identifier.setIdentified(true, capabilities);
                        }
                    });

                    ((MainActivity) getActivity()).onBackPressed();
                }
            });

            refreshData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cameraSource != null) {
                        imgBarCode.setVisibility(View.GONE);
                        //initialiseDetectorsAndSources();
                        edtEnterAuthKeyOrPublicKey.setText("");
                        mAnimation = new TranslateAnimation(
                                TranslateAnimation.ABSOLUTE, 0f,
                                TranslateAnimation.ABSOLUTE, 0f,
                                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                                TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
                        mAnimation.setDuration(3000);
                        mAnimation.setRepeatCount(-1);
                        mAnimation.setRepeatMode(Animation.REVERSE);
                        mAnimation.setInterpolator(new LinearInterpolator());
                        viewRedLine.setAnimation(mAnimation);
                    }
                }
            });

            if (publicKeyViewType == getResources().getInteger(R.integer.STATIC_NUMBER) || publicKeyViewType == getResources().getInteger(R.integer.STATIC_STRING)) {
                lytBarCodeScanner.setVisibility(View.GONE);
                txtInputStaticOOBValue.setHint("Enter Public Key Value");
            } else {
                lytBarCodeScanner.setVisibility(View.VISIBLE);
            }
        }
        else {

            barCodeScanner = new BarCodeScanner();
            detector = barCodeScanner.startBarCodeDetector(getActivity());

            if (!detector.isOperational()) {
                txtBarCode.setText("Barcode Detector initialisation failed");
                return;
            }
            //via public key
            txtAboutText.setText(getString(R.string.str_static_oob_attention));
            lytOutputOOB.setVisibility(View.GONE);
            lytStaticOOB.setVisibility(View.VISIBLE);
            lytInputOOB.setVisibility(View.GONE);
            lytBarCodeScanner.setVisibility(View.VISIBLE);

            txtSTPublicKey.setVisibility(View.VISIBLE);
            //txtSTPublicKey.setText(getString(R.string.str_oob_st_public_key));
            txtSTPublicKey.setText(getString(R.string.str_oob__pk));
            txtAuthKey.setVisibility(View.GONE);
            txtNordikAuth.setVisibility(View.GONE);
            txtStaticOOBHeading.setText("Static OOB - Public Key Authentication");
            edtEnterAuthKeyOrPublicKey.setText(getString(R.string.str_oob__pk));
            butAssignPublicKey.setVisibility(View.VISIBLE);
            butStaticOOBAuthenticate.setVisibility(View.GONE);
            butAssignPublicKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Static OOB with public key
                    //public key : 529AA0670D72CD6497502ED473502B037E8803B5C60829A5A3CAA219505530BA
                    //byte bytes = Utils.arrayFromString(edtEnterStaticOOB.getText().toString());
                    //byte pkByte = Byte.parseByte(edtEnterStaticOOB.getText().toString());
                    MainActivity.isPublicKeyEnabled = true;
                    byte[] pkByte = Utils.arrayFromString(edtEnterAuthKeyOrPublicKey.getText().toString());
                    capabilities.setPkTypes(pkByte);
                    //update capabilities object from this object
                    ((MainActivity) getActivity()).capabilities = capabilities;
                    ((MainActivity) getActivity()).fragmentCommunication(new ConnectionSetupFragment().getClass().getName(), null, -3, null, false, null);
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });

            if (publicKeyViewType == getResources().getInteger(R.integer.STATIC_NUMBER) || publicKeyViewType == getResources().getInteger(R.integer.STATIC_STRING)) {
                lytBarCodeScanner.setVisibility(View.GONE);
                txtInputStaticOOBValue.setHint("Enter Public Key Value");
            } else {
                lytBarCodeScanner.setVisibility(View.VISIBLE);
            }
        }

    }

    public void setBarCodeData() {

        if (barCodeScanner != null && detector != null) {
            String barCodeData = barCodeScanner.getBarCodeData(getActivity(), txtBarCode, imageUri);
        }
    }

    /*@Override
    public void onBackEventPre() {
        super.onBackEventPre();
        MainActivity.isPublicKeyEnabled = false;
        ((MainActivity) getActivity()).capabilities = MainActivity.identifier.getCapabilities();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (barCodeScanner != null && detector != null) {
            cameraSource = barCodeScanner.initialiseDetectorsAndSources(getActivity(), detector, surfaceView, txtBarCode);
        }
    }

    public void initialiseDetectorsAndSources() {

        if (barCodeScanner != null && detector != null) {
            cameraSource = barCodeScanner.initialiseDetectorsAndSources(getActivity(), detector, surfaceView, txtBarCode);
        }
    }
}
