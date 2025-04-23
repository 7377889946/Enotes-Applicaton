package com.crazycoder.serviceImpl;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.crazycoder.dto.CategoryDto;
import com.crazycoder.dto.CategoryResponse;
import com.crazycoder.exception.ExitCategoryException;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.exception.dtoValidationException;
import com.crazycoder.model.Category;
import com.crazycoder.repository.CategoryRepository;
import com.crazycoder.service.CategoryService;
import com.crazycoder.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {
    
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Validation validation;
	
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) throws dtoValidationException, ExitCategoryException {
		//Validation cheking 
		validation.categoryDtoValidation(categoryDto);
		
		//Exist category by name exception
		Boolean exitCategory=categoryRepository.existsByName(categoryDto.getName().trim());
		
		if(exitCategory) {
			throw new ExitCategoryException("Category alerady Exist in database");
		}
	
		
		//map categoryDto to original Category class
		Category category=modelMapper.map(categoryDto, Category.class);
		
		if(ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
			/*
			 * category.setCreatedBy(1); category.setCreatedOn(new Date());
			 */
		} else {
			Boolean status=updateCategory(category);
			if(status) {
				
			} else {
				return false;
			}
		}
		
		Category savedCategory=categoryRepository.save(category);
		if(ObjectUtils.isEmpty(savedCategory)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public Boolean updateCategory(Category category) {
	  Optional<Category> existCategory=categoryRepository.findByIdAndIsDeletedFalse(category.getId());
	  
	  if(existCategory.isPresent()) {
		  Category exCategory=existCategory.get();
		  category.setCreatedBy(exCategory.getCreatedBy());
		  category.setCreatedOn(exCategory.getCreatedOn());
		  category.setIsDeleted(exCategory.getIsDeleted());
			/*
			 * category.setUpdatedBy(1); category.setUpdatedOn(new Date());
			 */
		  return true;
	  } else {
		  return false;
	  }
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		
		List<Category> listCategories=categoryRepository.findByIsDeletedFalse();
		
		List<CategoryDto> listCategoryDtos =listCategories.stream()
				                           .map(cat->modelMapper.map(cat, CategoryDto.class))
				                           .toList();
		
		return listCategoryDtos;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		
		List<Category> getAllActiveCategory=categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		
		List<CategoryResponse> categoryResponses=getAllActiveCategory.stream()
				                                                  .map(cat->modelMapper.map(cat, CategoryResponse.class))
				                                                  .toList();
		return categoryResponses;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
	Category categoryById=categoryRepository.findByIdAndIsDeletedFalse(id)
			                                     .orElseThrow(()->new ResourceNotFoundException("Category Not Found with id "+id));
	  if(!ObjectUtils.isEmpty(categoryById)) {
		  
		  return modelMapper.map(categoryById, CategoryDto.class);
		  
	  } else {
		  return null;
	  }
		
	}
	
	@Override
	public Boolean deleteCategoryById(Integer id) {
		Optional<Category> getCategoryById=categoryRepository.findByIdAndIsDeletedFalse(id);
		
		if(getCategoryById.isPresent()) {
			Category category=getCategoryById.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}
}
