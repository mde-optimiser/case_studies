package models.service.composition.surrogates;

import java.util.ArrayList;
import java.util.List;

public interface ISurrogateModelsWrapper {
	List<Double> evaluate(ArrayList<Integer> values);
}