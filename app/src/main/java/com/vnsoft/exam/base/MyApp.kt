package com.vnsoft.exam.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.vnsoft.exam.R
import com.vnsoft.exam.injection.ApplicationModule
import com.vnsoft.exam.injection.DaggerApplicationComponent
import com.vnsoft.exam.utils.myapp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        myapp = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(
                this
            )
        ).build()
        myapp?.inject(this)

        initCalligraphy()
        printHashKey()

        Fresco.initialize(
            applicationContext,
            ImagePipelineConfig.newBuilder(applicationContext)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment().setNativeCodeDisabled(true)
                .build()
        )
    }

    private fun initCalligraphy() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/helveticaneue.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun printHashKey() {
        // Add code to print out the key hash
        try {
            val signatures: Array<Signature> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo: PackageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo.apkContentsSigners
            } else {
                val packageInfo: PackageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES
                )
                packageInfo.signatures
            }
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e(
                    "KeyHash",
                    "KeyHash:" + Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
}