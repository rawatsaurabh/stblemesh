/**
 * *****************************************************************************
 *
 * @file LoggerActivity.java
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
package com.st.bluenrgmesh.logger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.utils.AppDialogLoader;
import com.st.bluenrgmesh.view.fragments.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoggerFragment extends BaseFragment implements RepositoryObserver {

    private RecyclerView loggerRV;
    private Subject mUserDataRepository;
    private LoggersListAdapter mAdapter;
    private ArrayList<LoggerPojo> loggerlist = new ArrayList<>();
    private View view;
    private AppDialogLoader loader;
    private Context context;



    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_logger, container, false);

        loader = AppDialogLoader.getLoader(getActivity());
        Utils.updateActionBarForFeatures(getActivity(), new LoggerFragment().getClass().getName());
        initUI();
        registerObersever();
        updateLoggerList("", "");


        return view;
    }



    private void initUI() {
        loggerRV = (RecyclerView) view.findViewById(R.id.loggerRV);
        mAdapter = new LoggersListAdapter(getActivity(), loggerlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        loggerRV.setLayoutManager(mLayoutManager);
        loggerRV.setItemAnimator(new DefaultItemAnimator());
        loggerRV.setAdapter(mAdapter);
    }


    void registerObersever() {
        mUserDataRepository = UserDataRepository.getInstance();
        mUserDataRepository.registerObserver(this);
    }

    @Override
    public void onUserDataChanged(final String logs, final String type) {

        ((MainActivity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                updateLoggerList(logs, type);

            }
        });
    }


    @Override
    public Context getContext() {
        if(context==null){
            context=getActivity();
        }
        return context;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }*/


    private void updateLoggerList(final String logs, final String type) {
        try {


            if (loggerlist != null && loggerlist.size() > 0) {
                loggerlist.clear();
            }
            String json_str = JsonUtil.readFromFile(getActivity());
            if (json_str != null && !json_str.equalsIgnoreCase("")) {
                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(json_str);
                    JSONArray uniObject = mainObject.getJSONArray("logger");
                    for (int i = 0; i < uniObject.length(); i++) {
                        LoggerPojo loggerPojo = new LoggerPojo();
                        loggerPojo.setLogs(uniObject.getJSONObject(i).getString("logs"));
                        loggerPojo.setType(uniObject.getJSONObject(i).getString("type"));
                        loggerPojo.setDate(uniObject.getJSONObject(i).getString("date"));
                        loggerlist.add(loggerPojo);
                    }


                    mAdapter.notifyDataSetChanged();
                    loggerRV.scrollToPosition(loggerlist.size() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!logs.equalsIgnoreCase("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                String currentDateandTime = sdf.format(new Date());
                LoggerPojo loggerPojo = new LoggerPojo();
                loggerPojo.setLogs(logs);
                loggerPojo.setType(type);
                loggerPojo.setDate(currentDateandTime);
                loggerlist.add(loggerPojo);
                mAdapter.notifyDataSetChanged();
                loggerRV.scrollToPosition(loggerlist.size() - 1);
            } else {
                Utils.showToast(getActivity(),"No Logs Found !!");
            }
        } catch (Exception e) {

        }

    }
}
