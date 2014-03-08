package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveAlternativeGroupDetail;
import com.hyve.bom.concept.HyveAssemblyGroupDetail;
import com.hyve.bom.concept.HyveProductGroupDetail;
import com.hyve.bom.concept.HyveProductGroupMemberType;
import com.hyve.bom.concept.HyveProductGroupType;

public class HyveGroupLineLog extends HyveGroupChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int line;
	private HyveProductGroupMemberType logMemberType;
	private String lineComment;
	private UUID subGroupID;
	private int skuNo;
	private int minBOMQty;
	private int maxBOMQty;
	
	public HyveGroupLineLog(UUID logGroupID,  int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			int line, HyveProductGroupMemberType logMemberType, String lineComment,
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
	public int getLine() {
		return line;
	}
	
	public HyveProductGroupMemberType getLogMemberType() {
		return logMemberType;
	}
	public String getLineComment() {
		return lineComment;
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
	
	public HyveProductGroupDetail toHyveGroupProductDetail() {
		if (logMemberType == HyveProductGroupMemberType.ASSEMBLY_SUB_GROUP) {
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
				public HyveProductGroupMemberType getMemberType() {
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
				public HyveProductGroupMemberType getMemberType() {
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
