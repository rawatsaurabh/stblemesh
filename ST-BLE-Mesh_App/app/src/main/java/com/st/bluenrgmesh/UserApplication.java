/**
 * *****************************************************************************
 *
 * @file UserApplication.java
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

package com.st.bluenrgmesh;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.msi.moble.mobleAddress;
import com.msi.moble.mobleStatus;
import com.st.bluenrgmesh.utils.ExceptionHandler;
import com.st.bluenrgmesh.utils.LruBitmapCache;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.fabric.sdk.android.Fabric;

/*import com.crashlytics.android.answers.Answers;*/

/**
 * Class to store internal data and provide this data to all application components
 */
public class UserApplication extends Application {
    /**
     * The M configuration.
     */
    public Configuration mConfiguration;
    /**
     * The M demo.
     */
    boolean mDemo;
    private Activity mActive;

    private static final String TAG1 = "MoBLEapp";

    public static final String TAG = UserApplication.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static UserApplication mInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mInstance = this;

        UserApplication.context = getApplicationContext();
        Utils.DEBUG("Appcontroller >> onCreate() called");

        if(context.getResources().getBoolean(R.bool.bool_sendCrashDump)) {

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(null));

        }

        if (/*!BuildConfig.DEBUG &&*/ getResources().getBoolean(R.bool.bool_enableFabric)) {
            //Fabric.with(this, new Crashlytics());

        }

    }

    /**
     * Start moble status.
     *
     * @return the moble status
     */
    public mobleStatus start() {
        mobleStatus res = mConfiguration.getNetwork().start(this);
        if (res.failed()) {
            return res;
        }
        mConfiguration.getNetwork().advise(mCallback);
        return mobleStatus.SUCCESS;
    }


    /**
     * Stop moble status.
     *
     * @return the moble status
     */
    mobleStatus stop() {
        if (mConfiguration != null) {
            mConfiguration.getNetwork().unadvise(mCallback);
            return mConfiguration.getNetwork().stop();
        } else {
            trace("Network does not exist");
            return mobleStatus.FALSE;
        }
    }


    private final static String filename = Environment.getExternalStorageDirectory() + File.separator + "/configuration";

    void load() {
        try {
            mConfiguration = Configuration.load(this, filename);
        } catch (IOException e) {
            trace("Exception occurred while loading configuration");
            e.printStackTrace();
        }
    }

    /**
     * Load.
     *
     * @param path the path
     */
    void load(String path) {
        try {
            mConfiguration = Configuration.load(this, path);
        } catch (IOException e) {
            trace("Exception occurred while loading configuration");
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            mConfiguration.save(filename, this);
            trace("mConfiguration " + mConfiguration.toString());
        } catch (IOException e) {
            trace("Exception occurred while saving configuration");
            e.printStackTrace();
        }
    }

    /**
     * Sets activity.
     *
     * @param active the active
     */
    void setActivity(Activity active) {
        mActive = active;
    }

    /**
     * Trace.
     *
     * @param str the str
     */
    public static void trace(String str) {
        Log.v(TAG1, Thread.currentThread().getStackTrace()[3].getClassName() + "::" + Thread.currentThread().getStackTrace()[3].getMethodName() + ": " + str);
    }

    /**
     * Notify user.
     *
     * @param message  the message
     * @param activity the activity
     */
    void notifyUser(final String message, final Activity activity) {
        if (!mDemo) return;
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) vibrator.vibrate(500);

        ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);

        if (activity != null) {
            activity.runOnUiThread(new Notifier(message, activity));
        }
    }

    private static class Notifier implements Runnable {
        private String message;
        private Activity activity;

        /**
         * Instantiates a new Notifier.
         *
         * @param msg the msg
         * @param a   the a
         */
        Notifier(String msg, Activity a) {
            message = msg;
            activity = a;
        }

        @Override
        public void run() {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    private final defaultAppCallback mCallbackNetwork = new defaultAppCallback() {
        @Override
        public void onUpdateRemoteData(mobleAddress address, Object cookies, short offset, byte count, byte[] data) {
            if (!mDemo) return;
            if (cookies != null) return;
            if (count != 1) return;
            if (data.length != 1) return;
            if ((data[0] & 1) == 0) return;
            String name = null;
            for (String mac : mConfiguration.getDevicesSet()) {
                DeviceEntry entry = mConfiguration.getDevice(mac);
                if (null == entry)
                    continue;

                if (entry.getAddress().equals(address)) {
                    name = entry.getName();
                    break;
                }
            }
            if (name != null)
                notifyUser(name + " movement is detected", mActive);
            else
                notifyUser("Unknown device movement is detected", mActive);
        }
    };


    private final defaultAppCallback mCallback = new defaultAppCallback() {
        @Override
        public void onUpdateRemoteData(mobleAddress address, Object cookies, short offset, byte count, byte[] data) {
            if (!mDemo) return;
            if (cookies != null) return;
            if (count != 1) return;
            if (data.length != 1) return;
            if ((data[0] & 1) == 0) return;
            String name = null;
            for (String mac : mConfiguration.getDevicesSet()) {
                DeviceEntry entry = mConfiguration.getDevice(mac);
                if (null == entry)
                    continue;

                if (entry.getAddress().equals(address)) {
                    name = entry.getName();
                    break;
                }
            }
            if (name != null)
                notifyUser(name + " movement is detected", mActive);
            else
                notifyUser("Unknown device movement is detected", mActive);
        }
    };



    /*
    * Volley Request Data SSL Security
    *
    * */

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Utils.DEBUG("Appcontroller >> getRequestQueue() >> New volley request");
            /*HurlStack hurlStack = new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }
            };*/
            //trust server certificate if using https protocol
            //mRequestQueue = Volley.newRequestQueue(getApplicationContext(), hurlStack);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        Utils.DEBUG("UserApplication >> getRequestQueue() >> added request to queue");
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getAppContext() {
        return UserApplication.context;
    }


    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    public static synchronized UserApplication getInstance() {
        return mInstance;
    }

    /*private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.my_cert); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }*/

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = null;
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //Log.e(TAG, StringUtils.EMPTY, e);
        } catch (KeyManagementException e) {
            //Log.e(TAG, StringUtils.EMPTY, e);
            e.printStackTrace();
        }

        return sslSocketFactory;
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("192.168.64.136", session);
                return true;
            }
        };
    }
}
