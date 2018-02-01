package models.service.composition.fitness;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.service.composition.surrogates.rpc.SurrogateModelsRPCClient;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import uk.ac.kcl.interpreter.IGuidanceFunction;

/**
 * This is not thread safe
 */
@SuppressWarnings("all")
public abstract class AbstractRemoteGuidanceFunction implements IGuidanceFunction {
  private static Map<Integer, List<Double>> fitnessCache;
  
  public AbstractRemoteGuidanceFunction() {
    if ((AbstractRemoteGuidanceFunction.fitnessCache == null)) {
      HashMap<Integer, List<Double>> _hashMap = new HashMap<Integer, List<Double>>();
      AbstractRemoteGuidanceFunction.fitnessCache = _hashMap;
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
    ArrayList<Double> fitnessValues = SurrogateModelsRPCClient.sendRequest(casted);
    this.cacheStore(cacheHash, fitnessValues);
    return fitnessValues;
  }
  
  public boolean cacheExists(final int predictorsHash) {
    return AbstractRemoteGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(predictorsHash));
  }
  
  public void cacheStore(final int hash, final List<Double> fitness) {
    try {
      boolean _containsKey = AbstractRemoteGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(hash));
      boolean _not = (!_containsKey);
      if (_not) {
        InputOutput.<String>println(("Cached value with key: " + Integer.valueOf(hash)));
        AbstractRemoteGuidanceFunction.fitnessCache.put(Integer.valueOf(hash), fitness);
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
      boolean _containsKey = AbstractRemoteGuidanceFunction.fitnessCache.containsKey(Integer.valueOf(hash));
      if (_containsKey) {
        List<Double> _xblockexpression = null;
        {
          InputOutput.<String>println(("Retrieved cached value with key: " + Integer.valueOf(hash)));
          _xblockexpression = AbstractRemoteGuidanceFunction.fitnessCache.get(Integer.valueOf(hash));
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
