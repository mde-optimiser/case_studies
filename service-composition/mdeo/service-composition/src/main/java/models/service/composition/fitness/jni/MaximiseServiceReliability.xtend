package models.service.composition.fitness.jni

import org.eclipse.emf.ecore.EObject
import models.service.composition.fitness.jni.PredictorsCalculator

class MaximiseServiceReliability  extends AbstractNativeGuidanceFunction {
	
	override computeFitness(EObject model) {
		var predictors = new PredictorsCalculator().calculatePredictors(model);
		var fitness = this.evaluatePredictors(predictors)
		
		println(this.getName + ": " + fitness)

		return fitness.get(1)
	}
	
	override getName() {
		return "Minimise Service Reliability"
	}	
}
