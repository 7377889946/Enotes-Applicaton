package com.crazycoder.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoDto {
	private Integer id;
	private String title;
	private statusDto status;
	private Integer createdBy;
	private Date createdOn;
	private Integer updatedBy;
	private Date updatedOn;
	
	@Getter
	@Setter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	public static class statusDto{
		private Integer id;
		private String name;
	}
}
