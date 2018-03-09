package models.service.composition.fitness

import org.eclipse.emf.ecore.EObject

class MinimiseEnergyConsumption  extends AbstractRemoteGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var predictors = new PredictorsCalculator().calculatePredictors(model);
	
		var fitness = this.evaluatePredictors(predictors)
		
		return fitness.get(2)
	}
	
	override getName() {
		return "Minimise Energy Consumption"
	}	
}