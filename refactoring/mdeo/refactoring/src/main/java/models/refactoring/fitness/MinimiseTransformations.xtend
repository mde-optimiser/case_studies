package models.refactoring.fitness

import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution

class MinimiseTransformations implements IGuidanceFunction {

	override double computeFitness(Solution model) {
		//val cohesion = calculateCohesionRatio(model);

		//println("Matches found: " + model.transformationsChain.length)
		return model.transformationsChain.length
	}
	
	override getName() {
		return "Minimise Transformations"
	}
	
}