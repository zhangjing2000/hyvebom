package com.hyve.plan.service;

import com.hyve.plan.concept.HyvePlan;
import com.hyve.plan.forecast.ForecastEntry;
import com.hyve.plan.mps.MPSEntry;
import com.hyve.plan.mrp.MRPEntry;

public class HyvePlanServiceImpl implements HyvePlanService {

	public boolean isForecastSatisfiedByMPS(HyvePlan<ForecastEntry> forecasts,
			HyvePlan<MPSEntry> mps) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMPSSatisfiedByMRP(HyvePlan<MPSEntry> mps,
			HyvePlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		return false;
	}

	public void fromMPStoMRP(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp) {
		// TODO Auto-generated method stub
		
	}

}
