package com.ajinkya.formula1.di

import android.content.Context
import androidx.room.Room
import com.ajinkya.formula1.common.Constant
import com.ajinkya.formula1.data.local.data_source.LocalDataSource
import com.ajinkya.formula1.data.local.database.F1Dao
import com.ajinkya.formula1.data.local.database.F1Database
import com.ajinkya.formula1.data.remote.data_source.RemoteDataSource
import com.ajinkya.formula1.data.remote.service.ApiService
import com.ajinkya.formula1.data.repository.ConstructorStandingsRepository
import com.ajinkya.formula1.data.repository.DriverStandingsRepository
import com.ajinkya.formula1.data.repository.ScheduleRepository
import com.ajinkya.formula1.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ----------------- Remote Data Source -------------------- //
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return retrofit.client(client).build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)


    // ----------------- Local Data Source -------------------- //

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        F1Database::class.java,
        Constant.F1_DATABASE
    ).build()


    @Singleton
    @Provides
    fun provideDao(db: F1Database): F1Dao = db.f1Dao()

    @Provides
    @Singleton
    fun provideLocalDataSource(f1Dao: F1Dao) = LocalDataSource(f1Dao)


    // ----------------- Repository -------------------- //

    @Provides
    @Singleton
    fun provideScheduleRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ) = ScheduleRepository(localDataSource, remoteDataSource)


    @Provides
    @Singleton
    fun provideDriverStandingsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ) = DriverStandingsRepository(localDataSource, remoteDataSource)


    @Provides
    @Singleton
    fun provideConstructorStandingsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ) = ConstructorStandingsRepository(localDataSource, remoteDataSource)


    // ----------------- Use Cases -------------------- //

    @Provides
    @Singleton
    fun provideFormatScheduleDateUseCase() = FormatScheduleDateUseCase()


    @Provides
    @Singleton
    fun provideFormatScheduleTimeUseCase() = FormatScheduleTimeUseCase()


    @Provides
    @Singleton
    fun provideScheduleUseCase(
        scheduleRepository: ScheduleRepository,
        formatScheduleDateUseCase: FormatScheduleDateUseCase,
        formatScheduleTimeUseCase: FormatScheduleTimeUseCase
    ) = GetScheduleUseCase(scheduleRepository, formatScheduleDateUseCase, formatScheduleTimeUseCase)


    @Provides
    @Singleton
    fun provideConstructorStandingsUseCase(constructorStandingsRepository: ConstructorStandingsRepository) =
        GetConstructorStandingsUseCase(constructorStandingsRepository)


    @Provides
    @Singleton
    fun provideDriverStandingsUseCase(driverStandingsRepository: DriverStandingsRepository) =
        GetDriverStandingsUseCase(driverStandingsRepository)

}