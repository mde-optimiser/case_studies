package models.refactoring.fitness


import at.ac.tuwien.big.momot.examples.refactoring.refactoring.RefactoringModel
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution

class MinimiseContentSize implements IGuidanceFunction {

	override double computeFitness(Solution solution) {

		var refactoring = solution.model as RefactoringModel		
		val fitness = refactoring.properties.length + refactoring.entities.size() * 1.1
		
		println("Fitness: " + fitness)
		
		return fitness
	}
	
	override getName() {
		return "Minimise Content Size"
	}
	
}
