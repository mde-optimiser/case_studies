package tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import models.nrp.MaximiseSatisfaction;

public class MaximiseSatisfactionTest {

	final private static double DELTA = 1e-3;
	private EObject model;
	private MaximiseSatisfaction objective = new MaximiseSatisfaction();
	
	@Rule
	public TestWatcher watcher = new TestWatcher() {
	   protected void starting(Description description) {
	      System.out.println("Starting test: " + description.getMethodName());
	   }
	   
	   protected void finished(Description description) {
		      System.out.println();
		   }
	};
	
	@Test
	public void testMultiValuationsWithoutDependencies() {
		model = ModelLoadHelper.loadModel("multi-valuation-same-customer-no-depend-req.xmi");
		assertEquals(4.0d, objective.computeFitness(model), DELTA);
	}
	
	@Test
	public void testMultiValuationsWithDependencies() {
		model = ModelLoadHelper.loadModel("multi-valuation-same-customer-same-depend-req.xmi");
		assertEquals(4.0d, objective.computeFitness(model), DELTA);
	}
	
	@Test
	public void testPartialRealisations() {
		model = ModelLoadHelper.loadModel("partial-realisations.xmi");
		EObject customer = objective.getReferenceFeature(model, "customers").iterator().next();
		EObject requirement = objective.getReferenceFeature(model, "requirements").iterator().next();
		assertEquals(0.9d, objective.calculateFulfillment(requirement, customer), DELTA);
	}
	
	@Test
	public void testWeightingRequirementDependencies() {
		model = ModelLoadHelper.loadModel("weighting-requirement-dependencies.xmi");
		EObject customer = objective.getReferenceFeature(model, "customers").iterator().next();
		EObject requirement = objective.getReferenceFeature(model, "requirements").iterator().next();
		assertEquals(0.8d, objective.calculateFulfillment(requirement, customer), DELTA);
	}

}
