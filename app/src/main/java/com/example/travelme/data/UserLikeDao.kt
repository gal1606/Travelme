package com.example.travelme.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.travelme.models.UserLike
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLikeDao {

    @Upsert
    suspend fun upsertAll(trips: List<UserLike>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: UserLike)

    @Update
    suspend fun update(trip: UserLike)

    @Delete
    suspend fun delete(trip: UserLike)

    @Query("SELECT * from likes")
    fun getAllTrips(): Flow<List<UserLike>>
}