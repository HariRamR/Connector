package com.hari.connector.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hari.connector.models.Data

@Database(entities = [Data::class], version = 1)
abstract class ConnectorDatabase : RoomDatabase() {

    abstract fun getContactDAO(): ContactDAO

    companion object {
        private var database: ConnectorDatabase? = null

        fun getInstance(application: Application): ConnectorDatabase {

            if (database == null) {
                database = Room.databaseBuilder(
                    application.applicationContext, ConnectorDatabase::class.java,
                    "connector_db"
                ).fallbackToDestructiveMigration().build()
            }
            return database!!
        }
    }
}