package models.service.composition.surrogates.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>PredictorsTuple</code> class is used for storing the values for the various predictor variables of a
 * composition configuration.
 * 
 * @author Efstathiou Dionysios
 * 
 */
public class PredictorsTuple {
	private ArrayList<PredictorVariable> variables;

	public PredictorsTuple(String[] predictorNames, List<Integer> values) {
		variables = new ArrayList<PredictorVariable>();

		for (int i = 0; i < predictorNames.length; i++) {
			variables.add(new PredictorVariable(predictorNames[i], values.get(i)));
		}
	}

	public void addPredictor(String name, int value) {
		PredictorVariable variable = new PredictorVariable(name, value);
		variables.add(variable);
	}

	public String[] getNames() {
		String[] names = new String[variables.size()];

		for (int i = 0; i < variables.size(); i++)
			names[i] = variables.get(i).getName();

		return names;
	}

	public int[] getValues() {
		int[] predictors = new int[variables.size()];

		for (int i = 0; i < variables.size(); i++)
			predictors[i] = variables.get(i).getValue();

		return predictors;
	}

	public String toString() {
		String output = "";

		for (int i = 0; i < variables.size(); i++)
			output += "\n " + variables.get(i).getName() + ": " + variables.get(i).getValue();

		return output;
	}
}