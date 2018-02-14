package models.scrum

import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.interpreter.IGuidanceFunction
import org.eclipse.emf.common.util.EList

class MinimiseSprintsWithoutAverageVelocity implements IGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		
		var workitems = ((model.getFeature("backlog") as EObject).getFeature("workitems") as EList<EObject>);
		
		
		var totalEffort = workitems.fold(0)[result, item | result + (item.getFeature("Effort") as Integer)];
		
		var desiredSprints = 1d;
		
		if(totalEffort > 30){
		
			desiredSprints = Double.parseDouble(totalEffort.toString) / 30;
		
			if(desiredSprints -  desiredSprints.intValue > 0.5d){
				desiredSprints = Math.ceil(desiredSprints)
			} else {
				desiredSprints = Math.floor(desiredSprints)
			}
		
		}
		
		
		var sprints = (model.getFeature("sprints") as EList<EObject>).filter[ 
			sprint | (sprint.getFeature("committedItem") as EList<EObject>).length > 0].toList
		
		var fitness = 0
		
		if(sprints != null) {
			fitness = sprints.length	
		}
		
		println("Counted sprints: " + fitness)
		println("Counted desired sprints: " + desiredSprints);
		
		return fitness - desiredSprints
	}
	
	override getName() {
		return "Mimise empty sprints"
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
}