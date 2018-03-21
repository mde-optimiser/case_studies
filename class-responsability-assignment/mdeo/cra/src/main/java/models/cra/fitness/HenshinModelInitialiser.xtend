package models.cra.fitness

import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl
import org.eclipse.emf.henshin.interpreter.impl.UnitApplicationImpl
import org.eclipse.emf.henshin.interpreter.Engine
import org.eclipse.emf.henshin.interpreter.impl.ChangeImpl
import uk.ac.kcl.interpreter.IModelInitialiser
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.henshin.model.Module
import java.io.File
import java.io.FileFilter
import java.util.concurrent.ArrayBlockingQueue

class HenshinModelInitialiser implements IModelInitialiser {
	
	
	private UnitApplicationImpl unitRunner;
	private Module module;
	
	private ArrayBlockingQueue modelsQueue;
	
	new(){
		// Create an engine and a rule application:
		var engine = new EngineImpl();
		engine.getOptions().put(Engine.OPTION_DETERMINISTIC, false);
		this.unitRunner = new UnitApplicationImpl(engine);
		
		this.modelsQueue = new ArrayBlockingQueue<String>(30)
		
		listInitialModels.forEach[
			m | this.modelsQueue.add(m.name)
		]
		
		
		
	}
	
	
	def listInitialModels(){
		
		var file = new File("src/main/resources/models/cra/gen/")
		
		file.listFiles(new FileFilter(){
			
			override accept(File pathname) {
				pathname.path.endsWith(".xmi") == true
			}
			
		})
		
		
	}
	
	override initialise(EObject model) {
		
		val resourceSet = model.eResource.resourceSet as HenshinResourceSet
		
		var object = resourceSet.getEObject("gen/" + this.modelsQueue.poll)
	
		return object
	}
	
}