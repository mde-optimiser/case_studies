package models.service.composition.fitness;

import java.util.ArrayList;
import java.util.List;

import com.lubin.rpc.client.RPCClient;

import models.service.composition.surrogates.ISurrogateModelsWrapper;
import uk.ac.kcl.interpreter.IGuidanceFunction;

public abstract class AbstractRPCGuidanceFunction implements IGuidanceFunction {
	
	public static void main(String[] args) {	
		
	    ISurrogateModelsWrapper client = RPCClient.proxyBuilder(ISurrogateModelsWrapper.class)
	            .withServerNode("0.0.0.0", 9090)
	            .build();
	    
	    for(int i = 0; i < 1000; i++) {
	    	try {
	    		
	    		long startTime = System.nanoTime();
	    		
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
	    		
	    		List<Double> objectives = client.evaluate(values);
	    		
	    		long stopTime = System.nanoTime();
	    		
	    		System.out.println("Execution time: " + (stopTime - startTime)/1000000000.0);
	    		System.out.println("Outcome: " + objectives);
	    	
	    	} catch(Exception e) {
	    		System.out.println(e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
}