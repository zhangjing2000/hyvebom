package com.hyve.plan.forecast;

import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.LoggedHyvePlanImpl;
import com.hyve.plan.concept.PlanEntryLogType;
import com.hyve.plan.concept.HyveContract;

public class LoggedContractForecastImpl extends LoggedHyvePlanImpl<ForecastEntry> {

	public LoggedContractForecastImpl(HyveContract contract) {
		super(contract);
	}

	@Override
	public ForecastLog newPlanEntryLog(PlanEntryLogType logType,
			ForecastEntry planEntry, LogEntry logEntry) {
		return new ForecastLog(logType, planEntry, logEntry);
	}

}
