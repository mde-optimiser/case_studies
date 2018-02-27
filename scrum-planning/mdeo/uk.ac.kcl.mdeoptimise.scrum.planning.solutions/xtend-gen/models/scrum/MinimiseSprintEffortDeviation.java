package models.scrum;

import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class MinimiseSprintEffortDeviation implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "sprints");
    final Function1<EObject, Double> _function = (EObject sprint) -> {
      Object _feature_1 = this.getFeature(sprint, "committedItem");
      final Function2<Double, EObject, Double> _function_1 = (Double result, EObject item) -> {
        Object _feature_2 = this.getFeature(item, "Effort");
        return Double.valueOf(DoubleExtensions.operator_plus(result, ((Integer) _feature_2)));
      };
      Double _fold = IterableExtensions.<EObject, Double>fold(((EList<EObject>) _feature_1), Double.valueOf(0d), _function_1);
      return new Double((double) _fold);
    };
    List<Double> fitness = IterableExtensions.<Double>toList(ListExtensions.<EObject, Double>map(((EList<EObject>) _feature), _function));
    final List<Double> _converted_fitness = (List<Double>)fitness;
    double effortStandardDeviation = new StandardDeviation().evaluate(((double[])Conversions.unwrapArray(_converted_fitness, double.class)));
    InputOutput.<String>println(("Sprint effort distribution: " + fitness));
    InputOutput.<String>println(("Sprint effort standard deviation: " + Double.valueOf(effortStandardDeviation)));
    return effortStandardDeviation;
  }
  
  @Override
  public String getName() {
    return "Maximise average sprint effort";
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
