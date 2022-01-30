package com.pokerrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pokerrestapi.dto.SubmitVoteRequest;
import com.pokerrestapi.entity.Member;
import com.pokerrestapi.entity.MemberUserStory;
import com.pokerrestapi.entity.UserStory;
import com.pokerrestapi.service.VoteService;

@RestController
@RequestMapping("/planningPoker/voting")
public class VoteController {


	@Autowired
	VoteService voteService;
	
	@PostMapping("/startuserStoryVoting")
	public UserStory startuserStoryVoting(@RequestParam String uStoryId) {
		return voteService.startuserStoryVoting(uStoryId);
	}

	@PostMapping("/submitUserStoryVote")
	public int submitUserStoryVote( @Validated @RequestBody SubmitVoteRequest req ) {

		return voteService.submitUserStoryVote(req.getuStoryId(),req.getMemberId(),req.getSessionId(),req.getVotePoint());
	}

	@GetMapping("/showMemberVotingStatus")
	public List<Member> showMemberVotingStatus() {

		return voteService.showMemberVotingStatus();
	}

	@GetMapping("/showUserStoryVotingStatus")
	public List<UserStory> showUserStoryVotingStatus() {

		return voteService.showUserStoryVotingStatus();
	}

	@PostMapping("/stopuserStoryVoting")
	public List<MemberUserStory> stopuserStoryVoting(@RequestParam String uStoryId) {
		return voteService.stopuserStoryVoting(uStoryId);
	}

	@GetMapping("/getVoteCountForUserStory")
	public int getVoteCountForUserStory(@RequestParam String uStoryId) {
		return voteService.getVoteCountForUserStory(uStoryId);
	}

	@GetMapping("/getHighestVoteForUserStory")
	public int getHighestVoteForUserStory(@RequestParam String uStoryId) {
		return voteService.getHighestVoteForUserStory(uStoryId);
	}
}
