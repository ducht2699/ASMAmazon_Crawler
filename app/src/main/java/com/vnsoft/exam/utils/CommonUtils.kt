package com.vnsoft.exam.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.vnsoft.exam.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {

    private fun getActivityRoot(window: Window): View {
        return (window.decorView.rootView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(
            0
        )
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun getDPtoPX(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private const val KEYBOARD_VISIBLE_THRESHOLD_DP = 100f


    open interface KeyboardVisibilityEventListener {
        fun onVisibilityChanged(isOpen: Boolean)
    }

    fun setEventListener(
        activity: Activity,
        listener: KeyboardVisibilityEventListener?,
        window: Window
    ) {
        if (listener == null) {
            return
        }

        val activityRoot = getActivityRoot(window) ?: return
        activityRoot!!.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                private val r = Rect()

                private val visibleThreshold = Math.round(
                    getDPtoPX(
                        activity,
                        KEYBOARD_VISIBLE_THRESHOLD_DP
                    ).toFloat()
                )

                private var wasOpened = false

                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)

                    val heightDiff = activityRoot.rootView.height - r.height()

                    val isOpen = heightDiff > visibleThreshold
                    if (isOpen == wasOpened) {
                        return
                    }
                    wasOpened = isOpen
                    listener.onVisibilityChanged(isOpen)
                }
            })
    }

    fun setImageUrlwithNoRadiusv2S(imageView: ImageView, url: String) {
        val context = imageView.context
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 50, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "f5", null)
        return Uri.parse(path)
    }

    fun setImageRadiusFormUrl(imageView: ImageView, url: String?) {
        val context = imageView.context
        Glide.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .transform(CenterCrop(), RoundedCorners(10))
            .error(R.drawable.no_data)
            .into(imageView)
    }

    fun setImageFormUrl(imageView: ImageView, url: String?) {
        val context = imageView.context
        Glide.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .error(R.drawable.no_data)
            .into(imageView)
    }

    fun setImageAvatarFormUrl(imageView: ImageView, url: String?) {
        val context = imageView.context
        Glide.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .error(R.drawable.ic_avata_default)
            .into(imageView)
    }


    fun setImageRadiusFormUri(imageView: ImageView, url: String) {
        val context = imageView.context
        Glide.with(context).asBitmap().load(url).apply(RequestOptions().override(500, 500))
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }


    fun getImageDrawable(context: Context, id: Int): Drawable {
        return AppCompatResources.getDrawable(context, id)!!
    }

    private fun getTypeDate(c: Calendar): String {
        val sdf2 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf2.format(c.time)
    }

    fun convertRegexCard4(card: String): String {
        return card.replace("(?<=\\d{6})\\d(?=\\d{4})".toRegex(), "*")
    }

    fun getThumbByUrlVideo(url: String, mThumb: ImageView, context: Context) {
        val interval = 5000 * 1000.toLong()
        val options = RequestOptions().frame(interval).override(300, 200)
            .transform(CenterCrop(), RoundedCorners(15)).placeholder(R.drawable.no_data)


        // setup Glide request without the into() method
        // setup Glide request without the into() method
        val thumbnailRequest: RequestBuilder<Drawable> = Glide
            .with(context)
            .load(url)
            .override(50)
            .transform(CenterCrop(), RoundedCorners(15))
        Glide.with(context).load(url).thumbnail(thumbnailRequest).apply(options).into(mThumb)
    }

    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release()
            }
        }
        return bitmap
    }

    fun getThumbByUrlVideo1(url: String, mThumb: ImageView, activity: Activity) {
        activity.runOnUiThread(Runnable {
            val retriever = MediaMetadataRetriever()
            //give YourVideoUrl below
            //give YourVideoUrl below
            retriever.setDataSource(url, HashMap())
            // this gets frame at 2nd second
            // this gets frame at 2nd second
            val image =
                retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            mThumb.setImageBitmap(image)
        })
    }

    fun loadJSONFromAsset(context: Context): String? {
        var json: String?
        try {
            val `is`: InputStream = context.assets.open("CountryCodes.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    @SuppressLint("HardwareIds")
    fun getID(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun versionName(context: Context): String? {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun convertMillieToHMmSs(millie: Long): String? {
        val seconds = millie / 1000
        val second = seconds % 60
        val minute = seconds / 60 % 60
        val hour = seconds / (60 * 60) % 24
        val result = ""
        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            String.format("%02d:%02d", minute, second)
        }
    }

    fun setStatusBarColor(activity: Activity) {
        val window: Window = activity.getWindow()

// clear FLAG_TRANSLUCENT_STATUS flag:

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.colorBlack)
        }
    }

    fun setStatusColorIcon(view: View) {
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

