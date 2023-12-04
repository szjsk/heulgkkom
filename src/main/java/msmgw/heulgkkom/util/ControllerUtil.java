package msmgw.heulgkkom.util;

import java.util.function.BooleanSupplier;

public class ControllerUtil {

	private final boolean eval;

	public static ControllerUtil assertRequest(BooleanSupplier u){
		return new ControllerUtil(u.getAsBoolean());
	}

	ControllerUtil(boolean eval){
		this.eval = eval;
	}

	public void onThrow(String message){
		if(eval){
			throw new IllegalArgumentException(message);
		}
	}


}
