package com.io.appsfactorytesttask.modules


import android.content.Context
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.database.AppDataBase
import com.io.appsfactorytesttask.data.network.LastFmAPI
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @ApplicationContext app: Context): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(app.getString(R.string.API_ENDPOINT))
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): LastFmAPI = retrofit.create(LastFmAPI::class.java)



    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context) = AppDataBase.getDatabase(app.applicationContext)
}