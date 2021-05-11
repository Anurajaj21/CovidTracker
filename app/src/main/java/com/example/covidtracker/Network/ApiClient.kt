package com.example.covidtracker.Network

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient(val Base_url: String) {
    var retrofit : Retrofit? = null

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getApiClient(): Retrofit? {
        val httpClient = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
        httpClient.addInterceptor(){
            val original: Request = it.request()
// Customize the request
// Customize the request
            val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "form-data")
                    .method(original.method, original.body)
                    .build()
            // Customize or return the response
// Customize or return the response
            return@addInterceptor it.proceed(request)
        }
        fun OkHttpClient(): OkHttpClient = httpClient.build()
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient())
                    .build()
        }
        return retrofit
    }
}