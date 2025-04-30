package com.taskman.controller

import com.taskman.dto.CategoryRequest
import com.taskman.model.Category
import com.taskman.service.CategoryService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.validation.Valid

@Validated
@Controller("/api/categories")
class CategoryController(@Inject private val categoryService: CategoryService) {

    @Get
    @Secured("IS_AUTHENTICATED")  // Both regular users and admins can view categories
    fun getAllCategories(): HttpResponse<List<Category>> {
        return HttpResponse.ok(categoryService.getAllCategories())
    }
    
    @Get("/{id}")
    @Secured("IS_AUTHENTICATED")  // Both regular users and admins can view categories
    fun getCategoryById(id: Long): HttpResponse<Category> {
        val categoryOptional = categoryService.getCategoryById(id)
        return if (categoryOptional.isPresent) {
            HttpResponse.ok(categoryOptional.get())
        } else {
            HttpResponse.notFound()
        }
    }
    
    @Post
    @Secured("ROLE_ADMIN")  // Only admins can create categories
    fun createCategory(@Body @Valid request: CategoryRequest): HttpResponse<Category> {
        try {
            val category = categoryService.createCategory(request)
            return HttpResponse.created(category)
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest()
        }
    }
    
    @Put("/{id}")
    @Secured("ROLE_ADMIN")  // Only admins can update categories
    fun updateCategory(id: Long, @Body @Valid request: CategoryRequest): HttpResponse<Category> {
        try {
            val category = categoryService.updateCategory(id, request)
            return if (category != null) {
                HttpResponse.ok(category)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest()
        }
    }
    
    @Delete("/{id}")
    @Secured("ROLE_ADMIN")  // Only admins can delete categories
    fun deleteCategory(id: Long): HttpResponse<Unit> {
        return if (categoryService.deleteCategory(id)) {
            HttpResponse.noContent()
        } else {
            HttpResponse.notFound()
        }
    }
}