package com.crazycoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crazycoder.commonUtil.CommonUtil;
import com.crazycoder.dto.ToDoDto;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.service.TodoService;

@RestController
@RequestMapping("/api/v1/todo")
public class ToDoController {
	@Autowired
   private TodoService todoService;
	
	@PostMapping("/")
	public ResponseEntity<?> saveToDo(@RequestBody ToDoDto dto) throws ResourceNotFoundException{
	  Boolean status = todoService.saveTodo(dto);
	  if(status) {
		  return CommonUtil.createBuildResponseMessage("Todo saved Successfully", HttpStatus.OK);
	  } else {
		  return CommonUtil.createErrorResponseMessage("Todo Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	  }

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getToDoById(@PathVariable("id") Integer todoId) throws ResourceNotFoundException{
	  ToDoDto toDoDto = todoService.getToDoById(todoId);
	 return CommonUtil.createBuildResponse(toDoDto, HttpStatus.OK);

	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllToDoByUser() throws ResourceNotFoundException{
	  List<ToDoDto> toDoDtos=todoService.getToDoByUser();
	  if(CollectionUtils.isEmpty(toDoDtos)) {
		  return ResponseEntity.noContent().build();
	  } else {
		  return CommonUtil.createBuildResponse(toDoDtos, HttpStatus.OK);
	  }
	 
	}
}
