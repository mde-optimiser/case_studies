package models.cra

import uk.ac.kcl.interpreter.evolvers.parameters.IEvolverParametersFunction
import java.util.List
import org.eclipse.emf.ecore.EObject
import java.util.Collection
import java.util.Random

class RandomMutationNumberSelector implements IEvolverParametersFunction {
	
	override sample(List<EObject> model) {
		val cm = model.get(0)
		val featNumber = (cm.eGet(cm.eClass.getEStructuralFeature("features")) as Collection).size
		val rng = new Random
		var mutations = 0
		for (i : 0 ..< featNumber) {
			if (rng.nextFloat <	0.05) {
				mutations++
			}	
		}
		//println("Mutations: " + mutations)
		mutations
	}
	
}