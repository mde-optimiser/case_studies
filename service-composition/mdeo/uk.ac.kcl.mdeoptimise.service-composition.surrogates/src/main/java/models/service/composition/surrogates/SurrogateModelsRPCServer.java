package models.service.composition.surrogates;

import com.lubin.rpc.server.RPCServer;

public class SurrogateModelsRPCServer {
	public static void main(String[] args) {	
		try {
			
			RPCServer server = new RPCServer();
			server.run();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
