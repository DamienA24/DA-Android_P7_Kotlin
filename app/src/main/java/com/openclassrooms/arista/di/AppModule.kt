package com.openclassrooms.arista.di

import android.content.Context
import com.openclassrooms.arista.data.AppDatabase
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, coroutineScope: CoroutineScope): AppDatabase {
        return AppDatabase.getDatabase(context, coroutineScope)
    }


    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDtoDao {
        return appDatabase.userDtoDao()
    }


    @Provides
    fun provideSleepDao(appDatabase: AppDatabase): SleepDtoDao {
        return appDatabase.sleepDtoDao()
    }


    @Provides
    fun provideExerciseDao(appDatabase: AppDatabase): ExerciseDtoDao {
        return appDatabase.exerciseDtoDao()
    }


    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDtoDao): UserRepository {
        return UserRepository(userDao)
    }


    @Provides
    @Singleton
    fun provideSleepRepository(sleepDtoDao: SleepDtoDao): SleepRepository {
        return SleepRepository(sleepDtoDao)
    }


    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDtoDao: ExerciseDtoDao): ExerciseRepository {
        return ExerciseRepository(exerciseDtoDao)
    }
}