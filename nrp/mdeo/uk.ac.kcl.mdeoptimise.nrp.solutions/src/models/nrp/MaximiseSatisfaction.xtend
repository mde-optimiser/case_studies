package models.nrp

import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction

class MaximiseSatisfaction  implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var fitness = 0
		return fitness
	}
	
	override getName() {
		return "Minimise Next Release Customer Satisfaction"
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