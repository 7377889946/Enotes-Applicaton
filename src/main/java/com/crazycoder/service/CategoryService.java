package com.crazycoder.service;

import java.util.List;
import com.crazycoder.dto.CategoryDto;
import com.crazycoder.dto.CategoryResponse;
import com.crazycoder.exception.ExitCategoryException;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.exception.dtoValidationException;


public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto) throws dtoValidationException, ExitCategoryException;
	public List<CategoryDto> getAllCategory();
	public List<CategoryResponse> getActiveCategory();
	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;
	public Boolean deleteCategoryById(Integer id);
	
}
