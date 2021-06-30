package com.nxb.githubsearchdemo.di

import com.nxb.githubsearchdemo.BuildConfig
import com.nxb.githubsearchdemo.adapters.ReposAdapter
import com.nxb.githubsearchdemo.data.remote.RepoApi
import com.nxb.githubsearchdemo.repository.ApiRepository
import com.nxb.githubsearchdemo.repository.DefaultApiRepository
import com.nxb.githubsearchdemo.repository.FakeApiRepositoryTest
import com.nxb.githubsearchdemo.ui.CustomFragmentFactory
import com.nxb.githubsearchdemo.ui.TestCustomFragmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {


    @Singleton
    @Provides
    fun provideFragmentFactory(apiRepository: ApiRepository, reposAdapter : ReposAdapter) =
            TestCustomFragmentFactory(apiRepository,reposAdapter)


    @Singleton
    @Provides
    fun providesDefaultRepository(
     ) = FakeApiRepositoryTest() as ApiRepository

    @Singleton
    @Provides
    fun provideReposApi(client: OkHttpClient) =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(RepoApi::class.java)

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