package models.service.composition.surrogates.rpc;

import java.util.ArrayList;
import com.esotericsoftware.kryo.Kryo;

import models.service.composition.surrogates.IPredictorsMessage;
import models.service.composition.surrogates.PredictorsMessage;

public class RPCConnectionInformation {

	public static String HOSTNAME = "127.0.0.1";
	public static int PORT = 9090;
	public static int BLOCKTIME = 5000;
	
	public static void registerClasses(Kryo kryo) {
		
		kryo.register(IPredictorsMessage.class);
		kryo.register(PredictorsMessage.class);
		kryo.register(ArrayList.class);
		kryo.register(Integer.class);
		kryo.register(Double.class);
	}
}
