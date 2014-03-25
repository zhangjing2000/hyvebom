package com.hyve.plan.forecast;

import java.util.Date;
import java.util.UUID;

import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.concept.PlanEntry;

public class ForecastEntry implements PlanEntry{
	private final Date shipDate;
	private final HyvePlant plant;
	private final int shipQty;
	private final UUID custBOM;
	
	public ForecastEntry(Date shipDate, HyvePlant plantLocNo, int deliveryQty,
			UUID custBOM) {
		super();
		this.shipDate = shipDate;
		this.plant = plantLocNo;
		this.shipQty = deliveryQty;
		this.custBOM = custBOM;
	}
	
	public Date getPlanDate() {
		return shipDate;
	}
	public HyvePlant getPlanLocation() {
		return plant;
	}
	public int getPlanQty() {
		return shipQty;
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
				+ ((shipDate == null) ? 0 : shipDate.hashCode());
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
		ForecastEntry other = (ForecastEntry) obj;
		if (custBOM == null) {
			if (other.custBOM != null)
				return false;
		} else if (!custBOM.equals(other.custBOM))
			return false;
		if (shipDate == null) {
			if (other.shipDate != null)
				return false;
		} else if (!shipDate.equals(other.shipDate))
			return false;
		if (plant != other.plant)
			return false;
		return true;
	}
}
