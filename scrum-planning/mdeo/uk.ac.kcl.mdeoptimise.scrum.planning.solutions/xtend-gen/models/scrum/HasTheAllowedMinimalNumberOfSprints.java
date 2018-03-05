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
public class HasTheAllowedMinimalNumberOfSprints implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "backlog");
    Object _feature_1 = this.getFeature(((EObject) _feature), "workitems");
    EList<EObject> workitems = ((EList<EObject>) _feature_1);
    final Function2<Integer, EObject, Integer> _function = (Integer result, EObject item) -> {
      Object _feature_2 = this.getFeature(item, "Effort");
      return Integer.valueOf(((result).intValue() + (((Integer) _feature_2)).intValue()));
    };
    Integer totalEffort = IterableExtensions.<EObject, Integer>fold(workitems, Integer.valueOf(0), _function);
    double desiredSprints = 0d;
    int maximumVelocity = 35;
    if (((totalEffort).intValue() > maximumVelocity)) {
      double _parseDouble = Double.parseDouble(totalEffort.toString());
      double _divide = (_parseDouble / maximumVelocity);
      desiredSprints = _divide;
      int _intValue = Double.valueOf(desiredSprints).intValue();
      double _minus = (desiredSprints - _intValue);
      boolean _greaterThan = (_minus > 0.5d);
      if (_greaterThan) {
        desiredSprints = Math.ceil(desiredSprints);
      } else {
        desiredSprints = Math.floor(desiredSprints);
      }
    }
    Object _feature_2 = this.getFeature(model, "sprints");
    final Function1<EObject, Boolean> _function_1 = (EObject sprint) -> {
      Object _feature_3 = this.getFeature(sprint, "committedItem");
      int _length = ((Object[])Conversions.unwrapArray(((EList<EObject>) _feature_3), Object.class)).length;
      return Boolean.valueOf((_length > 0));
    };
    List<EObject> sprints = IterableExtensions.<EObject>toList(IterableExtensions.<EObject>filter(((EList<EObject>) _feature_2), _function_1));
    final List<EObject> _converted_sprints = (List<EObject>)sprints;
    int _length = ((Object[])Conversions.unwrapArray(_converted_sprints, Object.class)).length;
    String _plus = ("Counted sprints: " + Integer.valueOf(_length));
    InputOutput.<String>println(_plus);
    InputOutput.<String>println(("Counted minimal desired sprints: " + Double.valueOf(desiredSprints)));
    final List<EObject> _converted_sprints_1 = (List<EObject>)sprints;
    int _length_1 = ((Object[])Conversions.unwrapArray(_converted_sprints_1, Object.class)).length;
    boolean _lessThan = (_length_1 < desiredSprints);
    if (_lessThan) {
      final List<EObject> _converted_sprints_2 = (List<EObject>)sprints;
      int _length_2 = ((Object[])Conversions.unwrapArray(_converted_sprints_2, Object.class)).length;
      return (desiredSprints - _length_2);
    }
    return 0;
  }
  
  @Override
  public String getName() {
    return "Has the allowed minimal number of sprints";
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
