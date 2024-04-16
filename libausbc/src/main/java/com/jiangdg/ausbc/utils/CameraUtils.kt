package com.jiangdg.ausbc.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import androidx.core.content.ContextCompat
import com.jiangdg.ausbc.R
import com.serenegiant.usb.DeviceFilter

/** Camera tools
 *
 * @author Created by jiangdg on 2022/7/19
 */
object CameraUtils {

    /**
     * check is usb camera
     *
     * @param device see [UsbDevice]
     * @return true usb camera
     */
    fun isUsbCamera(device: UsbDevice?): Boolean {
        return when (device?.deviceClass) {
            UsbConstants.USB_CLASS_VIDEO -> {
                true
            }
            UsbConstants.USB_CLASS_MISC -> {
                var isVideo = false
                for (i in 0 until device.interfaceCount) {
                    val cls = device.getInterface(i).interfaceClass
                    if (cls == UsbConstants.USB_CLASS_VIDEO) {
                        isVideo = true
                        break
                    }
                }
                isVideo
            }
            else -> {
                false
            }
        }
    }

    /**
     * Filter needed usb device by according to filter regular
     *
     * @param context context
     * @param usbDevice see [UsbDevice]
     * @return true find success
     */
    fun isFilterDevice(context: Context?, usbDevice: UsbDevice?): Boolean {
        return DeviceFilter.getDeviceFilters(context, R.xml.default_device_filter)
            .find { devFilter ->
                devFilter.mProductId == usbDevice?.productId && devFilter.mVendorId == usbDevice.vendorId
            }.let { dev ->
                dev != null
            }
    }

    fun hasAudioPermission(ctx: Context): Boolean{
//        val locPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.RECORD_AUDIO)
//        return locPermission == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val readPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.RECORD_AUDIO)
            return readPermission == PackageManager.PERMISSION_GRANTED
        } else {
            val audioPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_MEDIA_AUDIO)
            val videoPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_MEDIA_VIDEO)
            return audioPermission == PackageManager.PERMISSION_GRANTED && videoPermission == PackageManager.PERMISSION_GRANTED
        }

    }

    fun hasStoragePermission(ctx: Context): Boolean{
//        val locPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        return locPermission == PackageManager.PERMISSION_GRANTED
//
//
//        //版本判断，如果比android 13 就走正常的权限获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val readPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
        } else {
            val audioPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_MEDIA_AUDIO)
            val imagePermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_MEDIA_IMAGES)
            val videoPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_MEDIA_VIDEO)
            return audioPermission == PackageManager.PERMISSION_GRANTED && imagePermission == PackageManager.PERMISSION_GRANTED && videoPermission == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasCameraPermission(ctx: Context): Boolean{
        val locPermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
        return locPermission == PackageManager.PERMISSION_GRANTED
    }
}