package util;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class URLParse {
	public static Map<String, String> getParams(String s) throws IOException {
		String decoded = URLDecoder.decode(s, "UTF-8");
		System.out.println("decoded = "+decoded);
		HashMap<String, String> params = new HashMap<String, String>();
		for (String eqP : s.split("&")) {
			String[] e = eqP.split("=");
			params.put(e[0], e[1]);
		}
		return params;
	}
}
