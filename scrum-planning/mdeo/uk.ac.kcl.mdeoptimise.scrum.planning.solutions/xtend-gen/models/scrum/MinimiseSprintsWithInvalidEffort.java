package models.scrum;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class MinimiseSprintsWithInvalidEffort implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "sprints");
    final Function1<EObject, Boolean> _function = (EObject sprint) -> {
      boolean _xblockexpression = false;
      {
        Object _feature_1 = this.getFeature(sprint, "committedItem");
        final Function2<Integer, EObject, Integer> _function_1 = (Integer result, EObject item) -> {
          Object _feature_2 = this.getFeature(item, "Effort");
          return Integer.valueOf(((result).intValue() + (((Integer) _feature_2)).intValue()));
        };
        Integer _fold = IterableExtensions.<EObject, Integer>fold(((EList<EObject>) _feature_1), Integer.valueOf(0), _function_1);
        Integer size = ((Integer) _fold);
        _xblockexpression = ((size).intValue() > 35);
      }
      return Boolean.valueOf(_xblockexpression);
    };
    List<EObject> invalidSprints = IterableExtensions.<EObject>toList(IterableExtensions.<EObject>filter(((EList<EObject>) _feature), _function));
    int fitness = 0;
    if ((invalidSprints != null)) {
      final List<EObject> _converted_invalidSprints = (List<EObject>)invalidSprints;
      fitness = ((Object[])Conversions.unwrapArray(_converted_invalidSprints, Object.class)).length;
    }
    InputOutput.<String>println(("Counted invalid UB sprints: " + Integer.valueOf(fitness)));
    return fitness;
  }
  
  @Override
  public String getName() {
    return "Minimise sprints with invalid effort";
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
