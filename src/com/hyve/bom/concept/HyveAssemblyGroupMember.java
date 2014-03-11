package com.hyve.bom.concept;

import java.util.UUID;

public class HyveAssemblyGroupMember extends HyveProductGroupMember {
	private final UUID subGroupID;
	private final int minBOMQty;
	private final int maxBOMQty;
	
	public HyveAssemblyGroupMember(UUID groupID, int lineNo,
			String linecomment, MemberType memberType, UUID subGroupID,
			int minBOMQty, int maxBOMQty) {
		super(groupID, lineNo, linecomment, memberType);
		this.subGroupID = subGroupID;
		this.minBOMQty = minBOMQty;
		this.maxBOMQty = maxBOMQty;
	}

	public UUID getSubGroupID() {
		return subGroupID;
	}

	public int getMinBOMQty() {
		return minBOMQty;
	}

	public int getMaxBOMQty() {
		return maxBOMQty;
	}
}
