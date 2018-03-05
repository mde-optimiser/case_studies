package models.service.composition.surrogates;

/*
 * Before running this file you must follow the following steps:
 * 1. Install rJava - by the following command in R install.packages('rJava')
 * 2. Add "-Djava.library.path=.:/usr/lib/R/site-library/rJava/jri/" into the VM arguments of the running configuration 
 * 3. Create an environmental variable R_HOME which contains the lib of the local R installation (/usr/lib/R) in /etc/environment (Linux)
 * 4. Install all the R libraries which are necessary for running the models (foreach, leaps, earth, DAAG, caret, e1071, rpart, languageR, randomForest, MASS, party)
 * Note: Uk Bristol mirror for R has all the above packages for R latest on Centos 7.
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import models.service.composition.surrogates.utils.FilesUtils;
import models.service.composition.surrogates.utils.PredictorsTuple;

public class SurrogateModelsWrapper {
	
	// The R engine which creates an interface between R and Java.
	private Rengine re;
	// Names of predictors variables
	private String[] predictors = { "ID", "Hops", "Orchestrators", "DevFast", "DevMedium", "DevSlow", "LoadSmall", "LoadMedium", "LoadBig" };
	// Chosen Surrogate Model - Available Models: LR, MARS, CART, RF
	private String surrogateModel = "LR";
	private int objectives = 3;
	
	/**
	 * Evaluates the fitness function of an individual based on the values of the considered predictor variables.
	 * 
	 * @param values
	 * @return
	 */
	public ArrayList<Double> evaluate(ArrayList<Integer> values) {
	
		
		loadSurrogateModel(surrogateModel);

		// Populate the predictors of an example composition configuration
		PredictorsTuple pred = new PredictorsTuple(predictors, values);

		ArrayList<Double> results = new ArrayList<Double>();

		for (int i = 1; i <= objectives; i++) {
			results.add(predictData(pred, i));
			System.out.println("Objective " + i + " : " + results.get(i - 1));
		}

		return results;
	}
	
	public PredictorsTuple getPredictorsTuple(ArrayList<Integer> predictorsArray) {
		return new PredictorsTuple(this.predictors, predictorsArray);
	}

	/**
	 * Method for loading the approximation model of interest in R. Loading the prediction model is done once.
	 * 
	 */
	public void loadSurrogateModel(String modelName) {	
		/**
		 * Set the VM argument for accessing the JRI which connects Java with R
		 */
		System.setProperty("java.library.path", ".:/usr/lib64/R/library/rJava/jri:/usr/bin/");
		
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		re = Rengine.getMainEngine();
		if (re == null)
			re = new Rengine(new String[] { "--vanilla" }, false, null);

		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}

		try {
			// Store the R model code into a temporary file
			File tempFileR = FilesUtils.createTempFile(FilesUtils.readFile("src/models/service/composition/resources/build" + modelName + ".R"));
			// File tempFileR = FilesUtils.createTempFile(FilesUtils.readFile("resources/build" + modelName + ".R"));
			// Store the training dataset into a temporary file too
			String trainingSet = FilesUtils.readFile("src/models/service/composition/resources/trainingSet.csv");
			File tempFileSet = FilesUtils.createTempFile(trainingSet);
			// Store to a variable on R the path name of the training set
			re.eval(String.format("path <- '%s'", tempFileSet.getAbsolutePath().toString())).asString();

			// Load the R model
			re.eval(String.format("source('%s')", tempFileR.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for calculating the QoS values (Delay, Network Latency, Success Ratio, Energy) of a candidate
	 * configurations based on a desired approximation model. The considered approximation models are the following: (a)
	 * Linear Regression, (b) Multivariate Adaptive Regression Splines, (c) Classification and Regression Trees, and (d)
	 * Random Forests.
	 * 
	 * @param pred
	 *            is the input values of the used predictor variables
	 * @param metric
	 *            is the QoS metric to be predicted
	 * 
	 * @return Returns the predicted QoS given a set of predictor variables.
	 */
	private double predictData(PredictorsTuple pred, int metric) {
		// Create a temporary .csv file containing the predictor variables of the configuration
		File csv = FilesUtils.writePredictorsCsv(pred.getNames(), pred.getValues());
		// Let know R about the position of the compary .csv file
		re.eval(String.format("path <- '%s'", csv.getAbsolutePath().toString())).asString();

		// Read the .csv file
		re.eval("newData <- read.csv(file=path,head=T,sep=',')").getContent().toString();
		// Do the necessary data transformation
		re.eval("newData[,2] = log(newData[,2])");

		// Do the actual prediction
		REXP prediction = re.eval("predict(modelQoS" + metric + ", newData[newData$ID == 1,])"); //

		return prediction.asDouble();
	}

	/**
	 * Terminates the R instance.
	 */
	public void shutdownR() {
		re.end();
	}

	public static void main(String[] args) throws IOException {
		
		
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(1);
		values.add(22);
		values.add(2);
		values.add(3);
		values.add(2);
		values.add(3);
		values.add(4);
		values.add(6);
		values.add(3);
		
		for (int i = 0; i < 10; i++) {
			SurrogateModelsWrapper tool = new SurrogateModelsWrapper();
			System.out.println(tool.evaluate(values));
		}
		// tool.shutdownR();
	}
}