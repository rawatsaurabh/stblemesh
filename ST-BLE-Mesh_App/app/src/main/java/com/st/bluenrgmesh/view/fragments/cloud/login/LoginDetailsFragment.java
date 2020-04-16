/**
 * *****************************************************************************
 *
 * @file LoginDetailsFragment.java
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.clouddata.LoginData;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONObject;


public class LoginDetailsFragment extends BaseFragment {

    TextView sign_up_final_tv;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    public RequestQueue requestQueue;
    private View view;
    private AppDialogLoader loader;
    private EditText user_name_edit;
    private String user_name;
    private EditText password_edit;
    private String password;
    private Button login_button;
    private TextView txtSignUp;
    private MeshRootClass meshRootClass;
    private TextView txtForgotPassword;
    private CheckBox cbStayLoggedIn;
    private boolean stayLoggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_details, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new LoginDetailsFragment().getClass().getName());
        updateJsonData();
        initUi();

        return view;
    }

    private void updateJsonData() {

        try {
            meshRootClass = ParseManager.getInstance().fromJSON(
                    new JSONObject(Utils.getBLEMeshDataFromLocal(getActivity())), MeshRootClass.class);
            Utils.DEBUG(">> Json Data : " + Utils.getBLEMeshDataFromLocal(getActivity()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initUi() {
        final Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        user_name_edit = (EditText) view.findViewById(R.id.user_name_edit);
        password_edit = (EditText) view.findViewById(R.id.password_edit);
        login_button = (Button) view.findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = user_name_edit.getText().toString();
                password = password_edit.getText().toString();
                callLoginApi(user_name, password, stayLoggedIn);
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
        txtForgotPassword = (TextView) view.findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) vibrator.vibrate(40);
                //((MainActivity)getActivity()).onBackPressed();
                Utils.moveToFragment(getActivity(),new RecoverPasswordFragment(),null, 0);

            }
        });
        cbStayLoggedIn = (CheckBox) view.findViewById(R.id.cbStayLoggedIn);
        cbStayLoggedIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stayLoggedIn = isChecked;
            }
        });
    }

    private void callLoginApi(final String username, final String password, boolean stayLoggedIn) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_LoginDetails);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("lUserName", username);
            requestObject.put("lPassword", password);
            requestObject.put("staySignedInFlag", stayLoggedIn);

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
                        Utils.DEBUG("Login onResponse() called : " + response.toString());

                        try {
                            LoginData loginData = ParseManager.getInstance().fromJSON(response, LoginData.class);
                            if(loginData.getStatusCode() == 101)
                            {
                                //error show pop
                                Utils.setLoginData(getActivity(), null);
                                Utils.setUserLoginKey(getActivity(), null);
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, loginData.getErrorMessage(), null);
                            }
                            else if(loginData.getStatusCode() == 0) {
                                //success
                                //check if comes from setup n/w
                                //save data to local
                                LoginData loginData1 = new LoginData();
                                loginData1.setUserName(username);
                                loginData1.setUserPassword(password);
                                loginData1.setUserKey(loginData.getResponseMessage());

                                //check if previous user
                                /*if(Utils.getPreviousUserLoginKey(getActivity()).equalsIgnoreCase(loginData.getUserKey()))
                                {
                                    if(Utils.isUserRegisteredToDownloadJson(getActivity()))
                                    {

                                    }
                                    if(meshRootClass != null && meshRootClass.getNodes() != null && meshRootClass.getNodes().size() > 0)
                                    {
                                        Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                    }
                                    else
                                    {
                                        Utils.setUserRegisteredToDownloadJson(getActivity(), "false");
                                    }
                                }*/
                                //Utils.setUserRegisteredToDownloadJson(getActivity(), "false");
                                Utils.setUserRegisteredToDownloadJson(getActivity(), "false");
                                String loginDataString = ParseManager.getInstance().toJSON(loginData1);
                                Utils.setLoginData(getActivity(), loginDataString);
                                Utils.setPreviousUserLoginKey(getActivity(), loginData.getResponseMessage());
                                Utils.setUserLoginKey(getActivity(), loginData.getResponseMessage());
                                Utils.clearFragmentFromBackStack(getActivity(), new LoginDetailsFragment().getClass().getName());
                                Utils.DEBUG("Login Data : " + Utils.getLoginData(getActivity()));
                                Utils.showToast(getActivity(), getString(R.string.str_login_success_label));
                                //((MainActivity)getActivity()).onGetNewProvisioner();

                                /*if(!Utils.isUserRegisteredToDownloadJson(getActivity()))
                                {
                                    ((MainActivity)getActivity()).resumeNetworkAndCallbacks(meshRootClass.getMeshName(), meshRootClass.getMeshUUID(), false);
                                }*/
                                ((MainActivity)getActivity()).openDrawer();
                            }

                        }catch (Exception e){}
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
