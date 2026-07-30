package com.example.showcase.features.MetaData.data.database.dao


import androidx.media3.common.util.UnstableApi
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.showcase.features.MetaData.data.database.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@UnstableApi
@Dao
interface SettingsDao {


	@Query("SELECT * FROM settings WHERE id = 1")
	fun observeSettings(): Flow<SettingsEntity?>

	@Query("SELECT * FROM settings WHERE id = 1")
	suspend fun getSettings(): SettingsEntity?

	@Upsert
	suspend fun saveSettings(settings: SettingsEntity)
}