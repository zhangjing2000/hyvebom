package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.TagType;

class HyveGroupTagLog extends HyveGroupChangeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HyveGroupTagLog(UUID logGroupID, int logUserID, Date logDate,
			GroupChangeLogType logType, String logComment,
			TagType tagType, String tagValue) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	
	private TagType tagType;
	private String tagValue;

	TagType getTagType() {
		return tagType;
	}
	
	String getTagValue() {
		return tagValue;
	}

	@Override
	public String toString() {
		return new StringBuilder("LineChangeLog")
			.append(super.toString())
			.append(",tagType:").append(tagType)
			.append(",tagValue:").append(tagValue)
			.toString();
	}
}
