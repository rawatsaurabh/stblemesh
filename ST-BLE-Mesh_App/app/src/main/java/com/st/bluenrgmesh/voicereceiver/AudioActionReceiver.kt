/**
 ******************************************************************************
 * @file    AudioActionReceiver.kt
 * @author  BLE Mesh Team
 * @version V1.12.000
 * @date    31-March-2020
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

package com.st.bluenrgmesh.voicereceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.msi.moble.ApplicationParameters
import com.st.bluenrgmesh.MainActivity
import com.st.bluenrgmesh.Utils
import com.st.bluenrgmesh.Utils.*
import java.lang.Exception

class AudioActionReceiver : BroadcastReceiver() {
    private var state: ApplicationParameters.OnOff = ApplicationParameters.OnOff.DISABLED

    override fun onReceive(context: Context, intent: Intent) {
        /*val command = intent.getStringExtra("COMMAND") ?: "unknown"
        Log.d("CES", "received: $command")
        Toast.makeText(context, "Received: $command", Toast.LENGTH_LONG).show()
        turnOnOffLights(command,context)*/
    }

    private fun turnOnOffLights(text: String, context: Context) {
        try {
            var isOnCommand = isCommand_ON(context, text.toLowerCase())
            val targetAddress = ApplicationParameters.Address(Utils.getGroupAddressForVoice(context).toInt(16))
            val tid = ApplicationParameters.TID(1)
            state = if (isOnCommand == 1) ApplicationParameters.OnOff.ENABLED else ApplicationParameters.OnOff.DISABLED

            if(isOnCommand == 1 || isOnCommand == 2)
            {
                MainActivity.network.onOffModel.setGenericOnOff(false, targetAddress,
                        state,
                        tid, null, null,
                        null)
            }
            else{
                makeText(context,"Word is not present in dictionary.", LENGTH_LONG).show()
            }

        }catch (e:Exception){
        e.printStackTrace()
        }
    }

    private fun isCommand_ON(context: Context, txt: String): Int {
      /*  if(getSpellingOnForVoice(context) == null && !getSpellingOnForVoice(context).contains("on"))
        {
            setSpellingOnForVoice(context, "on")
            setSpellingOnForVoice(context, "al")
            setSpellingOnForVoice(context, "aa")
            setSpellingOnForVoice(context, "uu")
            setSpellingOnForVoice(context, "own")
            setSpellingOnForVoice(context, "Light on")
            setSpellingOnForVoice(context, "Luci accese")
            setSpellingOnForVoice(context, "Luci accesa")
            setSpellingOnForVoice(context, "Accendi la luce")
            setSpellingOnForVoice(context, "Accendi")
            setSpellingOnForVoice(context, "Accende")
            setSpellingOnForVoice(context, "Accendere")
        }

        if(getSpellingOFFForVoice(context) == null && !getSpellingOFFForVoice(context).contains("off"))
        {
            setSpellingOFFForVoice(context, "off")
            setSpellingOFFForVoice(context, "Luci spente")
            setSpellingOFFForVoice(context, "Luce spenta")
            setSpellingOFFForVoice(context, "Spegni la luce")
            setSpellingOFFForVoice(context, "Spegni")
            setSpellingOFFForVoice(context, "Spegne")
            setSpellingOFFForVoice(context, "Spegnere")
        }*/

        if(getSpellingOnForVoice(context)!=null && getSpellingOnForVoice(context).size>0 && getSpellingOnForVoice(context).contains(txt))
        {
            return 1
        }
        else if(getSpellingOFFForVoice(context)!=null && getSpellingOFFForVoice(context).size>0 && getSpellingOFFForVoice(context).contains(txt))
        {
            return 2
        }
        else
        {
            return 3
        }
    }
}
