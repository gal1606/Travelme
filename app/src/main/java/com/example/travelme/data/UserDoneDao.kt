package com.example.travelme.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDoneDao {

    @Upsert
    suspend fun upsertAll(trips: List<UserDone>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: UserDone)

    @Update
    suspend fun update(trip: UserDone)

    @Delete
    suspend fun delete(trip: UserDone)

    @Query("SELECT * from `user-dones`")
    fun getAllTrips(): Flow<List<UserDone>>
}
