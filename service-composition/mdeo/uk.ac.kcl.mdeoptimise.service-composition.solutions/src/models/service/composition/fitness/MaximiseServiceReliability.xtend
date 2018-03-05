package models.service.composition.fitness

import org.eclipse.emf.ecore.EObject

class MaximiseServiceReliability  extends AbstractRemoteGuidanceFunction {
	
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