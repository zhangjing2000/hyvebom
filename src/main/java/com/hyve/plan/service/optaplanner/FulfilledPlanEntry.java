package com.hyve.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.plan.concept.HyvePlant;

@PlanningEntity(difficultyComparatorClass = PlanEntryDifficultyComparator.class)
public class FulfilledPlanEntry extends FixedPlanEntry {
	
	private int fulfilledQty = 0;
	
	public FulfilledPlanEntry(HyveProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, HyvePlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getSubGroupID(), 
				planItem.getSkuNo(), planDate, planItem.getMinBOMQty(), planLocation);
	}

	public FulfilledPlanEntry(MemberType itemType, FixedPlanEntry parent, UUID groupID,
			int skuNo, Date planDate, int planQty, HyvePlant planLocation) {
		super(itemType, parent, groupID, skuNo, planDate, planQty, planLocation);
	}
	
	@PlanningVariable(valueRangeProviderRefs = {"fulfilledQtyList"})
	public int getFulfilledQty() {
		return fulfilledQty;
	}
	public void setFulfilledQty(int fulfilledQty) {
		//System.out.println( this.toString() + "GroupID:" + getGroupID() + "skuNo:" + getSkuNo() + "fulfilledQty=" + fulfilledQty);
		this.fulfilledQty = fulfilledQty;
	}
	
	 @ValueRangeProvider(id = "fulfilledQtyList")
	 public List<Integer> getFulfilledQtyList() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0;i<=getBomQty();i++) {
			//System.out.println("getFulfilledQtyList, GroupID:" + getGroupID() + ",skuNo:" + getSkuNo() + ",fulfilledQty=" + fulfilledQty + ",i=" + i);
	    	result.add(i);
	    }
	    return result;
    }

	@Override 
	public String toString() {
		return "FulfilledPlanEntry [fulfilledQty=" + fulfilledQty
				+ ", getItemType()=" + getItemType() + ", getGroupID()="
				+ getGroupID() + ", getSkuNo()=" + getSkuNo()
				+ ", getPlanDate()=" + getPlanDate() + ", getPlanLocation()="
				+ getPlanLocation() + ", getBomQty()=" + getBomQty()
				+ ", getPlanQty()=" + getPlanQty() + "]";
	}
	
}
