/**
 * *****************************************************************************
 *
 * @file LoggerListAdapter.java
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st.bluenrgmesh.R;

import java.util.ArrayList;

public class LoggersListAdapter extends RecyclerView.Adapter<LoggersListAdapter.ViewHolder> {


    private ArrayList<LoggerPojo> loggerlist;
    private Context context;

    public LoggersListAdapter(Context context, ArrayList<LoggerPojo>loggerlist) {

        this.context = context;
        this.loggerlist = loggerlist;

    }

    @Override
    public LoggersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loggerlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LoggersListAdapter.ViewHolder holder, final int position) {
        holder.logsTV.setText(loggerlist.get(position).getLogs());
        holder.dateTV.setText(loggerlist.get(position).getDate());

        if (loggerlist.get(position).getType().equalsIgnoreCase(LoggerConstants.TYPE_SEND)) {
            holder.typeTV.setText(" | --->(Tx) | ");

        } else {
            holder.typeTV.setText(" | <---(Rx) | ");

        }

    }




    @Override
    public int getItemCount() {

        return loggerlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView logsTV,dateTV,typeTV;

        public ViewHolder(View itemView) {
            super(itemView);

            this.logsTV = (TextView) itemView.findViewById(R.id.logsTV);
            this.dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            this.typeTV = (TextView) itemView.findViewById(R.id.typeTV);

        }
    }


}
