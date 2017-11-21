package models.stack;

import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class MinimiseStandardDeviation implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "stacks");
    final Function1<EObject, Double> _function = (EObject e) -> {
      Object _feature_1 = this.getFeature(e, "load");
      return new Double((((Integer) _feature_1)).intValue());
    };
    List<Double> fitness = ListExtensions.<EObject, Double>map(((EList<EObject>) _feature), _function);
    final List<Double> _converted_fitness = (List<Double>)fitness;
    double sD = new StandardDeviation().evaluate(((double[])Conversions.unwrapArray(_converted_fitness, double.class)));
    InputOutput.<String>println(("Found deviation: " + Double.valueOf(sD)));
    return sD;
  }
  
  @Override
  public String getName() {
    return "Minimise Standard Deviation";
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
}
