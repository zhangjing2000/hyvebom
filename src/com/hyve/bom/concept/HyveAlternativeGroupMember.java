package com.hyve.bom.concept;

import java.util.UUID;

public class HyveAlternativeGroupMember extends HyveProductGroupMember {
	private UUID subGroupID;

	public HyveAlternativeGroupMember(UUID groupID, int lineNo,
			String linecomment, MemberType memberType, UUID subGroupID) {
		super(groupID, lineNo, linecomment, memberType);
		this.subGroupID = subGroupID;
	}

	public UUID getSubGroupID() {
		return subGroupID;
	}
}
