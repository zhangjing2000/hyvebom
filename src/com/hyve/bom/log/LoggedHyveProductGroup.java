package com.hyve.bom.log;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import com.hyve.bom.concept.GroupType;
import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.TagType;

public interface LoggedHyveProductGroup extends HyveProductGroup {
	SortedSet<HyveProductGroupMember> getGroupDetailsAtGivenTime(Date timeStamp);	
	SortedSet<HyveProductGroupMember> getGroupDetailsWithGivenTag(TagType tagType, String tagValue);
	SortedSet<HyveProductGroupMember> getGroupDetailsWithGivenTimeAndTag(Date timeStamp, TagType tagType, String tagValue);

	String getTagValueAtGivenTime(TagType tagType, Date timeStamp);
	Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp);
	//Date getRevisonDate(String revision);
	
	void setGroupName(String groupName, int entryID, Date entryDate, String comment);
	void setGroupType(GroupType groupType, int entryID, Date entryDate, String comment);
	//void newRevision(String revision, int entryID, Date entryDate, String comment);
	void archiveGroup(int entryID, Date entryDate, String comment);
	
	void addGroupDetail(int entryID, Date entryDate, String comment, int lineNo, 
			MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty);
	void updateGroupDetail(int entryID, Date entryDate, String comment, int line, MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty);
	void deleteGroupDetail(int entryID, Date entryDate, String comment, int line);
	
	void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment);
	void deleteTag(TagType tagType, int entryID, Date entryDate, String comment);	
	
	List<String> getDetailLogs();
	List<String> getTagLogs();
	
}
