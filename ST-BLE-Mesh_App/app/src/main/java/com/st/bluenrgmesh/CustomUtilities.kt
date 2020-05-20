package com.st.bluenrgmes;

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.st.bluenrgmesh.R
import com.st.bluenrgmesh.Utils
import java.io.File
import java.io.PrintWriter

class CustomUtilities {

    fun createFileStorage(context : Context) {
        var meshedStringData = Utils.filterJsonObject(context)
        var sd_main = File(context.filesDir,"stblueNrg")
        var success = true

        if (!sd_main.exists())
            success = sd_main.mkdir()

        if (success) {
            // directory exists or already created
            var dest = File(sd_main, context.getResources().getString(R.string.FILE_bluenrg_mesh_json))
            try {
                var fileString = meshedStringData
                PrintWriter(dest).use { out -> out.println(fileString) }
            } catch (e: Exception) {
                // handle the exception
            }
        }

        else {
            // directory creation is not successful
        }
    }
}