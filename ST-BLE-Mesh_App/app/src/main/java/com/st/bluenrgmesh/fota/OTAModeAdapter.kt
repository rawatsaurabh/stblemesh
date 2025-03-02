/**
******************************************************************************
* @file    OTAModeAdapter.kt
* @author  BLE Mesh Team
* @version V1.11.000
* @date    20-October-2019
* @brief   Commands Application file
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

package com.st.bluenrgmesh.fota

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.st.bluenrgmesh.R
import com.st.bluenrgmesh.models.meshdata.Nodes
import kotlinx.android.synthetic.main.adpter_otamode.view.*

class OTAModeAdapter(private val node_list : List<Nodes>,
                     private val context: Context? ,
                     private val callbackToFragment : CallbackToFragment?)
                     : RecyclerView.Adapter<OTAModeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adpter_otamode, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nodename_textview?.text = node_list[position].name
        holder.nodesUUID_textview?.text = node_list[position].uuid
        holder.upgrade_button.setOnClickListener{


        callbackToFragment!!.onClickCheckUpgrade(position)
        }
    }

    override fun getItemCount(): Int= node_list.size



class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val nodename_textview = view.nodename_textview
    val nodesUUID_textview  = view.nodeuuid_textview
    val upgrade_button = view.upgrade_button
}
}