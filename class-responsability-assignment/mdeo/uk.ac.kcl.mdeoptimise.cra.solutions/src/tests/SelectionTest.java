package tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.UnitApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.LoggingApplicationMonitor;
import org.eclipse.emf.henshin.interpreter.impl.UnitApplicationImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Unit;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SelectionTest {
	
	private static Engine engine;
	private static EObject model;
	private static List<EObject> roots;
	private static Module selection;
	private static UnitApplication unitApp;
	private static HenshinResourceSet rs;
	
	@BeforeAll
	public static void createTransformationEnvironment() {
		rs = new HenshinResourceSet("src/models/cra");
		rs.registerDynamicEPackages("architectureCRA.ecore");
		engine = new EngineImpl();		
	}
	
	@BeforeEach
	public void resetUnitApp() {
		unitApp = new UnitApplicationImpl(engine);
		selection = rs.getModule("selection.henshin");
	}

	// Dependency Selection
	
	@Test
	@DisplayName("Mark dependencies.")
	public void testMarkDependencies() {
		model = rs.getResource("../../tests/models/selection/unmarkedDependencies.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markMixedModelDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(2, traces.size());
	}
	
	@Test
	@DisplayName("Mark fulfilled dependencies (no dependencies).")
	public void testMarkFulfilledDependencies() {
		model = rs.getResource("../../tests/models/selection/noDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markFulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(0, traces.size());
	}
	
	@Test
	@DisplayName("Mark fulfilled dependencies (only fulfilled).")
	public void testMarkFulfilledDependenciesOnlyFulfilled() {
		model = rs.getResource("../../tests/models/selection/singleFulfilledFunctionalDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markFulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(1, traces.size());
	}
	
	@Test
	@DisplayName("Mark fulfilled dependencies (only nonfulfilled).")
	public void testMarkFulfilledDependenciesOnlyNonfulfilled() {
		model = rs.getResource("../../tests/models/selection/singleNonfulfilledDataDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markFulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(0, traces.size());
	}
	
	@Test
	@DisplayName("Mark fulfilled dependencies (mixed).")
	public void testMarkFulfilledDependenciesMixed() {
		model = rs.getResource("../../tests/models/selection/unmarkedDependencies.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markFulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(1, traces.size());
	}
	
	@Test
	@DisplayName("Mark unfulfilled dependencies (no dependencies).")
	public void testMarkUnfulfilledDependencies() {
		model = rs.getResource("../../tests/models/selection/noDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markUnfulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(0, traces.size());
	}
	
	@Test
	@DisplayName("Mark unfulfilled dependencies (only fulfilled).")
	public void testMarkUnfulfilledDependenciesOnlyFulfilled() {
		model = rs.getResource("../../tests/models/selection/singleFulfilledFunctionalDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markUnfulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(0, traces.size());
	}
	
	@Test
	@DisplayName("Mark unfulfilled dependencies (only nonfulfilled).")
	public void testMarkUnfulfilledDependenciesOnlyNonfulfilled() {
		model = rs.getResource("../../tests/models/selection/singleNonfulfilledDataDependency.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markUnfulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(1, traces.size());
	}
	
	@Test
	@DisplayName("Mark unfulfilled dependencies (mixed).")
	public void testMarkUnfulfilledDependenciesMixed() {
		model = rs.getResource("../../tests/models/selection/unmarkedDependencies.xmi").getContents().get(0);
		unitApp.setEGraph(new EGraphImpl(model));
		unitApp.setUnit(selection.getUnit("markUnfulfilledDependencies"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(null);
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		assertEquals(1, traces.size());
	}
	
//	@Test
//	@DisplayName("Remove dependency marks")
//	public void testDeleteDependencyMarks() {
//		roots = rs.getResource("../../tests/models/selection/markedDependencies.xmi").getContents();
//		model = roots.stream().filter(r -> r.eClass().getName().equals("ClassModel")).findFirst().get();
//		unitApp.setEGraph(new EGraphImpl(roots));
//		unitApp.setUnit(selection.getUnit("decreaseDependencyCounter"));
//		unitApp.setParameterValue("model", model);
//		unitApp.execute(null);
//				
//		roots = unitApp.getEGraph().getRoots();
//		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
//		assertEquals(1, traces.size());
//	}
	
//	@Test
//	@DisplayName("Select single data dependency")
//	public void testSelectSingleDataDependency() {
//		roots = rs.getResource("../../tests/models/selection/singleNonfulfilledDataDependency.xmi").getContents();
//		model = roots.stream().filter(r -> r.eClass().getName().equals("ClassModel")).findFirst().get();
//		unitApp.setEGraph(new EGraphImpl(roots));
//		unitApp.setUnit(selection.getUnit("selectDependency"));
//		unitApp.setParameterValue("model", model);
//		unitApp.execute(new LoggingApplicationMonitor());
//		roots = unitApp.getEGraph().getRoots();
//		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
//		assertEquals(1, traces.size());
//		
//	}
	
//	@Test
//	@DisplayName("Select single functional dependency")
//	public void testSelectSingleFunctionalDependency() {
//		roots = rs.getResource("../../tests/models/selection/singleFulfilledFunctionalDependency.xmi").getContents();
//		model = roots.stream().filter(r -> r.eClass().getName().equals("ClassModel")).findFirst().get();
//		unitApp.setEGraph(new EGraphImpl(roots));
//		unitApp.setUnit(selection.getUnit("selectDependency"));
//		unitApp.setParameterValue("model", model);
//		unitApp.execute(null);
//		roots = unitApp.getEGraph().getRoots();
//		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
//		assertEquals(1, traces.size());
//	}
		
//	@Test
//	@DisplayName("Select no dependency")
//	public void testSelectNoDependency() {
//		roots = rs.getResource("../../tests/models/selection/noDependency.xmi").getContents();
//		model = roots.stream().filter(r -> r.eClass().getName().equals("ClassModel")).findFirst().get();
//		unitApp.setEGraph(new EGraphImpl(roots));
//		unitApp.setUnit(selection.getUnit("selectDependency"));
//		unitApp.setParameterValue("model", model);
//		unitApp.execute(null);
//		roots = unitApp.getEGraph().getRoots();
//		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
//		assertEquals(0, traces.size());
//	}
	
//	@Test
//	@DisplayName("Select features of selected dependencies in two models")
//	public void testSelectFeaturesOfSelectedDependenciesInTwoModels() {
//		roots = rs.getResource("../../tests/models/selection/selectedDependenciesInTwoModels.xmi").getContents();
//		unitApp.setEGraph(new EGraphImpl(roots));
//		unitApp.setUnit(selection.getUnit("selectDependencyFeatures"));
//		unitApp.execute(null);
//				
//		roots = unitApp.getEGraph().getRoots();
//		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
//		EStructuralFeature nameAttribute = traces.get(0).eClass().getEStructuralFeature("name");
//		List<EObject> sels = traces.stream().filter(t -> t.eGet(nameAttribute).equals("selected")).collect(Collectors.toList());
//		assertEquals(6, sels.size());
//	}

	@Test
	@DisplayName("Select all dependencies")
	public void testSelectAllDependencies() {
		roots = rs.getResource("../../tests/models/selection/unmarkedDependencies.xmi").getContents();
		model = roots.stream().filter(r -> r.eClass().getName().equals("ClassModel")).findFirst().get();
		unitApp.setEGraph(new EGraphImpl(roots));
		
		// Remove noop for deterministic behavior
		Unit maybe = selection.getUnit("maybeSelectDependency");
		Unit removeDep = selection.getUnit("removeDependency");			
		((List)maybe.eGet(maybe.eClass().getEStructuralFeature("subUnits"))).remove(removeDep);
			
		unitApp.setUnit(selection.getUnit("selectMixedDependenciesForModel"));
		unitApp.setParameterValue("model", model);
		unitApp.execute(new LoggingApplicationMonitor());
				
		roots = unitApp.getEGraph().getRoots();
		List<EObject> traces = roots.stream().filter(r -> r.eClass().getName().equals("Trace")).collect(Collectors.toList());
		EStructuralFeature nameAttribute = traces.get(0).eClass().getEStructuralFeature("name");
		List<EObject> marks = traces.stream().filter(t -> t.eGet(nameAttribute).equals("mark")).collect(Collectors.toList());
		List<EObject> deps = traces.stream().filter(t -> t.eGet(nameAttribute).equals("dependency")).collect(Collectors.toList());
		List<EObject> sels = traces.stream().filter(t -> t.eGet(nameAttribute).equals("selected")).collect(Collectors.toList());
				
		assertAll("correct traces",
				() -> assertEquals(0, marks.size()),
				() -> assertEquals(2, deps.size()),
				() -> assertEquals(3, sels.size())
		);
	}	
	
	
}
