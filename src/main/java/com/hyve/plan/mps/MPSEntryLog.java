package com.hyve.plan.mps;

import java.util.Date;
import java.util.UUID;

import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.PlanEntryLog;
import com.hyve.plan.concept.PlanEntryLogType;

public class MPSEntryLog extends MPSEntry implements PlanEntryLog<MPSEntry> {

	private final UUID logID;
	private final PlanEntryLogType logType;
	private final int logUserID;
	private final Date logDate;
	private final String logComment;
	
	public MPSEntryLog(PlanEntryLogType logType, MPSEntry mpsEntry, LogEntry logEntry) {
		this(logType, mpsEntry.getPlanDate(), mpsEntry.getDeliveryDate(),
				mpsEntry.getPlanLocation(), 
				mpsEntry.getPlanQty(), mpsEntry.getCustBOM(), 
				logEntry.getLogUserID(), logEntry.getLogDate(), logEntry.getLogComment());
	}
	public MPSEntryLog(PlanEntryLogType logType, Date startDate, Date deliveryDate, int plantLocNo, int deliveryQty,
			UUID custBOM, int logUserID, Date logDate, String logComment) {
		super(startDate, deliveryDate, plantLocNo, deliveryQty, custBOM);
		this.logID = UUID.randomUUID();
		this.logType = logType;
		this.logUserID = logUserID;
		this.logDate = logDate;
		this.logComment = logComment;
	}

	public UUID getLogID() {
		return logID;
	}

	public PlanEntryLogType getLogType() {
		return logType;
	}
	
	public int getLogUserID() {
		return logUserID;
	}

	public Date getLogDate() {
		return logDate;
	}

	public String getLogComment() {
		return logComment;
	}
	@Override
	public int compareTo(PlanEntryLog<MPSEntry> o) {
		if (logDate.equals(o.getPlanDate())) {
			return logID.compareTo(o.getLogID());
		} else {
			return logDate.compareTo(o.getLogDate());
		}
	}
	@Override
	public MPSEntry getPlanEntry() {
		return this;
	}
}
