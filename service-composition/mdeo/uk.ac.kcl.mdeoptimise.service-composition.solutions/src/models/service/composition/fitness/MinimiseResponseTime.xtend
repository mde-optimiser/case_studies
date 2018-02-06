package models.service.composition.fitness

import org.eclipse.emf.ecore.EObject

class MinimiseResponseTime  extends AbstractRemoteGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var predictors = new PredictorsCalculator().calculatePredictors(model, null);
		var fitness = this.evaluatePredictors(predictors)
		
		return fitness.get(0)
	}
	
	override getName() {
		return "Minimise Response Time"
	}	
}