package models.service.composition.fitness

import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction

class MinimiseEnergyConsumption  extends AbstractRPCGuidanceFunction {
	
	override computeFitness(EObject model) {
		0
	}
	
	override getName() {
		return "Minimise Standard Deviation"
	}	
}