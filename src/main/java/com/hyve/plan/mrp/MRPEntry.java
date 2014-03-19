package com.hyve.plan.mrp;

import java.util.Date;

import com.hyve.plan.concept.PlanEntry;

public class MRPEntry implements PlanEntry {
	private final Date planRecDate;
	private final int planLocation;
	private final int planRecQty;
	private final int skuNo;
	
	public MRPEntry(Date startDate, int plantLocNo, int deliveryQty,
			int skuNo) {
		super();
		this.planRecDate = startDate;
		this.planLocation = plantLocNo;
		this.planRecQty = deliveryQty;
		this.skuNo = skuNo;
	}
	
	public Date getPlanDate() {
		return planRecDate;
	}
	public int getPlanLocation() {
		return planLocation;
	}
	public int getPlanQty() {
		return planRecQty;
	}
	public int getSkuNo() {
		return skuNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + skuNo;
		result = prime * result
				+ ((planRecDate == null) ? 0 : planRecDate.hashCode());
		result = prime * result + planLocation;
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
		MRPEntry other = (MRPEntry) obj;
		if (skuNo != other.skuNo)
			return false;
		if (planRecDate == null) {
			if (other.planRecDate != null)
				return false;
		} else if (!planRecDate.equals(other.planRecDate))
			return false;
		if (planLocation != other.planLocation)
			return false;
		return true;
	}
}
