package com.hari.connector.database

import androidx.room.*
import com.hari.connector.models.Data

@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contacts: List<Data>)

    @Query("DELETE FROM contactMaster")
    fun deleteContacts(): Int

    @Query("SELECT * FROM contactMaster")
    fun getAllContacts(): List<Data>
}