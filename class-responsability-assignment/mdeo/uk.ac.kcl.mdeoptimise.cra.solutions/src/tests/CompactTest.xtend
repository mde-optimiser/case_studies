package tests

import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.henshin.interpreter.Engine
import org.eclipse.emf.henshin.interpreter.UnitApplication
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl
import org.eclipse.emf.henshin.interpreter.impl.LoggingApplicationMonitor
import org.eclipse.emf.henshin.interpreter.impl.UnitApplicationImpl
import org.eclipse.emf.henshin.model.Module
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

import static extension tests.ClassModelExtensions.*

class CompactTest {
	private static EPackage epackage;
	private static Engine engine;
	private static Resource modelResource;
	private static EObject model;
	private static List<EObject> roots;
	private static Module crossover;
	private static UnitApplication unitApp;
	private static HenshinResourceSet rs;
		
	@BeforeAll
	def public static void createTransformationEnvironment() {
		rs = ModelHelper.prepareEnvironment
		ClassModelExtensions.setPackage(rs)
		engine = new EngineImpl();		
	}
	
	@BeforeEach
	def public void resetUnitApp() {
		if (modelResource !== null) {
			modelResource.unload
		}
		unitApp = new UnitApplicationImpl(engine);
		rs.getResource("crossover_compact.henshin").unload
		crossover = rs.getModule("crossover_compact.henshin")
	}
	
	@Test
	@DisplayName("Preserving distribution: All features need to be split.")
	def public void testDistrAllSplit() {
		modelResource = rs.getResource("../../tests/models/complete/allCombinedVsAllDistributed.xmi")
		roots = modelResource.contents
		val srcModel = roots.get(1)
		val trgModel = roots.get(0)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("srcModel", srcModel)
	
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeMoveFeature")
		val skip = crossover.getUnit("skipFeature")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed")
		assertAll("Correct encapsulation:",
			[assertTrue(trgModel.getEFeature("features").toCollection.forall[f | (f as EObject).getEFeature("isEncapsulatedBy") !== null], "Each feature is encapsulated.")],
			[assertTrue(trgModel.getEFeature("classes").toCollection.forall[c | (c as EObject).getEFeature("encapsulates").toCollection.size == 1], "Each class encapsulates one feature.")]
		)
	}
	
	
	@Test
	@DisplayName("Preserving distribution: All features need to be merged.")
	def public void testDistrAllMerge() {
		modelResource = rs.getResource("../../tests/models/complete/allCombinedVsAllDistributed.xmi")
		roots = modelResource.contents
		val srcModel = roots.get(0)
		val trgModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeMoveFeature")
		val skip = crossover.getUnit("skipFeature")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertTrue(trgModel.getEFeature("features").toCollection.map[f | (f as EObject).getEFeature("isEncapsulatedBy")].toSet.size == 1, "Each feature is encapsulated in the same class.")
	}
	
	@Test
	@DisplayName("Preserving distribution: Nothing should be changed.")
	def public void testDistrNothingShouldHappen() {
		modelResource = rs.getResource("../../tests/models/complete/polymorphModels.xmi")
		roots = modelResource.contents
		val srcModel = roots.get(0)
		val trgModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeMoveFeature")
		val skip = crossover.getUnit("skipFeature")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations unchanged:",
			[assertEquals(2, trgModel.getEFeature("classes").toCollection.size, "Correct number of classes")],
			[assertEquals("C2.1", (trgModel.getElement("M1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M1 in C2.1")],
			[assertEquals("C2.1", (trgModel.getElement("A1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "A1 in C2.1")],
			[assertEquals("C2.2", (trgModel.getElement("M2").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M2 in C2.2")]			
		)
	}
	
	@Test
	@DisplayName("Preserving distribution: Transfer contrary distribution.")
	def public void testDistrContrary() {
		modelResource = rs.getResource("../../tests/models/complete/contraryDistribution.xmi")
		roots = modelResource.contents
		val srcModel = roots.get(0)
		val trgModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeMoveFeature")
		val skip = crossover.getUnit("skipFeature")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations correct:",
			[assertEquals(2, trgModel.getEFeature("classes").toCollection.size, "Correct number of classes")],
			[assertSame(trgModel.getElement("M1").getEFeature("isEncapsulatedBy"), trgModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Merged correctly")],
			[assertNotSame(trgModel.getElement("M1").getEFeature("isEncapsulatedBy"), trgModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Split correctly")]			
		)
	}
	
	@Test
	@DisplayName("Preserving distribution: Transfer contrary distribution and create class.")
	def public void testDistrContraryNewClass() {
		modelResource = rs.getResource("../../tests/models/complete/contraryDistributionNewClassNeeded.xmi")
		roots = modelResource.contents
		val srcModel = roots.get(0)
		val trgModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeMoveFeature")
		val skip = crossover.getUnit("skipFeature")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations correct:",
			[assertEquals(3, trgModel.getEFeature("classes").toCollection.size, "Correct number of classes")],
			[assertSame(trgModel.getElement("M1").getEFeature("isEncapsulatedBy"), trgModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Merged correctly")],
			[assertNotSame(trgModel.getElement("M1").getEFeature("isEncapsulatedBy"), trgModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Split correctly")],
			[assertNotSame(trgModel.getElement("M1").getEFeature("isEncapsulatedBy"), trgModel.getElement("A2").getEFeature("isEncapsulatedBy"), "Split correctly")],	
			[assertNotSame(trgModel.getElement("A1").getEFeature("isEncapsulatedBy"), trgModel.getElement("A2").getEFeature("isEncapsulatedBy"), "Split correctly")]		
		)
	}
}