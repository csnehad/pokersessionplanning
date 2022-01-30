package com.pokerrestapi.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokerrestapi.constant.PlanningPokerConstant;
import com.pokerrestapi.entity.Member;
import com.pokerrestapi.entity.Session;
import com.pokerrestapi.entity.UserStory;
import com.pokerrestapi.exception.SessionNotFoundException;
import com.pokerrestapi.repository.MemberRepository;
import com.pokerrestapi.repository.SessionRepository;
import com.pokerrestapi.service.SessionService;

@Service
public class SessionServiceImpl implements SessionService {


	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	MemberRepository memberRepository;


	@Override 
	public Session createSession(Session pokerSession,String	memberName) {
		Session ps = null; 
		//search with title in in memory database
		//if exists then return the session; else create a new session
		if(pokerSession.getTitle()!=null &&
				sessionRepository.existsByTitle(pokerSession.getTitle())!=null) {

			ps = sessionRepository.existsByTitle(pokerSession.getTitle()); 

		}else { 
			//creating new session if not there
			Random random = new Random(); 
			int sessionVal = random.nextInt(10000);

			String session = String.valueOf(sessionVal);
			pokerSession.setSessionId(session); 
			//creating a member
			Member member = new	Member(memberName,PlanningPokerConstant.NOT_VOTED);

			Set<Member> members = new HashSet<Member>();
			members.add(member); 
			//Adding member to session and setting voting status as not voted
			pokerSession.setMembers(members);
			pokerSession.getTitle();
			pokerSession.setActive(true);
			ps = sessionRepository.save(pokerSession);
		}

		return ps;
	}


	@Override
	public List<Session> getAllSessions() {
		return sessionRepository.findAll();
	}
	
	@Override
	public List<Session> getAllActiveSessions() {
		List<Session> sessions= sessionRepository.findByStatus(true);
		return sessions;
	}

	@Override
	public Optional<Session> getSessionBySessionId(String sessionId) {
		//return Optional.ofNullable(sessionRepository.getPokerSessionBySessionId(sessionId));
		return sessionRepository.findById(sessionId);
	}


	@Override 
	public String getTitleOfTheSession(String sessionId) {
		Session pokerSession =  sessionRepository.getById(sessionId);
		return pokerSession.getTitle(); 
	}


	@Override
	public List<Member> addUpdateMemberList(String sessionId, String memberName) {
		
		Session pokerSession = sessionRepository.getById(sessionId);
		if(pokerSession!=null && !pokerSession.isActive()) {
			throw new SessionNotFoundException("Session is inactive");
		}
		//creating new member
		Member member = new Member(memberName,PlanningPokerConstant.NOT_VOTED);
		member.setSession(pokerSession);
		//add current member name to member list
		memberRepository.save(member);
		//get all member list
		Set<Member> memberSet = pokerSession.getMembers();
		//new member is not getting added so added this line
		//memberSet.add(member);
		List<Member> memberList = new ArrayList<Member>();
		memberList.addAll(memberSet);
		return memberList;
	}

	@Override
	public void destroySession(String sessionId) {
		Session session= sessionRepository.getById(sessionId);
		session.setActive(false);
		//sessionRepository.deleteById(sessionId);
		sessionRepository.save(session);

	}

	@Override
	public List<UserStory> getUserStoryListInSession(String sessionId) {
		Session pokerSession = sessionRepository.getById(sessionId);
		Set<UserStory> userStories = pokerSession.getUserStories();
		List<UserStory> userStoryList = new ArrayList<UserStory>();
		userStoryList.addAll(userStories);
		return userStoryList;
	}

	@Override
	public List<Member> getMembersInSession(String sessionId) {
		Session pokerSession = sessionRepository.getById(sessionId);
		Set<Member> members = pokerSession.getMembers();
		List<Member> memberList = new ArrayList<Member>();
		memberList.addAll(members);
		return memberList;
	}

}
