package models.composition.fitness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import models.service.composition.fitness.jni.PredictorsCalculator;

public class PredictorCalculatorTests {

	
	@Test
	public void assertThatThePredictorsResourceIsLoadedCorrectly() {
		
		EObject predictors = new PredictorsCalculator().getPredictors();
		
		assertNotNull(predictors);
	}
	
	@Test
	public void assertThatOrchestratorCounterReturnsAsExpected() {
		
		EObject orchestrationObject = new TestModelLoader().loadModel("test-configuration-crepe-1079190991.xmi");
		int orchestrators = new PredictorsCalculator().calculateOrchestrators(orchestrationObject);	
		
		assertEquals("Expected number of orchestrators: ", 5, orchestrators);
	}
	
	@Test
	public void assertThatHopsCounterReturnsAsExpected() {
		
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().calculateHops(orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of hops: ", 11, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType1FastReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1 devFast: ", 3, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType2MediumReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2 devMedium: ", 0, hops);
	}
	
	
	@Test
	public void assertThatCountNodeTypesForType3SlowReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3 devSlow: ", 6, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad3SmallReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3 loadSmall: ", 5, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad2MediumReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2 loadMedium: ", 4, hops);
	}
	
	@Test
	public void assertThatCountLoadNodesForLoad1BigReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1079190991.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1 loadBig: ", 0, hops);
	}

	@Test
	public void assertThatOrchestratorCounterReturnsAsExpectedModel2() {
		
		EObject orchestrationObject = new TestModelLoader().loadModel("test-configuration-crepe-1.xmi");
		int orchestrators = new PredictorsCalculator().calculateOrchestrators(orchestrationObject);	
		
		assertEquals("Expected number of orchestrators: ", 5, orchestrators);
	}
	
	@Test
	public void assertThatHopsCounterReturnsAsExpectedModel2() {
		
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().calculateHops(orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of hops: ", 8, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType1FastReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1 devFast: ", 2, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType2MediumReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2 devMedium: ", 0, hops);
	}
	
	
	@Test
	public void assertThatCountNodeTypesForType3SlowReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3 devSlow: ", 8, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad3SmallReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3 loadSmall: ", 4, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad2MediumReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2 loadMedium: ", 5, hops);
	}
	
	@Test
	public void assertThatCountLoadNodesForLoad1BigReturnsAsExpectedModel2() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		PredictorsCalculator predictorsCalculator = new PredictorsCalculator();
		EObject predictorsObject = predictorsCalculator.getPredictors();
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration-crepe-1.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1 loadBig: ", 0, hops);
	}
	
}