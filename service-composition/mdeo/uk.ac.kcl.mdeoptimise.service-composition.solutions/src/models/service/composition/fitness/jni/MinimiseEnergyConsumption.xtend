package models.service.composition.fitness.jni

import org.eclipse.emf.ecore.EObject
import models.service.composition.fitness.PredictorsCalculator

class MinimiseEnergyConsumption  extends AbstractNativeGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var predictors = new PredictorsCalculator().calculatePredictors(model);
	
		var fitness = this.evaluatePredictors(predictors)
		
		return fitness.get(2)
	}
	
	override getName() {
		return "Minimise Energy Consumption"
	}	
}
