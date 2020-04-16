/**
 * *****************************************************************************
 *
 * @file HealthConfigFragment.java
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
package com.st.bluenrgmesh.view.fragments.other.meshmodels.health;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.HealthModelClient;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.settings.NodeSettings;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.InputFilterMinMax;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import java.util.ArrayList;

import static com.st.bluenrgmesh.MainActivity.network;


public class HealthConfigFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private View view;
    private AppDialogLoader loader;
    private Button get_health_faultBT, get_health_attentionBT, get_health_periodBT, clear_health_faultBT;
    private EditText set_health_periodET, set_health_attentionET;
    private HealthModelClient mHealthModelClient;
    private NodeSettings nodeSettings;
    private ApplicationParameters.CompanyID companyID;
    private ApplicationParameters.Address TEST_M_ADDRESS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_model_config, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new HealthConfigFragment().getClass().getName());
        updateJsonData();
        initUi();

        return view;
    }

    private void initUi() {
        nodeSettings = (NodeSettings) getArguments().getSerializable(getString(R.string.key_serializable));
        TEST_M_ADDRESS = new ApplicationParameters.Address(Integer.parseInt(nodeSettings.getNodesUnicastAddress()));
        mHealthModelClient = network.getHealthModelClient();
        companyID = new ApplicationParameters.CompanyID(BluetoothAssignedNumbers.ST_MICROELECTRONICS);


        get_health_faultBT = (Button) view.findViewById(R.id.get_health_faultBT);
        get_health_attentionBT = (Button) view.findViewById(R.id.get_health_attentionBT);
        get_health_periodBT = (Button) view.findViewById(R.id.get_health_periodBT);
        clear_health_faultBT = (Button) view.findViewById(R.id.clear_health_faultBT);
        set_health_periodET = (EditText) view.findViewById(R.id.set_health_periodET);
        set_health_attentionET = (EditText) view.findViewById(R.id.set_health_attentionET);
        set_health_attentionET.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        set_health_periodET.setFilters(new InputFilter[]{new InputFilterMinMax("0", "15")});

        get_health_faultBT.setOnClickListener(this);
        get_health_attentionBT.setOnClickListener(this);
        get_health_periodBT.setOnClickListener(this);
        clear_health_faultBT.setOnClickListener(this);
        set_health_periodET.setOnEditorActionListener(this);
        set_health_attentionET.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (v.getId() == R.id.set_health_periodET ) {
                if(set_health_periodET.getText().toString().isEmpty()){
                    showToast("Enter value 0 to 15", Gravity.CENTER);
                }
                else{
                    ApplicationParameters.FastPeriodDivisor fastPeriodDivisor = new ApplicationParameters.FastPeriodDivisor(Integer.parseInt(set_health_periodET.getText().toString()), "FAST_FASTPERIODDIVISOR_DIVISOR_ONE");
                    mHealthModelClient.setHealthPeriod(true, TEST_M_ADDRESS, fastPeriodDivisor, mHealthPeriodStatusCallback);
                }
            } else  {
                if(set_health_attentionET.getText().toString().isEmpty()){
                    showToast("Enter value 0 to 255", Gravity.CENTER);
                }
                else{
                    ApplicationParameters.Attention attention = new ApplicationParameters.Attention(Integer.parseInt(set_health_attentionET.getText().toString()), "ATTENTION_ON");
                    mHealthModelClient.setHealthAttention(true, TEST_M_ADDRESS, attention, mHealthAttentionStatusCallback);
                }
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }


    private void updateJsonData() {


    }


    private final HealthModelClient.HealthFaultStatusCallback mHealthFaultStatusCallback = new HealthModelClient.HealthFaultStatusCallback() {
        @Override
        public void onFaultStatus(boolean timeout, ApplicationParameters.TestID testID, ApplicationParameters.CompanyID companyID, ArrayList<String> faultArrays) {
            if (loader != null) loader.dismiss();
            if (timeout) {
                showToast("Timeout error occured !", Gravity.CENTER);

                UserApplication.trace("Health Fault State");
            } else {
                UserApplication.trace("Company ID : " + companyID);
                UserApplication.trace("Fault Array : ");
                for (int i = 0; i < faultArrays.size(); i++) {
                    UserApplication.trace(faultArrays.get(i));//Populate this on the Pop up UI.
                }
                if (faultArrays != null) {
                    if (faultArrays.size() > 0) {
                        Utils.showHealthSettingPopup(getActivity(), faultArrays, "Get Health Fault Response");
                    } else {

                        showToast("No Health Faults Found..", Gravity.CENTER);

                    }
                } else {
                    showToast("No Faults Found !", Gravity.CENTER);


                }
            }
        }
    };

    private final HealthModelClient.HealthAttentionStatusCallback mHealthAttentionStatusCallback = new HealthModelClient.HealthAttentionStatusCallback() {
        @Override
        public void onHealthAttentionStatus(boolean timeout,
                                            ApplicationParameters.Attention attention) {

            if (timeout) {
                showToast("Timeout error occured !", Gravity.CENTER);

                UserApplication.trace("Health Attention State");
            } else {

                showToast("Attention timer state value = " + attention, Gravity.CENTER);
                UserApplication.trace("Parameters: attention " + attention);

            }
        }
    };
    private final HealthModelClient.HealthPeriodStatusCallback mHealthPeriodStatusCallback = new HealthModelClient.HealthPeriodStatusCallback() {
        @Override
        public void onPeroidStatus(boolean timeout, ApplicationParameters.FastPeriodDivisor mFastPeriodDivisor) {
            if (timeout) {
                showToast("Timeout error occured !", Gravity.CENTER);
                UserApplication.trace("Health Period State");
            } else {
                UserApplication.trace("FastPeriodDivisor : " + mFastPeriodDivisor);
                showToast("Fast period divisor value = " + mFastPeriodDivisor, Gravity.CENTER);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_health_attentionBT:
                mHealthModelClient.getHealthAttention(TEST_M_ADDRESS, mHealthAttentionStatusCallback);
                break;
            case R.id.get_health_faultBT:
                if (loader != null) loader.show();
                mHealthModelClient.getHealthFault(TEST_M_ADDRESS, companyID, mHealthFaultStatusCallback);
                break;
            case R.id.get_health_periodBT:
                mHealthModelClient.getHealthPeriod(true, TEST_M_ADDRESS, mHealthPeriodStatusCallback);
                break;
            case R.id.clear_health_faultBT:
                mHealthModelClient.clearHealthFault(false, TEST_M_ADDRESS, companyID, mHealthFaultStatusCallback);
                showToast("Health Faults are cleared..", Gravity.CENTER);
                break;
            default:
                break;
        }
    }

    public void showToast(String msg, int gravity) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        toast.setGravity(gravity, 0, 0);
        toast.show();

    }
}
