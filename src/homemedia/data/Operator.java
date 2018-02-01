package homemedia.data;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiPredicate;

public class Operator {
	
	private static final Map<String, BiPredicate<Float, Float>> FloatFuncts =
			new HashMap<String, BiPredicate<Float, Float>>();
	private static final Map<String, BiPredicate<Date, Date>> DateFuncts =
			new HashMap<String, BiPredicate<Date, Date>>();
	private static final Map<String, BiPredicate<String, String>> StringFuncts =
			new HashMap<String, BiPredicate<String, String>>();
	static {
		FloatFuncts.put("=", (a, b) -> a.equals(b));
		FloatFuncts.put(">", (a, b) -> a > b);
		FloatFuncts.put("<", (a, b) -> a < b);
		FloatFuncts.put(">=", (a, b) -> a >= b);
		FloatFuncts.put("<=", (a, b) -> a <= b);
		
		DateFuncts.put("=", (a, b) -> a.equals(b));
		DateFuncts.put(">", (a, b) -> a.after(b));
		DateFuncts.put("<", (a, b) -> a.before(b));
		DateFuncts.put(">=", (a, b) -> a.after(b) || a.equals(b));
		DateFuncts.put("<=", (a, b) -> a.before(b) || a.equals(b));
		
		StringFuncts.put("=", (a, b) -> a.equals(b));
		StringFuncts.put("contains", (a, b) -> a.contains(b));
	}
	
	public static boolean Test(String token, Float a, String[] values) {
		BiPredicate<Float, Float> func = FloatFuncts.get(token);
		Float lo = Float.parseFloat(values[0]);
		if(token.equals("between")) {
			Float hi = Float.parseFloat(values[1]);
			return Test(token, a, lo, hi);
		}
		return func.test(a, lo);
	}
	
	public static boolean Test(String token, Date a, String[] values) {
		BiPredicate<Date, Date> func = DateFuncts.get(token);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
		Date loDate = sdf.parse(values[0], new ParsePosition(0));
		if(loDate == null) return false;
		if(token.equals("between")) {
			Date hiDate = sdf.parse(values[1], new ParsePosition(0));
			if(hiDate == null) return false;
			return Test(token, a, loDate, hiDate);
		}
		return func.test(a, loDate);
	}
	
	public static boolean Test(String token, String a, String b) {
		BiPredicate<String,String> func = StringFuncts.get(token);
		return func.test(a, b);
	}
	
	public static boolean Test(String token, Float a, Float b, Float c) {
		return a >= b && a < c;
	}
	
	public static boolean Test(String token, Date a, Date b, Date c) {
		return a.after(b) && a.before(c);
	}
}
