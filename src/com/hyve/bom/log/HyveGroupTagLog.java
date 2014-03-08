package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupTagType;

public class HyveGroupTagLog extends HyveGroupChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HyveGroupTagLog(UUID logGroupID, int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			HyveProductGroupTagType tagType, String tagValue) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	
	private HyveProductGroupTagType tagType;
	private String tagValue;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public HyveProductGroupTagType getTagType() {
		return tagType;
	}
	
	public String getTagValue() {
		return tagValue;
	}
}
