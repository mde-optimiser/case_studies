package models.scrum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
public class MinimiseCustomerSatisfactionIndex implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "sprints");
    final EList<EObject> sprints = ((EList<EObject>) _feature);
    final StandardDeviation standardDeviationCalculator = new StandardDeviation();
    final ArrayList<Double> stakeholderImportanceSprintDeviation = new ArrayList<Double>();
    Object _feature_1 = this.getFeature(model, "stakeholders");
    final Consumer<EObject> _function = (EObject stakeholder) -> {
      final Function1<EObject, Double> _function_1 = (EObject sprint) -> {
        Object _feature_2 = this.getFeature(sprint, "committedItem");
        final Function1<EObject, Boolean> _function_2 = (EObject item) -> {
          return Boolean.valueOf(this.getFeature(item, "stakeholder").equals(stakeholder));
        };
        final Function2<Double, EObject, Double> _function_3 = (Double result, EObject item) -> {
          Object _feature_3 = this.getFeature(item, "Importance");
          return Double.valueOf(DoubleExtensions.operator_plus(result, ((Integer) _feature_3)));
        };
        Double _fold = IterableExtensions.<EObject, Double>fold(IterableExtensions.<EObject>filter(((EList<EObject>) _feature_2), _function_2), Double.valueOf(0d), _function_3);
        return new Double((double) _fold);
      };
      List<Double> effortAccrossSprints = ListExtensions.<EObject, Double>map(sprints, _function_1);
      final List<Double> _converted_effortAccrossSprints = (List<Double>)effortAccrossSprints;
      stakeholderImportanceSprintDeviation.add(Double.valueOf(standardDeviationCalculator.evaluate(((double[])Conversions.unwrapArray(_converted_effortAccrossSprints, double.class)))));
    };
    ((EList<EObject>) _feature_1).forEach(_function);
    double importanceStandardDeviation = standardDeviationCalculator.evaluate(((double[])Conversions.unwrapArray(stakeholderImportanceSprintDeviation, double.class)));
    InputOutput.<String>println(("Sprint stakeholder importance distribution: " + stakeholderImportanceSprintDeviation));
    InputOutput.<String>println(("Sprint Customer Satisfaction Index: " + Double.valueOf(importanceStandardDeviation)));
    return importanceStandardDeviation;
  }
  
  @Override
  public String getName() {
    return "Minimise Customer Satisfaction Index";
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
