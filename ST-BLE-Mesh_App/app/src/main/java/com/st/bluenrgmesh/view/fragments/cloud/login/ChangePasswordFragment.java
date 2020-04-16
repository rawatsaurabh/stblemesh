/**
 * *****************************************************************************
 *
 * @file ChangePasswordFragment.java
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

package com.st.bluenrgmesh.view.fragments.cloud.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.clouddata.TempPasswordData;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private TextView txtAlreadyRegistered;
    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private Button butChangePassword;
    private TextView txtSignUp;
    private EditText edtConfirmPassword;
    private TextView txtOldPassword;
    private String data;
    private TempPasswordData tempPasswordData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgotpassword, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new ChangePasswordFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        Bundle bundle = this.getArguments();
        data = (String)bundle.getSerializable(getString(R.string.key_serializable));
        try {
            tempPasswordData = ParseManager.getInstance().fromJSON(new JSONObject(data), TempPasswordData.class);
        } catch (JSONException e) {
        }

        final Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        txtAlreadyRegistered = (TextView) view.findViewById(R.id.txtAlreadyRegistered);
        txtOldPassword = (TextView) view.findViewById(R.id.txtOldPassword);
        edtNewPassword = (EditText) view.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        butChangePassword = (Button) view.findViewById(R.id.butChangePassword);
        butChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vibrator != null) vibrator.vibrate(40);
                String oldPassword = txtOldPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                if(oldPassword.length() > 1 &&  newPassword.length() > 1 && newPassword.equals(confirmPassword))
                {
                    call_ChangePassword_API(tempPasswordData.getSessionKey(), oldPassword, newPassword);
                }
                else
                {
                    Utils.showToast(getActivity(),"Your entry is not correct.");
                }
            }
        });

        txtAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.moveToFragment(getActivity(), new LoginDetailsFragment(), null, 0);
            }
        });
        txtSignUp = (TextView) view.findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                Utils.clearFragmentFromBackStack(getActivity(), new LoginDetailsFragment().getClass().getName());
                Utils.moveToFragment(getActivity(), new SignUpFragment(), null, 0);
            }
        });


        if(tempPasswordData != null)
        {
            txtOldPassword.setText(tempPasswordData.getTempPassword());
        }

    }

    public void call_ChangePassword_API(String sessionKey, String oldPassword, String newPassword) {

        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_changePassword);

        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", sessionKey);
            requestObject.put("oldPassword", oldPassword);
            requestObject.put("newPassword", newPassword);

        } catch (Exception e) {
            Utils.ERROR("Error while creating json request : " + e.toString());
        }
        MyJsonObjectRequest jsonObjReq = new MyJsonObjectRequest(
                false,
                getActivity(),
                Request.Method.POST,
                url,
                requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            return;
                        }
                        Utils.DEBUG("API_changePassword onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if (cloudResponseData.getStatusCode() == 110 || cloudResponseData.getStatusCode() == 101||
                                    cloudResponseData.getStatusCode() == 107|| cloudResponseData.getStatusCode() == 002) {
                                Utils.showToast(getActivity(), cloudResponseData.getErrorMessage());
                            }  else if (cloudResponseData.getStatusCode() == 0) {

                                Utils.showToast(getActivity(), getString(R.string.str_passwordchanged_success_label));
                                ((MainActivity)getActivity()).onBackPressed();
                                ((MainActivity)getActivity()).onBackPressed();
                                Utils.updateActionBarForFeatures(getActivity(), new LoginDetailsFragment().getClass().getName());
                            }

                        } catch (Exception e) {
                        }
                        loader.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ERROR("Error: " + error);
                //Utils.showToast(getActivity(), getString(R.string.string_common_error_message));
                loader.hide();
            }
        }
        );
        // Adding request to request queue
        UserApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
