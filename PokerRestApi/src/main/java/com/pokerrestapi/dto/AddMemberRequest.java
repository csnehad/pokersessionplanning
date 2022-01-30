package com.pokerrestapi.dto;

//@Getter @Setter
public class AddMemberRequest {

	String sessionId;
	public String getSessionId() {
		return sessionId;
	}
	public void setSession(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	String memberName;
	
}
