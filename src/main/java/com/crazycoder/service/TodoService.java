package com.crazycoder.service;
import java.util.List;
import com.crazycoder.dto.ToDoDto;
import com.crazycoder.exception.ResourceNotFoundException;


public interface TodoService {
	
	public Boolean saveTodo(ToDoDto toDoDto) throws ResourceNotFoundException;
	public ToDoDto getToDoById(Integer id) throws ResourceNotFoundException;
	public List<ToDoDto> getToDoByUser();
	
}
