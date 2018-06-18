package models.argumentation.fitness

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.EList

class MinimiseArgumentsPutForward implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		var scenario = new EcoreScenarioParser(model)
		
		var frameworkArguments = scenario.getArgumentationFrameworkArguments()
		
		var argumentsPutForward = scenario.getArgumentsPutForward(frameworkArguments);
		
		return argumentsPutForward.size
	}
	
	override getName() {
		return "Minimise persuader arguments put forward"
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
