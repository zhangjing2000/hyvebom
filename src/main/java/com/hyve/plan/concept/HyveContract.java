package com.hyve.plan.concept;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

public class HyveContract {
	private final int contractNo;
	private final int custNo;
	private String contractName;
	private Date startDate;
	private Date endDate;
	private final Set<Integer> planners;
	
	static enum ContractColumn {ContractName, StartDate, EndDate, Planners}
	static enum ContractChangeType {Set, Add, Delete}
	static class ContractChangeLog implements Comparable<ContractChangeLog>{
		private final UUID logID; 
		private final ContractColumn column;
		private final ContractChangeType changeType;
		private final String value;
		private final Date logDate;
		private final int logUser;
		
		ContractChangeLog(ContractColumn column, ContractChangeType changeType, String value, Date logDate, int logUser) {
			this.logID = UUID.randomUUID();
			this.column = column;
			this.changeType = changeType;
			this.value = value;
			this.logDate = logDate; 
			this.logUser = logUser;
		}

		public UUID getLogID() {
			return logID;
		}
		
		public ContractColumn getColumn() {
			return column;
		}

		public ContractChangeType getChangeType() {
			return changeType;
		}
		
		public String getValue() {
			return value;
		}

		public Date getLogDate() {
			return logDate;
		}

		public int getLogUser() {
			return logUser;
		}

		public int compareTo(ContractChangeLog arg0) {
			if (this.logDate.equals(arg0.getLogDate())) {
				return this.logID.compareTo(arg0.getLogID());
			} else {
				return logDate.compareTo(arg0.getLogDate());
			}
		}
	}
	
	private final SortedSet<ContractChangeLog> logs = new TreeSet<ContractChangeLog>();
	
	public HyveContract(int contractNo, int custNo) {
		super();
		this.contractNo = contractNo;
		this.custNo = custNo;
		this.planners = new TreeSet<Integer>();
	}

	public int getContractNo() {
		return contractNo;
	}
	
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public int getCustNo() {
		return custNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Set<Integer> getPlanners() {
		return planners;
	}
	
	public boolean hasPlanner(int userID) {
		return planners.contains(userID);
	}
	
	public void addPlanner(int userID) {
		if (!hasPlanner(userID)) planners.add(userID);
		
	}
	
	public void removePlanner(int plannerID, int entryID) {
		if (hasPlanner(plannerID)) planners.remove(entryID);
		logs.add(new ContractChangeLog(ContractColumn.Planners, ContractChangeType.Delete, "" + plannerID, new Date(), entryID));
	}
	
	public SortedSet<ContractChangeLog> getLogs() {
		return logs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contractNo;
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
		HyveContract other = (HyveContract) obj;
		if (contractNo != other.contractNo)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "HyveContract [contractNo=" + contractNo + ", contractName="
				+ contractName + ", custNo=" + custNo + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}
	
	
}
