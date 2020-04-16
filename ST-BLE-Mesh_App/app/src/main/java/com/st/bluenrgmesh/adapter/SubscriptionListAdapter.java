/**
 * *****************************************************************************
 *
 * @file SubscriptionListAdapter.java
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

import com.msi.moble.mobleAddress;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.meshdata.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, Object>> configuredNodes;
    private ArrayList<Element> eleSubList;
    private IRecyclerViewHolderClicks listener;
    private mobleAddress addr;
    private String type;
    private boolean isCheckedPreviously;

    public static interface IRecyclerViewHolderClicks {
        /**
         * On click recycler item.
         *
         * @param v          the v
         * @param position   the position
         * @param item       the item
         * @param isSelected the str selected
         */
        public void onClickRecyclerItem(CompoundButton v, int position, Map<String, Object> item, String mAutoAddress, boolean isSelected);
    }

    public SubscriptionListAdapter(Context context, ArrayList<Element> eleSubList, IRecyclerViewHolderClicks iRecyclerViewHolderClicks) {

        this.context = context;
        this.eleSubList = eleSubList;
        this.addr = addr;
        this.type = type;
        this.listener = iRecyclerViewHolderClicks;

    }

    @Override
    public SubscriptionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_row, parent, false);

        SubscriptionListAdapter.ViewHolder vh = new SubscriptionListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final SubscriptionListAdapter.ViewHolder holder, final int position) {

        final Map<String, Object> map = configuredNodes.get(position);

        final String mAutoAddress = (String) map.get("address");

        holder.radioButton.setText("" + map.get("title"));


        if ((Boolean) map.get("isChecked")) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }


        if (type.equals("isLightsGroupSetting")) {
            holder.radioButton.setChecked(true);
        }

        holder.txtView.setVisibility(View.GONE);

        if (holder.radioButton.isChecked()) {
            isCheckedPreviously = true;
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("isLightsGroupSetting")) {

                    RadioButton viewById = (RadioButton) v.findViewById(R.id.radioButton);
                    if (viewById.isChecked()) {
                        if (!(Boolean) map.get("isChecked")) {
                            map.put("isChecked", true);
                            viewById.setChecked(true);
                            listener.onClickRecyclerItem(holder.radioButton, position, map, mAutoAddress, true);
                        } else {
                            map.put("isChecked", false);
                            viewById.setChecked(false);
                            listener.onClickRecyclerItem(holder.radioButton, position, map, mAutoAddress, false);
                        }
                    } else if (!viewById.isChecked()) {
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return configuredNodes.size();
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
