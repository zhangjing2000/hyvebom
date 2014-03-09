package com.hyve.bom.concept;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

public interface HyveProductGroup {
	UUID getGroupID();
	String getGroupName();
	GroupType getGroupType();
	SortedSet<HyveProductGroupDetail> getGroupDetails();
	Map<TagType, String> getGroupTags();
	String getTagValue(TagType tagType);
}
