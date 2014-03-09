package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveAlternativeGroupDetail;
import com.hyve.bom.concept.HyveAssemblyGroupDetail;
import com.hyve.bom.concept.HyveProductGroupDetail;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.GroupType;

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
	
	HyveProductGroupDetail toHyveGroupProductDetail() {
		if (logMemberType == MemberType.ASSEMBLY_SUB_GROUP) {
			return new HyveAssemblyGroupDetail() {
				public UUID getGroupID() {
					return getLogGroupID();
				}
				public int getLineNo() {
					return line;
				}
				public String getLineComment() {
					return lineComment;
				}
				public MemberType getMemberType() {
					return logMemberType;
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
			};
		} else {
			return new HyveAlternativeGroupDetail() {

				@Override
				public UUID getGroupID() {
					return getLogGroupID();
				}

				@Override
				public int getLineNo() {
					return line;
				}

				@Override
				public String getLineComment() {
					return lineComment;
				}

				@Override
				public MemberType getMemberType() {
					return logMemberType;
				}

				@Override
				public UUID getSubGroupID() {
					return subGroupID;
				}

				@Override
				public int getSkuNo() {
					return skuNo;
				}
				
			};
		}
	}
}
