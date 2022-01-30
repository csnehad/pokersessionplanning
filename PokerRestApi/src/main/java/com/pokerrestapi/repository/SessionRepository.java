package com.pokerrestapi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pokerrestapi.entity.Session;

public interface SessionRepository extends JpaRepository<Session, String> {

	@Query(value = "SELECT ps from Session ps where ps.title=?1")
	Session existsByTitle(String title);

	//@Query(value = "SELECT ps from Session ps where ps.sessionId=?1")
	//Session getPokerSessionBySessionId(String sessionId);
	@Query(value="Select ps from Session ps where ps.isActive=?1")
	List<Session> findByStatus(Boolean isActive);

}
