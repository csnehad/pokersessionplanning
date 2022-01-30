package com.pokerrestapi.service;

import java.util.List;
import java.util.Optional;

import com.pokerrestapi.entity.Member;
import com.pokerrestapi.entity.Session;
import com.pokerrestapi.entity.UserStory;

public interface SessionService {

	Session createSession(Session session,String memberName);

	List<Session> getAllSessions();

	Optional<Session> getSessionBySessionId(String sessionId);

	String getTitleOfTheSession(String sessionId);

	List<Member> addUpdateMemberList(String sessionId, String memberName);

	void destroySession(String sessionId);

	List<UserStory> getUserStoryListInSession(String sessionId);

	List<Member> getMembersInSession(String sessionId);
	
	List<Session> getAllActiveSessions();


}
