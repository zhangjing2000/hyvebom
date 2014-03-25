package com.hyve.plan.service.optaplanner;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import com.hyve.plan.concept.HyvePlan;
import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.mps.MPSEntry;
import com.hyve.plan.mrp.MRPEntry;

@PlanningSolution
public class MPSReadinessCheckSolution implements Solution<HardSoftScore> {

	private final HyvePlan<MPSEntry> mps;
	private final HyvePlan<MRPEntry> mrp;
	
	private final Map<HyvePlant, Map<Date, List<? extends FixedPlanEntry>>> fulfilledMPS;
	private final Map<HyvePlant, Map<Date, List<MRPEntry>>> accumulatedMRP;
	
	private HardSoftScore score;
	
	public MPSReadinessCheckSolution(HyvePlan<MPSEntry> mps, HyvePlan<MRPEntry> mrp) {
		this.mps = mps;
		this.mrp = mrp;
		this.fulfilledMPS = new HashMap<HyvePlant, Map<Date, List<? extends FixedPlanEntry>>>();
		this.accumulatedMRP = new HashMap<HyvePlant, Map<Date, List<MRPEntry>>>();
		explodeMPS();
		accumulateMRP();
	}
	
	private void explodeMPS() {
		
	}
	
	private void accumulateMRP() {
		
	}
	
	public HyvePlan<MPSEntry> getMps() {
		return mps;
	}

	public HyvePlan<MRPEntry> getMrp() {
		return mrp;
	}

	public Map<HyvePlant, Map<Date, List<? extends FixedPlanEntry>>> getFulfilledMPS() {
		return fulfilledMPS;
	}

	public Map<HyvePlant, Map<Date, List<MRPEntry>>> getAccumulatedMRP() {
		return accumulatedMRP;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		return null;
	}

	@Override
	public HardSoftScore getScore() {
		return score;
	}

	@Override
	public void setScore(HardSoftScore score) {
		this.score = score;
	}
}
