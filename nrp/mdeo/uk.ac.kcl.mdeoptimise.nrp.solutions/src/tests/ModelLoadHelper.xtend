package tests

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet

class ModelLoadHelper {
	
	def static EObject loadModel(String filename) {
		val HenshinResourceSet resourceSet = new HenshinResourceSet("src/tests/testModels");
		val String metamodelPath = "../../models/nrp/nextReleaseProblem.ecore";
		resourceSet.registerDynamicEPackages(metamodelPath).get(0);
		resourceSet.getResource(filename).getContents().get(0);
	}
}