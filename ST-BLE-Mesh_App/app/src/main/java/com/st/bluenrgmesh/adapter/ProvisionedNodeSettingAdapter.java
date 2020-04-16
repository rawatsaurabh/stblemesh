/**
 * *****************************************************************************
 *
 * @file ProvisionedNodeSettingAdapter.java
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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;

import java.util.List;


public class ProvisionedNodeSettingAdapter extends RecyclerView.Adapter<ProvisionedNodeSettingAdapter.ViewHolder> {

    private final Context context;
    private final List<String> publicationListAddrss;
    private final String type;
    private final IRecyclerViewHolderClicks listener;
    private final List<String> publicationListName;
    private final String peerAddr;

    public static interface IRecyclerViewHolderClicks {
        public void onClickRecyclerItem(CompoundButton v, int position, String item, String mAutoAddress, boolean isSelected);
    }


    public ProvisionedNodeSettingAdapter(Context context, List<String> publicationListAddrss, List<String> publicationListName, String peerAddr, String type, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.publicationListAddrss = publicationListAddrss;
        this.publicationListName = publicationListName;
        this.peerAddr = peerAddr;
        this.type = type;
        this.listener = iRecyclerViewHolderClicks;

    }

    @Override
    public ProvisionedNodeSettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);

        ProvisionedNodeSettingAdapter.ViewHolder vh = new ProvisionedNodeSettingAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ProvisionedNodeSettingAdapter.ViewHolder holder, final int position) {

        if (type.equals("Element Settings")) {
            if (publicationListName != null) {
                for (int i = 0; i < publicationListAddrss.size(); i++) {
                    if (!publicationListAddrss.get(i).equals("")) {
                        if (publicationListAddrss.get(i).equals(peerAddr)) {
                            holder.radioButton.setChecked(position == i);
                        }
                    } else {
                    }
                }

                holder.txtView.setVisibility(View.GONE);
                holder.radioButton.setVisibility(View.VISIBLE);
                if (publicationListName.get(position).equals(context.getResources().getString(R.string.str_default_group_label))) {
                    holder.radioButton.setText(publicationListName.get(position).toString());
                } else {
                    holder.radioButton.setText("Element" + " : " + publicationListName.get(position).toString());
                }

            } else {
                holder.txtView.setVisibility(View.VISIBLE);
                holder.radioButton.setVisibility(View.GONE);
                holder.txtView.setText(publicationListAddrss.get(position).toString() /*+ publicationListName.get(position).toString()*/);
            }
        } else if (type.equals("Device Settings")) {
            if (publicationListName != null) {
                //publication
                boolean showLightsGrp = true;
                for (int i = 0; i < publicationListAddrss.size(); i++) {
                    if (!publicationListAddrss.get(i).equals("")) {
                        if (publicationListAddrss.get(i).equals(peerAddr)) {

                            holder.radioButton.setChecked(position == i);
                        }
                    } else {
                    }
                }

                holder.txtView.setVisibility(View.GONE);
                holder.radioButton.setVisibility(View.VISIBLE);
                holder.radioButton.setText(publicationListName.get(position).toString());
            } else {
                //subscription
                holder.txtView.setVisibility(View.VISIBLE);
                holder.radioButton.setVisibility(View.GONE);
                holder.txtView.setText(publicationListAddrss.get(position).toString() /*+ publicationListAddrss.get(position).toString()*/);
            }
        } else if (type.equals("isLightsGroupSetting")) {

            holder.radioButton.setChecked(true);

        } else if (type.equals("isAddNewGroup")) {

            holder.radioButton.setChecked(false);

        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("isLightsGroupSetting")) {
                    RadioButton viewById = (RadioButton) v.findViewById(R.id.radioButton);

                    if (viewById.isChecked()) {
                        listener.onClickRecyclerItem(holder.radioButton, position, publicationListAddrss.get(position), "", true);
                    } else {

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return publicationListAddrss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioButton;
        private final TextView txtView;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            txtView = (TextView) itemView.findViewById(R.id.txtView);

        }
    }
}
