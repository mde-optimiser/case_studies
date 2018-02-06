package models.composition.fitness.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import models.service.composition.fitness.PredictorsCalculator;

public class PredictorCalculatorTests {

	@Test
	public void assertThatOrchestratorCounterReturnsAsExpected() {
		
		EObject orchestrationObject = new TestModelLoader().loadModel("test-configuration.xmi");
		int orchestrators = new PredictorsCalculator().calculateOrchestrators(orchestrationObject);	
		
		assertEquals("Expected number of orchestrators: ", 1, orchestrators);
	}
	
	@Test
	public void assertThatHopsCounterReturnsAsExpected() {
		
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().calculateHops(orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of hops: ", 14, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType1ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1: ", 4, hops);
	}
	
	@Test
	public void assertThatCountNodeTypesForType2ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2: ", 0, hops);
	}
	
	
	@Test
	public void assertThatCountNodeTypesForType3ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countTypeNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3: ", 0, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad3ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(3, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 3: ", 5, hops);
	}
	

	@Test
	public void assertThatCountLoadNodesForLoad2ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(2, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 2: ", 0, hops);
	}
	
	@Test
	public void assertThatCountLoadNodesForLoad1ReturnsAsExpected() {
		TestModelLoader modelLoader = new TestModelLoader();
		
		EObject predictorsObject = modelLoader.loadModel("connected-nodes.xmi");
		
		EObject orchestrationObject = modelLoader.loadModel("test-configuration.xmi");
		
		int hops = new PredictorsCalculator().countLoadNodes(1, orchestrationObject, predictorsObject);	
		
		assertEquals("Expected number of nodes of type 1: ", 0, hops);
	}
}