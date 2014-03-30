package com.hyve.plan.service.optaplanner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;

import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.mrp.MRPEntry;

public class HyveMPSReadinessScoreCalculator implements SimpleScoreCalculator<MPSReadinessCheckSolution>{

	private int hardScore = 0;
	private int softScore = 0;
	public HardSoftScore calculateScore(MPSReadinessCheckSolution solution) {
		Map<HyvePlant, Map<Date, List<? extends FixedPlanEntry>>> fulfilled = solution.getFulfilledMPS();
		Map<HyvePlant, Map<Date, List<MRPEntry>>> mrp = solution.getAccumulatedMRP();
		for (Map.Entry<HyvePlant, Map<Date, List<? extends FixedPlanEntry>>> fulfilledByLocEntry: fulfilled.entrySet()) {
			HyvePlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, List<? extends FixedPlanEntry>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			Map<Date, List<MRPEntry>> mrpByLoc = mrp.get(fulfilledLoc);
			if ((mrpByLoc == null || mrpByLoc.size() == 0) && !fulfilledByLoc.isEmpty()) {
				hardScore += hardScore - 10000;
				continue;
			}
			for (Map.Entry<Date, List<? extends FixedPlanEntry>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
				Date fulfilledDate = fulfilledByDateEntry.getKey();
				List<? extends FixedPlanEntry> fulfilledPlan = fulfilledByDateEntry.getValue();
				Map<Integer, MRPEntry> mrpPlan = findMRPSupportByFulfilledDate(mrpByLoc, fulfilledDate);
				if (mrpPlan == null) {
					hardScore += hardScore - 10000;
					continue;
				}
				Map<Integer, Integer> skuDemands = accumulateDemandBySKU(fulfilledPlan);
				for (Map.Entry<Integer, Integer> skuDemand: skuDemands.entrySet()) {
					MRPEntry mrpEntry = mrpPlan.get(skuDemand.getKey());
					if (mrpEntry.getPlanQty() >= skuDemand.getValue()) 
						hardScore++;
					else 
						hardScore--;
				}
			}
		}
		return HardSoftScore.valueOf(hardScore, softScore);
	}
	private Map<Integer, Integer> accumulateDemandBySKU(
			List<? extends FixedPlanEntry> fulfilledPlan) {
		Map <Integer, Integer> skuDemand = new HashMap<Integer, Integer>();
		for (FixedPlanEntry fixedPlanEntry: fulfilledPlan) {
			if (fixedPlanEntry.getItemType() != FulfilledItemType.MATERIAL) {
				int fulfilledQty = skuDemand.get(fixedPlanEntry.getSkuNo());
				fulfilledQty += fixedPlanEntry.getFulfilledQty();
				skuDemand.put(fixedPlanEntry.getSkuNo(), fulfilledQty);
			}
		}
		return skuDemand;
	}
	
	private Map<Integer, MRPEntry> findMRPSupportByFulfilledDate(
			Map<Date, List<MRPEntry>> mrpByLoc, Date fulfilledDate) {
		List<MRPEntry> mrpPlan = null;
		Date mrpDate = null;
		for (Map.Entry<Date, List<MRPEntry>> mrpByDateEntry: mrpByLoc.entrySet()) {
			Date thisMRPDate = mrpByDateEntry.getKey();
			List<MRPEntry> mrpByDate = mrpByDateEntry.getValue();
			if (thisMRPDate.after(fulfilledDate) || mrpDate != null && mrpDate.after(thisMRPDate)) continue;
			mrpPlan = mrpByDate;
			mrpDate = thisMRPDate;
		}
		Map<Integer, MRPEntry> mrpSkuMap = new HashMap<Integer, MRPEntry>();
		if (mrpPlan == null) return mrpSkuMap;
		for (MRPEntry mrpEntry:mrpPlan) {
			MRPEntry skuMRPEntry = mrpSkuMap.get(mrpEntry.getSkuNo());
			if (skuMRPEntry == null) {
				skuMRPEntry = mrpEntry;
			} else {
				skuMRPEntry = new MRPEntry(mrpEntry.getPlanDate(), mrpEntry.getPlanLocation(), mrpEntry.getPlanQty() + skuMRPEntry.getPlanQty(), mrpEntry.getSkuNo());
			}
			mrpSkuMap.put(mrpEntry.getSkuNo(),  skuMRPEntry);
		}
		return mrpSkuMap;
	}

}
