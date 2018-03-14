package models.service.composition.fitness.jni

import org.eclipse.emf.ecore.EObject
import java.util.List
import java.util.LinkedHashSet

class EnsureAllAbstractServicesAreOrchestrated  extends AbstractNativeGuidanceFunction {
	
	override computeFitness(EObject model) {
		
		
		val abstractServices = model.getFeatureObject("abstractPlan").getFeatureList("abstractServices")
			.map[abstractService | abstractService.getFeatureInt("ID")];
		
		val orchestrators = model.getFeatureList("concretePlans").head.getFeatureList("orchestrators");
		
		
		 val orchestratedAbstractServices = new LinkedHashSet<Integer>();	
		
		orchestrators.forEach[orchestrator |
			
			var concreteServices = orchestrator.getFeatureList("concreteServices");
			
			concreteServices.forEach[concreteService | 
				orchestratedAbstractServices.add(concreteService.getFeatureObject("abstractService").getFeatureInt("ID")); 
			];
			
		]
		
		println("Total abstract services: " + abstractServices);
		println("Orchestrated abstract services: " + orchestratedAbstractServices);
		println("Unorchestrated abstract services: " + (abstractServices.size - orchestratedAbstractServices.size))
		
		return abstractServices.size - orchestratedAbstractServices.size
	}
	
	override getName() {
		return "Minimise Unorchestrated Services"
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
	}
	
	/**
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
		