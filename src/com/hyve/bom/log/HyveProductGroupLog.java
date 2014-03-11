package com.hyve.bom.log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.hyve.bom.concept.HyveAlternativeGroupMember;
import com.hyve.bom.concept.HyveAssemblyGroupMember;
import com.hyve.bom.concept.HyvePartGroupMember;
import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.concept.GroupType;
import com.hyve.bom.log.exception.InvalidGroupTypeException;
import com.hyve.bom.log.exception.NullGroupTypeException;

public class HyveProductGroupLog implements LoggedHyveProductGroup {

	private final UUID groupID;
	private final SortedSet<HyveGroupLineLog> lines;
	private final SortedSet<HyveGroupTagLog> tags;
	
	public HyveProductGroupLog(int entryID, Date entryDate, String comment, GroupType groupType, String groupName) {
		this.groupID = UUID.randomUUID();
		lines = new TreeSet<HyveGroupLineLog>();
		tags = new TreeSet<HyveGroupTagLog>();
		addTag(TagType.NEW_GROUP, groupID.toString(), entryID, entryDate, comment);
		setGroupType(groupType, entryID, entryDate, comment);
		setGroupName(groupName, entryID, entryDate, comment);
	}
	
	@Override
	public UUID getGroupID() {
		return groupID;
	}

	@Override
	public String getGroupName() {
		//return headers == null? null:headers.isEmpty()?null:headers.last().getGroupName();
		return getTagValue(TagType.GROUP_NAME);
	}

	@Override
	public GroupType getGroupType() {
		return GroupType.valueOf(getTagValue(TagType.GROUP_TYPE));
	}

	@Override
	public SortedSet<HyveProductGroupMember> getGroupDetails() {
		return getGroupDetailsAtGivenTime(null);
	}

	
	public SortedSet<HyveProductGroupMember> getGroupDetailsAtGivenTime(Date timeStamp) {
		Map<Integer, HyveProductGroupMember> map = new HashMap<Integer, HyveProductGroupMember>();
		for (HyveGroupLineLog log: lines) {
			if (timeStamp != null && log.getLogDate().after(timeStamp)) break;
			if (log.getLogType() == GroupChangeLogType.DELETE_GROUP_LINE) {
				map.remove(log.getLine());
			} else { 
				map.put(log.getLine(), log.toHyveGroupProductDetail());
			}
		}
		Comparator<HyveProductGroupMember> detailComparator = new Comparator<HyveProductGroupMember>() {
				@Override
				public int compare(HyveProductGroupMember d1,
						HyveProductGroupMember d2) {
					return d1.getLineNo() - d2.getLineNo();
				}
			};
		TreeSet<HyveProductGroupMember> result = new TreeSet<HyveProductGroupMember>(detailComparator);
		result.addAll(map.values());
		return result;
	}

	@Override
	public Map<TagType, String> getGroupTags() {
		return getGroupTagsAtGivenTime(null);
	}
	
	public String getTagValue(TagType tagType) {
		return getTagValueAtGivenTime(tagType, null);
	}
	
	public String getTagValueAtGivenTime(TagType tagType, Date timeStamp) {
		String returnValue = null;
		for (HyveGroupTagLog tagLog: tags) {
			if (timeStamp!= null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getTagType() != tagType) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				returnValue = tagLog.getTagValue();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				returnValue = null;
			}
		}
		return returnValue;
	}
	
	public Map<TagType, String> getGroupTagsAtGivenTime(Date timeStamp) {
		Map<TagType, String> map = new HashMap<TagType, String>();
		for (HyveGroupTagLog tagLog: tags) {
			if (timeStamp!= null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				map.put(tagLog.getTagType(), tagLog.getTagValue());
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				map.remove(tagLog.getLogType());
			}
		}
		return map;
	}
	
	public SortedSet<HyveProductGroupMember> getGroupDetailsWithGivenTag(TagType tagType, String tagValue) {
		return getGroupDetailsWithGivenTimeAndTag(null, tagType, tagValue);
	}
	
	public SortedSet<HyveProductGroupMember> getGroupDetailsWithGivenTimeAndTag(Date timeStamp, 
			TagType tagType, String tagValue) {
		// find tag time before timestamp
		Date tagTime = null; 
		for (HyveGroupTagLog tagLog: tags) {
			if (timeStamp != null && tagLog.getLogDate().after(timeStamp)) break;
			if (tagLog.getTagType() != tagType) continue;
			if (!tagLog.getTagValue().equals(tagValue)) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				tagTime = tagLog.getLogDate();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				tagTime = null;
			}
		}
		
		if (tagTime == null) {
			// no tag with given type and value found, return an empty result
			return new TreeSet<HyveProductGroupMember>();
		} else {
			return getGroupDetailsAtGivenTime(tagTime);
		}
	}

	public void setGroupName(String groupName, int entryID, Date entryDate, String comment) {
		addTag(TagType.GROUP_NAME, groupName, entryID, entryDate, comment);
	}
	
	public void setGroupType(GroupType groupType, int entryID, Date entryDate, String comment) {
		addTag(TagType.GROUP_TYPE, groupType.name(), entryID, entryDate, comment);
	}
	
	/*
	public void newRevision(String revision, int entryID, Date entryDate, String comment) {
		addTag(TagType.REVISION, revision, entryID, entryDate, comment);
	}
	*/
	public void archiveGroup(int entryID, Date entryDate, String comment) {
		addTag(TagType.ARCHIVE_GROUP, groupID.toString(), entryID, entryDate, comment);
	}
	
	public void addGroupDetail(int entryID, Date entryDate, String comment, int lineNo, 
			MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty) {
		boolean isInsert = true;
	
		SortedSet<HyveProductGroupMember> currentDetails = getGroupDetails();
		if (currentDetails.isEmpty()) {
			if (lineNo<= 0) lineNo = 1;
			isInsert = false;
		} else {
			HyveProductGroupMember lastLine = currentDetails.last();
			if (lineNo == 0) {
				lineNo = lastLine.getLineNo() + 1;
				isInsert = false;
			} else if (lineNo > lastLine.getLineNo()) {
				isInsert = false;
			}
		}
		HyveGroupLineLog log = new HyveGroupLineLog(groupID, entryID, entryDate,
				isInsert?GroupChangeLogType.UPDATE_GROUP_LINE:GroupChangeLogType.NEW_GROUP_LINE, 
				comment,
				lineNo, logMemberType, lineComment,
				subGroupID, skuNo, minBOMQty, maxBOMQty);
		addGroupDetail(log);
		if (!isInsert) return;
		int insertLineNo = lineNo;
		for (HyveProductGroupMember detail: currentDetails) {
			if (detail.getLineNo() < insertLineNo) continue;
			if (detail.getLineNo() == insertLineNo) {
				insertLineNo++;
				if (detail instanceof HyveAssemblyGroupMember) {
					HyveAssemblyGroupMember assemblyDetail = (HyveAssemblyGroupMember)detail;
					log = new HyveGroupLineLog(groupID,  entryID, entryDate,
							GroupChangeLogType.UPDATE_GROUP_LINE, comment,
							insertLineNo, assemblyDetail.getMemberType(), 
							assemblyDetail.getLineComment(),
							assemblyDetail.getSubGroupID(), 0, 
							assemblyDetail.getMinBOMQty(), 
							assemblyDetail.getMaxBOMQty());
				} else if (detail instanceof HyveAlternativeGroupMember) {
					HyveAlternativeGroupMember altDetail = (HyveAlternativeGroupMember)detail;
					log = new HyveGroupLineLog(groupID,  entryID, entryDate,
							GroupChangeLogType.UPDATE_GROUP_LINE, comment,
							insertLineNo, altDetail.getMemberType(), 
							altDetail.getLineComment(),
							altDetail.getSubGroupID(), 0, 
							0, 0);
				} else if (detail instanceof HyvePartGroupMember) {
					HyvePartGroupMember partDetail = (HyvePartGroupMember)detail;
					log = new HyveGroupLineLog(groupID,  entryID, entryDate,
							GroupChangeLogType.UPDATE_GROUP_LINE, comment,
							insertLineNo, partDetail.getMemberType(), 
							partDetail.getLineComment(),
							null, partDetail.getSkuNo(), 
							0, 0);
				}
				addGroupDetail(log);
			}
			if (detail.getLineNo() > insertLineNo) break;
		}
	}
	
	public void updateGroupDetail(int entryID, Date entryDate, String comment, int line, MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty) {
		HyveGroupLineLog log = new HyveGroupLineLog(groupID,  entryID, entryDate,
			GroupChangeLogType.UPDATE_GROUP_LINE, comment,
			line, logMemberType, lineComment,
			subGroupID, skuNo, minBOMQty, maxBOMQty);
		addGroupDetail(log);
	}
	
	public void deleteGroupDetail(int entryID, Date entryDate, String comment, int line) {
		HyveGroupLineLog log = new HyveGroupLineLog(groupID,  entryID, entryDate,
			GroupChangeLogType.DELETE_GROUP_LINE, comment,
			line, null, null,
			null, 0, 0, 0);
		addGroupDetail(log);
	}
	
	private void addGroupDetail(HyveGroupLineLog lineLog) {
		lines.add(lineLog);
	}
	
	private void checkGroupType(String groupTypeStr) {
		if (groupTypeStr == null) throw new NullGroupTypeException("cannot give a empty group type");
		try {
			if (GroupType.valueOf(groupTypeStr) == null) throw new InvalidGroupTypeException("wrong group type input:" + groupTypeStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidGroupTypeException("wrong group type input:" + groupTypeStr);
		}
	}
	
	/*
	private void checkRevision(String revision) {
		for (HyveGroupTagLog tagLog: tags) {
			//if (tagLog.getTagType() != TagType.REVISION) continue;
			if (tagLog.getTagValue().equals(revision)) throw new UpdateRevisionException("cannot add an exisitng revision");
		}
	}
	*/
	public void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment) {
		if (tagType == TagType.GROUP_TYPE) {
			checkGroupType(tagValue);
		}
		/*
		if (tagType == TagType.REVISION) {
			checkRevision(tagValue);
		}
		*/
		HyveGroupTagLog tagLog = new HyveGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.NEW_TAG, comment, tagType, tagValue);
		tags.add(tagLog);
	}

	public void deleteTag(TagType tagType, int entryID, Date entryDate, String comment) {
		//if (tagType == TagType.REVISION) throw new UpdateRevisionException("cannot remove an revision");
		HyveGroupTagLog tagLog = new HyveGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.DELETE_TAG, comment, tagType, null);
		tags.add(tagLog);
	}
	
	public List<String> getDetailLogs() {
		List<String> logs = new ArrayList<String>();
		for (HyveGroupLineLog lineLog: lines) {
			logs.add(lineLog.toString());
		}
		return logs;
	}

	public List<String> getTagLogs() {
		List<String> logs = new ArrayList<String>();
		for (HyveGroupTagLog tagLog: tags) {
			logs.add(tagLog.toString());
		}
		return logs;
	}
/*
	@Override
	public Date getRevisonDate(String revision) {
		Date tagTime = null;
		for (HyveGroupTagLog tagLog: tags) {
			if (tagLog.getTagType() != TagType.REVISION) continue;
			if (!tagLog.getTagValue().equals(revision)) continue;
			if (tagLog.getLogType() == GroupChangeLogType.NEW_TAG) {
				tagTime = tagLog.getLogDate();
			} else if (tagLog.getLogType() == GroupChangeLogType.DELETE_TAG) {  
				tagTime = null;
			}
		}
		return tagTime;
	}
	*/
}
