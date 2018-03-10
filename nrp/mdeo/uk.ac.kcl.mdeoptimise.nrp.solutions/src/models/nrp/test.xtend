package models.nrp

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.BasicExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.henshin.model.Module
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet

class test {
	def static void main(String[] args) {
		val model = loadModelHenshin()
		
//		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
//    		"ecore", new EcoreResourceFactoryImpl());
//    
//   		val  rs = new ResourceSetImpl();
//		// enable extended metadata
//		val extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
//			rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
// 	   	extendedMetaData);
//
//		var uri = URI.createFileURI("src/models/nrp/nextReleaseProblem.ecore")
//		val r = rs.getResource(uri, true);
//		val eObject = r.getContents().get(0);
//	
//		if (eObject instanceof EPackage) {
//			var p = eObject as EPackage;
//  			rs.getPackageRegistry().put(p.getNsURI(), p);
//		}
//		
//	// Load rules and models
//		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
//		"xmi", new XMIResourceFactoryImpl());
//		
//		uri = URI.createFileURI("src/models/nrp/example.xmi")
//		val res = rs.createResource(uri)
//		val model = res.contents
		println(model)
		
		val selectedArtifacts =  model.getReferenceFeature("solutions").get(0).getReferenceFeature("selectedArtifacts")
		val selectedRealisations = selectedArtifacts.map(sa | sa.getReferenceFeature("realisations")).flatten
		selectedRealisations.forEach(sr | sr.calcFitnessImpact)	
		
	}
	
	def static double calcFitnessImpact(EObject realisation) {
		val valuedRequirements = realisation.getReferenceFeature("requirement").get(0).getReferenceFeature("valuations")
		var impact = 0.0d
		for (valuedReq : valuedRequirements) {
			val valuation = valuedReq.getFeature("value") as Double
			val sumOfCustomerValues = valuedReq.getReferenceFeature("desiredBy").map(c | c.getFeature("value") as Double).reduce(v1, v2 | v1 + v2)
			impact += sumOfCustomerValues * valuation * (realisation.getFeature("percentage") as Double)
		}
		println((realisation.getFeature("requirement") as EObject).getFeature("name") + '::' + realisation.getFeature("percentage") + ": " + impact)
		return impact
	}
	
	
	def static loadModelHenshin() {
		val HenshinResourceSet resourceSet = new HenshinResourceSet("src/models/nrp");
		val String metamodelPath = "nextReleaseProblem.ecore";
		val EPackage metamodel = resourceSet.registerDynamicEPackages(metamodelPath).get(0);
		//resourceSet.getPackageRegistry().put(metamodel.getNsURI(), metamodel);

		// Load model
		resourceSet.getResource("NRP.xmi").getContents().get(0);
	}
	
	def static Object getFeature (EObject o, String feature) {
		
		if(o === null){
			println("Null object given")
		}
		
		o.eGet(o.eClass.getEStructuralFeature(feature))
		
	}
	
	def static Iterable<EObject> getReferenceFeature(EObject o, String feature) {
		
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