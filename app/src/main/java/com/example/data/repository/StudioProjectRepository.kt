package com.example.data.repository

import com.example.data.dao.StudioProjectDao
import com.example.data.model.StudioProject
import kotlinx.coroutines.flow.Flow

class StudioProjectRepository(private val dao: StudioProjectDao) {
    val allProjects: Flow<List<StudioProject>> = dao.getAllProjects()

    fun getProjectsByFeature(featureId: String): Flow<List<StudioProject>> =
        dao.getProjectsByFeature(featureId)

    suspend fun getProjectById(id: Long): StudioProject? = dao.getProjectById(id)

    suspend fun saveProject(project: StudioProject): Long = dao.insertProject(project)

    suspend fun deleteProject(project: StudioProject) = dao.deleteProject(project)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
