package com.hyve.plan.forecast;

import java.util.Date;
import java.util.UUID;

import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.concept.PlanEntryLog;
import com.hyve.plan.concept.PlanEntryLogType;

public class ForecastLog extends ForecastEntry implements PlanEntryLog<ForecastEntry> {

	private final UUID logID;
	private final PlanEntryLogType logType;
	private final int logUserID;
	private final Date logDate;
	private final String logComment;
	
	public ForecastLog(PlanEntryLogType logType, ForecastEntry forecastEntry, LogEntry logEntry) {
		this(logType, forecastEntry.getPlanDate(), forecastEntry.getPlanLocation(), 
				forecastEntry.getPlanQty(), forecastEntry.getCustBOM(), 
				logEntry.getLogUserID(), logEntry.getLogDate(), logEntry.getLogComment());
	}
	public ForecastLog(PlanEntryLogType logType, Date shipDate, HyvePlant plant, int shipQty,
			UUID custBOM, int logUserID, Date logDate, String logComment) {
		super(shipDate, plant, shipQty, custBOM);
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
	public int compareTo(PlanEntryLog<ForecastEntry> o) {
		if (logDate == null) {
			return -1;
		} else if (o.getLogDate() == null) {
			return 1;
		} else {
			if (logDate.equals(o.getPlanDate()))
				return logID.compareTo(o.getLogID());
			else 
				return logDate.compareTo(o.getLogDate());
		}
	}
	public ForecastEntry getPlanEntry() {
		return this;
	}
}
