package com.crazycoder.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Data
public class FileDetails {
	
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@jakarta.persistence.Id
	private Integer id;
	private String uploadFileName;
    private String displayFileName;
    private String originalFileName;
    private String path;
    private Long fileSize;
    
}
