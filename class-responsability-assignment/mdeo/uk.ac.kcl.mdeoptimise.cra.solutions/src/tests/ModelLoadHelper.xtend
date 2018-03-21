package tests

import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet

class ModelLoadHelper {
	
	def static EObject loadModel(String filename, int rootElement) {
		val HenshinResourceSet resourceSet = new HenshinResourceSet("src/tests/testModels");
		val String metamodelPath = "../../models/cra/architectureCRA.ecore";
		resourceSet.registerDynamicEPackages(metamodelPath);
		resourceSet.getResource(filename).getContents().get(rootElement);
	}
	
	def static List<EObject> getAllRootElements(String filename) {
		val HenshinResourceSet resourceSet = new HenshinResourceSet("src/tests/testModels");
		val String metamodelPath = "../../models/cra/architectureCRA.ecore";
		resourceSet.registerDynamicEPackages(metamodelPath);
		resourceSet.getResource(filename).getContents();
	}
	
	
}