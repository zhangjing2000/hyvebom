package com.hyve.bom.concept;

import java.util.UUID;

public class HyveProductGroupMember implements Comparable<HyveProductGroupMember>{
	private final UUID groupID;
	private final int lineNo;
	private final String lineComment;
	private final MemberType memberType;
	private final UUID subGroupID;
	private final int skuNo;
	private final int minBOMQty;
	private final int maxBOMQty;
	
	public HyveProductGroupMember(UUID groupID, int lineNo, String lineComment,
			MemberType memberType, UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty) {
		super();
		this.groupID = groupID;
		this.lineNo = lineNo;
		this.lineComment = lineComment;
		this.memberType = memberType;
		this.subGroupID = subGroupID;
		this.skuNo = skuNo;
		this.minBOMQty = minBOMQty;
		this.maxBOMQty = maxBOMQty;
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
	public UUID getSubGroupID() {
		return subGroupID;
	}
	public int getSkuNo() {
		return skuNo;
	}
	public int getMinBOMQty() {
		return minBOMQty;
	}
	public int getMaxBOMQty() {
		return maxBOMQty;
	}
	public int compareTo(HyveProductGroupMember m1) {
		return lineNo - m1.getLineNo();
	}
	public String toString() {
		return new StringBuilder("")
		.append("line#:").append(lineNo)
		.append(",member type:").append(memberType)
		.append(",lineComment:").append(lineComment)
		.append(",sub group:").append(subGroupID)
		.append(",skuNo:").append(skuNo)
		.append(",min_BOM_qty:").append(minBOMQty)
		.append(",max_BOM_qty:").append(maxBOMQty)
		.toString();
	}
}
