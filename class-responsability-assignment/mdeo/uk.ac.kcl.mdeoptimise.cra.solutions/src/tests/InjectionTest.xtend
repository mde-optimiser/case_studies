package tests

import java.util.List
import org.eclipse.emf.common.util.DiagnosticChain
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.Diagnostician
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

class InjectionTest {
	private static EPackage epackage;
	private static Engine engine;
	private static Resource modelResource;
	private static EObject model;
	private static List<EObject> roots;
	private static Module injection;
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
			modelResource.unload;
		}
		unitApp = new UnitApplicationImpl(engine);
		rs.getResource("injection.henshin").unload
		injection = rs.getModule("injection.henshin");
	}


	// Preserving Distribution

	@Test
	@DisplayName("Preserving distribution: All features need to be split")
	def public void testDistrSplit() {
		modelResource = rs.getResource("../../tests/models/injection/allDistributedVsAllCombined.xmi")
		roots = modelResource.contents
		var targetModel = roots.get(1)
		unitApp.setEGraph(new EGraphImpl(roots))
		unitApp.setUnit(injection.getUnit("injectPreservingDistribution"));
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed");
		assertEquals(3, targetModel.getEFeature("classes").toCollection.size);
	}
	
	@Test
	@DisplayName("Preserving distribution: All features need to be merged")
	def public void testDistrMerge() {
		modelResource = rs.getResource("../../tests/models/injection/allCombinedVsAllDistributed.xmi")
		roots = modelResource.contents
		var targetModel = roots.get(1)
		unitApp.setEGraph(new EGraphImpl(roots))
		unitApp.setUnit(injection.getUnit("injectPreservingDistribution"));
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed");
		assertEquals(1, targetModel.getEFeature("classes").toCollection.size);
	}
	
	// Preserving Dependency
	
	@Test
	@DisplayName("Preserving dependencies: Test split, merge-split and skip")
	def public void testDepSplitMergeSplitSkip() {
		modelResource = rs.getResource("../../tests/models/injection/preserveDependenciesComplete.xmi")
		roots = modelResource.contents
		val targetModel = roots.get(1)
		unitApp.setEGraph(new EGraphImpl(roots))
		unitApp.setUnit(injection.getUnit("injectPreservingDistribution"));
		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed");
		assertAll("correct distribution of features",
			[assertEquals(1, (targetModel.getElement("M1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("encapsulates").toCollection.size)],
			[assertEquals(1, (targetModel.getElement("A1").getEFeature("isEncapsulatedBy") as EObject).getEFeature("encapsulates").toCollection.size)],
			[assertTrue(targetModel.getElement("M2").getEFeature("isEncapsulatedBy") === targetModel.getElement("M3").getEFeature("isEncapsulatedBy"))]
		)
	}	
	
	@Test
	@DisplayName("Preserving dependencies: Exchange of a contrary dependency")
	def public void testDepContraryDependency() {
		
		val model1 = newModel("model1")
		val c11 = newClass("c1")
		val m11 = newMethod("m1")
		val a11 = newAttribute("a1")
		val model2 = newModel("model2")
		val c21 = newClass("c1")
		val c22 = newClass("c2")
		val m21 = newMethod("m1")
		val a21 = newAttribute("a1")
		val trc1 = newTrace("selected")
		val trc2 = newTrace("selected")
		val trc3 = newTrace("selected")
		val trc4 = newTrace("selected")
		val trc5 = newTrace("dependency")
		val trc6 = newTrace("dependency")
		
		model1.addClass(c11).addFeature(m11).addFeature(a11)
		model2.addClass(c21).addClass(c22).addFeature(m21).addFeature(a21)
		
		c11.assignFeature(m11).assignFeature(a11)
		c21.assignFeature(m21)
		c22.assignFeature(a21)
		
		m11.addDataDependency(a11)
		m21.addDataDependency(a21)	
		
		trc1.addSource(model1).addSource(c11)
		trc1.addTarget(m11)
		trc2.addSource(model1).addSource(c11)
		trc2.addTarget(a11)
		trc3.addSource(model2).addSource(c21)
		trc3.addTarget(m21)
		trc4.addSource(model2).addSource(c22)
		trc4.addTarget(a21)
		trc5.addSource(m11).addSource(a11)
		trc6.addSource(m21).addSource(a21)
		
		assertTrue(Diagnostician.INSTANCE.validate(model1, null as DiagnosticChain))
		assertTrue(Diagnostician.INSTANCE.validate(model2, null as DiagnosticChain))
			
		val eGraph = new EGraphImpl()
		eGraph.addGraph(model1)
		eGraph.addGraph(model2)
		eGraph.addGraph(trc1)
		eGraph.addGraph(trc2)
		eGraph.addGraph(trc3)
		eGraph.addGraph(trc4)
		eGraph.addGraph(trc5)
		eGraph.addGraph(trc6)
				
		unitApp.setEGraph(eGraph);
		unitApp.setUnit(injection.getUnit("injectPreservingDependencies"));
//		// The following assert will not trigger if the root unit of the rule always returns true (e.g. loop unit) 
		assertTrue(unitApp.execute(new LoggingApplicationMonitor), "Rule not executed");
	
		assertAll("",
			[assertEquals(2, model1.getEFeature("classes").toCollection.size)],
			[assertFalse(m11.getEFeature("isEncapsulatedBy") == a11.getEFeature("isEncapsulatedBy"))],
			[assertEquals(1, model2.getEFeature("classes").toCollection.size)],
			[assertTrue(m21.getEFeature("isEncapsulatedBy") == a21.getEFeature("isEncapsulatedBy"))]			
		)
	}	
}