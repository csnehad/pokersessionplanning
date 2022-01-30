package com.pokerrestapi.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokerrestapi.constant.PlanningPokerConstant;
import com.pokerrestapi.entity.Session;
import com.pokerrestapi.entity.UserStory;
import com.pokerrestapi.entity.VotingStatus;
import com.pokerrestapi.exception.UserStoryDeletionNotPermitted;
import com.pokerrestapi.repository.SessionRepository;
import com.pokerrestapi.repository.UserStoryRepository;
import com.pokerrestapi.service.UserStoryService;

@Service
public class UserStoryServiceImpl implements UserStoryService {


	@Autowired
	UserStoryRepository userStoryRepository;



	@Autowired
	SessionRepository sessionRepository;

	public UserStory addUserStory(UserStory userStory,String sessionId) {
		Session pokerSession = sessionRepository.getById(sessionId);
		userStory.setCount(0);
		userStory.setVotingStatus(VotingStatus.PENDING);
		userStory.setSession(pokerSession);
		//userStory.setVoteStatus(voteStatus);
		//voteStatus.setUserStory(userStory);
		return userStoryRepository.save(userStory);
	}

	@Override
	public void deleteUserStory(String uStoryId) {
		//check if status is pending then delete
		UserStory uStory = userStoryRepository.getById(uStoryId);
		if(uStory.getVotingStatus() != null && 
				uStory.getVotingStatus().toString().equals(PlanningPokerConstant.PENDING)) {
			userStoryRepository.deleteById(uStoryId);
		}else {
			//throw exception with message can not delete 
			throw new UserStoryDeletionNotPermitted("Delete Option not available for user story :"+uStoryId);
		}


	}

	@Override
	public List<UserStory> getAllUserStories() {
		List<UserStory> userStories = userStoryRepository.findAll();
		return userStories;
	}

	@Override
	public UserStory getUserStory(String uStoryId) {
		return userStoryRepository.getUserStory(uStoryId);
		//return userStoryRepository.getById(uStoryId);
	}

}
