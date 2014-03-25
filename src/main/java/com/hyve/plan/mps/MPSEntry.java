package com.hyve.plan.mps;

import java.util.Date;
import java.util.UUID;

import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.concept.PlanEntry;

public class MPSEntry implements PlanEntry {
	private final Date plannedDeliveryDate;
	private final Date plannedStartDate;
	private final HyvePlant plant;
	private final int plannedQty;
	private final UUID custBOM;
	
	public MPSEntry(Date startDate, Date endDate, HyvePlant plant, int deliveryQty,
			UUID custBOM) {
		super();
		this.plannedDeliveryDate = endDate;
		this.plannedStartDate = startDate;
		this.plant = plant;
		this.plannedQty = deliveryQty;
		this.custBOM = custBOM;
	}
	
	public Date getDeliveryDate() {
		return plannedDeliveryDate;
	}
	public Date getPlanDate() {
		return plannedStartDate;
	}
	public HyvePlant getPlanLocation() {
		return plant;
	}
	public int getPlanQty() {
		return plannedQty;
	}
	public UUID getCustBOM() {
		return custBOM;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custBOM == null) ? 0 : custBOM.hashCode());
		result = prime * result
				+ ((plannedDeliveryDate == null) ? 0 : plannedDeliveryDate.hashCode());
		result = prime * result + plant.getLocNo();
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
		MPSEntry other = (MPSEntry) obj;
		if (custBOM == null) {
			if (other.custBOM != null)
				return false;
		} else if (!custBOM.equals(other.custBOM))
			return false;
		if (plannedStartDate == null) {
			if (other.plannedStartDate != null)
				return false;
		} else if (!plannedStartDate.equals(other.plannedStartDate))
			return false;
		if (plannedDeliveryDate == null) {
			if (other.plannedDeliveryDate != null)
				return false;
		} else if (!plannedDeliveryDate.equals(other.plannedDeliveryDate))
			return false;
		if (plant != other.plant)
			return false;
		return true;
	}
}
