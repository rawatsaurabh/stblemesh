/**
 ******************************************************************************
 * @file    MyJsonObjectRequest.java
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

package com.st.bluenrgmesh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.st.bluenrgmesh.R;
import com.st.bluenrgmesh.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MyJsonObjectRequest extends JsonObjectRequest
{

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String JSESSIONID_KEY = "JSESSIONID";
    private SharedPreferences _preferences = null;
    private Context context;
    private boolean isGZIPCompressed;

    public MyJsonObjectRequest(boolean isGZIPCompressed, Context context, int method, String url,
                               JSONObject requestBody, Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);



        //to bypass server side reading if server is under development
        if(context.getResources().getBoolean(R.bool.bool_readFromAssets))
        {
            String fileName = null;
            /*if(url.contains(context.getString(R.string.)))
            {
                fileName = "";
            }
            else if(url.contains(context.getString(R.string.)))
            {
                fileName = "";
            }
            else
            {
                Utils.ERROR("No file name found for api : " + url + ", please update the asset folder");
                return;
            }*/


            try {
                listener.onResponse(new JSONObject(Utils.readFromAssets(context, fileName)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else
        {
            Utils.DEBUG("json request : " + requestBody.toString());
            Utils.DEBUG("url : " + url);
        }

        //for timeout add retry policy
        setRetryPolicy(new DefaultRetryPolicy((int)context.getResources().getInteger(R.integer.value_request_timeout),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.context = context;
        _preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.isGZIPCompressed = isGZIPCompressed;

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        Utils.DEBUG("parseNetworkResponse >> " + response.statusCode);
        if(context.getResources().getBoolean(R.bool.bool_readFromAssets))
        {
            //if reading from local then return null to handle call
            return Response.success(null ,HttpHeaderParser.parseCacheHeaders(response));
        }

        //Utils.DEBUG("parseNetworkResponse >> " + response.data);

        try {
            Utils.DEBUG("[raw json]: " + (new String(response.data)));

            checkSessionCookie(response);
            if (!response.notModified) {// Added for 304 response
                String jsonString = new String(isGZIPCompressed ? Utils.decompress(response.data) : response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                Utils.DEBUG("header content-type : " +  HttpHeaderParser.parseCharset(response.headers));
                if(jsonString == null || jsonString.length() < 3)
                {
                    Response.error(new ParseError(new NullPointerException()));
                }

                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else // Added for 304 response
                return  Response.error(new ParseError(new NullPointerException()));
        } catch (UnsupportedEncodingException e) {
            Utils.DEBUG("parseNetworkResponse >> UnsupportedEncodingException " + response.statusCode);
            if (response.statusCode == 200)// Added for 200 response
                return Response.error(new ParseError(new NullPointerException()));
            if (response.statusCode == 500)// Added for 500 response
                return  Response.error(new ParseError(new NullPointerException()));
            else
                return Response.error(new ParseError(e));
        } catch (JSONException je) {
            Utils.DEBUG("parseNetworkResponse >> JSONException " + response.statusCode);
            if (response.statusCode == 200)// Added for 200 response
                return  Response.error(new ParseError(new NullPointerException()));
            if (response.statusCode == 500)// Added for 500 response
                return  Response.error(new ParseError(new NullPointerException()));
            else
                return Response.error(new ParseError(je));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * Passing some request headers
     * */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        addSessionCookie(headers);

        return headers;
    }

    /**
     * save header values in shared prefs
     * @param response
     */
    public final void checkSessionCookie(NetworkResponse response) {

        Map<String, String> headers = response.headers;
        //{Content-Type=application/json;charset=UTF-8, Date=Tue, 21 Jun 2016 10:53:26 GMT, Server=Apache-Coyote/1.1, Set-Cookie=JSESSIONID=C155F33BBD4FB7FC140B71BACEBFB24C.sanjay; Path=/; Secure; HttpOnly, Transfer-Encoding=chunked, Vary=Accept-Encoding, X-Android-Received-Millis=1466506401400, X-Android-Response-Source=NETWORK 200, X-Android-Selected-Transport=http/1.1, X-Android-Sent-Millis=1466506400844}
        Utils.DEBUG("checkSessionCookie() called >> header is : " + headers);

        try
        {
            JSONObject object = new JSONObject(new String(response.data));

            SharedPreferences.Editor prefEditor = _preferences.edit();
            prefEditor.putString(JSESSIONID_KEY, object.getString("sessionId"));
            prefEditor.commit();

        }
        catch (Exception e)
        {

        }

        for (String key : headers.keySet())
        {
            Utils.DEBUG("" + key + "," + headers.get(key));
        }
        if (headers.containsKey(SET_COOKIE_KEY) && headers.get(SET_COOKIE_KEY).startsWith(JSESSIONID_KEY))
        {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];

                Utils.DEBUG("cookie >> " + cookie);
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(JSESSIONID_KEY, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * add saved headers in request call
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(JSESSIONID_KEY, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(JSESSIONID_KEY);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
            Utils.DEBUG("cookie key, value : " + COOKIE_KEY + ", " + builder.toString());
        }
    }
}