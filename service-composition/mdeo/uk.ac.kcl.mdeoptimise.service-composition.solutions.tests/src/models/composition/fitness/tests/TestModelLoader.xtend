//package models.composition.fitness.tests
//
//import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
//import org.eclipse.emf.ecore.EPackage
//import uk.ac.kcl.mdeoptimise.Optimisation
//import org.eclipse.emf.common.util.URI
//import java.util.Collections
//
//class TestModelLoader {
//	
//	private HenshinResourceSet henshinResourceSet
//
//    private EPackage theMetamodel
//	
//	
//	
//	def getResourceSet() {
//        if (henshinResourceSet == null) {
//            henshinResourceSet = new HenshinResourceSet("tests/resources/")
//        }
//
//        henshinResourceSet
//    }
//
//    def getMetamodel() {
//        if (theMetamodel == null) {
//            theMetamodel = getResourceSet.registerDynamicEPackages("OrchestrationMM.ecore").head
//        }
//
//        theMetamodel
//    }
//	
//	def loadModel(String path){
//		val resource = resourceSet.createResource(URI.createURI(path))
//		resource.load(Collections.EMPTY_MAP)
//		resource.allContents.head
//	}
//}
