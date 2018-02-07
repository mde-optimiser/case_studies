package models.composition.fitness.tests;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

public class TestModelLoader {

	private HenshinResourceSet henshinResourceSet;
	
	public HenshinResourceSet getResourceSet() {
		
		if(henshinResourceSet == null) {
			henshinResourceSet = new HenshinResourceSet("src/resources/");
		}
		
		return henshinResourceSet;
	}
	
	public EObject loadModel(String path) {
		
		Resource resource = getResourceSet().createResource(URI.createURI(path));
		
		try {
			
			resource.load(Collections.EMPTY_MAP);
			return resource.getAllContents().next();
		
		}catch (Exception e) {
			
		}
	
		return null;
	}
	
}
