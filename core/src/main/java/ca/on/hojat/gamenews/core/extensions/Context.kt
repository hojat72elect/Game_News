@file:JvmName("ContextUtils")
@file:Suppress("TooManyFunctions")

package ca.on.hojat.gamenews.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

val Context.configuration: Configuration
    get() = resources.configuration

fun Context.getCompatColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.getDimensionPixelSize(@DimenRes dimenId: Int): Int {
    return resources.getDimensionPixelSize(dimenId)
}

fun Context.getDimension(@DimenRes dimenId: Int): Float {
    return resources.getDimension(dimenId)
}

fun Context.showShortToast(message: CharSequence): Toast {
    return showToast(message, duration = Toast.LENGTH_SHORT)
}

fun Context.showLongToast(message: CharSequence): Toast {
    return showToast(message, duration = Toast.LENGTH_LONG)
}

fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, message, duration)
        .apply { show() }
}


/**
 * Checks whether the intent can be handled by some activity
 * on the device or not.
 *
 * Note: Due to [Android 11 package visibility changes](https://g.co/dev/packagevisibility), this
 * method does not work on Android 11 and above.
 *
 * @param intent The intent to check
 *
 * @return true if can be handled; false otherwise
 */
@SuppressLint("QueryPermissionsNeeded")
fun Context.canIntentBeHandled(intent: Intent): Boolean {
    return packageManager.queryIntentActivities(intent, 0).isNotEmpty()
}

inline fun <reified T : Any> Context.getSystemService(): T {
    return checkNotNull(ContextCompat.getSystemService(this, T::class.java)) {
        "The service ${T::class.java.simpleName} could not be retrieved."
    }
}


@get:Suppress("DEPRECATION")
@get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
val Context.isConnectedToNetwork: Boolean
    get() = (getSystemService<ConnectivityManager>().activeNetworkInfo?.isConnected == true)

