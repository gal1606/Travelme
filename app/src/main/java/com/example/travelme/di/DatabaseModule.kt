/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.travelme.di

import android.content.Context
import com.example.travelme.data.AppDatabase
import com.example.travelme.data.TripDao
import com.example.travelme.data.UserDoneDao
import com.example.travelme.data.UserLikeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTripDao(appDatabase: AppDatabase): TripDao {
        return appDatabase.tripDao()
    }

    @Provides
    fun provideUserLikeDao(appDatabase: AppDatabase): UserLikeDao {
        return appDatabase.userLikeDao()
    }

    @Provides
    fun provideUserDoneDao(appDatabase: AppDatabase): UserDoneDao {
        return appDatabase.userDoneDao()
    }
}
