package com.hyve.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.hyve.plan.concept.HyvePlant;

@PlanningEntity
public class FulfilledPlanEntry extends FixedPlanEntry {
	
	private int fulfilledQty = 0;
	
	public FulfilledPlanEntry(FulfilledItemType itemType, FixedPlanEntry parent, UUID groupID,
			int skuNo, Date planDate, int planQty, HyvePlant planLocation) {
		super(itemType, parent, groupID, skuNo, planDate, planQty, planLocation);
	}
	
	@PlanningVariable(valueRangeProviderRefs = {"fulfilledQty"})
	public int getFulfilledQty() {
		return fulfilledQty;
	}
	public void setFulfilledQty(int fulfilledQty) {
		this.fulfilledQty = fulfilledQty;
	}
	
	 @ValueRangeProvider(id = "fulfilledQty")
	 public List<Integer> getFulfilledQtyList() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0;i<=getBomQty();i++) {
	    	result.add(i);
	    }
	    return result;
    }

}
