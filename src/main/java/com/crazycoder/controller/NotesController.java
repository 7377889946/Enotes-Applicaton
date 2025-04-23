package com.crazycoder.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.crazycoder.commonUtil.CommonUtil;
import com.crazycoder.dto.FavoriteNotesDto;
import com.crazycoder.dto.NoteByPageDto;
import com.crazycoder.dto.NotesDto;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.model.FileDetails;
import com.crazycoder.service.NotesService;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;
	
	

	@PostMapping("/save-notes")
	public ResponseEntity<?> saveNotes(@RequestParam String notesDto, @RequestParam MultipartFile file)
			throws ResourceNotFoundException, IOException {
		Boolean savedNotes = notesService.saveNotes(notesDto, file);

		if (savedNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved Successfully", HttpStatus.OK);
		} else {
			return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadsNotes(@PathVariable Integer id) throws ResourceNotFoundException, IOException {
		FileDetails fileDetails = notesService.getFileDetils(id);
		byte[] fileAsByteArray = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(fileAsByteArray);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> notesDtos = notesService.getAllNotes();

		if (org.springframework.util.CollectionUtils.isEmpty(notesDtos)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(notesDtos, HttpStatus.OK);
		}
	}

	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Integer user_id = 1;
		NoteByPageDto notesDtos = notesService.getAllNotesByUser(user_id, pageNo, pageSize);
	
		return CommonUtil.createBuildResponse(notesDtos, HttpStatus.OK);
	}

	
     @GetMapping("/delete/{id}") 
     public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws ResourceNotFoundException{
	 notesService.softDeleteNotes(id); 
	 return CommonUtil.createBuildResponseMessage("Notes Deleted succesfully", HttpStatus.OK); 
	 }
	 


	  @GetMapping("/restore/{id}") 
	  public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException{
	  notesService.restoreNotes(id); 
	  return CommonUtil.createBuildResponseMessage("Notes Restored succesfully",HttpStatus.OK); 
	  }
	 

	
	 @GetMapping("/recycle-bin") 
	 public ResponseEntity<?> getUserRecycleBinNotes(){
	
	 Integer userId=1; 
	 List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
	 
	 if(org.springframework.util.CollectionUtils.isEmpty(notes)) {
		 return CommonUtil.createBuildResponseMessage("Recycle bin is Empty", HttpStatus.OK);
	  }
	  
	 return CommonUtil.createBuildResponse(notes,HttpStatus.OK); 
	 }
	 
	 
	 @DeleteMapping("/delete/{id}") 
     public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws ResourceNotFoundException {
	 notesService.hardDeleteNotes(id); 
	 return CommonUtil.createBuildResponseMessage("Notes Deleted succesfully", HttpStatus.OK); 
	 }
	 
	 
	 @DeleteMapping("/recyclebin-deleteAll/{id}") 
     public ResponseEntity<?> recycleBinAllDeleteNotes(@PathVariable Integer id) throws ResourceNotFoundException {
	 notesService.recycleBinAllDeleteNotes(id); 
	 return CommonUtil.createBuildResponseMessage("All Notes Deleted succesfully in recycleBin", HttpStatus.OK); 
	 }
	 
	 
	 @PostMapping("/fav-note/{notesid}")
	 public ResponseEntity<?> addFavoriteNote(@PathVariable Integer notesid) throws ResourceNotFoundException{
		 notesService.favoritesNotes(notesid);
		 return CommonUtil.createBuildResponseMessage("Favorite Added Succesfully", HttpStatus.OK);
	 }
	 
	 @DeleteMapping("/unfav-note/{favnotesid}")
	 public ResponseEntity<?> unfavoriteNote(@PathVariable Integer favnotesid) throws ResourceNotFoundException{
		 notesService.unfavoriteNotes(favnotesid);
		 return CommonUtil.createBuildResponseMessage("Notes gets unfavorite Successfully", HttpStatus.OK);
		 
	 }
	 
	 @GetMapping("/fav-note")
	 public ResponseEntity<?> getAllFavoriteNotes() throws ResourceNotFoundException{
		List<FavoriteNotesDto> favoriteNotesDtos = notesService.getFavoriteNoteByUser();
		if(org.springframework.util.CollectionUtils.isEmpty(favoriteNotesDtos)) {
			return ResponseEntity.noContent().build();
		}
		 return CommonUtil.createBuildResponse(favoriteNotesDtos, HttpStatus.OK); 
	 }
	 
	 
	 @GetMapping("/copy/{id}")
	 public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws ResourceNotFoundException{
		 
		 if(notesService.copyNotes(id)) {
			 return CommonUtil.createBuildResponseMessage("Copied Successfully", HttpStatus.OK);
		 } else {
			 return CommonUtil.createErrorResponseMessage("Did not copy due to internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 
	 }
	 
}
