package models.nrp;

import com.google.common.collect.Iterables;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class test {
  public static void main(final String[] args) {
    final EObject model = test.loadModelHenshin();
    InputOutput.<EObject>println(model);
    final Iterable<EObject> selectedArtifacts = test.getReferenceFeature(((EObject[])Conversions.unwrapArray(test.getReferenceFeature(model, "solutions"), EObject.class))[0], "selectedArtifacts");
    final Function1<EObject, Iterable<EObject>> _function = (EObject sa) -> {
      return test.getReferenceFeature(sa, "realisations");
    };
    final Iterable<EObject> selectedRealisations = Iterables.<EObject>concat(IterableExtensions.<EObject, Iterable<EObject>>map(selectedArtifacts, _function));
    final Consumer<EObject> _function_1 = (EObject sr) -> {
      test.calcFitnessImpact(sr);
    };
    selectedRealisations.forEach(_function_1);
  }
  
  public static double calcFitnessImpact(final EObject realisation) {
    final Iterable<EObject> valuedRequirements = test.getReferenceFeature(((EObject[])Conversions.unwrapArray(test.getReferenceFeature(realisation, "requirement"), EObject.class))[0], "valuations");
    double impact = 0.0d;
    for (final EObject valuedReq : valuedRequirements) {
      {
        Object _feature = test.getFeature(valuedReq, "value");
        final Double valuation = ((Double) _feature);
        final Function1<EObject, Double> _function = (EObject c) -> {
          Object _feature_1 = test.getFeature(c, "value");
          return ((Double) _feature_1);
        };
        final Function2<Double, Double, Double> _function_1 = (Double v1, Double v2) -> {
          return Double.valueOf(DoubleExtensions.operator_plus(v1, v2));
        };
        final Double sumOfCustomerValues = IterableExtensions.<Double>reduce(IterableExtensions.<EObject, Double>map(test.getReferenceFeature(valuedReq, "desiredBy"), _function), _function_1);
        double _impact = impact;
        double _multiply = DoubleExtensions.operator_multiply(sumOfCustomerValues, valuation);
        Object _feature_1 = test.getFeature(realisation, "percentage");
        double _multiply_1 = (_multiply * (((Double) _feature_1)).doubleValue());
        impact = (_impact + _multiply_1);
      }
    }
    Object _feature = test.getFeature(realisation, "requirement");
    Object _feature_1 = test.getFeature(((EObject) _feature), "name");
    String _plus = (_feature_1 + "::");
    Object _feature_2 = test.getFeature(realisation, "percentage");
    String _plus_1 = (_plus + _feature_2);
    String _plus_2 = (_plus_1 + ": ");
    String _plus_3 = (_plus_2 + Double.valueOf(impact));
    InputOutput.<String>println(_plus_3);
    return impact;
  }
  
  public static EObject loadModelHenshin() {
    EObject _xblockexpression = null;
    {
      final HenshinResourceSet resourceSet = new HenshinResourceSet("src/models/nrp");
      final String metamodelPath = "nextReleaseProblem.ecore";
      final EPackage metamodel = resourceSet.registerDynamicEPackages(metamodelPath).get(0);
      _xblockexpression = resourceSet.getResource("NRP.xmi").getContents().get(0);
    }
    return _xblockexpression;
  }
  
  public static Object getFeature(final EObject o, final String feature) {
    Object _xblockexpression = null;
    {
      if ((o == null)) {
        InputOutput.<String>println("Null object given");
      }
      _xblockexpression = o.eGet(o.eClass().getEStructuralFeature(feature));
    }
    return _xblockexpression;
  }
  
  public static Iterable<EObject> getReferenceFeature(final EObject o, final String feature) {
    final Object object = test.getFeature(o, feature);
    BasicEList<EObject> features = new BasicEList<EObject>();
    if ((object instanceof EObject)) {
      features.add(((EObject)object));
    } else {
      features = ((BasicEList<EObject>) object);
    }
    return features;
  }
}
