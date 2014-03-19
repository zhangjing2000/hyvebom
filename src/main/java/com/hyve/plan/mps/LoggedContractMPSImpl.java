package com.hyve.plan.mps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.LoggedHyvePlanImpl;
import com.hyve.plan.concept.PlanEntryLog;
import com.hyve.plan.concept.PlanEntryLogType;
import com.hyve.plan.concept.HyveContract;

public class LoggedContractMPSImpl extends  LoggedHyvePlanImpl<MPSEntry> {

	
	public LoggedContractMPSImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public PlanEntryLog<MPSEntry> newPlanEntryLog(PlanEntryLogType logType,
			MPSEntry planEntry, LogEntry logEntry) {
		return new MPSEntryLog(logType, planEntry, logEntry);
	}

}
