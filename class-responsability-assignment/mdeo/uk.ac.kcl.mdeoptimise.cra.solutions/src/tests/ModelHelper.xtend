package tests

import java.io.IOException
import java.util.HashMap
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.xmi.XMIResource
import org.eclipse.emf.henshin.interpreter.UnitApplication
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet

class ModelHelper {
	
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
	
	def static void save(EObject model, HenshinResourceSet rs) {
		
		val resource = rs.createResource("output.xmi")
		resource.getContents().clear()
		resource.getContents().add(model);
		val options = new HashMap<Object,Object>();		
		options.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			
		try {
			resource.save(options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
}