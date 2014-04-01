package com.hyve.plan.concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hyve.log.common.LogEntry;

public abstract class LoggedHyvePlanImpl<T extends PlanEntry> implements LoggedHyvePlan<T>{

	private final HyveContract hyveContract;
	private final List<PlanEntryLog<T>> logs = new ArrayList<PlanEntryLog<T>>();

	public LoggedHyvePlanImpl(HyveContract hyveContract) {
		super();
		this.hyveContract = hyveContract;
	}

	public HyveContract getContract() {
		return hyveContract;
	}

	public List<PlanEntryLog<T>> getPlanLogs() {
		return logs;
	}

	public List<T> getLatestPlan() {
		return getPlanSnapshot(null);
	}

	public List<T> getPlanSnapshot(Date snapshotDate) {
		List<T> result = new ArrayList<T>();
		Collections.sort(logs);
		for (PlanEntryLog<T> log:logs) {
			if (snapshotDate != null && log.getLogDate().after(snapshotDate)) break;
			result.add(log.getPlanEntry());
		}
		return result;
	}

	public void addPlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.ADD_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
	}

	public void deletePlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.DELETE_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
		
	}

	public void updatePlanEntry(T planEntry, LogEntry logEntry) {
		PlanEntryLog<T> planEntryLog = newPlanEntryLog(PlanEntryLogType.UPDATE_ENTRY, planEntry, logEntry); 
		logs.add(planEntryLog);
	}
	
	public  abstract PlanEntryLog<T> newPlanEntryLog(PlanEntryLogType logType, T planEntry, LogEntry logEntry);
}
