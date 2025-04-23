package com.crazycoder.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.crazycoder.Enum.TodoStatus;
import com.crazycoder.dto.ToDoDto;
import com.crazycoder.dto.ToDoDto.statusDto;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.model.Todo;
import com.crazycoder.repository.ToDoRepository;
import com.crazycoder.service.TodoService;
import com.crazycoder.util.Validation;

@Service
public class ToDoServiceImpl implements TodoService {
	
	@Autowired
	private ToDoRepository toDoRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validation validation;

	@Override
	public Boolean saveTodo(ToDoDto toDoDto) throws ResourceNotFoundException {
		
		//todo validation check
	    validation.todoValidation(toDoDto);
	    
	    Todo todo = mapper.map(toDoDto, Todo.class);
	    todo.setStatusId(toDoDto.getStatus().getId());
	    Todo todoObjectReturn = toDoRepository.save(todo);
	    if(!ObjectUtils.isEmpty(todoObjectReturn)) {
	    	return true;
	    }
		return false;
	}

	@Override
	public ToDoDto getToDoById(Integer id) throws ResourceNotFoundException {
	  Todo todo = toDoRepository.findById(id)
			                    .orElseThrow(() -> new ResourceNotFoundException("Todo is not found in database | Id invalid"));
	  
	   ToDoDto mappingDto=mapper.map(todo, ToDoDto.class);
	  
	   //set enum name in status name 
	   for(TodoStatus ts:TodoStatus.values()) {
		   if(ts.getId().equals(mappingDto.getStatus().getId())) {
			   statusDto dto = statusDto.builder()
					                    .id(ts.getId())
					                    .name(ts.getName())
					                    .build();
			  
			   System.out.print(mappingDto.getStatus().getName());
			  
		   }
	   }
	   return mappingDto;
	}

	@Override
	public List<ToDoDto> getToDoByUser() {
		Integer userId=1;
	    List<Todo> todos = toDoRepository.findByCreatedBy(userId);
	    return todos.stream().map(todo -> mapper.map(todo, ToDoDto.class)).toList();
	}
}


