package com.hyve.plan.service.optaplanner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;

import com.hyve.bom.concept.MemberType;
import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.mrp.MRPEntry;

public class HyveMPSReadinessScoreCalculator implements SimpleScoreCalculator<MPSReadinessCheckSolution>{

	public HardSoftScore calculateScore(MPSReadinessCheckSolution solution) {
		int  hardScore = 0, softScore = 0;
		//System.out.println("calculateScore Called");
		Map<HyvePlant, Map<Date, List<FixedPlanEntry>>> fulfilled = solution.getFulfilledMPS();
		Map<PlanEntryIndex, MRPEntry> mrp = solution.getAccumulatedMRP();
		for (Map.Entry<HyvePlant, Map<Date, List<FixedPlanEntry>>> fulfilledByLocEntry: fulfilled.entrySet()) {
			HyvePlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, List<FixedPlanEntry>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			for (Map.Entry<Date, List<FixedPlanEntry>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
				Date fulfilledDate = fulfilledByDateEntry.getKey();
				List<FixedPlanEntry> fulfilledPlan = fulfilledByDateEntry.getValue();
				Map<Integer, MRPEntry> mrpPlan = findMRPSupportByFulfilledDate(mrp, fulfilledLoc, fulfilledDate);
				if (mrpPlan == null) {
					hardScore -= 10;
					continue;
				}
				Map<Integer, Integer> skuDemands = accumulateDemandBySKU(fulfilledPlan);
				for (Map.Entry<Integer, Integer> skuDemand: skuDemands.entrySet()) {
					if (skuDemand.getValue() == 0) {
						hardScore++;
						continue;
					}
					MRPEntry mrpEntry = mrpPlan.get(skuDemand.getKey());
					if (mrpEntry == null) {
						hardScore-=10;
						continue;
					}
					if (mrpEntry.getPlanQty() >= skuDemand.getValue()) 
						hardScore++;
					else 
						hardScore--;
				}
			}
		}
		System.out.println("calculateScore Called, hardscore=" + hardScore);
		return HardSoftScore.valueOf(hardScore, softScore);
	}
	
	private Map<Integer, Integer> accumulateDemandBySKU(
			List<FixedPlanEntry> fulfilledPlan) {
		Map <Integer, Integer> skuDemand = new HashMap<Integer, Integer>();
		for (FixedPlanEntry fixedPlanEntry: fulfilledPlan) {
			if (fixedPlanEntry.getItemType() == MemberType.MATERIAL) {
				System.out.println("accoumulateDemandBySKU" + fixedPlanEntry);
				Integer fulfilledQty = skuDemand.get(fixedPlanEntry.getSkuNo());
				if (fulfilledQty == null) fulfilledQty = 0;
				fulfilledQty += fixedPlanEntry.getFulfilledQty();
				skuDemand.put(fixedPlanEntry.getSkuNo(), fulfilledQty);
			}
		}
		return skuDemand;
	}
	
	private Map<Integer, MRPEntry> findMRPSupportByFulfilledDate(
			Map<PlanEntryIndex, MRPEntry> mrp, HyvePlant plant, Date fulfilledDate) {
		Map<Integer, MRPEntry> mrpSkuMap = new HashMap<Integer, MRPEntry>();
		for (MRPEntry mrpEntry: mrp.values()) {
			HyvePlant thisMRPPlant = mrpEntry.getPlanLocation();
			if (!thisMRPPlant.equals(plant)) continue;
			Date thisMRPDate = mrpEntry.getPlanDate();
			if (thisMRPDate.after(fulfilledDate)) continue;
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
