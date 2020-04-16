/**
 ******************************************************************************
 * @file    ParseManager.java
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

package com.st.bluenrgmesh.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Primitives;
import com.st.bluenrgmesh.UserApplication;

import org.json.JSONObject;


public class ParseManager
{
    private static ParseManager parseManager = null;

    public static ParseManager getInstance()
    {
        if(parseManager == null)
        {
            parseManager = new ParseManager();
        }

        return parseManager;
    }

    public <T> T fromJSON(JSONObject objJson, Class<T> classOfT)
    {
        if(objJson == null)
        {
            return null;
        }

        Gson gson = (new GsonBuilder()).create();
        try
        {
            Object object = gson.fromJson(objJson.toString(), classOfT);
            return Primitives.wrap(classOfT).cast(object);
        }
        catch (Exception e)
        {
            UserApplication.trace("ST ParseManager >> fromJSON >> Error while parsing JSON : " + e.toString());
            e.printStackTrace();
        }

        return null;
    }

    public String toJSON(Object obj)
    {
        if(obj == null)
        {
            return null;
        }

        Gson gson = (new GsonBuilder()).create();
        try
        {
            return gson.toJson(obj);
        }
        catch (Exception e)
        {
            UserApplication.trace("ST ParseManager >> toJSON >> Error while getting JSON from object : " + e.toString());
        }

        return null;
    }


}
