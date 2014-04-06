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
		Map<HyvePlant, Map<Date, List<FixedPlanEntry>>> fulfilled = solution.getFulfilledMPS();
		Map<PlanEntryIndex, MRPEntry> mrp = solution.getAccumulatedMRP();
		Map<HyvePlant, Map<Date, Map<Integer, Integer>>> aggregatecDemands = aggregateDemands(fulfilled);
		for (Map.Entry<HyvePlant, Map<Date, Map<Integer, Integer>>> fulfilledByLocEntry: aggregatecDemands.entrySet()) {
			HyvePlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, Map<Integer, Integer>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			for (Map.Entry<Date, Map<Integer, Integer>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
				Date fulfilledDate = fulfilledByDateEntry.getKey();
				Map<Integer, MRPEntry> mrpPlan = findMRPSupportByFulfilledDate(mrp, fulfilledLoc, fulfilledDate);
				if (mrpPlan == null) {
					hardScore -= 10;
				}
				Map<Integer, Integer> skuDemands = fulfilledByDateEntry.getValue();
				for (Map.Entry<Integer, Integer> skuDemand: skuDemands.entrySet()) {
					int skuNo = skuDemand.getKey();
					int mpsQty = skuDemand.getValue();
					System.out.println("calculateScore Called, skuNo=" + skuNo + ",mpsQty =" + mpsQty);
					if (skuDemand.getValue() == 0) {
						hardScore++;
						System.out.println("calculateScore no demand, hardscore:" + hardScore);
						continue;
					}
					MRPEntry mrpEntry = mrpPlan.get(skuDemand.getKey());
					if (mrpEntry == null) {
						hardScore-=10;
						break;
					}
					if (mrpEntry.getPlanQty() >= skuDemand.getValue()) {
						hardScore++;
						System.out.println("calculateScore mrp meet demand, hardscore:" + hardScore + ",mrpEntry=" + mrpEntry);
					} else { 
						hardScore = -1;
						System.out.println("calculateScore mrp less than demand, hardscore:" + hardScore + ",mrpEntry=" + mrpEntry);
						break;
					}
				}
				if (hardScore < 0) break;
			}
		}
		System.out.println("calculateScore Called, hardscore=" + hardScore);
		return HardSoftScore.valueOf(hardScore, softScore);
	}
	
	private Map<HyvePlant, Map<Date, Map<Integer, Integer>>> aggregateDemands(
			Map<HyvePlant, Map<Date, List<FixedPlanEntry>>> fulfilled) {
		Map <HyvePlant, Map<Date, Map<Integer, Integer>>> demands = new HashMap<HyvePlant, Map<Date, Map<Integer, Integer>>>();
		for (Map.Entry<HyvePlant, Map<Date, List<FixedPlanEntry>>> fulfilledByLocEntry: fulfilled.entrySet()) {
			HyvePlant fulfilledLoc = fulfilledByLocEntry.getKey();
			Map<Date, Map<Integer, Integer>> demandsByLoc = demands.get(fulfilledLoc);
			if (demandsByLoc == null) {
				demandsByLoc = new HashMap<Date, Map<Integer, Integer>>();
				demands.put(fulfilledLoc, demandsByLoc);
			}
			Map<Date, List<FixedPlanEntry>> fulfilledByLoc = fulfilledByLocEntry.getValue();
			for (Date fulfillDate : fulfilledByLoc.keySet()) {
				Map<Integer, Integer> skuDemand = demandsByLoc.get(fulfillDate);
				if (skuDemand == null) {
					skuDemand = new HashMap<Integer, Integer>();
					demandsByLoc.put(fulfillDate, skuDemand);
				}
			}
		}
		for (Map.Entry<HyvePlant, Map<Date, Map<Integer, Integer>>> demandsByLocEntry: demands.entrySet()) {
			HyvePlant plant = demandsByLocEntry.getKey();
			Map<Date, List<FixedPlanEntry>> fulfilledByLoc = fulfilled.get(plant);
			Map<Date, Map<Integer, Integer>> demandsByLoc = demands.get(plant);
			for (Map.Entry<Date, Map<Integer, Integer>> demandByDate: demandsByLoc.entrySet()) {
				Date demandDate = demandByDate.getKey();
				Map<Integer, Integer> skuDemand = demandByDate.getValue();
				for (Map.Entry<Date, List<FixedPlanEntry>> fulfilledByDateEntry: fulfilledByLoc.entrySet()) {
					Date fulfillDate = fulfilledByDateEntry.getKey();
					if (fulfillDate.after(demandDate)) continue;
					List<FixedPlanEntry> fulfilledPlan = fulfilledByDateEntry.getValue();
					for (FixedPlanEntry fixedPlanEntry: fulfilledPlan) {
						if (fixedPlanEntry.getItemType() == MemberType.MATERIAL) {
							//System.out.println("accoumulateDemandBySKU:" + fixedPlanEntry);
							Integer fulfilledQty = skuDemand.get(fixedPlanEntry.getSkuNo());
							if (fulfilledQty == null) fulfilledQty = 0;
							fulfilledQty += fixedPlanEntry.getFulfilledQty();
							skuDemand.put(fixedPlanEntry.getSkuNo(), fulfilledQty);
						}
					}
				}
			}
		}
		return demands;
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
