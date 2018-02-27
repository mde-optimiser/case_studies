package models.service.composition.fitness

import java.util.List
import org.eclipse.emf.ecore.EObject
import java.util.ArrayList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import org.eclipse.emf.common.util.URI
import java.util.Collections

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
	
	//Cached predictors object
	private static EObject predictors;
	
	def EObject getPredictors(){
		
		if(PredictorsCalculator.predictors === null) {
			var henshinResourceSet = new HenshinResourceSet("/home/alxbrd/projects/alxbrd/github/case_studies/service-composition/mdeo/uk.ac.kcl.mdeoptimise.service-composition.solutions/src/models/service/composition");
			val resource = henshinResourceSet.createResource(URI.createURI("connected-nodes.xmi"));
					
			resource.load(Collections.EMPTY_MAP);
			PredictorsCalculator.predictors = resource.getAllContents().next();
		}
		
		return PredictorsCalculator.predictors;
	}
	
	def List<Integer> calculatePredictors(EObject model) {
		var predictors = new ArrayList<Integer>();
		
		//Always making the request from node 1
		predictors.add(1)
		
		//Orchestrators
		predictors.add(this.calculateOrchestrators(model));
		
		//Total Hops - hops
		predictors.add(this.calculateHops(model, this.getPredictors));
		
		//Fast Nodes - devFast
		predictors.add(this.countTypeNodes(1, model, this.getPredictors))
		
		//Medium Nodes - devMedium
		predictors.add(this.countTypeNodes(2, model, this.getPredictors))
		
		//Slow Nodes - devSlow
		predictors.add(this.countTypeNodes(3, model, this.getPredictors))
		
		//Highly Loaded Nodes - loadBig
		predictors.add(this.countLoadNodes(1, model, this.getPredictors))
		
		//Medium Loaded Nodes - loadMedium
		predictors.add(this.countLoadNodes(2, model, this.getPredictors))
		
		//Low Loaded Nodes - loadSmall
		predictors.add(this.countLoadNodes(3, model, this.getPredictors))
		
		return predictors;
	}
	
	/**
	 * Calculates the number of non-empty orchestrators in a concrete plan.
	 */
	def int calculateOrchestrators(EObject model) {
		var concretePlans = model.getFeature("concretePlans") as EList<EObject>
		
		//Count non empty orchestrators
		return concretePlans.fold(0)[ orchestratorsCount, concretePlan |
			orchestratorsCount + 
			
			((concretePlan.getFeature("orchestrators") as List<EObject>).filter[
				orchestrator | orchestrator.getFeature("abstractServices") !== null || orchestrator.getFeature("concreteServices") !== null
			]).toList.size
		]
	}
	
	/**
	 * Calculates the total number of hops between the nodes within a service composition configuration.
	 * Returns the total number of hops between a pair of nodes.
	 */
	def int calculateHops(EObject model, EObject predictors){
		
		var hops = 0;
		
		var orchestrators = model.getFeatureList("concretePlans").head.getFeatureList("orchestrators")

		//Measure the number of hops between the first node and the first orchestrator
		if( orchestrators.head !== null){
			
			var deployedNodeId = orchestrators.head.getFeatureObject("deployedOn").getFeatureInt("ID")
			
			hops = hops + lookupConnection(1, deployedNodeId, predictors)
		}
		
		//Doing this with a fold below crashes Eclipse. Enough said!
		val xtendHighlightingHack = new ArrayList<Integer>();
		
		orchestrators.forEach[ orchestrator | 
			
			val concreteServices = orchestrator.getFeatureList("concreteServices")
			
			//Count the hops between this orchestrator and the concrete services it orchestrates
			//from the abstract plans assigned to it
			val orchestratorHopsCount = concreteServices.fold(0)[
				planHops, concreteService | 
				
				planHops + lookupConnection(orchestrator.getFeatureObject("deployedOn").getFeatureInt("ID"), 
					concreteService.getFeatureObject("providedBy").getFeatureInt("ID"), predictors
				)
			]
			
			xtendHighlightingHack.add(orchestratorHopsCount);
		]
		
		hops += lookupConnection(1, orchestrators.last.getFeatureObject("deployedOn").getFeatureInt("ID"), predictors)
		
		hops += xtendHighlightingHack.fold(0)[result, countedHops | result + countedHops]
		
		return hops;
	}
	
	/**
	 * Looks up a node by ID in the predictor model
	 */
	def EObject lookupNode(int id, EObject predictors) {
		
		//TODO Cache this selection
		return predictors.getFeatureList("nodes").filter[
			node | node.getFeatureInt("ID") === id
		].head
	}
	
	/**
	 * Counts the number of hops between two given nodes.
	 * Uses the predictor model to query this information.
	 */
	def int lookupConnection(int startNode, int endNode, EObject predictors) {
		
		if(startNode === endNode) {
			return 0;
		}
		
		var connections = predictors.getFeatureList("connections");
		
		var hops = connections.filter[ 
			connection |
			
			(connection.getFeatureObject("src").getFeatureInt("ID") === startNode 
				&& connection.getFeatureObject("tgt").getFeatureInt("ID") === endNode)
			||
			(connection.getFeatureObject("src").getFeatureInt("ID") === endNode 
				&& connection.getFeatureObject("tgt").getFeatureInt("ID") === startNode)
			
		].head.getFeatureInt("hops")
		
		return hops
		
	}
	
	/**
	 * Counts the various types of nodes within a service composition configuration.
	 * Returns the number of nodes with of the specific type.
	 */
	def int countTypeNodes(int nodeType, EObject model, EObject predictors){
		
		var orchestrators = model.getFeatureList("concretePlans").head.getFeatureList("orchestrators")
		
		return orchestrators
			.filter[
				orchestrator | 
					lookupNode(orchestrator.getFeatureObject("deployedOn").getFeatureInt("ID"), predictors)
					.getFeatureInt("type") === nodeType
			]
			.fold(0)[
				counter, orchestrator | 
				
					val concreteServices = orchestrator.getFeatureList("concreteServices")

					//Count the hops between this orchestrator and the concrete services it orchestrates
					//from the abstract plans assigned to it
					//Added 1 to also count the orchestrator node which is of the same type
					counter + 1 + concreteServices.filter[
						concreteService | 
							lookupNode(concreteService.getFeatureObject("providedBy").getFeatureInt("ID"), predictors)
								.getFeatureInt("type") === nodeType
				].length
		]
	}
	
	/**
	 * Counts the various types of loaded nodes within a service composition configuration.
	 * Returns the number of nodes that have the specific load.
	 */
	def int countLoadNodes(int load, EObject model, EObject predictors) {
		
		var orchestrators = model.getFeatureList("concretePlans").head.getFeatureList("orchestrators")
		
		return orchestrators
			.filter[
				orchestrator | 	
					lookupNode(orchestrator.getFeatureObject("deployedOn").getFeatureInt("ID"), predictors)
					.getFeatureInt("load") === load
			].fold(0)[
				counter, orchestrator | 
				
					val concreteServices = orchestrator.getFeatureList("concreteServices")
					
					//Count the hops between this orchestrator and the concrete services it orchestrates
					//from the abstract plans assigned to it
					//Added 1 to also count the orchestrator node which is of the same type
					counter + 1 + concreteServices.filter[
						concreteService | 
							lookupNode(concreteService.getFeatureObject("providedBy").getFeatureInt("ID"), predictors)
								.getFeatureInt("load") === load
				].length
		]
	}
	
	/**
	 * 
	 * Helper function getting the value of the named feature (if it exists) for the given EObject
	 */
	def Object getFeature (EObject o, String feature) {
		
		if(o === null){
			println("Null object given")
		}
		
		o.eGet(o.eClass.getEStructuralFeature(feature))
		
	}
	
	/**
	 * 
	 * Helper function getting the value of the named feature (if it exists) for the given EObject
	 * as list of EObjects
	 */
	def List<EObject> getFeatureList(EObject o, String feature) {		
		return this.getFeature(o, feature) as List<EObject>
	}	/**
	 * 
	 * Helper function getting the value of the named feature (if it exists) for the given EObject
	 * as a single EObject
	 */
	def EObject getFeatureObject(EObject o, String feature) {
		return this.getFeature(o, feature) as EObject
	}
	
	
	def int getFeatureInt(EObject o, String feature) {
		return Integer.parseInt(o.getFeature(feature).toString)
	}
}