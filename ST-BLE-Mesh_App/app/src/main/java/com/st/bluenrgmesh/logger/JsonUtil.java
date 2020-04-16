/**
 * *****************************************************************************
 *
 * @file JsonUtil.java
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
import android.util.Log;

import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;



public class JsonUtil {

    private static StringBuilder sb;
    public static String toJSon(/*Person person*/String log,String number,Context context) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            String currentDateandTime = sdf.format(new Date());
            //Log.d("convertt==>>>",""+log);
            JSONObject jsonObj = new JSONObject();
            LoggerPojo loggerPojo = new LoggerPojo();
            loggerPojo.setLogs(log);
            loggerPojo.setType(number);
            loggerPojo.setDate(currentDateandTime);
            String read_old_logs = readFromFile(context);
            if (!read_old_logs.equalsIgnoreCase("")) {
                JSONObject PreviousJsonObj = new JSONObject(read_old_logs);
                JSONArray array = PreviousJsonObj.getJSONArray("logger");
                jsonObj.put("logs", loggerPojo.getLogs());
                jsonObj.put("type", loggerPojo.getType());
                jsonObj.put("date", loggerPojo.getDate());
                array.put(jsonObj);
                JSONObject currentJsonObject = new JSONObject();
                currentJsonObject.put("logger", array);
                writeToFile(currentJsonObject.toString(), context);
            } else {
                JSONArray jsonArr = new JSONArray();
                jsonObj.put("logs", loggerPojo.getLogs());
                jsonObj.put("type", loggerPojo.getType());
                jsonObj.put("date", loggerPojo.getDate());
                jsonArr.put(jsonObj);
                JSONObject currentJsonObject = new JSONObject();
                currentJsonObject.put("logger", jsonArr);
                writeToFile(currentJsonObject.toString(), context);
            }
            return readFromFile(context);

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }


    public static void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("logger.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context) {

        String ret = "";

        try {
            if(context==null) {
                context= Utils.contextMainActivity;
            }


            InputStream inputStream = (context).openFileInput("logger.json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean ClearLoggerFile(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, "logger.json");
        return file.delete();
    }
}