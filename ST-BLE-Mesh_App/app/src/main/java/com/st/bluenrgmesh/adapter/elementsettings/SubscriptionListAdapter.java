/**
 ******************************************************************************
 * @file    SubscriptionListAdapter.java
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
package com.st.bluenrgmesh.adapter.elementsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.adapter.ProvisionedNodeSettingAdapter;
import com.st.bluenrgmesh.models.meshdata.Group;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {

    private final Context context;
    private final SubscriptionListAdapter.IRecyclerViewHolderClicks listener;
    private final ArrayList<Group> subscriptionList;

    public static interface IRecyclerViewHolderClicks {
        public void onClickRecyclerItem(View v, int position, Group grp);
    }


    public SubscriptionListAdapter(Context context, ArrayList<Group> subscriptionList, SubscriptionListAdapter.IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.subscriptionList = subscriptionList;
        this.listener = iRecyclerViewHolderClicks;
        //setHasStableIds(true);

    }


    @Override
    public SubscriptionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);

        SubscriptionListAdapter.ViewHolder vh = new SubscriptionListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final SubscriptionListAdapter.ViewHolder holder, final int position) {

        holder.checkbox.setChecked(subscriptionList.get(position).isChecked());
        holder.txtView.setText(subscriptionList.get(position).getName() + " : " + subscriptionList.get(position).getAddress());

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickRecyclerItem(v, position, subscriptionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkbox;
        private final TextView txtView;

        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            txtView = (TextView) itemView.findViewById(R.id.txtView);

        }
    }
}
