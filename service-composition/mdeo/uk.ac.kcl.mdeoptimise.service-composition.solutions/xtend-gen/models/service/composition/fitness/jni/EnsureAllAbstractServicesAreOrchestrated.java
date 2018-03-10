package models.service.composition.fitness.jni;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;
import models.service.composition.fitness.jni.AbstractNativeGuidanceFunction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class EnsureAllAbstractServicesAreOrchestrated extends AbstractNativeGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    final Function1<EObject, Integer> _function = (EObject abstractService) -> {
      return Integer.valueOf(this.getFeatureInt(abstractService, "ID"));
    };
    final List<Integer> abstractServices = ListExtensions.<EObject, Integer>map(this.getFeatureList(this.getFeatureObject(model, "abstractPlan"), "abstractServices"), _function);
    final List<EObject> orchestrators = this.getFeatureList(IterableExtensions.<EObject>head(this.getFeatureList(model, "concretePlans")), "orchestrators");
    final LinkedHashSet<Integer> orchestratedAbstractServices = new LinkedHashSet<Integer>();
    final Consumer<EObject> _function_1 = (EObject orchestrator) -> {
      List<EObject> concreteServices = this.getFeatureList(orchestrator, "concreteServices");
      final Consumer<EObject> _function_2 = (EObject concreteService) -> {
        orchestratedAbstractServices.add(Integer.valueOf(this.getFeatureInt(this.getFeatureObject(concreteService, "abstractService"), "ID")));
      };
      concreteServices.forEach(_function_2);
    };
    orchestrators.forEach(_function_1);
    InputOutput.<String>println(("Total abstract services: " + abstractServices));
    InputOutput.<String>println(("Orchestrated abstract services: " + orchestratedAbstractServices));
    int _size = abstractServices.size();
    int _size_1 = orchestratedAbstractServices.size();
    int _minus = (_size - _size_1);
    String _plus = ("Unorchestrated abstract services: " + Integer.valueOf(_minus));
    InputOutput.<String>println(_plus);
    int _size_2 = abstractServices.size();
    int _size_3 = orchestratedAbstractServices.size();
    return (_size_2 - _size_3);
  }
  
  @Override
  public String getName() {
    return "Minimise Unorchestrated Services";
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   */
  public Object getFeature(final EObject o, final String feature) {
    Object _xblockexpression = null;
    {
      if ((o == null)) {
        InputOutput.<String>println("Null object given");
      }
      _xblockexpression = o.eGet(o.eClass().getEStructuralFeature(feature));
    }
    return _xblockexpression;
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   * as list of EObjects
   */
  public List<EObject> getFeatureList(final EObject o, final String feature) {
    Object _feature = this.getFeature(o, feature);
    return ((List<EObject>) _feature);
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   * as a single EObject
   */
  public EObject getFeatureObject(final EObject o, final String feature) {
    Object _feature = this.getFeature(o, feature);
    return ((EObject) _feature);
  }
  
  public int getFeatureInt(final EObject o, final String feature) {
    return Integer.parseInt(this.getFeature(o, feature).toString());
  }
}
