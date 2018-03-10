package models.service.composition.fitness;

import java.util.List;
import models.service.composition.fitness.AbstractRemoteGuidanceFunction;
import models.service.composition.fitness.PredictorsCalculator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class MaximiseServiceReliability extends AbstractRemoteGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    List<Integer> predictors = new PredictorsCalculator().calculatePredictors(model);
    List<Double> fitness = this.evaluatePredictors(predictors);
    String _name = this.getName();
    String _plus = (_name + ": ");
    String _plus_1 = (_plus + fitness);
    InputOutput.<String>println(_plus_1);
    return (fitness.get(1)).doubleValue();
  }
  
  @Override
  public String getName() {
    return "Minimise Service Reliability";
  }
}
