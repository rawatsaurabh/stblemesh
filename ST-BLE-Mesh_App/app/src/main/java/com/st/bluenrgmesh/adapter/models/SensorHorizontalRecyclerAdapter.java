/**
 ******************************************************************************
 * @file    SensorHorizontalRecyclerAdapter.java
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
package com.st.bluenrgmesh.adapter.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msi.moble.ApplicationParameters;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.models.sensor.SensorData;

import java.util.ArrayList;

public class SensorHorizontalRecyclerAdapter extends RecyclerView.Adapter<SensorHorizontalRecyclerAdapter.ViewHolder> {


    private String modelType;
    private Context context;
    private ArrayList<SensorData> requiredModel;
    private SensorModelRecyclerAdapter.IRecyclerViewHolderClicks listener;

    public SensorHorizontalRecyclerAdapter(Context context, ArrayList<SensorData> requiredModel, String modelType, SensorModelRecyclerAdapter.IRecyclerViewHolderClicks listener) {

        this.context = context;
        this.requiredModel = requiredModel;
        this.modelType = modelType;
        this.listener = listener;
    }

    public interface IRecyclerViewHolderClicks {
        void onClickRecyclerItem(ApplicationParameters.SensorDataPropertyId propertyID, int position);
    }


    @Override
    public SensorHorizontalRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sensor_model_horizontalview, parent, false);
        SensorHorizontalRecyclerAdapter.ViewHolder vh = new SensorHorizontalRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SensorHorizontalRecyclerAdapter.ViewHolder holder, final int position) {

        try {
            if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_TEMP_PROPERTYID) || requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_TEMPERATURE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Temperature");
                StringBuilder str = new StringBuilder(requiredModel.get(position).getSensorValue());
                str.setLength(5);
                holder.txtSensor1.setText( str.toString() + " ("+context.getResources().getString(R.string.str_degree)+")");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imgtemperature1));
            } else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_PRESSURE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Pressure");
                StringBuilder str = new StringBuilder(requiredModel.get(position).getSensorValue());
                str.setLength(6);
                holder.txtSensor1.setText(str.toString() +  " (bar)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imgpressure1));
            } else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_HUMIDITY_PROPERTYID)) {
                holder.txtSensorHeading.setText("Humidity");
                StringBuilder str = new StringBuilder(requiredModel.get(position).getSensorValue());
                str.setLength(5);
                holder.txtSensor1.setText(str.toString() + " (%)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imghumidity1));
            } else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_ACCELEROMETER_PROPERTYID)) {
                holder.txtSensorHeading.setText("Accelerometer");
                holder.txtSensor1.setText("X = " + requiredModel.get(position).getSubSensor().get(2)
                        + ",  " + "Y = " + requiredModel.get(position).getSubSensor().get(1)
                        + ",  " + "Z = " + requiredModel.get(position).getSubSensor().get(0) + " (mg)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imgaccelerometer1));
            } else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_GYRO_PROPERTYID)) {
                holder.txtSensorHeading.setText("Gyrometer");
                holder.txtSensor1.setText("X = " + requiredModel.get(position).getSubSensor().get(2)
                        + ",  " + "Y = " + requiredModel.get(position).getSubSensor().get(1)
                        + ",  " + "Z = " + requiredModel.get(position).getSubSensor().get(0) + " (radian/sec)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imggyrometer1));
            } else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_MAGNETO_PROPERTYID)) {
                holder.txtSensorHeading.setText("Magnetometer");
                holder.txtSensor1.setText("X = " + requiredModel.get(position).getSubSensor().get(2)
                        + ",  " + "Y = " + requiredModel.get(position).getSubSensor().get(1)
                        + ",  " + "Z = " + requiredModel.get(position).getSubSensor().get(0) + " (G)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.imgmagnetometer1));
            }

            else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_VOLTAGE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Voltage");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (V)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.voltageicon));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_CURRENT_PROPERTYID)) {
                holder.txtSensorHeading.setText("Current");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (mA)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.currrent));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_POWER_FACTOR_PROPERTYID)) {
                holder.txtSensorHeading.setText("Power");
                int power_val = Integer.parseInt(requiredModel.get(position).getSensorValue());
                holder.txtSensor1.setText(String.format("%2.02f",(float)power_val / 100) + " ");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.powera));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_POWER_ACTIVE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Apparent Power");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (W)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.powera));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_POWER_REACTIVE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Power Reactive");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (VAR)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.powerr));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_POWER_APPARENT_PROPERTYID)) {
                holder.txtSensorHeading.setText("Power AR");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (VA)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.powerap));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_ENERGY_ACTIVE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Energy Apparent");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (Wh)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.energya));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_ENERGY_REACTIVE_PROPERTYID)) {
                holder.txtSensorHeading.setText("Energy Reactive");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (VARh)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.energyr));
            }else if (requiredModel.get(position).getPropertyId() == context.getResources().getInteger(R.integer.SENSOR_MODEL_ENERGY_APPARENT_PROPERTYID)) {
                holder.txtSensorHeading.setText("Energy AR");
                holder.txtSensor1.setText(requiredModel.get(position).getSensorValue() + " (VAh)");
                holder.imgSensor.setImageDrawable(context.getDrawable(R.drawable.energyap));
            }

            holder.lytSensor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    ApplicationParameters.SensorDataPropertyId propertyID = requiredModel.get(position).getPropertyID();
                    listener.onClickRecyclerItem(propertyID, position);
                    return true;
                }
            });


        }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        /*return requiredModel.size()*/
        if(requiredModel != null && requiredModel.size() > 0)
        {
            return requiredModel.size();
        }
        else
        {
            return 6;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSensor;
        private TextView txtSensor1;
        private TextView txtSensorHeading;
        private LinearLayout lytSensor;

        public ViewHolder(View itemView) {
            super(itemView);

            lytSensor = (LinearLayout) itemView.findViewById(R.id.lytSensor);
            imgSensor = (ImageView) itemView.findViewById(R.id.imgSensor);
            txtSensor1 = (TextView) itemView.findViewById(R.id.txtSensor1);
            txtSensorHeading = (TextView) itemView.findViewById(R.id.txtSensorHeading);
        }
    }
}
