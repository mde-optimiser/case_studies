package models.scrum;

import java.util.List;
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
public class MinimiseSprints implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object sprints = this.getFeature(model, "sprints");
    if ((sprints == null)) {
      return 0;
    }
    EList<EObject> sprintsList = ((EList<EObject>) sprints);
    final EList<EObject> _converted_sprintsList = (EList<EObject>)sprintsList;
    int _length = ((Object[])Conversions.unwrapArray(_converted_sprintsList, Object.class)).length;
    String _plus = ("Counted sprints: " + Integer.valueOf(_length));
    InputOutput.<String>println(_plus);
    final Function1<EObject, Double> _function = (EObject sprint) -> {
      Object _feature = this.getFeature(sprint, "committedItem");
      final Function2<Double, EObject, Double> _function_1 = (Double result, EObject item) -> {
        Object _feature_1 = this.getFeature(item, "Effort");
        return Double.valueOf(DoubleExtensions.operator_plus(result, ((Integer) _feature_1)));
      };
      Double _fold = IterableExtensions.<EObject, Double>fold(((EList<EObject>) _feature), Double.valueOf(0d), _function_1);
      return new Double((double) _fold);
    };
    List<Double> sprintsEffortDistribution = IterableExtensions.<Double>toList(ListExtensions.<EObject, Double>map(sprintsList, _function));
    InputOutput.<String>println(("Sprint effort distribution: " + sprintsEffortDistribution));
    final EList<EObject> _converted_sprintsList_1 = (EList<EObject>)sprintsList;
    return ((Object[])Conversions.unwrapArray(_converted_sprintsList_1, Object.class)).length;
  }
  
  @Override
  public String getName() {
    return "Minimise number of sprints";
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
