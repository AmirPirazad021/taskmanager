package ir.badesaba.taskmanaer.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.BuildConfig
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.badesaba.taskmanaer.data.TodoRepositoryImpl
import ir.badesaba.taskmanaer.data.local.MyDatabase
import ir.badesaba.taskmanaer.data.local.TodoDao
import ir.badesaba.taskmanaer.data.remote.ApiService
import ir.badesaba.taskmanaer.domain.tasks.TaskRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    companion object {

        @Singleton
        @Provides
        fun provideOkHttpClient(
            app: Application,
        ): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)

            if (BuildConfig.DEBUG)
                client.addInterceptor(
                    ChuckerInterceptor.Builder(context = app)
                        .collector(ChuckerCollector(context = app))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(true)
                        .build()
                )

            return client.build()
        }

        @Singleton
        @Provides
        fun provideRetrofit(
            client: OkHttpClient
        ): Retrofit {
            val gson = GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl("https://api.badesaba.ir/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        @Provides
        fun provideFactorApi(
            retrofit: Retrofit
        ): ApiService = retrofit.create(ApiService::class.java)

        @Singleton
        @Provides
        fun provideMenuDatabase(
            @ApplicationContext context: Context
        ): MyDatabase = Room.databaseBuilder(
            context, MyDatabase::class.java, "task_manager"
        ).build()

        @Provides
        fun provideTodoDao(
            db: MyDatabase
        ): TodoDao = db.TodoDao()
//
//        @Provides
//        fun provideMenuApi(
//            retrofit: Retrofit
//        ): MenuApi = retrofit.create(MenuApi::class.java)

    }

    @Binds
    abstract fun bindTaskRepository(impl: TodoRepositoryImpl): TaskRepository

}