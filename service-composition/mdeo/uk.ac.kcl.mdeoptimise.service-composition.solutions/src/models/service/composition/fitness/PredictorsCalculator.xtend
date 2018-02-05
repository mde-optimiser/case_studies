package models.service.composition.fitness

import java.util.List
import org.eclipse.emf.ecore.EObject
import java.util.ArrayList
import org.eclipse.emf.common.util.EList

class PredictorsCalculator {
//	
//	  predictors.put("orchestrators", object.calculateOrhcestratos());
//      predictors.put("hops", object.calculateHops());
//      predictors.put("devFast", object.countTypeNodes(1));
//      predictors.put("devMedium", object.countTypeNodes(2));
//      predictors.put("devSlow", object.countTypeNodes(3));
//      predictors.put("loadBig", object.countLoadNodes(1));
//      predictors.put("loadMedium", object.countLoadNodes(2));
//      predictors.put("loadSmall", object.countLoadNodes(3));
	
	def List<Integer> calculatePredictors(EObject model) {
		var predictors = new ArrayList<Integer>();
		
		//Orchestrators
		predictors.add(this.calculateOrchestrators(model));
		
		//Total Hops - hops
		predictors.add(this.calculateHops(model));
		
		//Fast Nodes - devFast
		predictors.add(this.countTypeNodes(model, 1))
		
		//Medium Nodes - devMedium
		predictors.add(this.countTypeNodes(model, 2))
		
		//Slow Nodes - devSlow
		predictors.add(this.countTypeNodes(model, 3))
		
		//Highly Loaded Nodes - loadBig
		predictors.add(this.countLoadNodes(model, 1))
		
		//Medium Loaded Nodes - loadMedium
		predictors.add(this.countLoadNodes(model, 2))
		
		//Low Loaded Nodes - loadSmall
		predictors.add(this.countLoadNodes(model, 3))
		
		return predictors;
		
	}

	def int calculateOrchestrators(EObject model) {
		var concretePlans = model.getFeature("concretePlans") as EList<EObject>
		
		//Count non empty orchestrators
		return concretePlans.fold(0)[ orchestratorsCount, concretePlan |
			orchestratorsCount + 
			
			((concretePlan.getFeature("orchestrator") as EList<EObject>).filter[
				orchestrator | orchestrator.getFeature("abstractServices") !== null || orchestrator.getFeature("concreteServices") !== null
			] as EList<EObject>).size
		]
	}
	
	def int calculateHops(EObject model){
		return 0;
	}
	
	def int countTypeNodes(EObject model, int nodeType){
		return 0;
	}
	
	def int countLoadNodes(EObject model, int load) {
		return 0;
	}
	
	/**
	 * Helper function getting the value of the named feature (if it exists) for the given EObject.
	 */
	def Object getFeature (EObject o, String feature) {
		
		if(o === null){
			println("Null object given")
		}
		
		o.eGet (o.eClass.getEStructuralFeature(feature))
		
	}
}