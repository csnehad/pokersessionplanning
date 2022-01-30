package com.pokerrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pokerrestapi.dto.AddUserStoryRequest;
import com.pokerrestapi.entity.UserStory;
import com.pokerrestapi.exception.UserStoryDeletionNotPermitted;
import com.pokerrestapi.exception.UserStoryNotFoundException;
import com.pokerrestapi.service.UserStoryService;

@RestController
@RequestMapping("/planningPoker/userstory")
public class UserStoryController {

	
	@Autowired
	UserStoryService userStoryService;
	
	
	@PostMapping("/addUserStory")
	public ResponseEntity<UserStory> addUserStory(@Validated @RequestBody AddUserStoryRequest uStoryReq) {
		try {
			UserStory userStory = new UserStory(uStoryReq.getSessionId(), uStoryReq.getDescription(), 0);
			UserStory uStory = userStoryService.addUserStory(userStory,uStoryReq.getSessionId());
			return new ResponseEntity<>(uStory, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteUserStory")
	public void deleteUserStory(@RequestParam String uStoryId) {
		try {
			userStoryService.deleteUserStory(uStoryId);
		} catch (Exception e) {
			
			throw new UserStoryDeletionNotPermitted(" Exception Occured during delete userstory  "+uStoryId);
		}
	}
	
	@GetMapping("/getUserStory")
	public UserStory getUserStory(@RequestParam String uStoryId) {
		try {
			UserStory userStory = userStoryService.getUserStory(uStoryId);
			
			if(userStory == null)
				throw new UserStoryNotFoundException(" UserStory not found for user story id: "+uStoryId);
			return userStory;
			
		} catch (Exception e) {
			throw new UserStoryNotFoundException(" UserStory not found for user story id: "+uStoryId);
		}
		
	}
	
		
	@GetMapping("/getAllUserStories")
	public ResponseEntity<List<UserStory>> getAllUserStories() {
		try {
			List<UserStory> userStories = userStoryService.getAllUserStories();


			if (userStories.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(userStories, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
