/**
 * *****************************************************************************
 *
 * @file JoinNetworkRecyclerAdapter.java
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

package com.st.bluenrgmesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;

import java.util.ArrayList;


public class JoinNetworkRecyclerAdapter extends RecyclerView.Adapter<JoinNetworkRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> networkList;
    private ArrayList<String> netName;
    private IRecyclerViewHolderClicks listener;

    public JoinNetworkRecyclerAdapter(Context context, ArrayList<String> networksList, ArrayList<String> netName, IRecyclerViewHolderClicks l) {
        this.context = context;
        this.networkList = networksList;
        this.netName = netName;
        this.listener = l;
    }

    /**
     * The interface Recycler view holder clicks.
     */
    public static interface IRecyclerViewHolderClicks {

        public void onClickRecyclerItem(View v, int position, String strSelected);
    }

    @Override
    public JoinNetworkRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_join_network, parent, false);
        JoinNetworkRecyclerAdapter.ViewHolder vh = new JoinNetworkRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(JoinNetworkRecyclerAdapter.ViewHolder holder, final int position) {

        holder.txtNetworkName.setText(netName.get(position).toString());
        holder.txtNetworkUUID.setText(networkList.get(position).toString());
        holder.layNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRecyclerItem(v,position,networkList.get(position).toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return networkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNetworkName;
        private TextView txtNetworkUUID;
        private LinearLayout layNetwork;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNetworkName = (TextView) itemView.findViewById(R.id.txtNetworkName);
            txtNetworkUUID = (TextView) itemView.findViewById(R.id.txtNetworkUUID);
            layNetwork = (LinearLayout) itemView.findViewById(R.id.layNetwork);
        }
    }
}
