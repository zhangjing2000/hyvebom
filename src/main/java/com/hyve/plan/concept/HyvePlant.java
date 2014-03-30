package com.hyve.plan.concept;

import java.io.Serializable;

public class HyvePlant implements Serializable, Comparable<HyvePlant>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int locNo;
	private final String name;
	private final String countryCode;
	public HyvePlant(int locNo, String name, String countryCode) {
		super();
		this.locNo = locNo;
		this.name = name;
		this.countryCode = countryCode;
	}
	public int getLocNo() {
		return locNo;
	}
	public String getName() {
		return name;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public int compareTo(HyvePlant arg0) {
		return this.locNo - arg0.getLocNo();
	}
	@Override
	public int hashCode() {
		return locNo;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HyvePlant other = (HyvePlant) obj;
		if (locNo != other.locNo)
			return false;
		return true;
	}
}
