package models.scrum;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

@SuppressWarnings("all")
public class HasNoUnassignedWorkItems implements IGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    Object _feature = this.getFeature(model, "backlog");
    Object _feature_1 = this.getFeature(((EObject) _feature), "workitems");
    EList<EObject> workItems = ((EList<EObject>) _feature_1);
    int fitness = 0;
    boolean _notEquals = (!Objects.equal(workItems, null));
    if (_notEquals) {
      final Function1<EObject, Boolean> _function = (EObject workItem) -> {
        Object _feature_2 = this.getFeature(workItem, "isPlannedFor");
        return Boolean.valueOf(Objects.equal(_feature_2, null));
      };
      fitness = ((Object[])Conversions.unwrapArray(IterableExtensions.<EObject>toList(IterableExtensions.<EObject>filter(workItems, _function)), Object.class)).length;
    }
    InputOutput.<String>println(("Unassigned backlog work items: " + Integer.valueOf(fitness)));
    return fitness;
  }
  
  @Override
  public String getName() {
    return "Mimise unassigned work items";
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
