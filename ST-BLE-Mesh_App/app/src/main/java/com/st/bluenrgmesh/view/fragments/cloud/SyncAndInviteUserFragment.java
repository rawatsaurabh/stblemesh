/**
 * *****************************************************************************
 *
 * @file SyncAndInviteUserFragment.java
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

package com.st.bluenrgmesh.view.fragments.cloud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.st.bluenrgmesh.models.clouddata.CreateInvitationData;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.setting.ExchangeConfigFragment;

import org.json.JSONObject;


public class SyncAndInviteUserFragment extends BaseFragment {
    private View view;
    private AppDialogLoader loader;
    private EditText mesh_uuid_edit;
    private Button butMeshInvitation;
    private MeshRootClass meshRootClass;
    private TextView txtInviteCode;
    private Button butSync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.invitation_page, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new ExchangeConfigFragment().getClass().getName());
        updateJsonData();

        initUi();

        return view;
    }

    private void updateJsonData() {

        try {
            meshRootClass = ParseManager.getInstance().fromJSON(
                    new JSONObject(Utils.getBLEMeshDataFromLocal(getActivity())), MeshRootClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUi(){
        mesh_uuid_edit = (EditText) view.findViewById(R.id.mesh_uuid_edit);
        //Calling API to Create Invitation
        try {
        if(meshRootClass.getMeshUUID().length() > 0)
        {
            mesh_uuid_edit.setText(meshRootClass.getMeshUUID());
        }
        }catch (NullPointerException e){}
        butMeshInvitation = (Button) view.findViewById(R.id.butMeshInvitation);
        butMeshInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInvitationApi(meshRootClass.getMeshUUID(), Utils.getUserLoginKey(getActivity()));
            }
        });
        txtInviteCode = (TextView) view.findViewById(R.id.txtInviteCode);
        butSync = (Button) view.findViewById(R.id.butSync);
        butSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp_SyncCloudData(getActivity());
            }
        });

    }

    public void showPopUp_SyncCloudData(final Context context) {

        if (context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog_download_json);
        dialog.show();
        TextView txtInvitationCode = (TextView) dialog.findViewById(R.id.txtInvitationCode);
        Button butOk = (Button) dialog.findViewById(R.id.butOk);
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //((MainActivity) context).callDownloadAPI(context, null);
                Utils.showToast(getActivity(), "Syncing...");
                String json = Utils.filterJsonObject(context);
                String provUUID = Utils.getProvisionerUUID(context);
                callUploadApi(Utils.getUserLoginKey(context), meshRootClass.getMeshUUID(), json, provUUID);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });
    }

    private void callInvitationApi(final String meshUUID, String userkey) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_CreateInvitation);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", userkey);
            requestObject.put("MeshUUID", meshUUID);

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
                        Utils.DEBUG("API_CreateInvitation onResponse() called : " + response.toString());

                        try {
                            CreateInvitationData createInvitationData = ParseManager.getInstance().fromJSON(response, CreateInvitationData.class);
                            if(createInvitationData.getStatusCode() == 101)
                            {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, createInvitationData.getErrorMessage(), null);
                            }
                            else if(createInvitationData.getStatusCode() == 110)
                            {
                                //error show pop
                                //Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, createInvitationData.getErrorMessage(), null);
                                ((MainActivity)getActivity()).onBackPressed();
                                Utils.showToast(getActivity(), createInvitationData.getErrorMessage());
                                Utils.showPopUpForSeesionExpire(getActivity());
                            }
                            else
                            {
                                //success
                                updateUI(createInvitationData);
                                Utils.showPopForUserInvitation(getActivity(), createInvitationData.getResponseMessage());
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

    private void updateUI(CreateInvitationData createInvitationData) {

        txtInviteCode.setText(createInvitationData.getResponseMessage());
    }

    private void callUploadApi(final String userkey, final String meshuuid, final String json, String provuuid) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_UploadNetwork);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", userkey);
            requestObject.put("MeshUUID", meshuuid);
            requestObject.put("JsonString", json);
            requestObject.put("ProvUUID", provuuid);

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
                        Utils.DEBUG("Upload onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if(cloudResponseData.getStatusCode() == 0)
                            {
                                Utils.setCloudData(getActivity(), "");
                                Utils.setCloudData(getActivity(),  json);
                                //Utils.showToast(getActivity(), getString(R.string.str_uploaded_success_label));
                                loader.hide();
                                callDownloadAPI(meshuuid);
                            }
                            else if(cloudResponseData.getStatusCode() == 120)
                            {
                                Utils.showToast(getActivity(), cloudResponseData.getErrorMessage());
                                if(cloudResponseData.getErrorMessage().equalsIgnoreCase("You are not part of this Network"))
                                {
                                    Utils.setUserRegisteredToDownloadJson(getActivity(), "false");
                                    ((MainActivity)getActivity()).onBackPressed();
                                }
                            }
                            else
                            {
                                Utils.showToast(getActivity(), cloudResponseData.getResponseMessage() + " " + cloudResponseData.getErrorMessage());
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

    public void callDownloadAPI(String meshUUID) {

        updateJsonData();

        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE) + getString(R.string.URL_MED) + getString(R.string.API_DownloadNetwork);

        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", Utils.getUserLoginKey(getActivity()));
            requestObject.put("MeshUUID", meshUUID);

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
                        Utils.DEBUG("API_DownloadNetwork onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if (cloudResponseData.getStatusCode() == 110 || cloudResponseData.getStatusCode() == 201
                                    || cloudResponseData.getStatusCode() == 202) {
                                Utils.showToast(getActivity(), cloudResponseData.getErrorMessage());
                            } else if(cloudResponseData.getStatusCode() == 000){
                                Utils.showToast(getActivity(), "Successfully Synced...");
                                Utils.showToast(getActivity(), getString(R.string.str_cloud_success_label));
                                Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                String data = cloudResponseData.getResponseMessage();
                                data = data.replace("/", "");

                                Utils.setCloudData(getActivity(), data);
                                loader.hide();
                                Utils.showFileChooser(getActivity(), true);
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
