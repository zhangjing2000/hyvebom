package com.hyve.bom.snapshot;

import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.concept.GroupType;

public class HyveGroupSnapshot implements SnapshotedHyveProductGroup {

	private final UUID snapshotGroupID;
	private final Date snapshotDateTime;
	private final String groupName;
	private final GroupType groupType; 
	private final UUID loggedGroupID;
	private final SortedSet<HyveProductGroupMember> groupDetails; 
	private final Map<TagType, String> groupTags;
	
	public HyveGroupSnapshot(UUID loggedGroupID, Date snapshotDateTime,
			SortedSet<HyveProductGroupMember> groupDetails,
			Map<TagType, String> groupTags) {
		super();
		this.snapshotGroupID = UUID.randomUUID();
		this.loggedGroupID = loggedGroupID;
		this.groupTags = groupTags;
		this.snapshotDateTime = snapshotDateTime;
		this.groupName = groupTags.get(TagType.GROUP_NAME);
		this.groupType = GroupType.valueOf(groupTags.get(TagType.GROUP_TYPE));
		this.groupDetails = groupDetails;
	}

	@Override
	public UUID getGroupID() {
		return snapshotGroupID;
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public GroupType getGroupType() {
		return groupType;
	}

	@Override
	public SortedSet<HyveProductGroupMember> getGroupDetails() {
		return groupDetails;
	}

	@Override
	public Map<TagType, String> getGroupTags() {
		return groupTags;
	}

	@Override
	public String getTagValue(TagType tagType) {
		return groupTags.get(tagType);
	}

	@Override
	public UUID getLoggedGroupID() {
		return loggedGroupID;
	}

	@Override
	public Date getSnapshotDateTime() {
		return snapshotDateTime;
	}
}
