package models.scrum;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class MinimiseEmptySprints implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "sprints");
    final Function1<EObject, Boolean> _function = (EObject sprint) -> {
      Object _feature_1 = this.getFeature(sprint, "committedItem");
      int _length = ((Object[])Conversions.unwrapArray(((EList<EObject>) _feature_1), Object.class)).length;
      return Boolean.valueOf((_length == 0));
    };
    List<EObject> sprints = IterableExtensions.<EObject>toList(IterableExtensions.<EObject>filter(((EList<EObject>) _feature), _function));
    int fitness = 0;
    boolean _notEquals = (!Objects.equal(sprints, null));
    if (_notEquals) {
      final List<EObject> _converted_sprints = (List<EObject>)sprints;
      fitness = ((Object[])Conversions.unwrapArray(_converted_sprints, Object.class)).length;
    }
    InputOutput.<String>println(("Counted empty sprints: " + Integer.valueOf(fitness)));
    return fitness;
  }
  
  @Override
  public String getName() {
    return "Mimise empty sprints";
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
