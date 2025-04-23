package com.crazycoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crazycoder.commonUtil.CommonUtil;
import com.crazycoder.dto.CategoryDto;
import com.crazycoder.dto.CategoryResponse;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.exception.dtoValidationException;
import com.crazycoder.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;


	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody Category category) {
		Boolean savedCategory = categoryService.saveCategory(category);

		if (savedCategory) {
			return new ResponseEntity<>("saved Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory() {
		List<Category> categories = categoryService.getAllCategory();
		if (org.springframework.util.CollectionUtils.isEmpty(categories)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(categories, HttpStatus.OK);

	
	@PostMapping("/save-category")
	 public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws dtoValidationException{
		Boolean savedCategory=categoryService.saveCategory(categoryDto);
		
		if(savedCategory) {
			return CommonUtil.createBuildResponseMessage("Success", HttpStatus.OK);
//			return new ResponseEntity<>("saved Successfully",HttpStatus.OK);
		} else{
			return CommonUtil.createErrorResponseMessage("Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
//		    return new ResponseEntity<>("Not saved",HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory(){
	
		List<CategoryDto> categories=categoryService.getAllCategory();
		if(org.springframework.util.CollectionUtils.isEmpty(categories)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(categories, HttpStatus.OK);

		}
	}
	
	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory(){
		List<CategoryResponse> categories=categoryService.getActiveCategory();
		if(org.springframework.util.CollectionUtils.isEmpty(categories)) {
			return ResponseEntity.noContent().build();
		} else { 
			return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws ResourceNotFoundException{
		CategoryDto categoryDto=categoryService.getCategoryById(id); 
		if(ObjectUtils.isEmpty(categoryDto)) {
			return CommonUtil.createErrorResponseMessage("Object Not Found with Id :- "+id, HttpStatus.NOT_FOUND);
		} else {
			return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
		Boolean deleted=categoryService.deleteCategoryById(id);
		
		if(deleted) {
			return CommonUtil.createBuildResponseMessage("Category Deleted Successfully", HttpStatus.OK);
		} else {
			return CommonUtil.createErrorResponseMessage("Category is not found in database", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
