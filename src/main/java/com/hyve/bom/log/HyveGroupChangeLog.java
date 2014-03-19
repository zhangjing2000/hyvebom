package com.hyve.bom.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.hyve.log.common.LogEntry;

public abstract class HyveGroupChangeLog implements java.io.Serializable, Comparable<HyveGroupChangeLog>, LogEntry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final UUID logID;
	private final UUID logGroupID;
	private final int logUserID;
	private final Date logDate;
	private final GroupChangeLogType logType;
	private final String logComment;
	
	public HyveGroupChangeLog(UUID logGroupID, int logUserID, Date logDate, GroupChangeLogType logType, String logComment) {
		super();
		this.logID = UUID.randomUUID();
		this.logGroupID = logGroupID;
		this.logUserID = logUserID;
		this.logDate = logDate;
		this.logType = logType;
		this.logComment = logComment;
	}

	public UUID getLogID() {
		return logID;
	}
	public UUID getLogGroupID() {
		return logGroupID;
	}

	public int getLogUserID() {
		return logUserID;
	}

	public Date getLogDate() {
		return logDate;
	}

	public GroupChangeLogType getLogType() {
		return logType;
	}

	public String getLogComment() {
		return logComment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logID == null) ? 0 : logID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HyveGroupChangeLog other = (HyveGroupChangeLog) obj;
		if (logID == null) {
			if (other.logID != null)
				return false;
		} else if (!logID.equals(other.logID))
			return false;
		return true;
	}

	@Override
	public int compareTo(HyveGroupChangeLog arg0) {
		if(logDate.equals(arg0.getLogDate()))
			return logID.compareTo(arg0.logID);
		else 
			return logDate.compareTo(arg0.getLogDate());
	}

	@Override
	public String toString() {
		return new StringBuilder("")
		.append(",logDate:").append((new SimpleDateFormat("MM/dd/yyyy")).format(logDate))
		.append(",logType:").append(logType)
		.append(",logComment:").append(logComment)
		.toString();
		
	}
}
