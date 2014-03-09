package com.hyve.bom.log;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.hyve.bom.concept.HyveAlternativeGroupDetail;
import com.hyve.bom.concept.HyveAssemblyGroupDetail;
import com.hyve.bom.concept.HyveProductGroupDetail;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.concept.GroupType;
import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.log.exception.InvalidGroupTypeException;
import com.hyve.bom.log.exception.NullGroupTypeException;
import com.hyve.bom.log.exception.UpdateRevisionException;

public class HyveProductGroupLog implements HyveProductGroup {

	private final UUID groupID;
	//private final SortedSet<HyveGroupHeaderLog> headers;
	private final SortedSet<HyveGroupLineLog> lines;
	private final SortedSet<HyveGroupTagLog> tags;
	
	public HyveProductGroupLog(int entryID, Date entryDate, String comment, GroupType groupType, String groupName) {
		this.groupID = UUID.randomUUID();
		//headers = new TreeSet<HyveGroupHeaderLog>();
		lines = new TreeSet<HyveGroupLineLog>();
		tags = new TreeSet<HyveGroupTagLog>();
		//GroupChangeLogType logType = groupType == HyveProductGroupType.ASSEMBLY? 
		//		GroupChangeLogType.NEW_ASSEMBLY_GROUP:GroupChangeLogType.NEW_ALTERNATIVE_GROUP;
		//HyveGroupHeaderLog headerLog = new HyveGroupHeaderLog(groupID, entryID, entryDate, logType, comment, groupName, groupType);
		//headers.add(headerLog);
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
		//return headers == null? null:headers.isEmpty()?null:headers.last().getGroupType();
		return GroupType.valueOf(getTagValue(TagType.GROUP_TYPE));
	}

	@Override
	public SortedSet<HyveProductGroupDetail> getGroupDetails() {
		return getGroupDetailsAtGivenTime(null);
	}

	
	public SortedSet<HyveProductGroupDetail> getGroupDetailsAtGivenTime(Date timeStamp) {
		Map<Integer, HyveProductGroupDetail> map = new HashMap<Integer, HyveProductGroupDetail>();
		for (HyveGroupLineLog log: lines) {
			if (timeStamp != null && log.getLogDate().after(timeStamp)) break;
			if (log.getLogType() == GroupChangeLogType.DELETE_GROUP_LINE) {
				map.remove(log.getLine());
			} else { 
				map.put(log.getLine(), log.toHyveGroupProductDetail());
			}
		}
		Comparator<HyveProductGroupDetail> detailComparator = new Comparator<HyveProductGroupDetail>() {
				@Override
				public int compare(HyveProductGroupDetail d1,
						HyveProductGroupDetail d2) {
					return d1.getLineNo() - d2.getLineNo();
				}
			};
		TreeSet<HyveProductGroupDetail> result = new TreeSet<HyveProductGroupDetail>(detailComparator);
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
	
	public SortedSet<HyveProductGroupDetail> getGroupDetailsWithGivenTag(TagType tagType, String tagValue) {
		return getGroupDetailsWithGivenTimeAndTag(null, tagType, tagValue);
	}
	
	public SortedSet<HyveProductGroupDetail> getGroupDetailsWithGivenTimeAndTag(Date timeStamp, 
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
			return new TreeSet<HyveProductGroupDetail>();
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
	
	public void newRevision(String revision, int entryID, Date entryDate, String comment) {
		addTag(TagType.REVISION, revision, entryID, entryDate, comment);
	}
	
	public void addGroupDetail(int entryID, Date entryDate, String comment, int lineNo, 
			MemberType logMemberType, String lineComment,
			UUID subGroupID, int skuNo, int minBOMQty, int maxBOMQty) {
		boolean isInsert = true;
	
		SortedSet<HyveProductGroupDetail> currentDetails = getGroupDetails();
		if (currentDetails.isEmpty()) {
			if (lineNo<= 0) lineNo = 1;
			isInsert = false;
		} else {
			HyveProductGroupDetail lastLine = currentDetails.last();
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
		for (HyveProductGroupDetail detail: currentDetails) {
			if (detail.getLineNo() < insertLineNo) continue;
			if (detail.getLineNo() == insertLineNo) {
				insertLineNo++;
				if (detail instanceof HyveAssemblyGroupDetail) {
					HyveAssemblyGroupDetail assemblyDetail = (HyveAssemblyGroupDetail)detail;
					log = new HyveGroupLineLog(groupID,  entryID, entryDate,
							GroupChangeLogType.UPDATE_GROUP_LINE, comment,
							assemblyDetail.getLineNo(), assemblyDetail.getMemberType(), 
							assemblyDetail.getLineComment(),
							assemblyDetail.getSubGroupID(), 0, 
							assemblyDetail.getMinBOMQty(), 
							assemblyDetail.getMaxBOMQty());
				} else if (detail instanceof HyveAlternativeGroupDetail) {
					HyveAlternativeGroupDetail altDetail = (HyveAlternativeGroupDetail)detail;
					log = new HyveGroupLineLog(groupID,  entryID, entryDate,
							GroupChangeLogType.UPDATE_GROUP_LINE, comment,
							altDetail.getLineNo(), altDetail.getMemberType(), 
							altDetail.getLineComment(),
							altDetail.getSubGroupID(), altDetail.getSkuNo(), 
							0, 0);
				}
				addGroupDetail(log);
			}
			if (detail.getLineNo() > lineNo) break;
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
	
	public void addGroupDetail(HyveGroupLineLog lineLog) {
		lines.add(lineLog);
	}
	
	private void checkGroupType(String groupTypeStr) {
		if (groupTypeStr == null) throw new NullGroupTypeException("cannot give a empty group type");
		if (GroupType.valueOf(groupTypeStr) == null) throw new InvalidGroupTypeException("wrong group type input:" + groupTypeStr);
	}
	
	public void checkRevision(String revision) {
		for (HyveGroupTagLog tagLog: tags) {
			if (tagLog.getTagType() != TagType.REVISION) continue;
			if (tagLog.getTagValue().equals(revision)) throw new UpdateRevisionException("cannot add an exisitng revision");
		}
	}
	
	public void addTag(TagType tagType, String tagValue, int entryID, Date entryDate, String comment) {
		if (tagType == TagType.GROUP_TYPE) {
			checkGroupType(tagValue);
		}
		if (tagType == TagType.REVISION) {
			checkRevision(tagValue);
		}
		HyveGroupTagLog tagLog = new HyveGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.NEW_TAG, comment, tagType, tagValue);
		tags.add(tagLog);
	}

	public void deleteTag(TagType tagType, int entryID, Date entryDate, String comment) {
		if (tagType == TagType.REVISION) throw new UpdateRevisionException("cannot remove an revision");
		HyveGroupTagLog tagLog = new HyveGroupTagLog(groupID, entryID, entryDate, GroupChangeLogType.DELETE_TAG, comment, tagType, null);
		tags.add(tagLog);
	}
}