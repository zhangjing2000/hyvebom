package com.hyve.bom.log;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.concept.HyveProductGroupMember;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.TagType;
import com.hyve.bom.concept.GroupType;
import com.hyve.bom.log.HyveProductGroupLog;
import com.hyve.bom.log.exception.InvalidGroupTypeException;
import com.hyve.bom.log.exception.NullGroupTypeException;

public class HyveProductGroupLogTest {
	
	private HyveProductGroupLog ash13Server;
	private HyveProductGroupLog cables;
	private static String rackGroupName = "Ash13Rack";
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String ssdGroupName = "SSD";
	private static String harddriveGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";
	private static String cableGroupName = "Cable";
	private static int cableAltPart1 = 100101;
	private static int cableAltPart2 = 100102;
	private static int entryID = 4739;
	private static Date entryDate = new Date();
	private DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entryDate = sdf.parse("01/01/2014");
		ash13Server = new HyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, serverGroupName);
		cables = new HyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, cableGroupName);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHyveProductGroupLog() throws ParseException {
		entryDate = sdf.parse("01/01/2014");
		HyveProductGroup ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", GroupType.ASSEMBLY, rackGroupName);
		assertNotNull(ash13Rack);
	}

	@Test
	public void testGetGroupID() {
		UUID testServerGroupID = ash13Server.getGroupID();
		System.out.println("testServerGroupID:" + testServerGroupID);
		assertNotNull(testServerGroupID);
	}

	@Test
	public void testGetGroupName() {
		String testServerGroupName = ash13Server.getGroupName();
		System.out.println("testServerGroupName:" + testServerGroupName);
		assertNotNull(testServerGroupName);
		assertEquals(testServerGroupName, serverGroupName);
	}

	@Test
	public void testGetAssemblyGroupType() {
		GroupType testGroupType = ash13Server.getGroupType();
		System.out.println("testGroupType:" + testGroupType);
		assertNotNull(testGroupType);
		assertEquals(testGroupType, GroupType.ASSEMBLY);
	}

	@Test
	public void testGetAlternateGroupType() {
		GroupType testGroupType = cables.getGroupType();
		System.out.println("testGroupType:" + testGroupType);
		assertNotNull(testGroupType);
		assertEquals(testGroupType, GroupType.ALTERNATIVE);
	}

	@Test
	public void testGetAnAssemblyGroupDetailLine() throws ParseException {
		entryDate = sdf.parse("01/01/2014");
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", GroupType.ASSEMBLY, rackGroupName);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add server", 1, 
				MemberType.SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		SortedSet<HyveProductGroupMember> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==1);
		assertEquals(ash13Server.getGroupID(), details.first().getSubGroupID());
		assertEquals(ash13Server.getGroupID(), details.last().getSubGroupID());
	}

	@Test
	public void testGetAnAlternativeGroupDetailLine() throws ParseException {
		entryDate = sdf.parse("01/01/2014");
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", GroupType.ASSEMBLY, rackGroupName);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add server", 1, 
				MemberType.SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add cable", 2, 
				MemberType.SUB_GROUP, "ASH 13 cable", cables.getGroupID(), 0, 0, 0);
		SortedSet<HyveProductGroupMember> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==2);
		System.out.println(details.first().getClass());
		assertEquals(ash13Server.getGroupID(), details.first().getSubGroupID());
		System.out.println(details.last().getClass());
		assertEquals(cables.getGroupID(), details.last().getSubGroupID());
	}

	@Test
	public void testGetGroupDetailsAtGivenTime() throws ParseException {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("01/01/2014");
		Date date2 = sdf.parse("01/02/2014");
		Date date3 = sdf.parse("01/03/2014");
		Date date4 = sdf.parse("01/04/2014");
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, date1, "Init Rack Test", GroupType.ASSEMBLY, rackGroupName);
		HyveProductGroupLog ash13Server = new HyveProductGroupLog(entryID, date2, "Init Test", GroupType.ASSEMBLY, serverGroupName);
		HyveProductGroupLog cables = new HyveProductGroupLog(entryID, date3, "Init Test", GroupType.ALTERNATIVE, cableGroupName);
		ash13Rack.addGroupDetail(entryID, date2, "test add server", 1, 
				MemberType.SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		ash13Rack.addGroupDetail(entryID, date3, "test add cable", 2, 
				MemberType.SUB_GROUP, "ASH 13 cable", cables.getGroupID(), 0, 0, 0);
		ash13Rack.deleteGroupDetail(entryID, date4, "test remove server", 1);

		SortedSet<HyveProductGroupMember> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==1);
		System.out.println(details.first().getClass());
		assertEquals(cables.getGroupID(), details.first().getSubGroupID());

		SortedSet<HyveProductGroupMember> detailsAtDate1 = ash13Rack.getGroupDetailsAtGivenTime(date1); 
		assertNotNull(detailsAtDate1);
		assertTrue(detailsAtDate1.size()==0);
		
		SortedSet<HyveProductGroupMember> detailsAtDate2 = ash13Rack.getGroupDetailsAtGivenTime(date2); 
		assertNotNull(detailsAtDate2);
		assertTrue(detailsAtDate2.size()==1);
		System.out.println("date 2, first line:" + detailsAtDate2.first().getClass());
		assertEquals(ash13Server.getGroupID(), detailsAtDate2.first().getSubGroupID());

		SortedSet<HyveProductGroupMember> detailsAtDate3 = ash13Rack.getGroupDetailsAtGivenTime(date3); 
		assertNotNull(detailsAtDate3);
		assertTrue(detailsAtDate3.size()==2);
		System.out.println("date 3, first line:" + detailsAtDate3.first().getClass());
		assertEquals(ash13Server.getGroupID(), detailsAtDate3.first().getSubGroupID());
	
		System.out.println("date 3, last line:" + detailsAtDate3.last().getClass());
		assertEquals(cables.getGroupID(), detailsAtDate3.last().getSubGroupID());

		SortedSet<HyveProductGroupMember> detailsAtDate4 = ash13Rack.getGroupDetailsAtGivenTime(date4); 
		assertNotNull(detailsAtDate4);
		assertTrue(detailsAtDate4.size()==1);
		System.out.println("date 4, first line:" + detailsAtDate4.first().getClass());
		assertEquals(cables.getGroupID(), detailsAtDate4.first().getSubGroupID());
}

	@Test
	public void testGetGroupTags() {
		String tagValue = ash13Server.getTagValueAtGivenTime(TagType.GROUP_NAME, entryDate);
		assertNotNull(tagValue);
		assertEquals(tagValue, serverGroupName);
	}

	@Test
	public void testGetGroupTagsAtGivenTime() throws ParseException {
		entryDate = sdf.parse("01/02/2014");
		ash13Server.addTag(TagType.GROUP_NAME, null, entryID, entryDate, "Null group name");
		entryDate = sdf.parse("01/03/2014");
		String tagValue = ash13Server.getTagValueAtGivenTime(TagType.GROUP_NAME, entryDate);
		assertNull(tagValue);
		entryDate = sdf.parse("01/04/2014");
		ash13Server.addTag(TagType.GROUP_NAME, serverGroupName, entryID, entryDate, "New group name");
		entryDate = sdf.parse("01/05/2014");
		tagValue = ash13Server.getTagValueAtGivenTime(TagType.GROUP_NAME, entryDate);
		assertNotNull(tagValue);
		assertEquals(tagValue, serverGroupName);
	}

	@Test
	public void testDeleteroupTags() throws ParseException {
		String tagValue = ash13Server.getTagValue(TagType.GROUP_NAME);
		assertNotNull(tagValue);
		assertEquals(tagValue, serverGroupName);
		entryDate = sdf.parse("01/02/2014");
		ash13Server.deleteTag(TagType.GROUP_NAME, entryID, entryDate, "Null group name");
		entryDate = sdf.parse("01/03/2014");
		tagValue = ash13Server.getTagValue(TagType.GROUP_NAME);
		assertNull(tagValue);
		entryDate = sdf.parse("01/04/2014");
		ash13Server.addTag(TagType.GROUP_NAME, "new Ash 13 Server", entryID, entryDate, "New group name");
		entryDate = sdf.parse("01/05/2014");
		tagValue = ash13Server.getTagValue(TagType.GROUP_NAME);
		assertNotNull(tagValue);
		assertEquals(tagValue, "new Ash 13 Server");
		tagValue = ash13Server.getTagValueAtGivenTime(TagType.GROUP_NAME, sdf.parse("01/03/2014"));
		assertNull(tagValue);
	}

	@Test
	public void testSetGroupName() {
		ash13Server.addTag(TagType.GROUP_NAME, null, 4739, new Date(), "allow null group name");
		ash13Server.addTag(TagType.GROUP_NAME, "ASH13", 4739, new Date(), "reset group name");
	}

	@Test
	public void testSetGroupType() {
		try {
			ash13Server.addTag(TagType.GROUP_TYPE, null, 4739, new Date(), "Null Group Type");
			fail("Show throw exception");
		} catch (NullGroupTypeException e) {
			System.out.println(e.getMessage());
		}

		try {
			ash13Server.addTag(TagType.GROUP_TYPE, "BOM", 4739, new Date(), "Invalid Group Type");
			fail("Show throw exception");
		} catch (InvalidGroupTypeException e) {
			System.out.println(e.getMessage());
		}
	}

	private HyveProductGroup addGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		HyveProductGroup prodGroup = new HyveProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13Server.addGroupDetail(entryID, entryDate, entryComment, 0, memberType, lineComment, 
				prodGroup.getGroupID(), 0, minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private HyveProductGroup insertGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, GroupType groupType, String groupName, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		HyveProductGroup prodGroup = new HyveProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13Server.addGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				prodGroup.getGroupID(), lineNo, minBOMQty, maxBOMQty);
		return prodGroup;
	}
	
	private void updateGroupDetailToAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo, UUID subGroupID, MemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty) {
		ash13Server.updateGroupDetail(entryID, entryDate, entryComment, lineNo, memberType, lineComment, 
				subGroupID, 0, minBOMQty, maxBOMQty);
	}

	private void deleteGroupDetailFromAshServer(int entryID, Date entryDate, String entryComment, 
			int lineNo) {
		ash13Server.deleteGroupDetail(entryID, entryDate, entryComment, lineNo); 
	}

	@Test
	public void testWholeServer() throws ParseException {
		String comment;
		SortedSet<HyveProductGroupMember> details;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		Date date1 = sdf.parse("01/01/2014");
		cal.setTime(date1);
		
		comment = "day 1, empty group";
		entryDate = cal.getTime();
		ash13Server.addTag(TagType.FAMILY, "ASH", entryID, entryDate, "add family");
		ash13Server.addTag(TagType.MODEL, "13", entryID, entryDate, "add model");
		details = ash13Server.getGroupDetailsWithGivenTag(TagType.FAMILY, "ASH"); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		Map<TagType, String> tags = ash13Server.getGroupTags();
		assertEquals(tags.get(TagType.FAMILY), "ASH");
		assertEquals(tags.get(TagType.MODEL), "13");
		
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		printGroupDetails(comment, details);

		comment = "day2, add chassis";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		addGroupDetailToAshServer(entryID, entryDate, comment,
				GroupType.ASSEMBLY, chassisGroupName, MemberType.SUB_GROUP,
				"chassis sub bom", 1, 1);
			
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		printGroupDetails(comment, details);
		
		comment = "day3, add motherboard";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		addGroupDetailToAshServer(entryID, entryDate, comment,
				GroupType.ALTERNATIVE, motherboardGroupName, MemberType.SUB_GROUP,
				"motherboard sub bom", 1, 1);
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		printGroupDetails(comment, details);
		
		comment = "day4, add ssd";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		HyveProductGroup ssd = addGroupDetailToAshServer(entryID, entryDate, comment,
				GroupType.ALTERNATIVE, ssdGroupName, MemberType.SUB_GROUP,
				"ssd sub bom", 2, 2);
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 3);
		printGroupDetails(comment, details);
		
		comment = "day5, delete ssd";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		int lineNo = getLineNo(details, ssd);
		
		deleteGroupDetailFromAshServer(entryID, entryDate, comment, lineNo);
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 2);
		printGroupDetails(comment, details);
		
		comment = "day6, add processor";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		HyveProductGroup processor = addGroupDetailToAshServer(entryID, entryDate, comment,
				GroupType.ALTERNATIVE, processorGroupName, MemberType.SUB_GROUP,
				"processor sub bom", 1, 1);
		
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 3);
		printGroupDetails(comment, details);
		
		lineNo = getLineNo(details, processor);
		assertEquals(lineNo, 3);
		
		comment = "day7, update processor";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		updateGroupDetailToAshServer(entryID, entryDate, comment,
				lineNo, processor.getGroupID(), MemberType.SUB_GROUP,
				"processor sub bom", 2, 2);

		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(),3);
		lineNo = getLineNo(details, processor);
		assertEquals(lineNo, 3);
		printGroupDetails(comment, details);
		
		comment = "day8, add memory";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		HyveProductGroup memory = addGroupDetailToAshServer(entryID, entryDate, comment,
				GroupType.ALTERNATIVE, memoryGroupName, MemberType.SUB_GROUP,
				"memory sub bom", 2, 4);

		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(),4);
		
		lineNo = getLineNo(details, memory);
		assertEquals(lineNo, 4);
		printGroupDetails(comment, details);
		
		comment = "day9, update memory";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		updateGroupDetailToAshServer(entryID, entryDate, comment,
				lineNo, memory.getGroupID(), MemberType.SUB_GROUP,
				"memory sub bom", 2, 2);

		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(),4);
		
		lineNo = getLineNo(details, memory);
		assertEquals(lineNo, 4);
		lineNo = getLineNo(details, processor);
		assertEquals(lineNo, 3);
		printGroupDetails(comment, details);

		comment = "day10, update processor";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		
		updateGroupDetailToAshServer(entryID, entryDate, comment,
				lineNo, processor.getGroupID(), MemberType.SUB_GROUP,
				"processor sub bom", 1, 2);

		details = ash13Server.getGroupDetailsAtGivenTime(entryDate); 
		lineNo = getLineNo(details, processor);
		assertEquals(lineNo, 3);
		
		printGroupDetails(comment, details);

		comment = "day11, insert hard drive at line 3";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		HyveProductGroup harddrive = insertGroupDetailToAshServer(entryID, entryDate, comment,
				3, GroupType.ALTERNATIVE, harddriveGroupName, MemberType.SUB_GROUP,
				"hard drive sub bom", 4, 4);

		assertNotNull(harddrive);
		
		details = ash13Server.getGroupDetailsAtGivenTime(entryDate);
		printGroupDetails(comment, details);
		assertEquals(details.size(),5);
		lineNo = getLineNo(details, harddrive);
		assertEquals(lineNo, 3);
		lineNo = getLineNo(details, processor);
		assertEquals(lineNo, 4);

		printDetailLogs(ash13Server);
		printTagLogs(ash13Server);
	}

	private void addGroupDetailToCables(int entryID, Date entryDate, String entryComment, int skuNo) {
		cables.addGroupDetail(entryID, entryDate, entryComment, 0, MemberType.MATERIAL, entryComment, 
				null, skuNo, 1, 1);
	}

	private void updateGroupDetailToCables(int entryID, Date entryDate, String entryComment, int skuNo) {
		int lineNo = getLine(cables.getGroupDetails(), skuNo);
		cables.updateGroupDetail(entryID, entryDate, entryComment, lineNo, MemberType.MATERIAL, entryComment, 
				null, skuNo, 1, 1);
	}

	private void deleteGroupDetailFromCables(int entryID, Date entryDate, String entryComment, int skuNo) {
		int lineNo = getLine(cables.getGroupDetails(), skuNo);
		cables.deleteGroupDetail(entryID, entryDate, entryComment, lineNo);
	}

	private void printGroupDetails(String comment, SortedSet<HyveProductGroupMember> details) {
		System.out.println("-----------------Comment:" + comment + "-----------------------");
		for (HyveProductGroupMember detail:details) {
			System.out.println(detail);
		}
	}
	
	private void printDetailLogs(HyveProductGroupLog groupLog) {
		System.out.println("------------------Member Log-------------------------");
		for (String detailLog:groupLog.getDetailLogs()) {
			System.out.println("[MEMBER_LOG]:" + detailLog);
		}
	}
	
	private void printTagLogs(HyveProductGroupLog groupLog) {
		System.out.println("------------------Tag Log-------------------------");
		for (String tagLog:groupLog.getTagLogs()) {
			System.out.println("[TAG_LOG]:" + tagLog);
		}
	}
	
	@Test
	public void testAltCables() throws ParseException {
		String comment;
		SortedSet<HyveProductGroupMember> details;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		Date date1 = sdf.parse("01/01/2014");
		cal.setTime(date1);
		
		comment = "day 1, empty group";
		entryDate = cal.getTime();
		cables.addTag(TagType.FAMILY, "ASH", entryID, entryDate, "add family");
		cables.addTag(TagType.MODEL, "13", entryID, entryDate, "add model");
		details = cables.getGroupDetailsWithGivenTag(TagType.FAMILY, "ASH"); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		Map<TagType, String> tags = cables.getGroupTags();
		assertEquals(tags.get(TagType.FAMILY), "ASH");
		assertEquals(tags.get(TagType.MODEL), "13");
		
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		printGroupDetails(comment, details);

		comment = "day2, add alt cable 1";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		addGroupDetailToCables(entryID, entryDate, comment, cableAltPart1);
			
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		printGroupDetails(comment, details);
		
		comment = "day3, add alt cable 2";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		addGroupDetailToCables(entryID, entryDate, comment, cableAltPart2);
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		assertEquals(details.size(), 2);
		printGroupDetails(comment, details);
		
		comment = "day4, delete alt cable 1";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		int lineNo = getLine(details, cableAltPart1);
		assertEquals(lineNo, 1);
		deleteGroupDetailFromCables(entryID, entryDate, comment, cableAltPart1);
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 1);
		printGroupDetails(comment, details);
		
		comment = "day5, add alt cable 1 again";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		addGroupDetailToCables(entryID, entryDate, comment, cableAltPart1);
		printGroupDetails("before add back, " + comment, details);
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 2);
		lineNo = getLine(details, cableAltPart1);
		assertEquals(lineNo, 3);
		printGroupDetails("after add back, " + comment, details);
		
		comment = "day6, update alt cable 2";
		cal.add(Calendar.DATE, 1);
		entryDate = cal.getTime();
		updateGroupDetailToCables(entryID, entryDate, comment,cableAltPart2);
		details = cables.getGroupDetailsAtGivenTime(entryDate); 
		assertEquals(details.size(), 2);
		lineNo = getLine(details, cableAltPart2);
		assertEquals(lineNo, 2);
		printGroupDetails(comment, details);
		
		printDetailLogs(cables);
		printTagLogs(cables);
	}

	private int getLine(SortedSet<HyveProductGroupMember> details, int skuNo) {
		int lineNo;
		lineNo = 0;
		for (HyveProductGroupMember detail:details) {
				if (detail.getSkuNo() == skuNo) {
					lineNo = detail.getLineNo();
					break;
				}
		}
		return lineNo;
	}
	
	private int getLineNo(SortedSet<HyveProductGroupMember> details,
			HyveProductGroup detailGroup) {
		int lineNo;
		lineNo = 0;
		for (HyveProductGroupMember detail:details) {
			if (detail.getSubGroupID() != null  && detail.getSubGroupID().equals(detailGroup.getGroupID())) {
				lineNo = detail.getLineNo();
				break;
			}
		}
		return lineNo;
	}

}
