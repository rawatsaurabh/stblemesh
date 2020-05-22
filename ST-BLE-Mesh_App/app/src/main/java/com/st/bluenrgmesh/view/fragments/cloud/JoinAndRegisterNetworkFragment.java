/**
 * *****************************************************************************
 *
 * @file JoinAndRegisterNetworkFragment.java
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

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.JoinNetworkRecyclerAdapter;
import com.st.bluenrgmesh.adapter.NpaGridLayoutManager;
import com.st.bluenrgmesh.models.clouddata.CloudResponseData;
import com.st.bluenrgmesh.models.clouddata.CreateInvitationData;
import com.st.bluenrgmesh.models.clouddata.LoginData;
import com.st.bluenrgmesh.models.clouddata.NetworkListData;
import com.st.bluenrgmesh.models.clouddata.TempPasswordData;
import com.st.bluenrgmesh.models.meshdata.MeshRootClass;
import com.st.bluenrgmesh.parser.ParseManager;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.utils.MyJsonObjectRequest;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;
import com.st.bluenrgmesh.view.fragments.cloud.login.LoginDetailsFragment;
import com.st.bluenrgmesh.view.fragments.setting.ExchangeConfigFragment;

import org.json.JSONException;
import org.json.JSONObject;


public class JoinAndRegisterNetworkFragment extends BaseFragment {
    private View view;
    private AppDialogLoader loader;
    private Button buttonJoin;
    private EditText network_key_edit;
    private MeshRootClass meshRootClass;
    private RecyclerView recyclerJoinNet;
    private JoinNetworkRecyclerAdapter adapter;
    private Button butRegisterNetwork;
    private String data;
    private NetworkListData networkListData;
    private LoginData loginData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join_network, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        //Utils.updateActionBarForFeatures(getActivity(), new JoinNetworkFragment().getClass().getName());
        Utils.updateActionBarForFeatures(getActivity(), new ExchangeConfigFragment().getClass().getName());
        updateJsonData();
        initUi();

        return view;
    }

    private void updateJsonData() {

        try {
            meshRootClass = ParseManager.getInstance().fromJSON(
                    new JSONObject(Utils.getBLEMeshDataFromLocal(getActivity())), MeshRootClass.class);

            if(meshRootClass == null)
            {
                meshRootClass = new MeshRootClass();
                //meshRootClass.setMeshUUID("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUi(){

        Bundle bundle = this.getArguments();
        data = (String)bundle.getSerializable(getString(R.string.key_serializable));
        try {
            networkListData = ParseManager.getInstance().fromJSON(new JSONObject(data), NetworkListData.class);
        } catch (JSONException e) {
        }

        try {
            loginData = ParseManager.getInstance().fromJSON(new JSONObject(Utils.getLoginData(getActivity())), LoginData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        network_key_edit = (EditText) view.findViewById(R.id.network_key_edit);
        buttonJoin = (Button) view.findViewById(R.id.buttonJoin);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = network_key_edit.getText().toString();
                callJoinNetworkApi(Utils.getUserLoginKey(getActivity()), code);

            }
        });

        butRegisterNetwork = (Button) view.findViewById(R.id.butRegisterNetwork);
        butRegisterNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isUserLoggedIn(getActivity()))
                {
                    callCreateNetworkApi(Utils.getUserLoginKey(getActivity()), meshRootClass.getMeshUUID());
                }
            }
        });
        recyclerJoinNet = (RecyclerView) view.findViewById(R.id.recyclerJoinNet);
        NpaGridLayoutManager gridLayoutManager = new NpaGridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerJoinNet.setLayoutManager(gridLayoutManager);
        adapter = new JoinNetworkRecyclerAdapter(getActivity(), networkListData.getNetworksList(), networkListData.getNetName(), new JoinNetworkRecyclerAdapter.IRecyclerViewHolderClicks() {
            @Override
            public void onClickRecyclerItem(View v, int position, String meshUUID) {

                callDownloadAPI(meshUUID);

            }
        });
        recyclerJoinNet.setAdapter(adapter);

    }

    private void callJoinNetworkApi(String userKeyFromLogin, String codeInvitation) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_JoinNetwork);

        loader.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("iuserKey", userKeyFromLogin);
            requestObject.put("inCode", codeInvitation);

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
                        Utils.DEBUG("API Join_Network onResponse() called : " + response.toString());

                        try {
                            CreateInvitationData createInvitationData = ParseManager.getInstance().fromJSON(response, CreateInvitationData.class);
                            if(createInvitationData.getStatusCode() == 0)
                            {
                                //new Joiner : create new mesh data for new joiner
                                //success : set invited mesh uuid and other data as empty
                                MeshRootClass meshRootClass1 = new MeshRootClass();
                                meshRootClass1 = Utils.createEmptyDataForNewJoiner(meshRootClass1);
                                meshRootClass = meshRootClass1;
                                //meshRootClass.setMeshUUID(createInvitationData.getResponseMessage());
                                Utils.setBLEMeshDataToLocal(getActivity(), ParseManager.getInstance().toJSON(meshRootClass));
                                /*Utils.DEBUG("Mesh Data After Join : " + ParseManager.getInstance().toJSON(meshRootClass));
                                Utils.showToast(getActivity(), getString(R.string.str_network_joined_success_label));
                                ((MainActivity)getActivity()).onBackPressed();*/
                                String userKey = Utils.getUserLoginKey(getActivity());
                                String provName = loginData.getUserName().toString();
                                //String meshUUID = meshRootClass.getMeshUUID();
                                String meshUUID = createInvitationData.getResponseMessage();
                                String provUUID = Utils.getProvisionerUUID(getActivity());
                                loader.hide();
                                callRegProvApi(userKey,provName,meshUUID,provUUID);
                            }
                            else if(createInvitationData.getStatusCode() == 121 || createInvitationData.getStatusCode() == 210
                                    || createInvitationData.getStatusCode() == 110|| createInvitationData.getStatusCode() == 120)
                            {

                                Utils.showToast(getActivity(), createInvitationData.getErrorMessage());
                                ((MainActivity)getActivity()).onBackPressed();
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

    private void callCreateNetworkApi(final String userkey, final String meshUUID) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE) + getString(R.string.URL_MED) + getString(R.string.API_CreateNetwork);

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
                        Utils.DEBUG("CreateNetwork onResponse() called : " + response.toString());

                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if (cloudResponseData.getStatusCode() == 201 || cloudResponseData.getStatusCode() == 202
                                    || cloudResponseData.getStatusCode() == 011) {
                                //error show pop
                                Utils.showPopUp(getActivity(), new LoginDetailsFragment().getClass().getName(), true, false, cloudResponseData.getErrorMessage(), null);
                            } else if (cloudResponseData.getStatusCode() == 000 /*|| cloudResponseData.getStatusCode() == 11*/) {
                                //Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                Utils.showToast(getActivity(), getString(R.string.str_netwrk_success_label));
                                String json = Utils.filterJsonObject(getActivity());
                                String provUUID = Utils.getProvisionerUUID(getActivity());
                                loader.hide();
                                callUploadApi(userkey, meshUUID, json, provUUID);
                            } else if (cloudResponseData.getStatusCode() == 11) {
                                Utils.setUserRegisteredToDownloadJson(getActivity(), "false");
                                Utils.showToast(getActivity(),cloudResponseData.getErrorMessage());
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
                                //Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                //Utils.showToast(getActivity(), getString(R.string.str_uploaded_success_label));
                                loader.hide();
                                callDownloadAPI(meshuuid);
                            }
                            else
                            {
                                Utils.showToast(getActivity(), cloudResponseData.getResponseMessage());
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
                            if (cloudResponseData.getStatusCode() == 110 ||cloudResponseData.getStatusCode() == 303 || cloudResponseData.getStatusCode() == 201 || cloudResponseData.getStatusCode() == 202) {
                                Utils.showToast(getActivity(), cloudResponseData.getErrorMessage());
                            } else if(cloudResponseData.getStatusCode() == 000){
                                //Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                Utils.showToast(getActivity(), getString(R.string.str_cloud_success_label));
                                String data = cloudResponseData.getResponseMessage();
                                data = data.replace("/", "");
                                //data = data.replace("Node", "Nodes");
                                Utils.setCloudData(getActivity(), data);
                                loader.hide();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showFileChooser(getActivity(),true);
                                    }
                                }, 200);
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

    private void callRegProvApi(String userKeyFromLogin, String provName, String mesh , String ProvUUID) {
        String tag_json_obj = "json_obj_req";
        String url = getString(R.string.URL_BASE)  + getString(R.string.URL_MED) + getString(R.string.API_provisionerRegistration);

        final String MeshUUID  = mesh;

        loader.show();

        Utils.DEBUG("userKey : " + userKeyFromLogin + "\nprovName :"+ provName + "\nMeshUUID :"+ MeshUUID + "\nProvUUID :"+ ProvUUID);

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("userKey", userKeyFromLogin);
            requestObject.put("provName", provName);
            requestObject.put("MeshUUID", mesh);
            requestObject.put("ProvUUID", ProvUUID);

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
                        Utils.DEBUG("API Join_Network onResponse() called : " + response.toString());
                        try {
                            CloudResponseData cloudResponseData = ParseManager.getInstance().fromJSON(response, CloudResponseData.class);
                            if(cloudResponseData.getStatusCode() == 0)
                            {
                                Utils.setUserRegisteredToDownloadJson(getActivity(), "true");
                                Utils.showToast(getActivity(), getString(R.string.str_register_success_label));
                                loader.hide();
                                callDownloadAPI(MeshUUID);
                            }
                            else
                            {
                                Utils.showToast(getActivity(), cloudResponseData.getResponseMessage());
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
