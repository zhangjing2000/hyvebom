package com.hyve.bom.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.hyve.bom.concept.GroupType;
import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.log.HyveProductGroupLog;
import com.hyve.bom.log.LoggedHyveProductGroup;
import com.hyve.bom.snapshot.HyveGroupSnapshot;
import com.hyve.bom.snapshot.SnapshotedHyveProductGroup;

public class HyveProductGroupServiceImpl implements HyveProductGroupService {

	private final Map<UUID, LoggedHyveProductGroup> groupLogs = new HashMap<UUID, LoggedHyveProductGroup>();
	private final Map<UUID, SnapshotedHyveProductGroup> groupSnapshots = new HashMap<UUID, SnapshotedHyveProductGroup>();
	private final Map<UUID, Map<Date, SnapshotedHyveProductGroup>> logsToSnapshot
		= new HashMap<UUID, Map<Date, SnapshotedHyveProductGroup>>();
	private final Map<String, SnapshotedHyveProductGroup> groupRevisions = new HashMap<String, SnapshotedHyveProductGroup>();
	
	//private boolean printTrace = false;
	public LoggedHyveProductGroup createHyveProductGroupLog(int entryID, Date entryDate, String comment,
			GroupType groupType, String groupName) {
		LoggedHyveProductGroup groupLog = new HyveProductGroupLog(entryID, entryDate, comment, groupType, groupName);
		groupLogs.put(groupLog.getGroupID(), groupLog);
		return groupLog;
	}

	private UUID getSnapshotGroupID(UUID logGroupID, Date snapshotDateTime) {
		Map<Date, SnapshotedHyveProductGroup> snapshots = logsToSnapshot.get(logGroupID);
		if (snapshots == null) return null;
		SnapshotedHyveProductGroup snapshot = snapshots.get(snapshotDateTime);
		return (snapshot == null?null:snapshot.getGroupID());
	}
	
	private void putSnapshotGroup(UUID logGroupID, Date snapshotDateTime, SnapshotedHyveProductGroup snapshot) {
		groupSnapshots.put(snapshot.getGroupID(), snapshot);
		Map<Date, SnapshotedHyveProductGroup> snapshots = logsToSnapshot.get(logGroupID);
		if (snapshots == null) {
			snapshots = new HashMap<Date, SnapshotedHyveProductGroup>();
			logsToSnapshot.put(logGroupID, snapshots);
		}
		snapshots.put(snapshotDateTime, snapshot);
	}
	
	private SnapshotedHyveProductGroup createHyveProductGroupSnapshot(LoggedHyveProductGroup groupLog, Date snapshotDateTime) {
		UUID snapshotGroupID = UUID.randomUUID();
		UUID logUUID = groupLog.getGroupID();
		Map<TagType, String> tags = groupLog.getGroupTagsAtGivenTime(snapshotDateTime);
		SortedSet<HyveProductGroupMember> logDetails = groupLog.getGroupDetailsAtGivenTime(snapshotDateTime);
		SortedSet<HyveProductGroupMember> snapshotDetails = new TreeSet<HyveProductGroupMember>();
		for (HyveProductGroupMember logDetail:logDetails) {
			HyveProductGroupMember snapshotDetail = null;
			UUID logSubGroupID = logDetail.getSubGroupID();
			int minBOMQty = logDetail.getMinBOMQty();
			int maxBOMQty = logDetail.getMaxBOMQty();
			UUID snapshotSubGroupID = null;
			if (logSubGroupID != null) {
				SnapshotedHyveProductGroup subGroupSnapshot = null;
				snapshotSubGroupID = getSnapshotGroupID(logSubGroupID, snapshotDateTime);
				if (snapshotSubGroupID == null) {
					LoggedHyveProductGroup logSubGroup = groupLogs.get(logSubGroupID);
					if (logSubGroup == null) 
						throw new GroupNotFoundException("Group:" + logSubGroupID + " not found");
					subGroupSnapshot = createHyveProductGroupSnapshot(logSubGroup, snapshotDateTime);
					snapshotSubGroupID = subGroupSnapshot.getGroupID();
				}
			}
			snapshotDetail = new HyveProductGroupMember(snapshotGroupID, logDetail.getLineNo(), 
					logDetail.getLineComment(), logDetail.getMemberType(), 
					snapshotSubGroupID, logDetail.getSkuNo(), minBOMQty, maxBOMQty);
			if (snapshotDetail != null) snapshotDetails.add(snapshotDetail);
		}
		/*
		if (printTrace) {
			System.out.println("snapshot date:" + snapshotDateTime);
			System.out.println("GroupName:" + groupLog.getGroupName());
			for (Map.Entry<TagType, String> entry: tags.entrySet()) {
				System.out.println("tags: key:" + entry.getKey() + ",value:" + entry.getValue());
			}
		}
		*/
		SnapshotedHyveProductGroup snapshot = new HyveGroupSnapshot(logUUID, snapshotDateTime, snapshotDetails, tags);
		putSnapshotGroup(logUUID, snapshotDateTime, snapshot);
		return snapshot;
	}

	public SnapshotedHyveProductGroup createHyveProductGroupSnapshot(
			LoggedHyveProductGroup groupLog, Date snapshotDateTime, String revision) {
		if (groupRevisions.get(revision) != null) 
			throw new UpdateRevisionException("Revision " + revision + " has been used");
		SnapshotedHyveProductGroup snapshot = createHyveProductGroupSnapshot(groupLog, snapshotDateTime);
		groupRevisions.put(revision, snapshot);
		return snapshot;
	}

	public SnapshotedHyveProductGroup getHyveProductGroupSnapshot(String revision) {
		return groupRevisions.get(revision);
	}

	public SnapshotedHyveProductGroup getHyveProductGroupSnapshot(UUID groupID) {
		return groupSnapshots.get(groupID);
	}
}
