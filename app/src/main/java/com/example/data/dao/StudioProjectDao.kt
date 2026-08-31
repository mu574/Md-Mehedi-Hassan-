package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudioProject
import kotlinx.coroutines.flow.Flow

@Dao
interface StudioProjectDao {
    @Query("SELECT * FROM studio_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<StudioProject>>

    @Query("SELECT * FROM studio_projects WHERE featureId = :featureId ORDER BY createdAt DESC")
    fun getProjectsByFeature(featureId: String): Flow<List<StudioProject>>

    @Query("SELECT * FROM studio_projects WHERE id = :id")
    suspend fun getProjectById(id: Long): StudioProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: StudioProject): Long

    @Update
    suspend fun updateProject(project: StudioProject)

    @Delete
    suspend fun deleteProject(project: StudioProject)

    @Query("DELETE FROM studio_projects WHERE id = :id")
    suspend fun deleteById(id: Long)
}
