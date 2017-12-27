package com.example.model;

import java.util.HashMap;
import java.util.Map;

public class Operator {
	
	private static final Map<String, Condition2args<Float>> FloatCondts =
			new HashMap<String, Condition2args<Float>>();
	static {
		FloatCondts.put("=", (a, b) -> a.equals(b));
		FloatCondts.put(">", (a, b) -> a > b);
		FloatCondts.put("<", (a, b) -> a < b);
		FloatCondts.put(">=", (a, b) -> a >= b);
		FloatCondts.put("<=", (a, b) -> a <= b);
	}
	
	public static boolean TestFloat(String token, Float a, Float b) {
		
		Condition2args<Float> cond = FloatCondts.get(token);
		return cond.Test(a, b);
		
	}
}
