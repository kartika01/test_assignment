package com.example.testassignment.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testassignment.DB.CompanyDataItem

@Database(entities = arrayOf(CompanyDataItem::class, Member::class), version = 1, exportSchema = false)
@TypeConverters(Members::class)
abstract class DatabaseClass : RoomDatabase() {

    abstract fun companyDao(): CompanyDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DatabaseClass? = null

        fun getDatabase(context: Context): DatabaseClass {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseClass::class.java,
                    "company_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}