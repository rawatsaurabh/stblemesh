/**
 ******************************************************************************
 * @file    ModelRepository.java
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
package com.st.bluenrgmesh.utils;

import com.msi.moble.ApplicationParameters;

import java.util.HashMap;

public class ModelRepository {

    public static ModelRepository modelRepository;
    public static HashMap<String, ApplicationParameters.GenericModelID> mModelMap =new HashMap<String, ApplicationParameters.GenericModelID>();

    public static synchronized ModelRepository getInstance()
    {
        if(modelRepository == null)
        {
            modelRepository = new ModelRepository();
        }

        return modelRepository;
    }

    public ApplicationParameters.GenericModelID getModelSelected(String modelName) {

        return mModelMap.get(modelName);
    }


    public ModelRepository()
    {

        mModelMap.put(ApplicationParameters.ModelID.CONFIGURATION_SERVER.getName(),ApplicationParameters.ModelID.CONFIGURATION_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.CONFIGURATION_CLIENT.getName(),ApplicationParameters.ModelID.CONFIGURATION_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.HEALTH_MODEL_SERVER.getName(),ApplicationParameters.ModelID.HEALTH_MODEL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.HEALTH_MODEL_CLIENT.getName(),ApplicationParameters.ModelID.HEALTH_MODEL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_ONOFF_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_ONOFF_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_ONOFF_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_LEVEL_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_LEVEL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_LEVEL_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_LEVEL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_DEFAULT_TRANSITION_TIME_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_DEFAULT_TRANSITION_TIME_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_DEFAULT_TRANSITION_TIME_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_DEFAULT_TRANSITION_TIME_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_SETUP_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_POWER_ON_OFF_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_SETUP_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_POWER_LEVEL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_BATTERY_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_BATTERY_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_BATTERY_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_BATTERY_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_LOCATION_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_LOCATION_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_LOCATION_SETUP_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_LOCATION_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_LOCATION_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_LOCATION_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_ADMIN_PROPERTY_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_ADMIN_PROPERTY_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_MANUFACTURER_PROPERTY_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_MANUFACTURER_PROPERTY_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_USER_PROPERTY_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_USER_PROPERTY_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_CLIENT_PROPERTY_SERVER.getName(),ApplicationParameters.ModelID.GENERIC_CLIENT_PROPERTY_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.GENERIC_PROPERTY_CLIENT.getName(),ApplicationParameters.ModelID.GENERIC_PROPERTY_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.SENSOR_MODEL_SERVER.getName(),ApplicationParameters.ModelID.SENSOR_MODEL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SENSOR_SETUP_SERVER.getName(),ApplicationParameters.ModelID.SENSOR_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SENSOR_CLIENT.getName(),ApplicationParameters.ModelID.SENSOR_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.TIME_SERVER.getName(),ApplicationParameters.ModelID.TIME_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.TIME_SETUP_SERVER.getName(),ApplicationParameters.ModelID.TIME_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.TIME_CLIENT.getName(),ApplicationParameters.ModelID.TIME_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.SCENE_SERVER.getName(),ApplicationParameters.ModelID.SCENE_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SCENE_SETUP_SERVER.getName(),ApplicationParameters.ModelID.SCENE_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SCENE_CLIENT.getName(),ApplicationParameters.ModelID.SCENE_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.SCHEDULER_SERVER.getName(),ApplicationParameters.ModelID.SCHEDULER_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SCHEDULER_SETUP_SERVER.getName(),ApplicationParameters.ModelID.SCHEDULER_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.SCHEDULER_CLIENT.getName(),ApplicationParameters.ModelID.SCHEDULER_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LIGHTNESS_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_LIGHTNESS_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LIGHTNESS_SETUP_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_LIGHTNESS_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LIGHTNESS_CLIENT.getName(),ApplicationParameters.ModelID.LIGHT_LIGHTNESS_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_CTL_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_CTL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_CTL_SETUP_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_CTL_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_CTL_CLIENT.getName(),ApplicationParameters.ModelID.LIGHT_CTL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_CTL_TEMPERATURE_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_CTL_TEMPERATURE_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_HSL_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_HSL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_HSL_SETUP_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_HSL_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_HSL_CLIENT.getName(),ApplicationParameters.ModelID.LIGHT_HSL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_HSL_HUE_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_HSL_HUE_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_HSL_SATURATION_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_HSL_SATURATION_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_XYL_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_XYL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_XYL_SETUP_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_XYL_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_XYL_CLIENT.getName(),ApplicationParameters.ModelID.LIGHT_XYL_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LC_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_LC_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LC_SETUP_SERVER.getName(),ApplicationParameters.ModelID.LIGHT_LC_SETUP_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.LIGHT_LC_CLIENT.getName(),ApplicationParameters.ModelID.LIGHT_LC_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.ST_VENDOR_SERVER.getName(),ApplicationParameters.ModelID.ST_VENDOR_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.ST_SILVAIR_MODEL_0.getName(),ApplicationParameters.ModelID.ST_SILVAIR_MODEL_0);
        mModelMap.put(ApplicationParameters.ModelID.ST_SILVAIR_MODEL_1.getName(),ApplicationParameters.ModelID.ST_SILVAIR_MODEL_1);
        mModelMap.put(ApplicationParameters.ModelID.ST_SILVAIR_MODEL_2.getName(),ApplicationParameters.ModelID.ST_SILVAIR_MODEL_2);
        mModelMap.put(ApplicationParameters.ModelID.ST_SILVAIR_MODEL_B.getName(),ApplicationParameters.ModelID.ST_SILVAIR_MODEL_B);
        mModelMap.put(ApplicationParameters.ModelID.ST_SILVAIR_MODEL_E.getName(),ApplicationParameters.ModelID.ST_SILVAIR_MODEL_E);
        mModelMap.put(ApplicationParameters.ModelID.FIRMWARE_UPDATE_SERVER.getName(),ApplicationParameters.ModelID.FIRMWARE_UPDATE_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.FIRMWARE_UPDATE_CLIENT.getName(),ApplicationParameters.ModelID.FIRMWARE_UPDATE_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.FIRMWARE_DISTRIBUTION_SERVER.getName(),ApplicationParameters.ModelID.FIRMWARE_DISTRIBUTION_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.FIRMWARE_DISTRIBUTION_CLIENT.getName(),ApplicationParameters.ModelID.FIRMWARE_DISTRIBUTION_CLIENT);
        mModelMap.put(ApplicationParameters.ModelID.BLOB_TRANSFER_MODEL_SERVER.getName(),ApplicationParameters.ModelID.BLOB_TRANSFER_MODEL_SERVER);
        mModelMap.put(ApplicationParameters.ModelID.BLOB_TRANSFER_MODEL_CLIENT.getName(),ApplicationParameters.ModelID.BLOB_TRANSFER_MODEL_CLIENT);

    }

}
