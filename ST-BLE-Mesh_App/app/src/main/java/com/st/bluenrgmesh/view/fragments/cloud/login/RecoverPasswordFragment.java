/**
 * *****************************************************************************
 *
 * @file RecoverPasswordFragment.java
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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.clouddata.QuestionAnswerData;
import com.st.bluenrgmesh.models.clouddata.TempPasswordData;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONObject;


public class RecoverPasswordFragment extends BaseFragment {

    private View view;
    private AppDialogLoader loader;
    private QuestionAnswerData qa;
    private TextView txtQuest1;
    private TextView txtQuest2;
    private TextView txtQuest3;
    private EditText edtAns1;
    private EditText edtAns2;
    private EditText edtAns3;
    private Button butSubmit;
    private String userName;
    private TempPasswordData tempPasswordData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recoverpassword, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new RecoverPasswordFragment().getClass().getName());
        initUi();

        return view;
    }

    private void initUi() {

        showPopForUserName(getActivity());
        txtQuest1 = (TextView) view.findViewById(R.id.txtQuest1);
        txtQuest2 = (TextView) view.findViewById(R.id.txtQuest2);
        txtQuest3 = (TextView) view.findViewById(R.id.txtQuest3);
        edtAns1 = (EditText) view.findViewById(R.id.edtAns1);
        edtAns2 = (EditText) view.findViewById(R.id.edtAns2);
        edtAns3 = (EditText) view.findViewById(R.id.edtAns3);
        butSubmit = (Button) view.findViewById(R.id.butSubmit);
        butSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();
                String username = userName;
                String edtAns11 = edtAns1.getText().toString();
                String edtAns22 = edtAns2.getText().toString();
                String edtAns33 = edtAns3.getText().toString();
                if(username != null)
                {
                    call_submitSecurityAnswer_API(username, edtAns11, edtAns22, edtAns33);
                }
            }
        });

    }

    public void showPopForUserName(final Context context) {

        if (context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_username);
        dialog.setCancelable(false);
        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                //dialog.dismiss();
            }
        });

        final EditText edtUsername = (EditText) dialog.findViewById(R.id.edtUsername);
        Button but = (Button) dialog.findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = edtUsername.getText().toString();
                call_retrieveSecurityQuestions_API(dialog, userName);
            }
        });
    }

    public void call_retrieveSecurityQuestions_API(final Dialog dialog, String s) {

        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_retrieveSecurityQuestions);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userName", s);

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
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if(cloudResponseData.getStatusCode() == 101)
                            {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, cloudResponseData.getErrorMessage(), null);
                            }
                            else if(cloudResponseData.getStatusCode() == 0) {
                                //success
                                //check if comes from setup n/w
                                //save data to local
                                dialog.dismiss();
                                String s1 = cloudResponseData.getResponseMessage().toString();
                                qa = ParseManager.getInstance().fromJSON(new JSONObject(s1), QuestionAnswerData.class);
                                if(qa != null)
                                {
                                    updateQuestionAnswerUi(qa);
                                }

                            }

                        }catch (Exception e){}
                        loader.hide();
                        loader.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ERROR("Error: " + error);
                //Utils.showToast(getActivity(), getString(R.string.string_common_error_message));
                loader.hide();
                loader.dismiss();
            }
        }
        );
        // Adding request to request queue
        UserApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void call_submitSecurityAnswer_API(String username, String edtAns11, String edtAns22, String edtAns33) {

        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_submitSecurityAnswer);

        //loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userName", username);
            requestObject.put("firstPassword", edtAns11);
            requestObject.put("secondPassword", edtAns22);
            requestObject.put("thirdPassword", edtAns33);

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
                        Utils.DEBUG("QA onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if(cloudResponseData.getStatusCode() == 101 || cloudResponseData.getStatusCode() == 002)
                            {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, cloudResponseData.getErrorMessage(), null);
                            }
                            else if(cloudResponseData.getStatusCode() == 0) {
                                //success
                                String s1 = cloudResponseData.getResponseMessage().toString();
                                tempPasswordData = ParseManager.getInstance().fromJSON(new JSONObject(s1), TempPasswordData.class);
                                if(tempPasswordData != null)
                                {
                                    loader.dismiss();
                                    //((MainActivity)getActivity()).onBackPressed();
                                    Utils.moveToFragment(getActivity(), new ChangePasswordFragment(), s1, 0);
                                }

                            }

                        }catch (Exception e){}
                        loader.dismiss();
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

    private void updateQuestionAnswerUi(QuestionAnswerData qa) {

        if(qa.getFirstQuestion().equals("") || qa.getFirstQuestion() == null )
        {
            txtQuest1.setVisibility(View.GONE);
            edtAns1.setVisibility(View.GONE);
        }
        else
        {
            txtQuest1.setText("Ques 1 : " + (qa.getFirstQuestion()));
        }

        if(qa.getSecondQuestion().equals("") || qa.getSecondQuestion() == null )
        {
            txtQuest2.setVisibility(View.GONE);
            edtAns2.setVisibility(View.GONE);
        }
        else
        {
            txtQuest2.setText("Ques 1 : " + (qa.getSecondQuestion()));
        }

        if(qa.getThirdQuestion().equals("") || qa.getThirdQuestion() == null )
        {
            txtQuest3.setVisibility(View.GONE);
            edtAns3.setVisibility(View.GONE);
        }
        else
        {
            txtQuest3.setText("Ques 1 : " + (qa.getThirdQuestion()));
        }
    }
}
