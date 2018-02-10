package models.scrum

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.EList

class MinimiseSprints implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var sprints = model.getFeature("sprints")
		
		var fitness = 0
		
		if(sprints !== null) {
			fitness = (sprints as EList<EObject>).length	
		}
		
		println("Counted sprints: " + fitness)
		
		
		var sprinta = (model.getFeature("sprints") as EList<EObject>).map[ sprint | 
			
			new Double((sprint.getFeature("committedItem") as EList<EObject>).fold(0d)[ result, item |
				
				result + item.getFeature("Effort") as Integer
				
			])
			
		].toList
		
		
		println("Sprint effort distribution: " + sprinta)
		
		return fitness
	}
	
	override getName() {
		return "Minimise Number of Sprints"
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