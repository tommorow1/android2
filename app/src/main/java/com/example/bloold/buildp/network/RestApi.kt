package com.example.bloold.buildp.network

import android.content.Context
import com.example.bloold.buildp.BuildConfig
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by bloold on 18.10.17.
 */
object RestApi {
    private lateinit var retrofit: Retrofit
    private val BASE_URL = "http://ruinnet.idefa.ru/api_app/object/list/"

    fun init(context: Context, authenticator: Authenticator): Unit {

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)
}