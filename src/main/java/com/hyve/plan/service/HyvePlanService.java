package com.hyve.plan.service;

import com.hyve.plan.concept.HyvePlan;
import com.hyve.plan.forecast.ForecastEntry;
import com.hyve.plan.mps.MPSEntry;
import com.hyve.plan.mrp.MRPEntry;

public interface HyvePlanService {
	boolean isForecastSatisfiedByMPS(HyvePlan<ForecastEntry> forecast, HyvePlan<MPSEntry> mps);
	boolean isMPSSatisfiedByMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp);
	void fromMPStoMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp);
}
