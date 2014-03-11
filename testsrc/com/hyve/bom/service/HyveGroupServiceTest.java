package com.hyve.bom.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hyve.bom.concept.GroupType;
import com.hyve.bom.concept.HyveAlternativeGroupMember;
import com.hyve.bom.concept.HyveAssemblyGroupMember;
import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.log.LoggedHyveProductGroup;
import com.hyve.bom.snapshot.SnapshotedHyveProductGroup;

public class HyveGroupServiceTest {
	private LoggedHyveProductGroup ash13ServerLog;
	private SnapshotedHyveProductGroup ash13ServerSnapshot;
	private HyveProductGroupService service;
	private DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static int entryID = 4739;
	private Date entryDate;
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String harddriveGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entryDate = sdf.parse("01/01/2014");
		service = new HyveProductGroupServiceImpl();
		ash13ServerLog = service.createHyveProductGroupLog(entryID, entryDate, "Init Test",  GroupType.ASSEMBLY, serverGroupName);
	}

	@After
	public void tearDown() throws Exception {
  	}

	@Test
	public void testCreateSnapshot() throws ParseException {
		int revision;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = sdf.parse("01/01/2014");
		Date date2 = sdf.parse("01/02/2014");
		Date date3 = sdf.parse("01/03/2014");
		Date date4 = sdf.parse("01/04/2014");
		Date date5 = sdf.parse("01/05/2014");
		Date date6 = sdf.parse("01/06/2014");
		Date date7 = sdf.parse("01/07/2014");
		Date date8 = sdf.parse("01/08/2014");
		
		revision = 1;
		ash13ServerLog.addTag(TagType.FAMILY, "ASH", entryID, date1, "add family");
		ash13ServerLog.addTag(TagType.MODEL, "13", entryID, date1, "add model");
		ash13ServerSnapshot = service.createHyveProductGroupSnapshot(ash13ServerLog, date1, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		assertEquals(ash13ServerSnapshot.getLoggedGroupID(), ash13ServerLog.getGroupID());
		assertEquals(ash13ServerSnapshot.getGroupName(), ash13ServerLog.getGroupName());
		
		revision++;
		addGroupDetailToAshServer(entryID, date2, "add chassis",
				GroupType.ASSEMBLY, chassisGroupName, MemberType.ASSEMBLY_SUB_GROUP,
				"chassis sub bom", 1, 1);
		ash13ServerSnapshot = service.createHyveProductGroupSnapshot(ash13ServerLog, date2, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
			
		revision++;
		addGroupDetailToAshServer(entryID, date3, "add motherboard",
				GroupType.ALTERNATIVE, motherboardGroupName, MemberType.ALTERNATIVE_SUB_GROUP,
				"motherboard sub bom", 1, 1);
		ash13ServerSnapshot = service.createHyveProductGroupSnapshot(ash13ServerLog, date3, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		
		revision++;
		HyveProductGroup processor = addGroupDetailToAshServer(entryID, date3, "add processor",
				GroupType.ALTERNATIVE, processorGroupName, MemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 1, 1);
		ash13ServerSnapshot = service.createHyveProductGroupSnapshot(ash13ServerLog, date3, "revision" + revision);
		assertNotNull(ash13ServerSnapshot);
		
		revision++;
		int lineNo = getLineNo(ash13ServerSnapshot.getGroupDetails(), processor);
		
		updateGroupDetailToAshServer(entryID, date4, "update processor",
				lineNo, processor.getGroupID(), MemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 2, 2);

		revision++;
		HyveProductGroup memory = addGroupDetailToAshServer(entryID, date5, "add memory",
				GroupType.ALTERNATIVE, memoryGroupName, MemberType.ALTERNATIVE_SUB_GROUP,
				"memory sub bom", 2, 4);

		revision++;
		lineNo = getLineNo(ash13ServerSnapshot.getGroupDetails(), processor);
		
		updateGroupDetailToAshServer(entryID, date6, "update memory",
				lineNo, memory.getGroupID(), MemberType.ALTERNATIVE_SUB_GROUP,
				"memory sub bom", 2, 2);

		revision++;
		lineNo = getLineNo(ash13ServerSnapshot.getGroupDetails(), processor);
		
		updateGroupDetailToAshServer(entryID, date7, "update processor",
				lineNo, processor.getGroupID(), MemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 1, 2);

		revision++;
		HyveProductGroup harddrive = insertGroupDetailToAshServer(entryID, date8, "add hard drive",
				3, GroupType.ALTERNATIVE, harddriveGroupName, MemberType.ALTERNATIVE_SUB_GROUP,
				"hard drive sub bom", 4, 4);

		assertNotNull(harddrive);
		
	}

	private HyveProductGroup addGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		HyveProductGroup prodGroup = service.createHyveProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13ServerLog.addGroupDetail(entryID, entryDate, entryComment, 0, memberType, lineComment, 
				prodGroup.getGroupID(), 0, minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private HyveProductGroup insertGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		HyveProductGroup prodGroup = service.createHyveProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13ServerLog.addGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				prodGroup.getGroupID(), lineNo, minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private void updateGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, UUID subGroupID, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		ash13ServerLog.updateGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				subGroupID, 0, minBOMQty, maxBOMQty);
	}

	private int getLineNo(SortedSet<HyveProductGroupMember> details,
			HyveProductGroup detailGroup) {
		int lineNo;
		lineNo = 0;
		for (HyveProductGroupMember detail:details) {
			if (detail instanceof HyveAssemblyGroupMember) {
				if (((HyveAssemblyGroupMember)detail).getSubGroupID().equals(detailGroup.getGroupID())) {
					lineNo = detail.getLineNo();
					break;
				}
			} else { // alternative
				if (((HyveAlternativeGroupMember)detail).getSubGroupID().equals(detailGroup.getGroupID())) {
					lineNo = detail.getLineNo();
					break;
				}
			}
		}
		return lineNo;
	}

}
