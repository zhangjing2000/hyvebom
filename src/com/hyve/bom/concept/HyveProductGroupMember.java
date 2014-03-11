package com.hyve.bom.concept;

import java.util.UUID;

public abstract class HyveProductGroupMember implements Comparable<HyveProductGroupMember>{
	private final UUID groupID;
	private final int lineNo;
	private final String lineComment;
	private final MemberType memberType;
	public HyveProductGroupMember(UUID groupID, int lineNo, String lineComment,
			MemberType memberType) {
		super();
		this.groupID = groupID;
		this.lineNo = lineNo;
		this.lineComment = lineComment;
		this.memberType = memberType;
	}
	public UUID getGroupID() {
		return groupID;
	}
	public int getLineNo() {
		return lineNo;
	}
	public String getLineComment() {
		return lineComment;
	}
	public MemberType getMemberType() {
		return memberType;
	}
	public int compareTo(HyveProductGroupMember m1) {
		return lineNo - m1.getLineNo();
	}
}
