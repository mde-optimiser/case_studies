package tests

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import java.io.IOException
import org.eclipse.emf.ecore.xmi.XMIResource
import java.util.HashMap
import org.eclipse.emf.ecore.resource.Resource

class ModelHelper {
	
	var HenshinResourceSet rs
	
	def static HenshinResourceSet prepareEnvironment() {
		val HenshinResourceSet rs = new HenshinResourceSet("src/models/nrp");
		val String metamodelPath = "nextReleaseProblem.ecore";
		rs.registerDynamicEPackages(metamodelPath).get(0);
		return rs
	}
	
	def static EObject loadFirstModel(HenshinResourceSet rs, String filename) {
		rs.getResource(filename).contents.get(0);
	}
	
	def static EObject getModel(Resource r, int i) {
		r.contents.get(i)
	}
	
	def static void save(HenshinResourceSet rs, EObject model, String filename) {
		val resource = rs.createResource(filename)
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