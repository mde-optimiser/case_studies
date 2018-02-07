package models.service.composition.surrogates.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import models.service.composition.surrogates.IPredictorsMessage;
import models.service.composition.surrogates.PredictorsMessage;
import models.service.composition.surrogates.SurrogateModelsWrapper;


public class SurrogateModelsRPCClient {

	private static Client client;
	
	static {
		SurrogateModelsRPCClient.client = SurrogateModelsRPCClient.initialiseClient();
		 ObjectSpace.registerClasses(SurrogateModelsRPCClient.client.getKryo());
	}
	
	
	public static void main(String[] args) {
		
		while(true) {
			
			System.out.println("Got response: " + SurrogateModelsRPCClient.sendRequest(new ArrayList<Integer>()));
		}
		
	}
	
	private static Client initialiseClient() {
	    
		Client client = new Client();
	    RPCConnectionInformation.registerClasses(client.getKryo());
		
	    client.start();
	   
	    try {
	    	
	    	//Block time, host, port
			client.connect(RPCConnectionInformation.BLOCKTIME, 
					RPCConnectionInformation.HOSTNAME, 
					RPCConnectionInformation.PORT);
		
	    } catch (IOException e) {
			System.out.println(String.format("Could not connect to server on port: %s", RPCConnectionInformation.PORT));
			e.printStackTrace();
	    }   
	    
	    System.out.println("Initialized client");
	    return client;
	
	}
	
	public static ArrayList<Double> sendRequest(ArrayList<Integer> predictors) {
		
		IPredictorsMessage modelWrapper = 
				ObjectSpace.getRemoteObject(SurrogateModelsRPCClient.client, 50, IPredictorsMessage.class);
				
		return modelWrapper.calculatePredictors(predictors);
	}
	
}
