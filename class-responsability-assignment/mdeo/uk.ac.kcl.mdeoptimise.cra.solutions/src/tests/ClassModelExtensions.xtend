package tests

import java.util.Collection
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EFactory
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.ResourceSet

class ClassModelExtensions {
	
	private static EPackage craPack
	private static EFactory fac
	private static EClass mmClassmodel
	private static EClass mmClass
	private static EClass mmMethod
	private static EClass mmAttribute
	private static EPackage trcPack
	private static EClass mmTrc
	
	def static setPackage(ResourceSet rs) {
		craPack = rs.packageRegistry.getEPackage("http://momot.big.tuwien.ac.at/architectureCRA/1.0")
		trcPack = rs.packageRegistry.getEPackage("http://www.eclipse.org/emf/2011/Henshin/Trace")
		fac = craPack.getEFactoryInstance
		mmClassmodel = craPack.getEClassifier("ClassModel") as EClass
		mmClass = craPack.getEClassifier("Class") as EClass
		mmMethod = craPack.getEClassifier("Method") as EClass
		mmAttribute = craPack.getEClassifier("Attribute") as EClass	
		mmTrc = trcPack.getEClassifier("Trace") as EClass
	}
	
	def static EObject newModel(String name) {
		createModelObject(mmClassmodel, name)
	}
	
	def static EObject newClass(String name) {
		createModelObject(mmClass, name)		
	}
	
	def static EObject newMethod(String name) {
		createModelObject(mmMethod, name)
	}
	
	def static EObject newAttribute(String name) {
		createModelObject(mmAttribute, name)
	}
	
	def static EObject createModelObject(EClass type, String name) {
		var object = fac.create(type)
		object.setEFeature("name", name)
		object
	}
	
	def static EObject newTrace(String name) {
		var trace = trcPack.EFactoryInstance.create(mmTrc)
		trace.setEFeature("name", name)
		trace
	}
		
	def static EObject addClass(EObject classmodel, EObject clazz) {
		classmodel.getEFeature("classes").toCollection.add(clazz)
		classmodel
	}
	
	def static EObject addFeature(EObject classmodel, EObject feature) {
		classmodel.getEFeature("features").toCollection.add(feature)
		classmodel
	}
	
	def static EObject assignFeature(EObject clazz, EObject feature) {
		clazz.getEFeature("encapsulates").toCollection.add(feature)
		clazz
	}
	
	def static EObject addDataDependency(EObject method, EObject feature) {
		method.getEFeature("dataDependency").toCollection.add(feature)
		method
	}
	
	def static EObject addFunctionalDependency(EObject method, EObject feature) {
		method.getEFeature("functionalDependency").toCollection.add(feature)
		method
	}
	
	def static EObject addSource(EObject trace, EObject feature) {
		trace.getEFeature("source").toCollection.add(feature)
		trace
	}
	
	def static EObject addTarget(EObject trace, EObject feature) {
		trace.getEFeature("target").toCollection.add(feature)
		trace
	}
	
	def static Object getEFeature (EObject o, String featureName) {		
		if(o === null){
			println("Null object given")
		}		
		o.eGet(o.eClass.getEStructuralFeature(featureName))		
	}
	
	def static void setEFeature(EObject o, String featureName, Object feature) {
		o.eSet(o.eClass.getEStructuralFeature(featureName), feature)	
	}	
	
	def static Collection toCollection (Object o) {
		if (o instanceof Collection) {
			o  as Collection
		} else {
			println("Object is no collection")
			null
		}
	}
	
	
	def static EObject getElement(EObject classmodel, String elementName) {
		classmodel.eAllContents.findFirst[o | o.getEFeature("name") != null &&  o.getEFeature("name").equals(elementName)]
	}
}