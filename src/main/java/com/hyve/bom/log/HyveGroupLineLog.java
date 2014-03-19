package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;

class HyveGroupLineLog extends HyveGroupChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int line;
	private MemberType logMemberType;
	private String lineComment;
	private UUID subGroupID;
	private int skuNo;
	private int minBOMQty;
	private int maxBOMQty;
	
	HyveGroupLineLog(UUID logGroupID,  int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			int line, MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.line = line;
		this.logMemberType = logMemberType;
		this.lineComment = lineComment;
		this.subGroupID = subGroupID;
		this.skuNo = skuNo;
		this.minBOMQty = minBOMQty;
		this.maxBOMQty = maxBOMQty;
	}
	int getLine() {
		return line;
	}
	
	MemberType getLogMemberType() {
		return logMemberType;
	}
	
	String getLineComment() {
		return lineComment;
	}
	
	UUID getSubGroupID() {
		return subGroupID;
	}
	
	int getSkuNo() {
		return skuNo;
	}
	
	int getMinBOMQty() {
		return minBOMQty;
	}
	
	int getMaxBOMQty() {
		return maxBOMQty;
	}

	@Override
	public String toString() {
		return new StringBuilder("LineChangeLog")
			.append(super.toString())
			.append(",line#:").append(line)
			.append(",memberType:").append(logMemberType)
			.append(",lineComment:").append(lineComment)
			.append(",sub group:").append(subGroupID)
			.append(",sku_no:").append(skuNo)
			.append(",min_BOM_qty:").append(minBOMQty)
			.append(",max_BOM_qty:").append(maxBOMQty)
			.toString();
	}
	
	HyveProductGroupMember toHyveGroupProductDetail() {
		return new HyveProductGroupMember(getLogGroupID(), line, lineComment, logMemberType, subGroupID, skuNo, minBOMQty, maxBOMQty);
	}
}
