package com.crazycoder.serviceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.crazycoder.dto.FavoriteNotesDto;
import com.crazycoder.dto.NoteByPageDto;
import com.crazycoder.dto.NotesDto;
import com.crazycoder.dto.NotesDto.FileDto;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.model.FavoriteNotes;
import com.crazycoder.model.FileDetails;
import com.crazycoder.model.Notes;
import com.crazycoder.repository.CategoryRepository;
import com.crazycoder.repository.FavoriteNotesRepository;
import com.crazycoder.repository.FileDetailsRepository;
import com.crazycoder.repository.NotesRepository;
import com.crazycoder.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.model.Model;

@Service
public class NotesServiceImpl implements NotesService{
	
	@Autowired
	private FavoriteNotesRepository favoriteNotesRepository;
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private  FileDetailsRepository fileDetailsRepository;
    
	@Value("${file.upload.path}")
	private String uploadpath;
	
	@Override
	public Boolean saveNotes(String notesDto, MultipartFile file) throws ResourceNotFoundException, IOException {
		//Now notes came in String format we need to convert in NotesDto using ObjectMapper 
          ObjectMapper objectMapper=new ObjectMapper(); // 
          NotesDto notesDto2=objectMapper.readValue(notesDto,NotesDto.class);
          
          notesDto2.setIsDeleted(false);
          notesDto2.setDeletedOn(null);
          
          //updated notes 
          if(!ObjectUtils.isEmpty(notesDto2.getId())) {
        	  updateNotes(notesDto2,file);
          }
	
		//Validation check whether category is exists is not in category table
		categoryRepository.findByIdAndIsDeletedFalse(notesDto2.getCategory().getId())
		                  .orElseThrow(()-> new ResourceNotFoundException("Category id not exist"));
		
		Notes note=modelMapper.map(notesDto2, Notes.class);
		
		
		//File store in local file and file object is created
		FileDetails fileDetails=saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDetails)) {
		    note.setFilesDetails(fileDetails);
		} else {
			if(ObjectUtils.isEmpty(notesDto2.getId())) {
				note.setFilesDetails(null);
			}
			
		}
		
		Notes savedNotes=notesRepository.save(note);
		if(ObjectUtils.isEmpty(savedNotes)) {
			return false;
		} else {
			return true;
		}
	}

	private void updateNotes(NotesDto notesDto2, MultipartFile file) throws ResourceNotFoundException {
		Notes exNotes=notesRepository.findById(notesDto2.getId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));
		
		if(ObjectUtils.isEmpty(file)) {
			notesDto2.setFilesDetails(modelMapper.map(exNotes.getFilesDetails(), FileDto.class));
		}
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			
			//FileExtension check whether its correct file received or not.
			List<String> extensionList=Arrays.asList("xlsx","docs","png","pdf","pptx");
			
			
			String extension=FilenameUtils.getExtension(file.getOriginalFilename());
			
			if(!extensionList.contains(extension)) {
				throw new IllegalArgumentException("File Extension only allowed [pdf,xlsx,docs,png,pptx]");
			}
			
			//create random string for uploaded filename
			String randomFile=UUID.randomUUID().toString();
			String uploadFileName=randomFile+"."+extension;
			
			File pathFile=new File(uploadpath);
			
			if(!pathFile.exists()) {
				pathFile.mkdir();
			}
			
			String storePath=uploadpath.concat(uploadFileName);
			
			//uploadFile;
			long uploadInLocalfile=Files.copy(file.getInputStream(),Paths.get(storePath));
			
			if(uploadInLocalfile!=0) {
				FileDetails fileDetails=new FileDetails();
				fileDetails.setOriginalFileName(file.getOriginalFilename());
				fileDetails.setDisplayFileName(displayFileName(file.getOriginalFilename()));
				fileDetails.setFileSize(file.getSize());
				fileDetails.setUploadFileName(uploadFileName);
				fileDetails.setPath(storePath);
				FileDetails savedFile=fileDetailsRepository.save(fileDetails);
				return savedFile;	
			}
		}
		return null;
	}

	private String displayFileName(String file) {
		String extension=FilenameUtils.getExtension(file);
		String filename=FilenameUtils.removeExtension(file);
		
		if(filename.length()>8) {
			filename=filename.substring(0,7);
		}
		
	   return filename+"."+extension;
	}

	@Override
	public List<NotesDto> getAllNotes() {
       return notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();              
	}

	
	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws ResourceNotFoundException, IOException {
		
		InputStream inputStream=new FileInputStream(fileDetails.getPath());
		
		return StreamUtils.copyToByteArray(inputStream);
		
	}

	@Override
	public FileDetails getFileDetils(Integer id) throws ResourceNotFoundException {
		FileDetails fileDetails = fileDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("File not Found in database"));
		return fileDetails;
	}

	public NoteByPageDto getAllNotesByUser(Integer user_id,Integer pageNo, Integer pageSize ) {
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Notes> listOfNotes = notesRepository.findByCreatedByAndIsDeletedFalse(user_id,pageable);
		
		List<NotesDto> notesDtos = listOfNotes.stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();
		
		NoteByPageDto notes = NoteByPageDto.builder()
				 .notesDtos(notesDtos)
				 .pageNo(listOfNotes.getNumber()) 
				 .pageSize(listOfNotes.getSize())
				 .totalElements(listOfNotes.getTotalElements())
				 .totalPages(listOfNotes.getTotalPages())
				 .isFirst(listOfNotes.isFirst())
				 .isLast(listOfNotes.isLast())
				 .build();
		return notes;
	}
	
	 @Override
	 public void softDeleteNotes(Integer id) throws ResourceNotFoundException {
	 
	  Notes notes = notesRepository.findByIdAndIsDeletedFalse(id) .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid or not found"));
	  notes.setIsDeleted(true);
	  notes.setDeletedOn(LocalDateTime.now());
	  notesRepository.save(notes); 
	  }
	
	  @Override 
	  public void restoreNotes(Integer id) throws ResourceNotFoundException { 
	    Notes notes=notesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes id invalid or not found"));
		 notes.setIsDeleted(false);
		 notes.setDeletedOn(null);
		 notesRepository.save(notes);
	  }
	 

	 @Override
	 public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
	 List<Notes> notes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
	 List<NotesDto> notesDtos = notes.stream().map(note -> modelMapper.map(note,NotesDto.class)).toList(); 
	 return notesDtos;
	 
	 }

	@Override
	public void hardDeleteNotes(Integer id) throws ResourceNotFoundException {
	
   Notes notes = notesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes id not present in database"));
   
    if(notes.getIsDeleted()) {
    	notesRepository.delete(notes);
    } else {
    	throw new IllegalArgumentException("You can not perform hard delete");
    }
		
	}

	@Override
	public void recycleBinAllDeleteNotes(Integer id) {
		
		List<Notes> recyclebinDeletedNotes= notesRepository.findByCreatedByAndIsDeletedTrue(id);
		
		if(!CollectionUtils.isEmpty(recyclebinDeletedNotes)) {
			notesRepository.deleteAll(recyclebinDeletedNotes);
		} 
		
	}

	@Override
	public void favoritesNotes(Integer NotesId) throws ResourceNotFoundException {
		
		Integer userid=2;
	    Notes notes = notesRepository.findById(NotesId).orElseThrow(() -> new ResourceNotFoundException("Notes is not Found by given id"));
	    
	    FavoriteNotes favoriteNotes = FavoriteNotes.builder().note(notes).userId(userid).build();
	    
	    favoriteNotesRepository.save(favoriteNotes);
	
	}

	@Override
	public void unfavoriteNotes(Integer favoriteNotesId) throws ResourceNotFoundException {
		FavoriteNotes favoriteNotes = favoriteNotesRepository.findById(favoriteNotesId)
				.orElseThrow(() -> new ResourceNotFoundException("Favorite Notes is not found"));
		
		favoriteNotesRepository.deleteById(favoriteNotesId);
		
	}

	@Override
	public List<FavoriteNotesDto> getFavoriteNoteByUser() {
		Integer userId=2;
		
		List<FavoriteNotesDto> favoriteNotesDtos = favoriteNotesRepository.findByUserId(userId).stream()
				                                   .map(fav -> modelMapper.map(fav, FavoriteNotesDto.class)).toList();
		return favoriteNotesDtos;
	}
	
	
	@Override
	public Boolean copyNotes(Integer id) throws ResourceNotFoundException {
		Notes notes = notesRepository.findById(id)
				                 .orElseThrow( () -> new ResourceNotFoundException("Notes is not found in database"));
		
		Notes copyNote = Notes.builder()
				.title(notes.getTitle())
				.description(notes.getDescription())
				.category(notes.getCategory())
				.isDeleted(false)
				.filesDetails(null)
				.build();
		//TODO :: Need to check user validation as side JWT
		Notes saveCopyNote = notesRepository.save(copyNote);
		
		if(ObjectUtils.isEmpty(saveCopyNote)) {
			return false;
		} else {
			return true;
		}	
	}
}
