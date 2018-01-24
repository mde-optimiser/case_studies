package models.zoo;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import uk.ac.kcl.interpreter.IGuidanceFunction;

/**
 * Fitness function for zoo models. The fitness is given by the number of empty cages: The more cages are left empty, the
 * higher the fitness of the model.
 */
@SuppressWarnings("all")
public class ZooFitnessFunction implements IGuidanceFunction {
  /**
   * We expect the model to be an instance of the Zoo meta-class
   * and will count the number of empty cages.
   */
  @Override
  public double computeFitness(final EObject model) {
    final EClass zooClass = model.eClass();
    final EStructuralFeature cages = zooClass.getEStructuralFeature("cages");
    EClassifier _eType = cages.getEType();
    final EClass cageClass = ((EClass) _eType);
    final EStructuralFeature cagedAnimals = cageClass.getEStructuralFeature("animals");
    Object _eGet = model.eGet(cages);
    final Function2<Double, EObject, Double> _function = (Double cnt, EObject cage) -> {
      Double _xifexpression = null;
      Object _eGet_1 = cage.eGet(cagedAnimals);
      boolean _isEmpty = ((EList<EObject>) _eGet_1).isEmpty();
      if (_isEmpty) {
        _xifexpression = Double.valueOf(((cnt).doubleValue() + 1));
      } else {
        _xifexpression = cnt;
      }
      return _xifexpression;
    };
    final Double emptyCages = IterableExtensions.<EObject, Double>fold(((EList<EObject>) _eGet), Double.valueOf(0.0), _function);
    return ((-1) * (emptyCages).doubleValue());
  }
  
  @Override
  public String getName() {
    return "Empty Cages";
  }
}
