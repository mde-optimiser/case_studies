package models.service.composition.fitness

import uk.ac.kcl.interpreter.IGuidanceFunction
import com.lubin.rpc.client.RPCClient
import models.service.composition.surrogates.ISurrogateModelsWrapper
import java.util.List
import java.util.Map
import java.io.InvalidObjectException
import java.util.ArrayList
import java.util.HashMap
import models.service.composition.surrogates.SurrogateModelsWrapper

/**
 * This is not thread safe
 */
abstract class AbstractRemoteGuidanceFunction implements IGuidanceFunction {


	private static ISurrogateModelsWrapper rpcClient;
	private static Map<Integer, List<Double>> fitnessCache;

	new(){
		//if(AbstractRemoteGuidanceFunction.rpcClient === null){
		//	AbstractRemoteGuidanceFunction.rpcClient = RPCClient.proxyBuilder(ISurrogateModelsWrapper)
	    //        .withServerNode("localhost", 9090)
	    //        .build();
		//}
		
		if(AbstractRemoteGuidanceFunction.fitnessCache === null){
			AbstractRemoteGuidanceFunction.fitnessCache = new HashMap<Integer, List<Double>>();
		}
	}

	def List<Double> evaluatePredictors(List<Integer> predictors){
		
		println("Predictors for evaluation:" + predictors);
		
		var cacheHash = predictors.hashCode
		
		if(cacheExists(cacheHash)) {
			return cacheRetrieve(cacheHash);
		}
		
		var casted = predictors as ArrayList<Integer>
		
		println(casted)
		
		//WTF casting
		//var fitnessValues = this.loadRpcClient.evaluate(casted)
		
		var fitnessValues = new SurrogateModelsWrapper().evaluate(casted)
		
		this.cacheStore(cacheHash, fitnessValues);
		
		return fitnessValues;
			
	}
	
	def boolean cacheExists(int predictorsHash) {
		return this.fitnessCache.containsKey(predictorsHash)
	}
	
	def void cacheStore(int hash, List<Double> fitness){
		
		if(!fitnessCache.containsKey(hash)){
			println("Cached value with key: " + hash)
			fitnessCache.put(hash, fitness)
		} else {
			throw new InvalidObjectException("Unexpected cache value")
		}
	}
	
	def List<Double> cacheRetrieve(int hash){
		
		if(fitnessCache.containsKey(hash)){
			println("Retrieved cached value with key: " + hash)
			fitnessCache.get(hash)
		} else {
			throw new InvalidObjectException("Could not find cached value")
		}
	}
	
	def ISurrogateModelsWrapper loadRpcClient(){
		return AbstractRemoteGuidanceFunction.rpcClient;
	}
}