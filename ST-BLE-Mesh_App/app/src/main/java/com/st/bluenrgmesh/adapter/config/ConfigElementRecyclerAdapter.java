/**
 ******************************************************************************
 * @file    ConfigElementRecyclerAdapter.java
 * @author  BLE Mesh Team
 * @version V1.11.000
 * @date    20-October-2019
 * @brief   User Application file
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; COPYRIGHT(c) 2017 STMicroelectronics</center></h2>
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
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
 *
 * BlueNRG-Mesh is based on Motorola’s Mesh Over Bluetooth Low Energy (MoBLE)
 * technology. STMicroelectronics has done suitable updates in the firmware
 * and Android Mesh layers suitably.
 *
 ******************************************************************************
 */
package com.st.bluenrgmesh.adapter.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.models.meshdata.Nodes;
import com.st.bluenrgmesh.view.fragments.setting.configuration.ConfigurationFragment;

public class ConfigElementRecyclerAdapter extends RecyclerView.Adapter<ConfigElementRecyclerAdapter.ViewHolder> {

    private Context context;
    private Nodes node;
    private IRecyclerViewHolderClicks listener;

    public ConfigElementRecyclerAdapter(Context context, Nodes node, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.node = node;
        this.listener = iRecyclerViewHolderClicks;
    }

    public interface IRecyclerViewHolderClicks {

        void notifyAdapter(int position);

    }


    @Override
    public ConfigElementRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_config_element, parent, false);
        ConfigElementRecyclerAdapter.ViewHolder vh = new ConfigElementRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ConfigElementRecyclerAdapter.ViewHolder holder, final int position) {

        holder.txtElement.setText(node.getElements().get(position).getElementName());
        if(node.getElements().get(position).isConfigured)
        {
            holder.txtConfig.setText("Configured");
            holder.txtConfig.setTextColor(context.getResources().getColor(R.color.tv));
            holder.txtElement.setTextColor(context.getResources().getColor(R.color.tv));
        }
        holder.lytconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listener.notifyAdapter(position);
                /*if(!node.getElements().get(position).isConfigured)
                {
                    Utils.moveToFragment((MainActivity)context, new ConfigurationFragment(), node, 0);
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return node.getElements().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout lytconfig;
        private TextView txtElement;
        private TextView txtConfig;

        public ViewHolder(View itemView) {
            super(itemView);

            lytconfig = (RelativeLayout) itemView.findViewById(R.id.lytconfig);
            txtElement = (TextView) itemView.findViewById(R.id.txtElement);
            txtConfig = (TextView) itemView.findViewById(R.id.txtConfig);

        }
    }
}
