package com.crazycoder.dto;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NotesDto {
	private Integer id;
	private String title;
	private String description;
	private CategoryDto category;
	private Integer createdBy;
	private Date createdOn;
	private Integer updatedBy;
	private Date updatedOn;
	private FileDto filesDetails;
    private Boolean isDeleted;
	private LocalDateTime deletedOn;
	
	//FileDto inner class
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
    public static class FileDto{
		private Integer id;
	    private String displayFileName;
	    private String originalFileName;
    }
	
	//inner class define
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
    public static class CategoryDto{
		private Integer id;
		private String name;
    }
}
