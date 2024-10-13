package msmgw.heulgkkom.util;

import java.util.function.BooleanSupplier;

public class AssertUtil {

	private final boolean eval;

	public static AssertUtil ifThen(BooleanSupplier u){
		return new AssertUtil(u.getAsBoolean());
	}

	AssertUtil(boolean eval){
		this.eval = eval;
	}

	public void onThrow(String message){
		if(eval){
			throw new IllegalArgumentException(message);
		}
	}


}
