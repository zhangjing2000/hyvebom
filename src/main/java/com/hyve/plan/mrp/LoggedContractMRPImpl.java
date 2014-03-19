package com.hyve.plan.mrp;

import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.LoggedHyvePlanImpl;
import com.hyve.plan.concept.PlanEntryLog;
import com.hyve.plan.concept.PlanEntryLogType;
import com.hyve.plan.concept.HyveContract;

public class LoggedContractMRPImpl extends  LoggedHyvePlanImpl<MRPEntry> {
	
	public LoggedContractMRPImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MRPEntry> newPlanEntryLog(PlanEntryLogType logType,
			MRPEntry planEntry, LogEntry logEntry) {
		return new MRPEntryLog(logType, planEntry, logEntry);
	}

}
