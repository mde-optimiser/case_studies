package models.service.composition.fitness.jni;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.service.composition.surrogates.SurrogateModelsWrapper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import uk.ac.kcl.interpreter.IGuidanceFunction;

/**
 * This is not thread safe
 */
@SuppressWarnings("all")
public abstract class AbstractNativeGuidanceFunction implements IGuidanceFunction {
  private static Map<Integer, List<Double>> fitnessCache;
  
  private static SurrogateModelsWrapper nativeFitnessExecutor;
  
  public AbstractNativeGuidanceFunction() {
    if ((AbstractNativeGuidanceFunction.fitnessCache == null)) {
      HashMap<Integer, List<Double>> _hashMap = new HashMap<Integer, List<Double>>();
      AbstractNativeGuidanceFunction.fitnessCache = _hashMap;
    }
    if ((AbstractNativeGuidanceFunction.nativeFitnessExecutor == null)) {
      SurrogateModelsWrapper _surrogateModelsWrapper = new SurrogateModelsWrapper();
      AbstractNativeGuidanceFunction.nativeFitnessExecutor = _surrogateModelsWrapper;
    }
  }
  
  public List<Double> evaluatePredictors(final List<Integer> predictors) {
    InputOutput.<String>println(("Predictors for evaluation:" + predictors));
    int cacheHash = predictors.hashCode();
    boolean _cacheExists = this.cacheExists(cacheHash);
    if (_cacheExists) {
      return this.cacheRetrieve(cacheHash);
    }
    ArrayList<Integer> casted = ((ArrayList<Integer>) predictors);
    InputOutput.<ArrayList<Integer>>println(casted);
    ArrayList<Double> fitnessValues = AbstractNativeGuidanceFunction.nativeFitnessExecutor.evaluate(casted);
    this.cacheStore(cacheHash, fitnessValues);
    return fitnessValues;
  }
  
  public boolean cacheExists(final int predictorsHash) {
    return AbstractNativeGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(predictorsHash));
  }
  
  public void cacheStore(final int hash, final List<Double> fitness) {
    try {
      boolean _containsKey = AbstractNativeGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(hash));
      boolean _not = (!_containsKey);
      if (_not) {
        InputOutput.<String>println(("Cached value with key: " + Integer.valueOf(hash)));
        AbstractNativeGuidanceFunction.fitnessCache.put(Integer.valueOf(hash), fitness);
      } else {
        throw new InvalidObjectException("Unexpected cache value");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<Double> cacheRetrieve(final int hash) {
    try {
      List<Double> _xifexpression = null;
      boolean _containsKey = AbstractNativeGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(hash));
      if (_containsKey) {
        List<Double> _xblockexpression = null;
        {
          InputOutput.<String>println(("Retrieved cached value with key: " + Integer.valueOf(hash)));
          _xblockexpression = AbstractNativeGuidanceFunction.fitnessCache.get(Integer.valueOf(hash));
        }
        _xifexpression = _xblockexpression;
      } else {
        throw new InvalidObjectException("Could not find cached value");
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
