package com.mcac.data.di

import android.content.Context
import androidx.compose.ui.layout.ScaleFactor
import com.mcac.data.remote.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

fun apiServiceProvider(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun retrofitProvider(okHttpClient: OkHttpClient,baseUrl:String):Retrofit{
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}
fun okHttpClientProvider(httpLoggingInterceptor: HttpLoggingInterceptor,cache: Cache):OkHttpClient{
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .cache(cache)
        .build()
}
fun httpLoggingInterceptorProvider(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return httpLoggingInterceptor
}

fun cacheProvider(file: File): Cache {
    return Cache(file, (10 * 1024 * 1024).toLong())
}

fun fileProvider(context: Context): File {
    val file = File(context.filesDir, "McacCache")
    if (!file.exists()) {
        file.mkdir()
    }
    return file
}