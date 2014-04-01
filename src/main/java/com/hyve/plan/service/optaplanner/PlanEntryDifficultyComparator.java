package com.hyve.plan.service.optaplanner;

import java.util.Comparator;

public class PlanEntryDifficultyComparator implements Comparator<FulfilledPlanEntry> {

	public int compare(FulfilledPlanEntry arg0, FulfilledPlanEntry arg1) {
		if (arg0.getSkuNo() > 0) {
			return arg1.getSkuNo() > 0? 0:-1;
		} else {
			return arg1.getSkuNo() > 0? 1:0;
		}
	}

}
