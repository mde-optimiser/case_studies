package models.argumentation.fitness

import org.eclipse.emf.ecore.EObject
import net.sf.tweety.arg.dung.DungTheory
import java.util.ArrayList
import org.eclipse.emf.common.util.EList
import net.sf.tweety.arg.dung.syntax.Argument
import net.sf.tweety.arg.dung.syntax.Attack
import net.sf.tweety.arg.dung.PreferredReasoner
import java.util.Map
import java.util.HashMap
import net.sf.tweety.arg.dung.GroundReasoner

class ECoreToDungHelper {
	
	EObject solutionModel = null;
	Map<DungTheory, Argument> dungMap = null;
	
	new(EObject object) {
		solutionModel = object;
		dungMap = solutionPAFGraphs
	}
	
	def Map<DungTheory, Argument> getSolutionPAFGraphs() {
		
		val persuadeeArgumentatonFrameworksMap = new HashMap<DungTheory, Argument>();
			
		(this.solutionModel.getFeature("persuadeeargumentationframework") as EList<EObject>).forEach[framework | 
			
			val argumentsList = new ArrayList<Argument>();
			val argumentAttacks = new ArrayList<Attack>();
			var Argument topicArgument = null//Query the pah to get the topic
			
			
			var topic = (framework.getFeature("hasPersuadee") as EObject).getFeature("hasTopic") as EObject
			
			if(topic != null){
				topicArgument = new Argument((topic.getFeature("argument") as EObject).getFeature("ID").toString)
			}
			
			val frameworkArguments = (framework.getFeature("hasArgument") as EList<EObject>).map[ argument | argument.getFeature("ID").toString];
			
			(framework.getFeature("hasArgument") as EList<EObject>).forEach[
				argument |
				
				val sourceDungArgument = new Argument(argument.getFeature("ID").toString);
				
				(argument.getFeature("attacks") as EList<EObject>).forEach[
					arg | 
					
					if(frameworkArguments.contains(arg.getFeature("ID").toString)) {
						var targetDungArgument = new Argument(arg.getFeature("ID").toString);
						argumentAttacks.add(new Attack(sourceDungArgument, targetDungArgument))
					}

				]
				
				argumentsList.add(sourceDungArgument);
			]
			
			val dungTheoryGraph = new DungTheory();
			
			dungTheoryGraph.addAll(argumentsList)
			dungTheoryGraph.addAllAttacks(argumentAttacks)
			
			persuadeeArgumentatonFrameworksMap.put(dungTheoryGraph, topicArgument)
		]
		
		return persuadeeArgumentatonFrameworksMap;
	}
	
	def double countAcceptableTopics() {
		
		return this.dungMap.keySet.fold(0d)[result, pafGraph | 
			
			var reasoner = new GroundReasoner(pafGraph);
			var argumentTopic = dungMap.get(pafGraph);
			var query = reasoner.query(argumentTopic);
			
			if(query.answerBoolean){
				return result + 1
			}
			
			return result  
		]
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