package models.nrp;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class MinimiseCost implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    final Function2<Double, EObject, Double> _function = (Double result, EObject artifact) -> {
      Object _feature = this.getFeature(IterableExtensions.<EObject>head(this.getReferenceFeature(artifact, "costs")), "amount");
      return Double.valueOf(DoubleExtensions.operator_plus(result, ((Double) _feature)));
    };
    final Double selectedArtifactsCost = IterableExtensions.<EObject, Double>fold(this.getReferenceFeature(IterableExtensions.<EObject>head(this.getReferenceFeature(model, "solutions")), "selectedArtifacts"), Double.valueOf(0d), _function);
    InputOutput.<String>println(("Calculated selectedArtifacts cost: " + selectedArtifactsCost));
    return (selectedArtifactsCost).doubleValue();
  }
  
  @Override
  public String getName() {
    return "Minimise Next Release Cost";
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject.
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
  
  public Iterable<EObject> getReferenceFeature(final EObject o, final String feature) {
    final Object object = this.getFeature(o, feature);
    BasicEList<EObject> features = new BasicEList<EObject>();
    if ((object instanceof EObject)) {
      features.add(((EObject)object));
    } else {
      features = ((BasicEList<EObject>) object);
    }
    return features;
  }
}
