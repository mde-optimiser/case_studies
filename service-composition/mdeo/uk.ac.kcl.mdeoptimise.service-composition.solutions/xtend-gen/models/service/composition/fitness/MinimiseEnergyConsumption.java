package models.service.composition.fitness;

import java.util.List;
import models.service.composition.fitness.AbstractRemoteGuidanceFunction;
import models.service.composition.fitness.PredictorsCalculator;
import org.eclipse.emf.ecore.EObject;

@SuppressWarnings("all")
public class MinimiseEnergyConsumption extends AbstractRemoteGuidanceFunction {
  @Override
  public double computeFitness(final EObject model) {
    List<Integer> predictors = new PredictorsCalculator().calculatePredictors(model);
    List<Double> fitness = this.evaluatePredictors(predictors);
    return (fitness.get(2)).doubleValue();
  }
  
  @Override
  public String getName() {
    return "Minimise Energy Consumption";
  }
}
