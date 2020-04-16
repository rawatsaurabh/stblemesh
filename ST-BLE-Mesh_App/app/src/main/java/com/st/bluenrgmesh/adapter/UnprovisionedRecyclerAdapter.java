/**
 * *****************************************************************************
 *
 * @file UnprovisionedRecylerAdapter.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.models.newmeshdata.Node;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Handler;

public class UnprovisionedRecyclerAdapter extends RecyclerView.Adapter<UnprovisionedRecyclerAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Nodes> mUnprovisionedData;
    private IRecyclerViewHolderClicks listener;

    public UnprovisionedRecyclerAdapter(Context context, ArrayList<Nodes> mUnprovisionedData, String rssiData, IRecyclerViewHolderClicks l) {

        this.context = context;
        this.mUnprovisionedData = mUnprovisionedData;
        this.listener = l;
    }


    public interface IRecyclerViewHolderClicks {
        //public void onClickRecyclerItem(View v, int position, Map<String, Object> map);
        public void onClickRecyclerItemNode(View v, int position, Nodes node);
    }


    @Override
    public UnprovisionedRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row_unprovisioned, parent, false);

        UnprovisionedRecyclerAdapter.ViewHolder vh = new UnprovisionedRecyclerAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final UnprovisionedRecyclerAdapter.ViewHolder holder, final int position) {

        try {
            if(!mUnprovisionedData.get(position).getUUID().equalsIgnoreCase("")) {

                //holder.cdDevices.setTag(mUnprovisionedData.get(position).getAddress());
                holder.txtUUID.setText(Utils.insertDashUUID(mUnprovisionedData.get(position).getUUID().toString()));
                holder.textViewSubtitle.setText(mUnprovisionedData.get(position).getAddress());
                holder.txtRssi.setText(mUnprovisionedData.get(position).getRssi());
                holder.txtName.setText(mUnprovisionedData.get(position).getName());
                int abs = Math.abs(Integer.parseInt(mUnprovisionedData.get(position).getRssi()));
                int posRange = 0;
                if(abs < 120)
                {
                    posRange = 120 - abs;
                }
                //holder.speedometer.speedTo(posRange);

                holder.textViewSubtitle.setTag(mUnprovisionedData.get(position).getAddress());
                holder.textViewSubtitle.setTextColor(context.getResources().getColor(R.color.ST_primary_blue));
                holder.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //listener.onClickRecyclerItem(v, position, map);
                /*Nodes node = new Nodes(0);
                node.setUUID(holder.txtUUID.getText().toString() + "");
                node.setAddress(device.getAddress().toString());
                node.setRssi(String.valueOf(device.getmRssi()));*/
                        try {
                            listener.onClickRecyclerItemNode(v, position, mUnprovisionedData.get(position));
                        } catch (Exception e) {
                            Utils.DEBUG("mUnprovisionedData size : " + mUnprovisionedData.size());
                            Utils.DEBUG("position : " + position);
                        }

                    }
                });

                if (!mUnprovisionedData.get(position).isChecked()) {
                    holder.txtRssi.setTextColor(context.getResources().getColor(R.color.st_black));
                } else {
                    holder.txtRssi.setTextColor(context.getResources().getColor(R.color.color_gray_light1));
                }

               /* if (context.getResources().getBoolean(R.bool.bool_showMacAddress)) {
                    holder.textViewSubtitle.setVisibility(View.VISIBLE);
                }*/

                if (context.getResources().getBoolean(R.bool.bool_showMacAddress)) {
                    holder.txtUUID.setVisibility(View.GONE);
                    holder.textViewSubtitle.setVisibility(View.VISIBLE);
                } else {
                    holder.txtUUID.setVisibility(View.VISIBLE);
                    holder.textViewSubtitle.setVisibility(View.GONE);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(final UnprovisionedRecyclerAdapter.ViewHolder holder, final int position, List<Object> payloads) {

        if(!payloads.isEmpty()) {
            if (payloads.get(0) instanceof String) {
                holder.txtRssi.setText(String.valueOf(payloads.get(0)));
            }
        }else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return mUnprovisionedData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageButtonAdd;
        private final TextView textViewTitle;
        private final TextView textViewSubtitle;
        private final TextView txtUUID;
        private final TextView txtRssi;
        private final TextView txtName;
        //private final CardView cdDevices;

        public ViewHolder(View itemView)
        {
            super(itemView);

            imageButtonAdd = (ImageView) itemView.findViewById(R.id.imageButtonAdd);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewSubtitle = (TextView) itemView.findViewById(R.id.textViewSubtitle);
            txtRssi = (TextView) itemView.findViewById(R.id.txtRssi);
            txtUUID = (TextView) itemView.findViewById(R.id.txtUUID);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            //cdDevices = (CardView) itemView.findViewById(R.id.cdDevices);
        }
    }
}
