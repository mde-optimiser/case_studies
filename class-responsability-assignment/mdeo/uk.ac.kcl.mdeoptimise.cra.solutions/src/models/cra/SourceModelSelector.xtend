package models.cra

import uk.ac.kcl.interpreter.evolvers.parameters.IEvolverParametersFunction
import java.util.List
import org.eclipse.emf.ecore.EObject

class SourceModelSelector implements IEvolverParametersFunction {
	
	override sample(List<EObject> model) {
		return model.get(0)		
	}
	
}