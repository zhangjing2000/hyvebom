package com.hyve.plan.service.optaplanner;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import com.hyve.bom.concept.HyveProductGroup;
import com.hyve.bom.concept.MemberType;
import com.hyve.bom.concept.GroupType;
import com.hyve.bom.log.LoggedHyveProductGroup;
import com.hyve.bom.service.HyveProductGroupService;
import com.hyve.bom.service.HyveProductGroupServiceImpl;
import com.hyve.log.common.LogEntry;
import com.hyve.plan.concept.HyveContract;
import com.hyve.plan.concept.HyvePlan;
import com.hyve.plan.concept.HyvePlant;
import com.hyve.plan.mps.LoggedContractMPSImpl;
import com.hyve.plan.mps.MPSEntry;
import com.hyve.plan.mrp.LoggedContractMRPImpl;
import com.hyve.plan.mrp.MRPEntry;
import com.hyve.plan.service.optaplanner.MPSReadinessCheckSolution;

public class HyvePlanTest {
	
	private static String rackGroupName = "Ash13Rack";
	private static String serverGroupName = "Ash13Server";
	private static String chassisGroupName ="Chassis";
	private static String motherboardGroupName ="Motherboard";
	private static String ssdGroupName = "SSD";
	private static String harddriveGroupName = "HardDrive";
	private static String processorGroupName = "Processor";
	private static String memoryGroupName = "Memory";
	private static String cableGroupName = "Cable";
	private static String powerGroupName = "Power";
	private static String screwsGroupName = "Screws";
	private static int processorPart = 100001;
	private static int harddrivePart = 100002;
	private static int motherboardPart = 100003;
	private static int ssdPart = 100004;
	private static int memoryPart1 = 100005;
	private static int memoryPart2 = 100006;
	private static int cableAltPart1 = 100101;
	private static int cableAltPart2 = 100102;
	private static int powerPart = 100201;
	private static int screwAltPart1 = 100301;
	private static int screwAltPart2 = 100302;
	private static int entryID = 4739;
	private static DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static Date entryDate; 
	
	private HyveContract contract;
	private Solver solver;
	private HyveProductGroupService bomService;
	private LogEntry logEntry;
	
	private HyvePlant plant = new HyvePlant(1, "MFG", "US");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		entryDate = sdf.parse("01/01/2014");
		contract = new HyveContract(1,1);
		bomService = new HyveProductGroupServiceImpl();
		logEntry = new LogEntry() {
			public int getLogUserID() {
				return entryID;
			}
			public Date getLogDate() {
				return entryDate;
			}
			public String getLogComment() {
				return "test";
			}
		};
		initSolver();
	}

	private void initSolver() {
		 SolverFactory solverFactory = new XmlSolverFactory(
	                "/com/hyve/plan/mps/MPSToMRPSolverConfig.xml");
		 solver = solverFactory.buildSolver();		
	}
	
	private HyveProductGroup initAFullRackBOM() {
		LoggedHyveProductGroup rack = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, rackGroupName);
		LoggedHyveProductGroup server = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, serverGroupName);
		LoggedHyveProductGroup cables = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, cableGroupName);
		LoggedHyveProductGroup power = initPowerAltGroup();
		LoggedHyveProductGroup screws = initScrewsAltGroup();

		rack.addGroupDetail(entryID, entryDate, "Add Servers", 1, MemberType.SUB_GROUP, "Servers", server.getGroupID(), 0, 10, 20);
		rack.addGroupDetail(entryID, entryDate, "Add Cables", 2, MemberType.SUB_GROUP, "Cables", cables.getGroupID(), 0, 1, 1);
		rack.addGroupDetail(entryID, entryDate, "Add Power", 3, MemberType.SUB_GROUP, "Power", power.getGroupID(), 0, 1, 1);
		rack.addGroupDetail(entryID, entryDate, "Add Screws", 4, MemberType.SUB_GROUP, "Screws", screws.getGroupID(), 0, 10, 20);
		
		LoggedHyveProductGroup chassis = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, chassisGroupName);
		LoggedHyveProductGroup motherboard = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, motherboardGroupName);
		LoggedHyveProductGroup processor = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, processorGroupName);
		LoggedHyveProductGroup memory = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, memoryGroupName);
		LoggedHyveProductGroup harddrive = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, harddriveGroupName);
		LoggedHyveProductGroup ssd = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, ssdGroupName);

		server.addGroupDetail(entryID, entryDate, "Add Chassis", 1, MemberType.SUB_GROUP, "Chassis", chassis.getGroupID(), 0, 1, 1);
		chassis.addGroupDetail(entryID, entryDate, "Add Screw", 1, MemberType.MATERIAL, "Screw", screws.getGroupID(), 0, 5, 5);
		chassis.addGroupDetail(entryID, entryDate, "Add Power", 2, MemberType.SUB_GROUP, "Power", power.getGroupID(), 0, 1, 1);
		
		server.addGroupDetail(entryID, entryDate, "Add Motherboard", 2, MemberType.SUB_GROUP, "motherboard", motherboard.getGroupID(), 0, 1, 1);
		server.addGroupDetail(entryID, entryDate, "Add Processor", 3, MemberType.SUB_GROUP, "Processor", processor.getGroupID(), 0, 1, 1);
		server.addGroupDetail(entryID, entryDate, "Add Memory", 4, MemberType.SUB_GROUP, "Memory", memory.getGroupID(), 0, 2, 2);
		server.addGroupDetail(entryID, entryDate, "Add Harddrive",5, MemberType.SUB_GROUP, "Harddrive", harddrive.getGroupID(), 0, 2, 2);
		server.addGroupDetail(entryID, entryDate, "Add SSD", 6, MemberType.SUB_GROUP, "SSD", ssd.getGroupID(), 0, 1, 1);
		
		cables.addGroupDetail(entryID, entryDate, "Add Cable1", 1, MemberType.MATERIAL, "cable1", null, cableAltPart1, 1, 1);
		cables.addGroupDetail(entryID, entryDate, "Add Cable2", 2, MemberType.MATERIAL, "cable2", null, cableAltPart2, 1, 1);
		
		processor.addGroupDetail(entryID, entryDate, "Add Processor", 1, MemberType.MATERIAL, "Processor Part", null, processorPart, 1, 1);

		motherboard.addGroupDetail(entryID, entryDate, "Add Motherboard part", 1, MemberType.MATERIAL, "Motherboard Part", null, motherboardPart, 1, 1);
		
		memory.addGroupDetail(entryID, entryDate, "Add Memory part", 1, MemberType.MATERIAL, "Memory Part", null, memoryPart1, 1, 1);
		memory.addGroupDetail(entryID, entryDate, "Add Memory part", 2, MemberType.MATERIAL, "Memory Part", null, memoryPart2, 1, 1);
		
		harddrive.addGroupDetail(entryID, entryDate, "Add Hard drive part", 1, MemberType.MATERIAL, "HardDrive Part", null, harddrivePart, 1, 1);

		ssd.addGroupDetail(entryID, entryDate, "Add SSD part", 1, MemberType.MATERIAL, "SSD Part", null, ssdPart, 1, 1);
		return bomService.createHyveProductGroupSnapshot(rack, entryDate, "1.0");
	}

	private HyveProductGroup initAServerBOM() {
		LoggedHyveProductGroup server = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, serverGroupName);
		LoggedHyveProductGroup cables = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, cableGroupName);
		LoggedHyveProductGroup power = initPowerAltGroup();
		LoggedHyveProductGroup screws = initScrewsAltGroup();

		LoggedHyveProductGroup chassis = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, chassisGroupName);
		LoggedHyveProductGroup motherboard = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, motherboardGroupName);
		LoggedHyveProductGroup processor = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, processorGroupName);
		LoggedHyveProductGroup memory = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, memoryGroupName);
		LoggedHyveProductGroup harddrive = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, harddriveGroupName);
		LoggedHyveProductGroup ssd = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, ssdGroupName);

		server.addGroupDetail(entryID, entryDate, "Add Chassis", 1, MemberType.SUB_GROUP, "Chassis", chassis.getGroupID(), 0, 1, 1);
		chassis.addGroupDetail(entryID, entryDate, "Add Screw", 1, MemberType.MATERIAL, "Screw", screws.getGroupID(), 0, 5, 5);
		chassis.addGroupDetail(entryID, entryDate, "Add Power", 2, MemberType.SUB_GROUP, "Power", power.getGroupID(), 0, 1, 1);
		
		server.addGroupDetail(entryID, entryDate, "Add Motherboard", 2, MemberType.SUB_GROUP, "motherboard", motherboard.getGroupID(), 0, 1, 1);
		server.addGroupDetail(entryID, entryDate, "Add Processor", 3, MemberType.SUB_GROUP, "Processor", processor.getGroupID(), 0, 1, 1);
		server.addGroupDetail(entryID, entryDate, "Add Memory", 4, MemberType.SUB_GROUP, "Memory", memory.getGroupID(), 0, 2, 2);
		server.addGroupDetail(entryID, entryDate, "Add Harddrive",5, MemberType.SUB_GROUP, "Harddrive", harddrive.getGroupID(), 0, 2, 2);
		server.addGroupDetail(entryID, entryDate, "Add SSD", 6, MemberType.SUB_GROUP, "SSD", ssd.getGroupID(), 0, 1, 1);
		
		cables.addGroupDetail(entryID, entryDate, "Add Cable1", 1, MemberType.MATERIAL, "cable1", null, cableAltPart1, 1, 1);
		cables.addGroupDetail(entryID, entryDate, "Add Cable2", 2, MemberType.MATERIAL, "cable2", null, cableAltPart2, 1, 1);
		
		processor.addGroupDetail(entryID, entryDate, "Add Processor", 1, MemberType.MATERIAL, "Processor Part", null, processorPart, 1, 1);

		motherboard.addGroupDetail(entryID, entryDate, "Add Motherboard part", 1, MemberType.MATERIAL, "Motherboard Part", null, motherboardPart, 1, 1);
		
		memory.addGroupDetail(entryID, entryDate, "Add Memory part", 1, MemberType.MATERIAL, "Memory Part", null, memoryPart1, 1, 1);
		memory.addGroupDetail(entryID, entryDate, "Add Memory part", 2, MemberType.MATERIAL, "Memory Part", null, memoryPart2, 1, 1);
		
		harddrive.addGroupDetail(entryID, entryDate, "Add Hard drive part", 1, MemberType.MATERIAL, "HardDrive Part", null, harddrivePart, 1, 1);

		ssd.addGroupDetail(entryID, entryDate, "Add SSD part", 1, MemberType.MATERIAL, "SSD Part", null, ssdPart, 1, 1);
		return bomService.createHyveProductGroupSnapshot(server, entryDate, "1.0");
	}

	private HyveProductGroup initAChassisBOM() {
		LoggedHyveProductGroup power = initPowerAltGroup();
		LoggedHyveProductGroup screws = initScrewsAltGroup();

		LoggedHyveProductGroup chassis = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ASSEMBLY, chassisGroupName);

		chassis.addGroupDetail(entryID, entryDate, "Add Screw", 1, MemberType.SUB_GROUP, "Screw", screws.getGroupID(), 0, 5, 5);
		chassis.addGroupDetail(entryID, entryDate, "Add Power", 2, MemberType.SUB_GROUP, "Power", power.getGroupID(), 0, 1, 1);
		
		return bomService.createHyveProductGroupSnapshot(chassis, entryDate, "1.0");
	}
	
	private LoggedHyveProductGroup initPowerAltGroup() {
		LoggedHyveProductGroup power = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, powerGroupName);
		power.addGroupDetail(entryID, entryDate, "Add Power", 1, MemberType.MATERIAL, "Power Part", null, powerPart, 1, 1);
		return power;
	}
	
	private LoggedHyveProductGroup initScrewsAltGroup() {
		LoggedHyveProductGroup screws = bomService.createHyveProductGroupLog(entryID, entryDate, "Init Test", GroupType.ALTERNATIVE, screwsGroupName);
		screws.addGroupDetail(entryID, entryDate, "Add Screw1", 1, MemberType.MATERIAL, "screw1", null, screwAltPart1, 1, 1);
		screws.addGroupDetail(entryID, entryDate, "Add Screw2", 2, MemberType.MATERIAL, "screw2", null, screwAltPart2, 1, 1);
		return screws;
	}
	
	@After
	public void tearDown() throws Exception {
	}

	private HyvePlan<MPSEntry> init1RackMPS() {
		LoggedContractMPSImpl mps = new LoggedContractMPSImpl(contract);
		HyveProductGroup chassis = initAChassisBOM();
		MPSEntry planEntry;
		try {
			planEntry = new MPSEntry(sdf.parse("01/02/2014"), sdf.parse("01/03/2014"), plant, 1, chassis.getGroupID());
			mps.addPlanEntry(planEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mps;
	}
	
	private HyvePlan<MRPEntry> init1RackMRP() {
		LoggedContractMRPImpl mrp = new LoggedContractMRPImpl(contract);
		try {
			MRPEntry screwPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, screwAltPart2);
			MRPEntry powerPlanEntry = new MRPEntry(sdf.parse("01/02/2014"),  plant, 10, powerPart);
			mrp.addPlanEntry(screwPlanEntry, logEntry);
			mrp.addPlanEntry(powerPlanEntry, logEntry);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return mrp;
	}
	
	@Test
	public void test1RackMPS() {
		HyvePlan<MPSEntry> mps = init1RackMPS();
		HyvePlan<MRPEntry> mrp = init1RackMRP();
		MPSReadinessCheckSolution unsolvedSolution = new MPSReadinessCheckSolution(mps, mrp, bomService);
		solver.solve(unsolvedSolution);
		MPSReadinessCheckSolution solvedSolution = (MPSReadinessCheckSolution)solver.getBestSolution();
		assertNotNull(solvedSolution);
		System.out.println("score=" + solvedSolution.getScore().getHardScore());
		assertTrue(solvedSolution.getScore().getHardScore() > 0);
	}

}
