package models.cra.parameters

import uk.ac.kcl.interpreter.evolvers.parameters.IEvolverParametersFunction
import java.util.List
import org.eclipse.emf.ecore.EObject

class BooleanEvolverParameterTrue implements IEvolverParametersFunction {
	
	override sample(List<EObject> arg0) {
		return true;
	}
	
}