package com.example.githubrepo.data.local

import androidx.room.TypeConverter
import com.example.githubrepo.data.remote.RestClient
import com.example.githubrepo.data.remote.RestClient.moshi
import com.example.githubrepo.data.remote.model.License
import com.example.githubrepo.data.remote.model.Permissions
import com.squareup.moshi.JsonAdapter

object DbTypeConverter {
    @TypeConverter
    @JvmStatic
    fun licenceObjectToString(license: License?) :String {
        val jsonAdapter: JsonAdapter<License> = moshi.adapter(License::class.java)
        return jsonAdapter.toJson(license)
    }
    @TypeConverter
    @JvmStatic
    fun stringToLicenseObject(license :String): License? {
        val jsonAdapter: JsonAdapter<License> = moshi.adapter(License::class.java)
        return jsonAdapter.fromJson(license)
    }
    @TypeConverter
    @JvmStatic
    fun permissionToJson(permission:Permissions?) : String {
        val jsonAdapter: JsonAdapter<Permissions> = moshi.adapter(Permissions::class.java)
        return jsonAdapter.toJson(permission)
    }
    @TypeConverter
    @JvmStatic
    fun stringToPermissionObject(json : String) : Permissions? {
        val jsonAdapter: JsonAdapter<Permissions> = moshi.adapter(Permissions::class.java)
        return jsonAdapter.fromJson(json)
    }
}