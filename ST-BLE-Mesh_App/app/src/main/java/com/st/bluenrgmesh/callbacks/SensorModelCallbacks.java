/**
 * *****************************************************************************
 *
 * @file SensorModelCallbacks.java
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

package com.st.bluenrgmesh.callbacks;

import android.content.Intent;
import android.os.Handler;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.msi.moble.ApplicationParameters;
import com.msi.moble.SensorModelClient;
import com.st.bluenrgmesh.MainActivity;
import com.st.bluenrgmesh.UserApplication;
import com.st.bluenrgmesh.Utils;
import com.st.bluenrgmesh.logger.LoggerConstants;
import com.st.bluenrgmesh.models.sensor.SensorData;
import com.st.bluenrgmesh.models.sensor.CompleteSensorData;
import com.st.bluenrgmesh.parser.ParseManager;

import java.util.ArrayList;



public class SensorModelCallbacks {

    private final static int MEMS_ACCELEROMETER_SENSOR_PROPERTY_ID = 0x2BA1;
    private final static int MEMS_GYROMETER_SENSOR_PROPERTY_ID = 0x2BA2;
    private final static int MEMS_MAGNETOMETER_SENSOR_PROPERTY_ID = 0x2AA1;
    private final static int ENV_TEMPERATURE_SENSOR_ROPERTY_ID = 0x0071;
    private final static int ENV_PRESSURE_SENSOR_ROPERTY_ID = 0x2A6D ;
    private final static int ENV_HUMIDITY_SENSOR_ROPERTY_ID = 0x2A6F ;
    private final static int VOLTAGE_SENSOR_ROPERTY_ID = 0x0005;
    private final static int CURRENT_SENSOR_ROPERTY_ID = 0x0004;
    private final static int POWER_FACTOR_SENSOR_ROPERTY_ID = 0x0072;
    private final static int POWER_ACTIVE_SENSOR_ROPERTY_ID = 0x0073;
    private final static int POWER_REACTIVE_SENSOR_ROPERTY_ID = 0x0074;
    private final static int POWER_APPARENT_SENSOR_ROPERTY_ID = 0x0075;
    private final static int ENERGY_ACTIVE_SENSOR_ROPERTY_ID = 0x0083;
    private final static int ENERGY_REACTIVE_SENSOR_ROPERTY_ID = 0x0084;
    private final static int ENERGY_APPARENT_SENSOR_ROPERTY_ID = 0x0085;



    public static SensorModelClient.GetSensorStatusCallback mGetSensorStatusCallback = new SensorModelClient.GetSensorStatusCallback() {
        public void ongetSensorStatus(boolean timeout, ArrayList<ApplicationParameters.SensorDataFormat> format,
                                      ArrayList<ApplicationParameters.SensorDataLength> length,
                                      final ArrayList<ApplicationParameters.SensorDataPropertyId> propertyIds,
                                      ArrayList<String> sensordata) {

            if(timeout){
                ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("GetSensor Status Callback==>"+"timeout", LoggerConstants.TYPE_RECEIVE);
                UserApplication.trace("Timeout Occurs");
                Utils.showToast(Utils.contextMainActivity, "Timeout Occurs.");
                Utils.showPopUpForMessage(Utils.contextMainActivity, "Timeout Occur \n Make sure your sensor device switched on and under network range.");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        CompleteSensorData sensorList = new CompleteSensorData();
                        sensorList.setSensorList(null);
                        String data = ParseManager.getInstance().toJSON(sensorList);
                        Utils.setSensorData(Utils.contextMainActivity, data);
                        //UserApplication.trace("Application  Parameters: slen " +   data);
                        //Utils.updateSensorEvents(data,((MainActivity) Utils.contextMainActivity).sensorPropertyId); //sensorPropertyId : -1
                    }
                });

                try {
                    Intent intent = new Intent("sensor_data_broadcast");
                    // You can also include some extra data.
                    //intent.putExtra("message", "0");
                    LocalBroadcastManager.getInstance(Utils.contextMainActivity).sendBroadcast(intent);
                }catch (Exception e){

                }
            } else{

                ApplicationParameters.SensorDataPropertyId mPropertyId = null;
                ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote("GetSensor Status Callback==>"+"Success", LoggerConstants.TYPE_RECEIVE);

                for(int i = 0; i < propertyIds.size() ; i++)
                {
                   // UserApplication.trace("Raw Sensor data for Sensor  => " +  getSensorFromPropertyId(propertyIds.get(i)) + " =>  " + sensordata.get(i));
                    UserApplication.trace("Format for Sensor => " +  getSensorFromPropertyId(propertyIds.get(i)) + " =>  " + format.get(i));
                    UserApplication.trace("Length for Sensor => " +  getSensorFromPropertyId(propertyIds.get(i)) + " =>  " + length.get(i));
                }

                for(String sdat : sensordata) {
                    UserApplication.trace("Application  Parameters: SensorData =>" +(sdat));
                    ((MainActivity)Utils.contextMainActivity).mUserDataRepository.getNewDataFromRemote(sdat, LoggerConstants.TYPE_RECEIVE);
                }

                final ArrayList<SensorData> completeSensorData = fetchSensorDataFromHexData(sensordata, propertyIds);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        CompleteSensorData sensorList = new CompleteSensorData();
                        sensorList.setSensorList(completeSensorData);
                        String data = ParseManager.getInstance().toJSON(sensorList);
                        Utils.setSensorData(Utils.contextMainActivity, data);
                        UserApplication.trace("Application  Parameters: slen " +   data + " PropertyId Size : " + propertyIds.size());
                        Utils.updateSensorEvents(data,((MainActivity) Utils.contextMainActivity).sensorPropertyId);
                    }
                });

            }
        }

        private String getSensorFromPropertyId(ApplicationParameters.SensorDataPropertyId sensorDataPropertyId) {

            String sensor = null;
            switch (sensorDataPropertyId.getValue()) {

                case 0x0071:
                    sensor = "TEMPERATURE";
                    break;
                case 0x2BA1:
                    sensor = "ACCELEROMETER";
                    break;
                case 0x2BA2:
                    sensor = "GYROMETER";
                    break;
                case 0x2AA1:
                    sensor = "MAGNETOMETER";
                    break;
                case 0x2A6D:
                    sensor = "PRESSURE SENSOR";
                    break;
                case 0x2A6F:
                    sensor = "HUMIDITY SENSOR";
                    break;
                case 0x0005:
                    sensor = "VOLTAGE SENSOR";
                    break;
                case 0x0004:
                    sensor = "CURRENT SENSOR";
                    break;
                case 0x0072:
                    sensor = "POWER FACTOR";
                    break;
                case 0x0073:
                    sensor = "POWER ACTIVE";
                    break;
                case 0x0074:
                    sensor = "POWER REACTIVE";
                    break;
                case 0x0075:
                    sensor = "POWER APPARENT";
                    break;
                case 0x0083:
                    sensor = "ENERGY ACTIVE";
                    break;
                case 0x0084:
                    sensor = "ENERGY REACTIVE";
                    break;
                case 0x0085:
                    sensor = "ENERGY APPARENT";
                    break;
            }


            return sensor;
        }
    };

    private static ArrayList<SensorData> fetchSensorDataFromHexData(ArrayList<String> data, ArrayList<ApplicationParameters.SensorDataPropertyId> propertyIds) {

        ArrayList<SensorData> list = new ArrayList<>();
        try {
            for (int i = 0; i < data.size(); i++) {
                String[] splited = data.get(i).split("\\s+");

                ArrayList requirHexArr = new ArrayList();
                for (int j = 0; j < splited.length; j++) {
                    if (splited[j].contains("x")) {
                        requirHexArr.add(splited[j]);
                    }
                }

                ArrayList hexValueArr = new ArrayList();
                for (int j = 0; j < requirHexArr.size(); j++) {
                    int ind1 = requirHexArr.get(j).toString().indexOf("x");
                    int ind2 = requirHexArr.get(j).toString().indexOf(")");
                    hexValueArr.add(requirHexArr.get(j).toString().substring(ind1 + 1, ind2)); //included, excluded
                }

                String strInvertAppend = "";
                for (int j = hexValueArr.size() - 1; j > -1; j--) {
                    strInvertAppend = strInvertAppend + hexValueArr.get(j);
                }

                System.out.println("Sppended : " + strInvertAppend);

                char[] chars = strInvertAppend.toCharArray();
                SensorData sensorData = new SensorData();
                sensorData.setPropertyID(propertyIds.get(i));
                sensorData.setPropertyId(propertyIds.get(i).getValue());
                if (chars.length <= 8) {
                    //T,P,H, Smart Plug
                    String s = new String(chars);
                    //sensorData.setSensorValue(String.valueOf(Utils.convertHexToFloat(s)));
                    if(propertyIds.size() == 9)
                    {
                        sensorData.setSensorValue(String.valueOf(Utils.convertHexToIntValue(s)));
                    }
                    else
                    {
                        sensorData.setSensorValue(String.valueOf(Utils.convertHexToFloat(s)));
                    }

                    //System.out.println("Data : " + String.valueOf(Utils.convertHexToFloat(s)));
                    //System.out.println("Data : " + String.valueOf(Utils.convertHexToIntValue(s)));
                } else {
                    //devide into 8 per each //A,E,M
                    ArrayList<String> subData = new ArrayList();
                    for (int j = 0; j <= chars.length; j++) {
                        if (j % 8 == 0 && j != 0) {
                            System.out.println(j);
                            String strr = getSensorDataFromHex(new String(chars), j - 8, j);
                            //System.out.println("XYZ : " + strr);
                            subData.add(String.valueOf(Utils.convertHexToIntValue(strr)));
                        }
                    }

                    sensorData.setSubSensor(subData);
                    for (int j = 0; j < subData.size(); j++) {
                        int i1 = Utils.convertHexToIntValue(subData.get(j).toString());
                        System.out.println("XYZ : " + i1);
                    }

                }

                list.add(sensorData);
            }
        }catch (Exception e){}

        return list;
    }

    private static String getSensorDataFromHex(String sensorStr, int includeNmbr, int excludeNmbr) {

        String substring = sensorStr.substring(includeNmbr, excludeNmbr);

        return substring;
    }
}
