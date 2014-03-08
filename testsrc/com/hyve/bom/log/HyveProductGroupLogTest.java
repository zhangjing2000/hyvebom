package com.hyve.bom.log;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hyve.bom.concept.HyveAlternativeGroupDetail;
import com.hyve.bom.concept.HyveAssemblyGroupDetail;
import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.concept.HyveProductGroupDetail;
import com.hyve.bom.concept.HyveProductGroupMemberType;
import com.hyve.bom.concept.HyveProductGroupTagType;
import com.hyve.bom.concept.HyveProductGroupType;
import com.hyve.bom.log.HyveProductGroupLog;

public class HyveProductGroupLogTest {
	
	private HyveProductGroupLog ash13Server;
	private HyveProductGroupLog cables;
	private static String rackGroupName = "Ash13Rack";
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String ssdGroupName = "SSD";
	private static String hdGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";
	private static String cableGroupName = "Cable";
	private static int entryID = 4739;
	private static Date entryDate = new Date();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ash13Server = new HyveProductGroupLog(entryID, entryDate, "Init Test", HyveProductGroupType.ASSEMBLY, serverGroupName);
		cables = new HyveProductGroupLog(entryID, entryDate, "Init Test", HyveProductGroupType.ALTERNATIVE, cableGroupName);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHyveProductGroupLog() {
		HyveProductGroup ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", HyveProductGroupType.ASSEMBLY, rackGroupName);
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
		HyveProductGroupType testGroupType = ash13Server.getGroupType();
		System.out.println("testGroupType:" + testGroupType);
		assertNotNull(testGroupType);
		assertEquals(testGroupType, HyveProductGroupType.ASSEMBLY);
	}

	@Test
	public void testGetAlternateGroupType() {
		HyveProductGroupType testGroupType = cables.getGroupType();
		System.out.println("testGroupType:" + testGroupType);
		assertNotNull(testGroupType);
		assertEquals(testGroupType, HyveProductGroupType.ALTERNATIVE);
	}

	@Test
	public void testGetAnAssemblyGroupDetailLine() {
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", HyveProductGroupType.ASSEMBLY, rackGroupName);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add server", 1, 
				HyveProductGroupMemberType.ASSEMBLY_SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		SortedSet<HyveProductGroupDetail> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==1);
		assertTrue(details.first() instanceof HyveAssemblyGroupDetail);
		assertEquals(ash13Server.getGroupID(), ((HyveAssemblyGroupDetail)details.first()).getSubGroupID());
		assertTrue(details.last() instanceof HyveAssemblyGroupDetail);
		assertEquals(ash13Server.getGroupID(), ((HyveAssemblyGroupDetail)details.last()).getSubGroupID());
	}

	@Test
	public void testGetAnAlternativeGroupDetailLine() {
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, entryDate, "Init Rack Test", HyveProductGroupType.ASSEMBLY, rackGroupName);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add server", 1, 
				HyveProductGroupMemberType.ASSEMBLY_SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		ash13Rack.addGroupDetail(entryID, entryDate, "test add cable", 2, 
				HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP, "ASH 13 cable", cables.getGroupID(), 0, 0, 0);
		SortedSet<HyveProductGroupDetail> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==2);
		System.out.println(details.first().getClass());
		assertTrue(details.first() instanceof HyveAssemblyGroupDetail);
		assertEquals(ash13Server.getGroupID(), ((HyveAssemblyGroupDetail)details.first()).getSubGroupID());
		System.out.println(details.last().getClass());
		assertTrue(details.last() instanceof HyveAlternativeGroupDetail);
		assertEquals(cables.getGroupID(), ((HyveAlternativeGroupDetail)details.last()).getSubGroupID());
	}

	@Test
	public void testGetGroupDetailsAtGivenTime() throws ParseException {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("01/01/2014");
		Date date2 = sdf.parse("01/02/2014");
		Date date3 = sdf.parse("01/03/2014");
		Date date4 = sdf.parse("01/04/2014");
		HyveProductGroupLog ash13Rack = new HyveProductGroupLog(entryID, date1, "Init Rack Test", HyveProductGroupType.ASSEMBLY, rackGroupName);
		HyveProductGroupLog ash13Server = new HyveProductGroupLog(entryID, date2, "Init Test", HyveProductGroupType.ASSEMBLY, serverGroupName);
		HyveProductGroupLog cables = new HyveProductGroupLog(entryID, date3, "Init Test", HyveProductGroupType.ALTERNATIVE, cableGroupName);
		ash13Rack.addGroupDetail(entryID, date2, "test add server", 1, 
				HyveProductGroupMemberType.ASSEMBLY_SUB_GROUP, "ASH 13 server", ash13Server.getGroupID(), 0, 10, 10);
		ash13Rack.addGroupDetail(entryID, date3, "test add cable", 2, 
				HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP, "ASH 13 cable", cables.getGroupID(), 0, 0, 0);
		ash13Rack.deleteGroupDetail(entryID, date4, "test remove server", 1);

		SortedSet<HyveProductGroupDetail> details = ash13Rack.getGroupDetails(); 
		assertNotNull(details);
		assertTrue(details.size()==1);
		System.out.println(details.first().getClass());
		assertTrue(details.first() instanceof HyveAlternativeGroupDetail);
		assertEquals(cables.getGroupID(), ((HyveAlternativeGroupDetail)details.first()).getSubGroupID());

		SortedSet<HyveProductGroupDetail> detailsAtDate1 = ash13Rack.getGroupDetailsAtGivenTime(date1); 
		assertNotNull(detailsAtDate1);
		assertTrue(detailsAtDate1.size()==0);
		
		SortedSet<HyveProductGroupDetail> detailsAtDate2 = ash13Rack.getGroupDetailsAtGivenTime(date2); 
		assertNotNull(detailsAtDate2);
		assertTrue(detailsAtDate2.size()==1);
		System.out.println("date 2, first line:" + detailsAtDate2.first().getClass());
		assertTrue(detailsAtDate2.first() instanceof HyveAssemblyGroupDetail);
		assertEquals(ash13Server.getGroupID(), ((HyveAssemblyGroupDetail)detailsAtDate2.first()).getSubGroupID());

		SortedSet<HyveProductGroupDetail> detailsAtDate3 = ash13Rack.getGroupDetailsAtGivenTime(date3); 
		assertNotNull(detailsAtDate3);
		assertTrue(detailsAtDate3.size()==2);
		System.out.println("date 3, first line:" + detailsAtDate3.first().getClass());
		assertTrue(detailsAtDate3.first() instanceof HyveAssemblyGroupDetail);
		assertEquals(ash13Server.getGroupID(), ((HyveAssemblyGroupDetail)detailsAtDate3.first()).getSubGroupID());
	
		System.out.println("date 3, last line:" + detailsAtDate3.last().getClass());
		assertTrue(detailsAtDate3.last() instanceof HyveAlternativeGroupDetail);
		assertEquals(cables.getGroupID(), ((HyveAlternativeGroupDetail)detailsAtDate3.last()).getSubGroupID());

		SortedSet<HyveProductGroupDetail> detailsAtDate4 = ash13Rack.getGroupDetailsAtGivenTime(date4); 
		assertNotNull(detailsAtDate4);
		assertTrue(detailsAtDate4.size()==1);
		System.out.println("date 4, first line:" + detailsAtDate4.first().getClass());
		assertTrue(detailsAtDate4.first() instanceof HyveAlternativeGroupDetail);
		assertEquals(cables.getGroupID(), ((HyveAlternativeGroupDetail)detailsAtDate4.first()).getSubGroupID());
}

	@Test
	public void testGetGroupTags() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGroupTagsAtGivenTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGroupName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGroupType() {
		fail("Not yet implemented");
	}

	private HyveProductGroup addGroupDetailAndRevisionToAshServer(int entryID, Date entryDate, String entryComment, 
			HyveProductGroupType groupType, String groupName, HyveProductGroupMemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty, String revision) {
		HyveProductGroup prodGroup = new HyveProductGroupLog(entryID, entryDate, entryComment, groupType, groupName);
		ash13Server.addGroupDetail(entryID, entryDate, entryComment, 0, memberType, lineComment, 
				prodGroup.getGroupID(), 0, minBOMQty, maxBOMQty);
		ash13Server.addTag(HyveProductGroupTagType.REVISION, revision, entryID, entryDate, revision);
		return prodGroup;
	}
	
	private void updateGroupDetailAndRevisionToAshServer(int entryID, Date entryDate, String entryComment, 
			UUID subGroupID, HyveProductGroupMemberType memberType, 
			String lineComment, int minBOMQty, int maxBOMQty, String revision) {
		ash13Server.updateGroupDetail(entryID, entryDate, entryComment, 0, memberType, lineComment, 
				subGroupID, 0, minBOMQty, maxBOMQty);
		ash13Server.addTag(HyveProductGroupTagType.REVISION, revision, entryID, entryDate, revision);
	}

	private SortedSet<HyveProductGroupDetail> getRevisionAndAssertNotNull(int revision) {
		SortedSet<HyveProductGroupDetail> details = ash13Server.getGroupDetailsWithGivenTag(HyveProductGroupTagType.REVISION, "revision " + revision); 
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		System.out.println("details.size:" + details.size());
		return details;
	}
	
	@Test
	public void testAddTag() throws ParseException {
		SortedSet<HyveProductGroupDetail> details;
		int revision;
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("01/01/2014");
		Date date2 = sdf.parse("01/02/2014");
		Date date3 = sdf.parse("01/03/2014");
		Date date4 = sdf.parse("01/04/2014");
		Date date5 = sdf.parse("01/05/2014");
		Date date6 = sdf.parse("01/06/2014");
		Date date7 = sdf.parse("01/07/2014");
		
		revision = 1;
		ash13Server.addTag(HyveProductGroupTagType.FAMILY, "ASH", entryID, date1, "add family");
		ash13Server.addTag(HyveProductGroupTagType.MODEL, "13", entryID, date1, "add model");
		ash13Server.addTag(HyveProductGroupTagType.REVISION, "revision " + revision, entryID, date1, "revision " + revision);
		details = ash13Server.getGroupDetailsWithGivenTag(HyveProductGroupTagType.FAMILY, "ASH"); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		Map<HyveProductGroupTagType, String> tags = ash13Server.getGroupTags();
		assertEquals(tags.get(HyveProductGroupTagType.FAMILY), "ASH");
		assertEquals(tags.get(HyveProductGroupTagType.MODEL), "13");
		assertEquals(tags.get(HyveProductGroupTagType.REVISION), "revision 1");
		
		details = ash13Server.getGroupDetailsWithGivenTag(HyveProductGroupTagType.REVISION, "revision 1"); 
		assertNotNull(details);
		assertTrue(details.isEmpty());
		
		revision++;
		addGroupDetailAndRevisionToAshServer(entryID, date2, "add chassis",
				HyveProductGroupType.ASSEMBLY, chassisGroupName, HyveProductGroupMemberType.ASSEMBLY_SUB_GROUP,
				"chassis sub bom", 1, 1, "revision " + revision);
			
		details = getRevisionAndAssertNotNull(revision);
		
		revision++;
		addGroupDetailAndRevisionToAshServer(entryID, date3, "add motherboard",
				HyveProductGroupType.ALTERNATIVE, motherboardGroupName, HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"motherboard sub bom", 1, 1, "revision " + revision);
		
		details = getRevisionAndAssertNotNull(revision);
		
		revision++;
		HyveProductGroup processor = addGroupDetailAndRevisionToAshServer(entryID, date3, "add processor",
				HyveProductGroupType.ALTERNATIVE, processorGroupName, HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 1, 1, "revision " + revision);
		
		details = getRevisionAndAssertNotNull(revision);
		
		revision++;
		updateGroupDetailAndRevisionToAshServer(entryID, date4, "update processor",
				processor.getGroupID(), HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 2, 2, "revision " + revision);

		details = getRevisionAndAssertNotNull(revision);
		
		revision++;
		HyveProductGroup memory = addGroupDetailAndRevisionToAshServer(entryID, date5, "add memory",
				HyveProductGroupType.ALTERNATIVE, memoryGroupName, HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"memory sub bom", 2, 4, "revision " + revision);

		details = getRevisionAndAssertNotNull(revision);
		
		revision++;
		updateGroupDetailAndRevisionToAshServer(entryID, date6, "update memory",
				memory.getGroupID(), HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"memory sub bom", 2, 2, "revision " + revision);

		details = getRevisionAndAssertNotNull(revision);

		revision++;
		updateGroupDetailAndRevisionToAshServer(entryID, date7, "update processor",
				processor.getGroupID(), HyveProductGroupMemberType.ALTERNATIVE_SUB_GROUP,
				"processor sub bom", 1, 2, "revision 8");

		details = getRevisionAndAssertNotNull(revision);
		for (HyveProductGroupDetail detail:details) {
			System.out.println("lineNo:" + detail.getLineNo() + ",comment:"+detail.getLineComment());
		}
	}

}
