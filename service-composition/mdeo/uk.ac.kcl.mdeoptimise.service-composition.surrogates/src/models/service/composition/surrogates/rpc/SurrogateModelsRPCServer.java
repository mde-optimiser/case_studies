package models.service.composition.surrogates.rpc;

import java.io.IOException;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import models.service.composition.surrogates.IPredictorsMessage;
import models.service.composition.surrogates.PredictorsMessage;

public class SurrogateModelsRPCServer {

	public static void main(String[] args) {
		
	    Server server = new Server();
	    
	    RPCConnectionInformation.registerClasses(server.getKryo());
	    
	    server.start();
	    
	    System.out.println(String.format("Server started on host %s listening on port %s", 
	    		RPCConnectionInformation.HOSTNAME, RPCConnectionInformation.PORT));
	    
	    try {
			
	    	server.bind(RPCConnectionInformation.PORT);
			
		} catch (IOException e) {
			//TODO perhaps add a better exception handler or logger?
			e.printStackTrace();
			System.out.println(String.format("RPC server port already in use: %s", RPCConnectionInformation.PORT));
		}
	    
	    int otherObjectID = server.getKryo().getRegistration(IPredictorsMessage.class).getId();
		server.getKryo().register(IPredictorsMessage.class, otherObjectID);
	    
	    ObjectSpace.registerClasses(server.getKryo());
	    final ObjectSpace objectSpace = new ObjectSpace();
  
	    PredictorsMessage messageObject = new PredictorsMessage();	    
	    objectSpace.register(50, messageObject);
	  
		server.addListener(new Listener() {
			public void connected (final Connection connection) {
				// Allow the connection to access objects in the ObjectSpace.
				objectSpace.addConnection(connection);
				
				System.out.println(String.format("Connection received from: %s", connection.getRemoteAddressTCP()));
				
			}
		});
	}
	
}
