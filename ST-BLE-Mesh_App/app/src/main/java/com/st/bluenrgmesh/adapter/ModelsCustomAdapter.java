/**
 * *****************************************************************************
 *
 * @file ModelsCustomAdapter.java
 * @author BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
 * * @brief User Application file
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
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Model;

import java.util.ArrayList;


public class ModelsCustomAdapter extends RecyclerView.Adapter<ModelsCustomAdapter.MyViewHolder>{

    private final ArrayList<Model> models_list;
    private  String which_model;
    private LayoutInflater inflater;
    private Context context;

    public ModelsCustomAdapter(Context context, ArrayList<Model> models_list, String which_model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.models_list=models_list;
        this.which_model=which_model;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_adapter_models, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str = models_list.get(position).getModelName().replace("_", " ");
        //String modelName = str.replace("SERVER"," ");
        holder.modelTV.setText(str);
        holder.modelTV_id.setText("0x"+models_list.get(position).getModelId());
    }

    @Override
    public int getItemCount() {
        return models_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView modelTV;
        TextView modelTV_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            modelTV = (TextView)itemView.findViewById(R.id.modelTV);
            modelTV_id = (TextView)itemView.findViewById(R.id.modelTV_id);


        }
    }
}