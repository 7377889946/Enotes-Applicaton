package com.crazycoder.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class NoteByPageDto {
	
	private List<NotesDto> notesDtos;
	private Integer pageNo;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private Boolean isFirst;
	private Boolean isLast;
	

}
