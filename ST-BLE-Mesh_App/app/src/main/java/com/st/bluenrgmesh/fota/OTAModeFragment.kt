/**
******************************************************************************
* @file    OTAModeFragment.kt
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
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.st.bluenrgmesh.MainActivity
import com.st.bluenrgmesh.R
import com.st.bluenrgmesh.Utils
import com.st.bluenrgmesh.Utils.startOTAServiceMessage
import com.st.bluenrgmesh.datamap.Nucleo
import com.st.bluenrgmesh.models.meshdata.Nodes
import com.st.bluenrgmesh.utils.AppDialogLoader
import kotlinx.android.synthetic.main.ota_mode_fragment.*
import java.util.*
import kotlin.concurrent.schedule

class OTAModeFragment : Fragment(), CallbackToFragment {


    private var position: Int = 0
    private lateinit var node_list: List<Nodes>
    private var mycontext: Context? = null
    private var loader: AppDialogLoader? = null

    override fun onOTAReceivedCallback(status: String, from: String) {
        //loader?.hide()
        MainActivity.network.unadvise(Utils.otaserviceCallback)

        if (from.equals("OTA",true)) {
            if (status.equals("SUCCESS", false)) {

                onClickUpgrade(position)

            } else {
                Toast.makeText(mycontext, "Timeout Error Occurred!", Toast.LENGTH_LONG).show()
            }

        }

       /* else {
            if (status.equals("SUCCESS", false)) {
                startActivity(Intent(activity, FotaActivity::class.java))

            } else {
                Toast.makeText(mycontext, "Timeout Error Occurred!", Toast.LENGTH_LONG).show()
            }
        }*/
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mycontext = context
    }


    override fun onClickUpgrade(position: Int) {
        //loader?.show()


        val nodeaddress = Integer.parseInt(node_list[position].elements[0].unicastAddress.toString(), 16)

       // MainActivity.network.advise(Utils.otaCheckserviceCallback)
        startOTAServiceMessage(nodeaddress, this@OTAModeFragment, Nucleo.APPLI_ENABLE_OTA)


        Timer("startFotaMode", false).schedule(500) {
            startActivity(Intent(activity, FotaActivity::class.java))
        }



    }

    override fun onClickCheckUpgrade(position: Int) {
        //loader?.show()
        this.position = position

        val nodeaddress = Integer.parseInt(node_list[position].elements[0].unicastAddress.toString(), 16)

        MainActivity.network.advise(Utils.otaserviceCallback)
        startOTAServiceMessage(nodeaddress, this@OTAModeFragment, Nucleo.APPLI_OTA_CHECK)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Utils.updateActionBarForFeatures(activity, OTAModeFragment::class.java.simpleName)
        loader = AppDialogLoader.getLoader(activity)

        return inflater.inflate(R.layout.ota_mode_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNodeListFromJson()
        setAdapterToList()
    }

    private fun getNodeListFromJson() {

        node_list = arguments?.getSerializable(getString(R.string.key_serializable)) as List<Nodes>
        node_list = node_list.drop(1) //removing provisioner from the list

    }

    private fun setAdapterToList() {
        recycler_otamode.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = OTAModeAdapter(node_list = node_list, context = mycontext, callbackToFragment = this@OTAModeFragment)
        }

    }

}

