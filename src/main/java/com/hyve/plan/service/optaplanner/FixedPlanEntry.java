package com.hyve.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.concept.PlanEntry;

public class FixedPlanEntry implements PlanEntry {
	
	private final MemberType itemType;
	private final FixedPlanEntry parent;
	private final List<FixedPlanEntry> children;
	private final UUID groupID;
	private final int skuNo;
	private final Date planDate;
	private final HyvePlant planLocation;
	private int bomQty;
	
	public FixedPlanEntry(HyveProductGroupMember planItem, FixedPlanEntry fulfilledParent, Date planDate, HyvePlant planLocation) {
		this(planItem.getMemberType(), fulfilledParent, planItem.getSubGroupID(), 
				planItem.getSkuNo(), planDate, planItem.getMinBOMQty(), planLocation);
	}

	public FixedPlanEntry(MemberType itemType, FixedPlanEntry fulfilledParent, UUID groupID,
			int skuNo, Date planDate, int bomQty, HyvePlant planLocation) {
		super();
		this.itemType = itemType;
		this.parent = fulfilledParent;
		this.groupID = groupID;
		this.skuNo = skuNo;
		this.planDate = planDate;
		this.bomQty = bomQty;
		this.planLocation = planLocation;
		this.children = new ArrayList<FixedPlanEntry>();
		if (this.parent != null)
			this.parent.getFulfilledChildren().add(this);
	}
	
	public MemberType getItemType() {
		return itemType;
	}
	public FixedPlanEntry getFulfilledParent() {
		return parent;
	}
	public List<FixedPlanEntry> getFulfilledChildren() {
		return children;
	}
	
	public UUID getGroupID() {
		return groupID;
	}
	public int getSkuNo() {
		return skuNo;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public HyvePlant getPlanLocation() {
		return planLocation;
	} 
	public int getBomQty() {
		return bomQty;
	}
	public int getPlanQty() {
		return parent == null? getFulfilledQty():parent.getPlanQty() * getFulfilledQty();
	}
	public int getFulfilledQty() {
		return getBomQty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
		result = prime * result
				+ ((itemType == null) ? 0 : itemType.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result
				+ ((planDate == null) ? 0 : planDate.hashCode());
		result = prime * result
				+ ((planLocation == null) ? 0 : planLocation.hashCode());
		result = prime * result + skuNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FixedPlanEntry other = (FixedPlanEntry) obj;
		if (groupID == null) {
			if (other.groupID != null)
				return false;
		} else if (!groupID.equals(other.groupID))
			return false;
		if (itemType != other.itemType)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (planDate == null) {
			if (other.planDate != null)
				return false;
		} else if (!planDate.equals(other.planDate))
			return false;
		if (planLocation == null) {
			if (other.planLocation != null)
				return false;
		} else if (!planLocation.equals(other.planLocation))
			return false;
		if (skuNo != other.skuNo)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "FixedPlanEntry [itemType=" + itemType + ", parent="  + (parent==null?"Null":"Not Null")
				+ ", children=" + children + ", groupID=" + groupID
				+ ", skuNo=" + skuNo + ", planDate=" + planDate
				+ ", planLocation=" + planLocation + ", bomQty=" + bomQty + "]";
	}
	
}
