package models.nrp.fitness

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.ecore.EObject
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution

class MaximiseSatisfaction  implements IGuidanceFunction {
	
	/**
	 * Computes the overall satisfaction generated by the selected software 
	 * artifacts.
	 * <p>
	 * The overall satisfaction is determined by the sum of all customer
	 * satisfactions (weighted by their importance value). 
	 * A customer's satisfaction is determined by the sum of satisfaction 
	 * contributions of the requirements directly desired by the customer.
	 * The contribution of such a requirement, in turn, is determined by its
	 * level of fulfillment weighted by its valuation assigned by the customer.
	 * </p>
	 * <p>
	 * The level of fulfillment of a requirement is usually determined by the
	 * percentage to which a software artifact realises the requirement. 
	 * However, a requirement can be simultaneously fulfilled to different
	 * degrees by different artifacts. Additionally, it can also be fulfilled
	 * by a combination of other requirements. In the latter case, the level of
	 * fulfillment is determined by the weighted sum of the levels of 
	 * fulfillment of those dependencies.
	 * In both of the above cases, the highest contribution of fulfillment
	 * will determine the overall level of fulfillment of the requirement.
	 * </p>
	 * <p>
	 * <em>Example:</em></br>
	 * Requirement A depends on two Requirements B and C. The valuations 
	 * connecting A to B and C have the values v(B)=2, v(C)=4. The level of 
	 * fulfillments are f(B)=0.8, f(C)=0.5. The level of fulfillment for A
	 * then is f(A)=(0.8*2 + 0.5*4)/6=0.6.
	 * In case there is a direct realisation for A with a percentage of 0.8
	 * the overall level of fulfillment will be max(0.6, 0.8)=0.8.
	 * </p> 
	 * 
	 */
	override computeFitness(Solution solution) {
		var satisfaction = solution.model.getReferenceFeature('customers').fold(0.0d)[result, customer | 
			result + ((customer.getFeature('importance') as Double) * customer.calculateSatisfaction)
		]
		
		println("Found satisfaction: " + satisfaction)
		
		return satisfaction;
	}
	
	/**
	 * Calculates the weighted sum of all valuations of direct requirements for
	 * the given customer. Direct requirements are those which do not depend on
	 * other requirements.
	 */
	def Double calculateSatisfaction(EObject customer) {
		val directValuations = customer.getReferenceFeature('assigns').filter(v | v.getReferenceFeature('contributesTo').isEmpty)
		val maxDirectValuations = directValuations.selectMaximalValuations
		maxDirectValuations.calcOverallFulfillmentContribution(customer)
	}
	
	/**
	 * Calculates the fulfillment of the given requirement, that is the highest
	 * degree to which the requirement is fulfilled by either direct 
	 * realisations or a combination of dependency requirements.
	 */
	def Double calculateFulfillment(EObject requirement, EObject customer) {
		val maxRealisation = requirement.getReferenceFeature('realisations').filter(r | r.isImplemented).map(r | r.getFeature('percentage') as Double).fold(0.0d)[result, perc | Math.max(result, perc)]
		val dependencyValuations = requirement.getDependencyValuations(customer)
		
		var aggregatedDepFulfillments = 0.0d	
		if (!dependencyValuations.isNullOrEmpty) {
			val maxDependencyValuations = dependencyValuations.selectMaximalValuations			
			aggregatedDepFulfillments = maxDependencyValuations.calcOverallFulfillmentContribution(customer)
		}
		Math.max(maxRealisation, aggregatedDepFulfillments)
	}
	
	/**
	 * Returns an iterable with all valuations of requirements which the given 
	 * requirement depends on and which have been assigned by the given 
	 * customer.
	 */	
	def Iterable<EObject> getDependencyValuations(EObject requirement, EObject customer) {
		requirement.getReferenceFeature('combines').filter(v | v.getReferenceFeature('assignedBy').exists[c | c === customer])
	}
	
	/**
	 * From the given valuations select only those which are maximal. In other 
	 * words, if multiple valuations reference the same requirement, only the
	 * one with the highest value will be selected. If multiple valuations with
	 * the maximum value exist, only one of them will be selected.
	 */	
	def Iterable<EObject> selectMaximalValuations(Iterable<EObject> valuations) {
		valuations.reject(v1 | valuations.exists[v2 | v1 !== v2
			&& (v1.getFeature('requirement') === v2.getFeature('requirement')) 
			&& (v1.getFeature('value') as Double) <= (v2.getFeature('value') as Double)
		])				
	}
	
	/**
	 * Sums up the fulfillment contributions of the requirements referenced by
	 * the given valuations regarding the weight imposed by these valuations.
	 */
	def Double calcOverallFulfillmentContribution(Iterable<EObject> valuations, EObject customer) {
		if (valuations.exists[v | (v.getFeature('value') as Double) < 0.0d]) {
			throw new IllegalArgumentException("Valuation values need to be greater than 0.")
		}
		val sumOfWeights = valuations.map(v | (v.getFeature('value') as Double)).fold(0.0d)[result, value | result + value]
		if (sumOfWeights == 0.0d) {
			throw new IllegalArgumentException("The sum of valuation values may not be 0.")
		}
		valuations.map(v | (v.getFeature('value') as Double) * (v.getFeature('requirement') as EObject).calculateFulfillment(customer))
			.fold(0.0d)[result, weightedValue | result + weightedValue] / sumOfWeights	
	}
	
	/**
	 * Checks if a realisation is implemented. That is, if all artifacts the
	 * realisation directly or indirectly depends on are selected in the solution. 
 	 */
	def isImplemented(EObject realisation) {
		realisation.getReferenceFeature('dependsOn').forall[sa | sa.isSelectedWithRequirements]
	}

	def boolean isSelectedWithRequirements(EObject softwareArtifact) {
		(softwareArtifact.getReferenceFeature('solutions').head !== null)
			&& softwareArtifact.getReferenceFeature('requires').forall[reqSa| reqSa.isSelectedWithRequirements]
	}

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
	
	override getName() {
		"Maximise Next Release Customer Satisfaction"
	}
}