package models.service.composition.fitness.jni

import org.eclipse.emf.ecore.EObject
import models.service.composition.fitness.jni.PredictorsCalculator

class MinimiseResponseTime  extends AbstractNativeGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var predictors = new PredictorsCalculator().calculatePredictors(model);
		var fitness = this.evaluatePredictors(predictors)
		
		var responseTime = fitness.get(0);
		
		if(responseTime < 0){
			return responseTime * -1
		}
		
		return fitness.get(0)
	}
	
	override getName() {
		return "Minimise Response Time"
	}	
}
