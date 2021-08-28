package com.nesyou.getter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.loader.content.CursorLoader
import io.flutter.embedding.engine.plugins.activity.ActivityAware

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/** GetterPlugin */
class GetterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity;
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "getter")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "get") {
            val type: Int? = call.argument("type")
            var selection: String? = null
            when (type) {
                GetterType.Audios.ordinal -> selection = (
                        MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)
                GetterType.Videos.ordinal -> selection = (
                        MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                GetterType.Photos.ordinal -> selection = (
                        MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                GetterType.All.ordinal -> selection = (
                        MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                                + " OR "
                                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                                + " OR "
                                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)
            }
            result.success(get(selection))
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun get(selection: String?): List<String> {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.ARTIST,
            MediaStore.Files.FileColumns.DURATION,
            MediaStore.Files.FileColumns.SIZE
        )

        val queryUri: Uri = MediaStore.Files.getContentUri("external")

        val cursorLoader = CursorLoader(
            context,
            queryUri,
            projection,
            selection,
            null,  // Selection args (none).
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        )

        val cursor: Cursor? = cursorLoader.loadInBackground()

        val list = ArrayList<String>()

        if (cursor != null) {
            cursor.moveToFirst()
            do {
                var artist : String? = null
                var duration : String? = null

                if (cursor.getString(5) != null) {
                    artist = cursor.getString(5)
                }
                if (cursor.getString(6) != null) {
                    duration = cursor.getString(6)
                }
                val data : Media =  Media(
                    cursor.getString(0),
                    cursor.getString(4),
                    (LocalDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(2)), ZoneId.systemDefault())).toString(),
                    cursor.getString(1),
                    duration,
                    artist,
                    cursor.getString(7),
                    cursor.getString(3)
                )
                list.add(
                    data.toString()
                )
            } while (cursor.moveToNext())
        }
        return list
    }
}

private enum class GetterType {
    Videos, Photos, Audios, All
}


private class Media(
    var id: String,
    var title: String,
    var date: String,
    var path: String,
    var duration: String?,
    var artist: String?,
    var size: String,
    var mimeType: String
) {
    override fun toString(): String {
        return "{\"id\": $id,\"title\": \"$title\",\"date\": \"$date\",\"path\": \"$path\",\"duration\": $duration,\"artist\": \"$artist\" ,\"size\": $size,\"mime_type\": \"$mimeType\"}"
    }
}
