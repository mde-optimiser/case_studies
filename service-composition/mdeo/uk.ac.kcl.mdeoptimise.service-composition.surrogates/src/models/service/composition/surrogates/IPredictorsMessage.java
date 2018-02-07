package models.service.composition.surrogates;

import java.util.ArrayList;
import java.util.List;

public interface IPredictorsMessage {
	
	ArrayList<Double> calculatePredictors(ArrayList<Integer> predictors);
}
