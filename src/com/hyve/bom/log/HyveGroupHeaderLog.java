package com.hyve.bom.log;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupType;

public class HyveGroupHeaderLog extends HyveGroupChangeLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String groupName;
	private final HyveProductGroupType groupType;
	
	public HyveGroupHeaderLog(UUID logGroupID, int logUserID, Date logDate, 
			GroupChangeLogType logType, String logComment, 
			String groupName, HyveProductGroupType groupType) {
		super(logGroupID, logUserID, logDate, logType, logComment);
		this.groupName = groupName;
		this.groupType = groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	public HyveProductGroupType getGroupType() {
		return groupType;
	}

}
