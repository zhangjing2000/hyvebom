package com.hyve.bom.concept;

import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

public interface HyveProductGroup {
	UUID getGroupID();
	String getGroupName();
	HyveProductGroupType getGroupType();
	SortedSet<HyveProductGroupDetail> getGroupDetails();
	Map<HyveProductGroupTagType, String> getGroupTags();
	String getTagValue(HyveProductGroupTagType tagType);
}
