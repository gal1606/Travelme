package com.example.travelme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.travelme.extension.TripsSQLDBWorker
import com.example.travelme.extension.TripsSQLDBWorker.Companion.KEY_FILENAME
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike

@Database(entities = [Trip::class, UserDone::class, UserLike::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun userLikeDao(): UserLikeDao
    abstract fun userDoneDao(): UserDoneDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "travelme-db")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<TripsSQLDBWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}
