/**
 ******************************************************************************
 * @file    VoiceAssistActivity.java
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


package com.st.bluenrgmesh.voice_assist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msi.moble.ApplicationParameters;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceAssistAcivity extends AppCompatActivity {

	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private EditText group_address;
	private Button save_addressBT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_assist);
		group_address = (EditText) findViewById(R.id.group_address);
		group_address.setText(  Utils.getGroupAddressForVoice(this));
		save_addressBT  = findViewById(R.id.save_addressBT);
		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		save_addressBT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.setGroupAddressForVoice(VoiceAssistAcivity.this,group_address.getText().toString());
				group_address.clearFocus();
				Toast.makeText(VoiceAssistAcivity.this,"Address Saved Successfully!",Toast.LENGTH_LONG).show();

			}
		});

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});

	}

	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	private  ApplicationParameters.OnOff state= ApplicationParameters.OnOff.DISABLED;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
try {
	if (requestCode == REQ_CODE_SPEECH_INPUT) {
		if (resultCode == RESULT_OK && null != data) {
			ApplicationParameters.Address targetAddress = new ApplicationParameters.Address(Integer.parseInt(Utils.getGroupAddressForVoice(this), 16));


			ArrayList<String> result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			assert result != null;
			txtSpeechInput.setText(result.get(0));

			if (result.get(0).contains("on")) {
				state = ApplicationParameters.OnOff.ENABLED;
			} else if (result.get(0).contains("off")) {
				state = ApplicationParameters.OnOff.DISABLED;

			}
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					MainActivity.network.getOnOffModel().setGenericOnOff(false, targetAddress,
							state,
							new ApplicationParameters.TID(1), null, null,
							null);
				}
			}, 100);

		}
	}
}catch (Exception e)
{

}
	}

}
