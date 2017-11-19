package com.example.bloold.buildp.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule

import java.util.TimeZone
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 24.05.16.
 */
object ServiceGenerator {
    val SITE_URL = "http://ruinnet.idefa.ru"
    private val BASE_URL = SITE_URL+"/api_app/"
    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            //.authenticator(new TokenAuthenticator())
            .addNetworkInterceptor(AddAccessTokenInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

    private val builder = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(
                    ObjectMapper().registerModule(SimpleModule())
                            .setTimeZone(TimeZone.getDefault())
            ))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    /*** API для работы с сервером  */
    val serverApi: ServerApi
        get() = retrofit(BASE_URL).create(ServerApi::class.java)

    private fun retrofit(domain: String): Retrofit =
            builder.client(httpClient.build()).baseUrl(domain).build()
}
