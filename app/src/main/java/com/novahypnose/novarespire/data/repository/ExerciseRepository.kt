package com.novahypnose.novarespire.data.repository

import com.novahypnose.novarespire.data.models.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository pour la gestion des exercices de respiration
 */
interface ExerciseRepository {
    fun getAllExercises(): Flow<Result<List<Exercise>>>
    fun getExerciseById(id: String): Flow<Result<Exercise?>>
}

@Singleton
class ExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {
    
    override fun getAllExercises(): Flow<Result<List<Exercise>>> = flow {
        try {
            val exercises = Exercise.getAll()
            emit(Result.success(exercises))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override fun getExerciseById(id: String): Flow<Result<Exercise?>> = flow {
        try {
            val exercise = Exercise.getAll().find { it.id == id }
            emit(Result.success(exercise))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}