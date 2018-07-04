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

class ModularCompleteTest {
	
	private static EPackage epackage;
	private static Engine engine;
	private static Resource modelResource;
	private static EObject model;
	private static List<EObject> roots;
	private static Module crossover;
	private static Module selection;
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
		rs.getResource("crossover.henshin").unload
		crossover = rs.getModule("crossover.henshin")
		rs.getResource("selection.henshin").unload
		selection = rs.getModule("selection.henshin")
	}
	
	@Test
	@DisplayName("Random selection preserving distribution: All features need to be split.")
	def public void testDistrAllSplit() {
		modelResource = rs.getResource("../../tests/models/complete/allCombinedVsAllDistributed.xmi")
		roots = modelResource.contents
		val firstModel = roots.get(0)
		val secondModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("matchingSelection", true);
				
		// Remove noop for deterministic behavior
		val maybe = selection.getUnit("maybeSelectFeature");
		val noop = selection.getUnit("noop");			
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(noop));
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed")
		assertAll("Correct encapsulation:",
			[assertTrue(firstModel.getEFeature("features").toCollection.forall[f | (f as EObject).getEFeature("isEncapsulatedBy") !== null], "Each feature in first model is encapsulated.")],
			[assertTrue(firstModel.getEFeature("classes").toCollection.forall[c | (c as EObject).getEFeature("encapsulates").toCollection.size == 1], "Each class in first model encapsulates one feature.")],
			[assertTrue(secondModel.getEFeature("features").toCollection.map[f | (f as EObject).getEFeature("isEncapsulatedBy")].toSet.size == 1, "Each feature in second model is encapsulated in the same class.")]
		)
	}
	
	@Test
	@DisplayName("Random selection preserving distribution: Nothing should be changed.")
	def public void testDistrNothingShouldHappen() {
		modelResource = rs.getResource("../../tests/models/complete/polymorphModels.xmi")
		roots = modelResource.contents
		val firstModel = roots.get(0)
		val secondModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("matchingSelection", true)
		
		// Remove noop for deterministic behavior
		val maybe = selection.getUnit("maybeSelectFeature")
		val noop = selection.getUnit("noop")		
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(noop))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations unchanged:",
			[assertEquals(2, firstModel.getEFeature("classes").toCollection.size, "Correct number of classes in first model")],
			[assertEquals("C1.1", (firstModel.getElement("M1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M1 in C1.1 in first model")],
			[assertEquals("C1.1", (firstModel.getElement("A1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "A1 in C1.1 in first model")],
			[assertEquals("C1.2", (firstModel.getElement("M2").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M2 in C1.2 in first model")],	
			[assertEquals(2, secondModel.getEFeature("classes").toCollection.size, "Correct number of classes in second model")],
			[assertEquals("C2.1", (secondModel.getElement("M1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M1 in C2.1 in second model")],
			[assertEquals("C2.1", (secondModel.getElement("A1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "A1 in C2.1 in second model")],
			[assertEquals("C2.2", (secondModel.getElement("M2").getEFeature("isEncapsulatedBy") as EObject).getEFeature("name"), "M2 in C2.2 in second model")]
		)
	}
	
	@Test
	@DisplayName("Random selection preserving distribution: Transfer contrary distribution.")
	def public void testDistrContrary() {
		modelResource = rs.getResource("../../tests/models/complete/contraryDistribution.xmi")
		roots = modelResource.contents
		val firstModel = roots.get(0)
		val secondModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("matchingSelection", true)
		
		// Remove noop for deterministic behavior
		val maybe = selection.getUnit("maybeSelectFeature")
		val noop = selection.getUnit("noop")		
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(noop))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations correct:",
			[assertEquals(2, firstModel.getEFeature("classes").toCollection.size, "Correct number of classes in first model")],
			[assertSame(firstModel.getElement("M1").getEFeature("isEncapsulatedBy"), firstModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Merged correctly in first model")],
			[assertNotSame(firstModel.getElement("M1").getEFeature("isEncapsulatedBy"), firstModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Split correctly in first model")],
			[assertEquals(2, secondModel.getEFeature("classes").toCollection.size, "Correct number of classes in second model")],
			[assertSame(secondModel.getElement("M1").getEFeature("isEncapsulatedBy"), secondModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Merged correctly in second model")],
			[assertNotSame(secondModel.getElement("M1").getEFeature("isEncapsulatedBy"), secondModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Split correctly in second model")]				
		)
	}
	
	@Test
	@DisplayName("Random selection preserving distribution: Transfer contrary distribution and create class.")
	def public void testDistrContraryNewClass() {
		modelResource = rs.getResource("../../tests/models/complete/contraryDistributionNewClassNeeded.xmi")
		roots = modelResource.contents
		val firstModel = roots.get(0)
		val secondModel = roots.get(1)
		unitApp.EGraph = new EGraphImpl(roots)
		unitApp.setUnit(crossover.getUnit("selectRandomFeatures_injectPreservingDistribution"))
		unitApp.setParameterValue("matchingSelection", true)
		
		// Remove noop for deterministic behavior
		val maybe = selection.getUnit("maybeSelectFeature")
		val noop = selection.getUnit("noop")		
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(noop))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("All encapsulations correct:",
			[assertEquals(2, firstModel.getEFeature("classes").toCollection.size, "Correct number of classes in first model")],
			[assertSame(firstModel.getElement("M1").getEFeature("isEncapsulatedBy"), firstModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Merged correctly in first model")],
			[assertSame(firstModel.getElement("M2").getEFeature("isEncapsulatedBy"), firstModel.getElement("A2").getEFeature("isEncapsulatedBy"), "Merged correctly in first model")],
			[assertNotSame(firstModel.getElement("M1").getEFeature("isEncapsulatedBy"), firstModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Split correctly in first model")],
			[assertEquals(3, secondModel.getEFeature("classes").toCollection.size, "Correct number of classes in second model")],
			[assertSame(secondModel.getElement("M1").getEFeature("isEncapsulatedBy"), secondModel.getElement("M2").getEFeature("isEncapsulatedBy"), "Merged correctly in second model")],
			[assertNotSame(secondModel.getElement("M1").getEFeature("isEncapsulatedBy"), secondModel.getElement("A1").getEFeature("isEncapsulatedBy"), "Split correctly in second model")],
			[assertNotSame(secondModel.getElement("M1").getEFeature("isEncapsulatedBy"), secondModel.getElement("A2").getEFeature("isEncapsulatedBy"), "Split correctly in second model")],	
			[assertNotSame(secondModel.getElement("A1").getEFeature("isEncapsulatedBy"), secondModel.getElement("A2").getEFeature("isEncapsulatedBy"), "Split correctly in second model")]		
		)
	}
}