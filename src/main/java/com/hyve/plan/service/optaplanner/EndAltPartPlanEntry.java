package com.hyve.plan.service.optaplanner;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.plan.concept.HyvePlant;

public class EndAltPartPlanEntry extends FixedPlanEntry {
	
	public EndAltPartPlanEntry(HyveProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, HyvePlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getSubGroupID(), 
				planItem.getSkuNo(), planDate, planItem.getMinBOMQty(), planLocation);
	}

	public EndAltPartPlanEntry(MemberType itemType, FixedPlanEntry parent, UUID groupID,
			int skuNo, Date planDate, int planQty, HyvePlant planLocation) {
		super(itemType, parent, groupID, skuNo, planDate, planQty, planLocation);
	}
	
	public int getFulfilledQty() {
		if (getFulfilledParent() == null) return 0;
		if (getFulfilledParent().getFulfilledChildren() == null) return 0;
		int totalFulfilledQty = 0;
		for (FixedPlanEntry sibling: getFulfilledParent().getFulfilledChildren()) {
			if (sibling.equals(this)) continue;
			totalFulfilledQty += sibling.getFulfilledQty();
		}
		return getFulfilledParent().getFulfilledQty() - totalFulfilledQty;
	}
	

	@Override
	public String toString() {
		return "EndAltPartPlanEntry [fulfilledQty=" + getFulfilledQty()
				+ ", itemType=" + getItemType() + ", groupID="
				+ getGroupID() + ", skuNo=" + getSkuNo()
				+ ", planDate=" + getPlanDate() + ", plant="
				+ getPlanLocation() + ", bomQty=" + getBomQty()
				+ ", planQty=" + getPlanQty() + "]";
	}

}
