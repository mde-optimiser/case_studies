package models.scrum

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.EList

class MinimiseSprints implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var sprints = model.getFeature("sprints")
		
		//No sprints
		if(sprints === null) {
			return 0
		}
		
		var sprintsList = (sprints as EList<EObject>)
		
		
		println("Counted sprints: " + sprintsList.length)
		
		//Added this for debugging
		var sprintsEffortDistribution = sprintsList.map[ sprint | 
			
			new Double((sprint.getFeature("committedItem") as EList<EObject>).fold(0d)[ result, item |
				
				result + item.getFeature("Effort") as Integer
				
			])
			
		].toList
		
		println("Sprint effort distribution: " + sprintsEffortDistribution)
		
		return sprintsList.length
	}
	
	override getName() {
		return "Minimise number of sprints"
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