package com.hyve.plan.concept;

import java.util.Date;
import java.util.List;

import com.hyve.log.common.LogEntry;

public interface LoggedHyvePlan<T extends PlanEntry> extends HyvePlan<T> {
	List<T> getPlanSnapshot(Date snapshotDate);
	List<PlanEntryLog<T>> getPlanLogs();
	void addPlanEntry(T planEntry, LogEntry logEntry);
	void deletePlanEntry(T planEntry, LogEntry logEntry);
	void updatePlanEntry(T planEntry, LogEntry logEntry);
}
