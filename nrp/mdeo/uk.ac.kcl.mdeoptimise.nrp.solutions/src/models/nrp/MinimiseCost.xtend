package models.nrp

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.BasicEList

class MinimiseCost  implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		val selectedArtifacts =  model.getReferenceFeature("solutions").get(0).getReferenceFeature("selectedArtifacts")
		val selectedRealisations = selectedArtifacts.map(sa | sa.getReferenceFeature("realisations")).flatten
		selectedRealisations.forEach(sr | sr.calcFitnessImpact)
		
		return 1.0d
		
	}
	
	def double calcFitnessImpact(EObject realisation) {
		val list = newArrayList(1,2,3)
		list.reduce[p1, p2| p1 + p2]
		val valuedRequirements = realisation.getReferenceFeature("requirement").get(0).getReferenceFeature("valuations")
		for (valuedReq : valuedRequirements) {
			val sumOfCustomerValues = valuedReq.getReferenceFeature("desiredBy")
			
			//valuedReq.getFeature("value") * realisation.getFeature("percentage")
		}
		return 1.0d
	}
	
	override getName() {
		return "Minimise Next Release Cost"
	}

	/**
	 * Helper function getting the value of the named feature (if it exists) for the given EObject.
	 */
	def Object getFeature (EObject o, String feature) {
		
		if(o === null){
			println("Null object given")
		}
		
		o.eGet(o.eClass.getEStructuralFeature(feature))
		
	}
	
	def Iterable<EObject> getReferenceFeature(EObject o, String feature) {
		
		val object = (o.getFeature(feature))
		var features = new BasicEList<EObject>();
		
		if(object instanceof EObject) {
					features.add(object)
		} else {
			features = object as BasicEList<EObject>;
		}		
		return features
	}	
}