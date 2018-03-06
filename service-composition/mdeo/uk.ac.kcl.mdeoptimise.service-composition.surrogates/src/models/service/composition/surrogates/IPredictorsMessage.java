package models.service.composition.surrogates;

import java.util.ArrayList;

public interface IPredictorsMessage {
	
	ArrayList<Double> calculatePredictors(ArrayList<Integer> predictors);
}
