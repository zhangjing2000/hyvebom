package com.hyve.bom.snapshot;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroupDetail;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.concept.GroupType;
import com.hyve.bom.concept.HyveProductGroup;

public class HyveGroupSnapshot implements HyveProductGroup {

	private final UUID snapshotGroupID;
	private final String groupName;
	private final GroupType groupType; 
	private final SortedSet<HyveProductGroupDetail> groupDetails; 
	private final Map<TagType, String> groupTags;
	
	public HyveGroupSnapshot(UUID snapshotGroupID, String groupName,
			GroupType groupType,
			SortedSet<HyveProductGroupDetail> groupDetails,
			Map<TagType, String> groupTags) {
		super();
		this.snapshotGroupID = snapshotGroupID;
		this.groupName = groupName;
		this.groupType = groupType;
		this.groupDetails = groupDetails;
		this.groupTags = groupTags;
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
	public SortedSet<HyveProductGroupDetail> getGroupDetails() {
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
}
