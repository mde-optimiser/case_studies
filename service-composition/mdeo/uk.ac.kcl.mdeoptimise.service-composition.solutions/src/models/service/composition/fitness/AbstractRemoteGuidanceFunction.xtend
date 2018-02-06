package models.service.composition.fitness

import uk.ac.kcl.interpreter.IGuidanceFunction
import com.lubin.rpc.client.RPCClient
import models.service.composition.surrogates.ISurrogateModelsWrapper
import java.util.List
import java.util.Map
import java.io.InvalidObjectException
import java.util.ArrayList


/**
 * This is not thread safe
 */
abstract class AbstractRemoteGuidanceFunction implements IGuidanceFunction {


	private static ISurrogateModelsWrapper rpcClient;
	private Map<Integer, List<Double>> fitnessCache;

	new(){
		if(AbstractRemoteGuidanceFunction.rpcClient === null){
			AbstractRemoteGuidanceFunction.rpcClient = RPCClient.proxyBuilder(ISurrogateModelsWrapper)
	            .withServerNode("0.0.0.0", 9090)
	            .build();
		}
	}

	def List<Double> evaluatePredictors(List<Integer> predictors){
		
		var cacheHash = predictors.hashCode
		
		if(cacheExists(cacheHash)) {
			return cacheRetrieve(cacheHash);
		}
		
		//WTF casting
		var fitnessValues = this.loadRpcClient.evaluate(predictors as ArrayList<Integer>)
		
		this.cacheStore(cacheHash, fitnessValues);
		
		return fitnessValues;
			
	}
	
	def boolean cacheExists(int predictorsHash) {
		return this.fitnessCache.containsKey(predictorsHash)
	}
	
	def void cacheStore(int hash, List<Double> fitness){
		
		if(!fitnessCache.containsKey(hash)){
			fitnessCache.put(hash, fitness)
		} else {
			throw new InvalidObjectException("Unexpected cache value")
		}
	}
	
	def List<Double> cacheRetrieve(int hash){
		
		if(fitnessCache.containsKey(hash)){
			fitnessCache.get(hash)
		} else {
			throw new InvalidObjectException("Could not find cached value")
		}
	}
	
	def ISurrogateModelsWrapper loadRpcClient(){
		return AbstractRemoteGuidanceFunction.rpcClient;
	}
}