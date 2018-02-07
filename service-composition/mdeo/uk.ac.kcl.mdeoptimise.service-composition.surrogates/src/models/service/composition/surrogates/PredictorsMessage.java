package models.service.composition.surrogates;

import java.util.ArrayList;

public class PredictorsMessage implements IPredictorsMessage {

	public ArrayList<Double> calculatePredictors(ArrayList<Integer> predictors){
		
		SurrogateModelsWrapper surrogate = new SurrogateModelsWrapper();
		
		return surrogate.evaluate(predictors);
		
	}
	
}
