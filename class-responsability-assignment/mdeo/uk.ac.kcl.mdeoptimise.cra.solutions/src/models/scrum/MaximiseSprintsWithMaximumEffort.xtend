package models.scrum

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.EList
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation

class MaximiseSprintsWithMaximumEffort implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
						
		var validSprints = (model.getFeature("sprints") as EList<EObject>).filter[ sprint | 
			
			(sprint.getFeature("committedItem") as EList<EObject>).fold(0)[ result, item |
				
				result + item.getFeature("Effort") as Integer
				
			] as Integer > 25
			
		].toList
		
		var fitness = 0
		
		if(validSprints !== null) {
			fitness = validSprints.length	
		}
		
		//var effortStandardDeviation = new StandardDeviation().evaluate(fitness)
		
		//println("Sprint effort distribution: " + fitness)
		//println("Sprint effort standard deviation: " + effortStandardDeviation)
		
		println("Sprints with 30 points: " + fitness);
		
		return fitness * -1
	}
	
	override getName() {
		return "Maximise sprints with maximum effort: "
	}

	/**
	 * Helper function getting the value of the named feature (if it exists) for the given EObject.
	 */
	def Object getFeature (EObject o, String feature) {
		
		if(o === null){
			println("Null object given")
		}
		
		o.eGet(o.eClass.getEStructuralFeature(feature))
		
	}	
}
