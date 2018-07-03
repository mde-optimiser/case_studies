package tests

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.henshin.interpreter.Engine
import org.eclipse.emf.henshin.interpreter.UnitApplication
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl
import org.eclipse.emf.henshin.interpreter.impl.UnitApplicationImpl
import org.eclipse.emf.henshin.model.Module
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

import static extension tests.ModelExtensions.*
import static extension tests.ModelHelper.*
import org.eclipse.emf.henshin.interpreter.impl.LoggingApplicationMonitor

class CompactTest {
	private static Engine engine;
	private static Resource modelResource;
	private static Module crossover;
	private static UnitApplication unitApp;
	private static HenshinResourceSet rs;
		
	@BeforeAll
	def public static void createTransformationEnvironment() {
		rs = ModelHelper.prepareEnvironment
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
	@DisplayName("Both solutions are empty. No artifacts are selected.")
	def public void testSolutionsEmpty() {
		modelResource =  rs.getResource("../../tests/models/complete/emptySolutions.xmi")
		val srcModel = modelResource.getModel(0)
		val trgModel = modelResource.getModel(1)
		unitApp.EGraph = new EGraphImpl(modelResource.contents)
		unitApp.setUnit(crossover.getUnit("selectRandomArtifacts_injectPreservingSelection"))
		unitApp.setParameterValue("srcModel", srcModel)
	
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeInjectArtifactSelection")
		val skip = crossover.getUnit("skipArtifact")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertTrue(trgModel.getEFeature("solutions").toCollection.forall[s | (s as EObject).getEFeature("selectedArtifacts").toCollection.size == 0], "No artifact selected.")
	}
	
	
	@Test
	@DisplayName("All solutions should be selected in target.")
	def public void testSelectAll() {
		modelResource =  rs.getResource("../../tests/models/complete/allSelectedVsEmptySolution.xmi")
		val srcModel = modelResource.getModel(0)
		val trgModel = modelResource.getModel(1)
		unitApp.EGraph = new EGraphImpl(modelResource.contents)
		unitApp.setUnit(crossover.getUnit("selectRandomArtifacts_injectPreservingSelection"))
		unitApp.setParameterValue("srcModel", srcModel)

		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeInjectArtifactSelection")
		val skip = crossover.getUnit("skipArtifact")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertTrue(trgModel.getEFeature("availableArtifacts").toCollection.map[a | (a as EObject).getEFeature("solutions")].toSet.size == 1, "Each artifact is part of the same solution.")
	}
	
	@Test
	@DisplayName("All solutions should be deselected in target.")
	def public void testDeselectAll() {
		modelResource =  rs.getResource("../../tests/models/complete/allSelectedVsEmptySolution.xmi")
		val srcModel = modelResource.getModel(1)
		val trgModel = modelResource.getModel(0)
		unitApp.EGraph = new EGraphImpl(modelResource.contents)
		unitApp.setUnit(crossover.getUnit("selectRandomArtifacts_injectPreservingSelection"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeInjectArtifactSelection")
		val skip = crossover.getUnit("skipArtifact")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed")
		rs.save(trgModel, "trgmodel.xmi")
		assertTrue(trgModel.getEFeature("solutions").toCollection.forall[s | (s as EObject).getEFeature("selectedArtifacts").toCollection.size == 0], "No artifact selected.")
	}
	
	@Test
	@DisplayName("Same selection in both solutions. Nothing should be changed.")
	def public void testSameSelected() {
		modelResource =  rs.getResource("../../tests/models/complete/sameSelectedInEachSolution.xmi")
		val srcModel = modelResource.getModel(0)
		val trgModel = modelResource.getModel(1)
		unitApp.EGraph = new EGraphImpl(modelResource.contents)
		unitApp.setUnit(crossover.getUnit("selectRandomArtifacts_injectPreservingSelection"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeInjectArtifactSelection")
		val skip = crossover.getUnit("skipArtifact")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		rs.save(trgModel, "trg2model.xmi")
		assertAll("Correct selection: ",
			[assertTrue(trgModel.getElement("SA1").getEFeature("solutions").toCollection.size == 1, "SA1 should be selected.")],
			[assertTrue(trgModel.getElement("SA2").getEFeature("solutions").toCollection.size == 0, "SA2 should not be selected.")]
		)
	}
	
	@Test
	@DisplayName("Different selection in both solutions. Both selections should be changed.")
	def public void testDifferentSelected() {
		modelResource =  rs.getResource("../../tests/models/complete/differentSelectedInEachSolution.xmi")
		val srcModel = modelResource.getModel(0)
		val trgModel = modelResource.getModel(1)
		unitApp.EGraph = new EGraphImpl(modelResource.contents)
		unitApp.setUnit(crossover.getUnit("selectRandomArtifacts_injectPreservingSelection"))
		unitApp.setParameterValue("srcModel", srcModel)
		
		// Remove skip for deterministic behavior
		val maybe = crossover.getUnit("maybeInjectArtifactSelection")
		val skip = crossover.getUnit("skipArtifact")
		assertTrue(maybe.getEFeature("subUnits").toCollection.remove(skip))
		
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(null), "Rule not executed")
		assertAll("Correct selection: ",
			[assertTrue(trgModel.getElement("SA1").getEFeature("solutions").toCollection.size == 1, "SA1 should be selected.")],
			[assertTrue(trgModel.getElement("SA2").getEFeature("solutions").toCollection.size == 0, "SA2 should not be selected.")]
		)
	}
}