package tests

import java.util.Collection
import org.eclipse.emf.ecore.EObject

class ModelExtensions {
	
	/**
	 * Returns for the given EObject the value of the feature with name 
	 * featureName or null if such a feature does not exists.
	 */
	def static Object getEFeature (EObject o, String featureName) {		
		if(o === null){
			println("Null object given")
			throw new IllegalArgumentException
		}		
		val feature = o.eClass.getEStructuralFeature(featureName)
		if (feature === null) {
			println("Feature not existing")
			return null
		}
		o.eGet(feature)	
	}
	
	def static void setEFeature(EObject o, String featureName, Object feature) {
		o.eSet(o.eClass.getEStructuralFeature(featureName), feature)	
	}	
	
	def static Collection toCollection (Object o) {
		if (o instanceof Collection) {
			o  as Collection
		} else {
			println("Object is no collection")
			throw new IllegalArgumentException
		}
	}	
	
	def static EObject getElement(EObject model, String elementName) {
		model.eAllContents.findFirst[o | o.getEFeature("name") !== null && o.getEFeature("name").equals(elementName)]
	}
}