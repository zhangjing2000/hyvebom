package com.hyve.plan.service.optaplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.concept.PlanEntry;

public class FixedPlanEntry implements PlanEntry {
	
	private final FulfilledItemType itemType;
	private final FixedPlanEntry parent;
	private final List<FixedPlanEntry> children;
	private final UUID groupID;
	private final int skuNo;
	private final Date planDate;
	private final HyvePlant planLocation;
	private int bomQty;
	
	public FixedPlanEntry(FulfilledItemType itemType, FixedPlanEntry fulfilledParent, UUID groupID,
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
		this.parent.getFulfilledChildren().add(this);
	}
	
	public FulfilledItemType getItemType() {
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
		return parent.getPlanQty() * getFulfilledQty();
	}
	public int getFulfilledQty() {
		return getBomQty();
	}
}
