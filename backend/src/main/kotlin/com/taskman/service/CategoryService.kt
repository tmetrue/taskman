package com.taskman.service

import com.taskman.dto.CategoryRequest
import com.taskman.model.Category
import com.taskman.repository.CategoryRepository
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategories(): List<Category> {
        return try {
            categoryRepository.findAllOrderByDisplayOrderAsc()
        } catch (e: Exception) {
            // Fallback to unordered if custom query fails
            categoryRepository.findAll().toList()
        }
    }
    
    fun getCategoryById(id: Long): Optional<Category> {
        return categoryRepository.findById(id)
    }
    
    fun getCategoryByName(name: String): Optional<Category> {
        return categoryRepository.findByName(name)
    }
    
    fun createCategory(request: CategoryRequest): Category {
        // Check if category with that name already exists
        if (categoryRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Category with name '${request.name}' already exists")
        }
        
        val category = Category(
            name = request.name,
            description = request.description,
            displayOrder = request.displayOrder
        )
        
        return categoryRepository.save(category)
    }
    
    fun updateCategory(id: Long, request: CategoryRequest): Category? {
        val existingCategory = categoryRepository.findById(id).orElse(null) ?: return null
        
        // Check if new name conflicts with an existing category (other than this one)
        if (request.name != existingCategory.name && 
            categoryRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Category with name '${request.name}' already exists")
        }
        
        existingCategory.name = request.name
        existingCategory.description = request.description
        existingCategory.displayOrder = request.displayOrder
        
        return categoryRepository.update(existingCategory)
    }
    
    fun deleteCategory(id: Long): Boolean {
        if (!categoryRepository.existsById(id)) {
            return false
        }
        
        categoryRepository.deleteById(id)
        return true
    }
}