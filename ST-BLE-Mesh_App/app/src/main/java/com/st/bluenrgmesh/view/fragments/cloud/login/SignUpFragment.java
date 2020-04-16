/**
 * *****************************************************************************
 *
 * @file SignUpFragment.java
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.st.bluenrgmesh.models.clouddata.SignUpData;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;

public class SignUpFragment extends BaseFragment {

    public RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private View view;
    private AppDialogLoader loader;
    private Button sign_up_button;
    private EditText user_name_edit;
    private EditText password_edit;
    private EditText email_id_edit;
    private String user_name;
    private String password;
    private String email_id;
    private Button butSignIn;
    private TextView txtSignIn;
    private Switch swtAskSecurityQuestion;
    private LinearLayout lytQuestionAnswer;
    private EditText edtQuest1;
    private EditText edtQuest2;
    private EditText edtQuest3;
    private EditText edtAns1;
    private EditText edtAns2;
    private EditText edtAns3;
    private ArrayList<String> listQuestion = new ArrayList<>();
    private ArrayList<String> listAnswer = new ArrayList<>();
    private MeshRootClass meshRootClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new SignUpFragment().getClass().getName());
        updateJsonData();
        initUi();

        return view;
    }

    public void initUi(){

        user_name_edit = (EditText) view.findViewById(R.id.user_name_edit_SU);
        password_edit = (EditText) view.findViewById(R.id.password_edit_SU);
        email_id_edit = (EditText) view.findViewById(R.id.email_id_edit_SU);

        txtSignIn = (TextView) view.findViewById(R.id.txtSignIn);
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearFragmentFromBackStack(getActivity(), new SignUpFragment().getClass().getName());
                Utils.moveToFragment(getActivity(), new LoginDetailsFragment(), null, 0);
            }
        });
        sign_up_button = (Button) view.findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listQuestion.clear();
                listAnswer.clear();
                user_name = user_name_edit.getText().toString();
                password = password_edit.getText().toString();
                email_id = email_id_edit.getText().toString();

                listQuestion.add(edtQuest1.getText().toString());
                listQuestion.add(edtQuest2.getText().toString());
                listQuestion.add(edtQuest3.getText().toString());

                listAnswer.add(edtAns1.getText().toString());
                listAnswer.add(edtAns2.getText().toString());
                listAnswer.add(edtAns3.getText().toString());

                callSignUpApi(email_id, user_name, password);

            }
        });

        lytQuestionAnswer = (LinearLayout) view.findViewById(R.id.lytQuestionAnswer);

        edtQuest1 = (EditText) view.findViewById(R.id.edtQuest1);
        edtQuest2 = (EditText) view.findViewById(R.id.edtQuest2);
        edtQuest3 = (EditText) view.findViewById(R.id.edtQuest3);
        edtAns1 = (EditText) view.findViewById(R.id.edtAns1);
        edtAns2 = (EditText) view.findViewById(R.id.edtAns2);
        edtAns3 = (EditText) view.findViewById(R.id.edtAns3);

        swtAskSecurityQuestion = (Switch) view.findViewById(R.id.swtAskSecurityQuestion);
        swtAskSecurityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch viewById = (Switch)v.findViewById(R.id.swtAskSecurityQuestion);
                if(viewById.isChecked())
                {
                    lytQuestionAnswer.setVisibility(View.VISIBLE);
                }
                else
                {
                    lytQuestionAnswer.setVisibility(View.GONE);
                }

            }
        });


    }

    private void callSignUpApi(final String email, final String usrename , final String password) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_SignUp);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("Username", usrename);
            requestObject.put("password", password);
            requestObject.put("Email", email);

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
                        Utils.DEBUG("Signup onResponse() called : " + response.toString());

                        try {
                            SignUpData signup = ParseManager.getInstance().fromJSON(response, SignUpData.class);
                            if(signup.getStatusCode() == 106 || signup.getStatusCode() == 105|| signup.getStatusCode() == 104)
                            {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, signup.getErrorMessage(), null);
                            }
                            else if(signup.getStatusCode() == 0)
                            {
                                //set response
                                Utils.showToast(getActivity(), getString(R.string.str_signup_successfull_label));
                                LoginData loginData1 = new LoginData();
                                loginData1.setUserName(usrename);
                                loginData1.setUserPassword(password);
                                loginData1.setUserKey(signup.getResponseMessage());
                                String loginDataString = ParseManager.getInstance().toJSON(loginData1);
                                Utils.setLoginData(getActivity(), loginDataString);
                                Utils.setUserLoginKey(getActivity(), signup.getResponseMessage());
                                Utils.setPreviousUserLoginKey(getActivity(), signup.getResponseMessage());

                                //call securityquestionapi
                                callregisterSecurityQuestionApi(usrename);

                            }
                            else
                            {
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, signup.getErrorMessage(), null);
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

    private void callregisterSecurityQuestionApi(String usrename) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_registerSecurityQuestion);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userName", usrename);
            requestObject.put("firstQuestion", listQuestion.get(0));
            requestObject.put("secondQuestion", listQuestion.get(1));
            requestObject.put("thirdQuestion", listQuestion.get(2));
            requestObject.put("firstPassword", listAnswer.get(0));
            requestObject.put("secondPassword", listAnswer.get(1));
            requestObject.put("thirdPassword", listAnswer.get(2));

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
                        Utils.DEBUG("Signup onResponse() called : " + response.toString());

                        try {
                            SignUpData signup = ParseManager.getInstance().fromJSON(response, SignUpData.class);
                            if(signup.getStatusCode() == 106 || signup.getStatusCode() == 101 || signup.getStatusCode() == 002)
                            {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, signup.getErrorMessage(), null);
                            }
                            else if(signup.getStatusCode() == 000)
                            {
                                Utils.clearFragmentFromBackStack(getActivity(), new SignUpFragment().getClass().getName());
                                /*if(!Utils.isUserRegisteredToDownloadJson(getActivity()))
                                {
                                    ((MainActivity)getActivity()).resumeNetworkAndCallbacks(meshRootClass.getMeshName(), meshRootClass.getMeshUUID(), false);
                                }*/

                                Utils.onGetNewProvisioner(getActivity());
                                ((MainActivity)getActivity()).openDrawer();
                                Utils.DEBUG("SignUp Data : " + Utils.getLoginData(getActivity()));
                            }
                            else
                            {
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, signup.getErrorMessage(), null);
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

    private void updateJsonData() {

        try {
            meshRootClass = ParseManager.getInstance().fromJSON(
                    new JSONObject(Utils.getBLEMeshDataFromLocal(getActivity())), MeshRootClass.class);
            Utils.DEBUG(">> Json Data : " + Utils.getBLEMeshDataFromLocal(getActivity()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}