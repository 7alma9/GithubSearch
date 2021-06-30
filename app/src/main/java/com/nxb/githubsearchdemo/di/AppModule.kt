package com.nxb.githubsearchdemo.di

import com.android.example.github.util.LiveDataCallAdapterFactory
import com.nxb.githubsearchdemo.BuildConfig
import com.nxb.githubsearchdemo.adapters.ReposAdapter
import com.nxb.githubsearchdemo.data.remote.RepoApi
import com.nxb.githubsearchdemo.repository.ApiRepository
import com.nxb.githubsearchdemo.repository.DefaultApiRepository
import com.nxb.githubsearchdemo.ui.CustomFragmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideFragmentFactory( reposAdapter : ReposAdapter) =
        CustomFragmentFactory(reposAdapter)


    @Singleton
    @Provides
    fun providesDefaultRepository(
        repoApi: RepoApi
    ) = DefaultApiRepository(repoApi) as ApiRepository

    @Singleton
    @Provides
    fun provideReposApi(client: OkHttpClient) =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                LiveDataCallAdapterFactory()
            )
            .build().create(RepoApi::class.java)

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {


        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    }


    @Singleton
    @Provides
    fun provideString(): String{
        return "This is a TEST string I'm providing for injection"
    }

}