package com.pokerrestapi.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokerrestapi.constant.PlanningPokerConstant;
import com.pokerrestapi.entity.Member;
import com.pokerrestapi.entity.MemberUserStory;
import com.pokerrestapi.entity.Session;
import com.pokerrestapi.entity.UserStory;
import com.pokerrestapi.entity.VotingStatus;
import com.pokerrestapi.exception.AlreadyVotedException;
import com.pokerrestapi.exception.SessionNotFoundException;
import com.pokerrestapi.repository.MemberRepository;
import com.pokerrestapi.repository.MemberUserStoryRepository;
import com.pokerrestapi.repository.SessionRepository;
import com.pokerrestapi.repository.UserStoryRepository;
import com.pokerrestapi.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	UserStoryRepository userStoryRepository;

	@Autowired
	MemberUserStoryRepository memberUserStoryRepository;

	@Override
	public UserStory startuserStoryVoting(String uStoryId) {
		UserStory userStory = userStoryRepository.getById(uStoryId);
		userStory.setVotingStatus(VotingStatus.VOTING);
		userStoryRepository.save(userStory);
		return userStory;
	}

	@Override
	public int submitUserStoryVote(String uStoryId,String memberId,String sessionId,Integer votePoint) {
		Session session= sessionRepository.getById(sessionId);
		if(session!=null && !session.isActive()) {
			throw new SessionNotFoundException("Session is inactive");
		}
		//get userstory by uStoryId
		UserStory uStory = userStoryRepository.getById(uStoryId);
		//get count of user story
		int count = uStory.getCount();
		Member member = memberRepository.getById(memberId);		
		String memberStatus = member.getMemberStatus();
		//	List<MemberUserStory> list=memberUserStoryRepository.findByUserStoryId(uStoryId);

		/*
		 * for(MemberUserStory muStory:list) { Member member1=muStory.getMember();
		 * if(member1.getMemberId().equalsIgnoreCase(memberId)){ //if member is voted
		 * and trying to vote second time then throws exception member already voted
		 * if(muStory.isVoted()){ throw new
		 * MemberAlreadyVotedException("Member already voted"); } } }
		 */

		//if userStory Status is VOTED then throw exception with message No More Vote Accepted
		if(uStory.getVotingStatus()!=null && uStory.getVotingStatus().compareTo(VotingStatus.VOTED)==0) {
			throw new AlreadyVotedException("Voting is not Available");
		}else if(uStory.getVotingStatus()!=null && uStory.getVotingStatus().compareTo(VotingStatus.PENDING)==0){
			//change the status to VOTING
			uStory.setVotingStatus(VotingStatus.VOTING);
		}


		//increment count by 1
		count = count+1;
		uStory.setCount(count);

		//update member status to VOTED from NOT_VOTED
		if(memberStatus != null && memberStatus.equalsIgnoreCase(PlanningPokerConstant.NOT_VOTED)) {
			member.setMemberStatus(PlanningPokerConstant.VOTED);
		}
		//getting details of member, userstory and votepoint
		MemberUserStory memberUstory = new MemberUserStory();
		memberUstory.setMember(member);
		memberUstory.setUserStory(uStory);
		memberUstory.setVotePoint(votePoint);
		//memberUstory.setVoted(true);
		memberUserStoryRepository.save(memberUstory);
		return count;
	}

	@Override
	public List<Member> showMemberVotingStatus() {
		//get all members with status
		return memberRepository.findAll();
	}

	@Override
	public List<UserStory> showUserStoryVotingStatus() {
		// get all userStories
		return userStoryRepository.findAll();
	}


	@Override
	public List<MemberUserStory> stopuserStoryVoting(String uStoryId) {
		// update userstory status to VOTED
		UserStory userStory = userStoryRepository.getById(uStoryId); 
		userStory.setVotingStatus(VotingStatus.VOTED);
		userStoryRepository.save(userStory);
		//fetch MemberUserStory against a uStoryId
		List<MemberUserStory> memberUserStoryList = memberUserStoryRepository.findByUserStoryId(uStoryId);
		return memberUserStoryList;
	}

	@Override
	public int getVoteCountForUserStory(String uStoryId) {
		UserStory userStory = userStoryRepository.getById(uStoryId); 

		int voteCount = userStory.getCount();
		return voteCount;
	}

	@Override
	public int getHighestVoteForUserStory(String uStoryId) {

		UserStory userStory = userStoryRepository.getById(uStoryId);

		Set<MemberUserStory> memberUserStories = userStory.getMemberUstory();
		List<MemberUserStory> memberUserStoryList = new ArrayList<MemberUserStory>();
		memberUserStoryList.addAll(memberUserStories);

		Optional<MemberUserStory> memberUStory = memberUserStoryList.stream().sorted(Comparator.comparingInt(MemberUserStory::getVotePoint).reversed()).findFirst();
		int voteFinalResult = memberUStory.get().getVotePoint();
		return voteFinalResult;
	}



}
