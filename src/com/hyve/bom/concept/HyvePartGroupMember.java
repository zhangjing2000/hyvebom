package com.hyve.bom.concept;

import java.util.UUID;

public class HyvePartGroupMember extends HyveProductGroupMember {
	private final int skuNo;

	public HyvePartGroupMember(UUID groupID, int lineNo, String linecomment,
			MemberType memberType, int skuNo) {
		super(groupID, lineNo, linecomment, memberType);
		this.skuNo = skuNo;
	}

	public int getSkuNo() {
		return skuNo;
	}
}
