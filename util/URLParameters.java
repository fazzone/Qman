package util;

import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;

public class URLParameters {
	public static Map<String, String> parseParameters(String pathEnc) {
		String path = null;
		try {
			path = URLDecoder.decode(pathEnc, "UTF-8");
		} catch (Exception e) {
			throw new Error(e);
		}
		HashMap<String, String> params = new HashMap<>();
		/*int paramStartIdx = path.indexOf('?');
		if (paramStartIdx < 0)
		return params;*/
		if (path.isEmpty())
			return params;

		int paramStartIdx = 0;
		String paramsBlob = path.substring(paramStartIdx);
		String[] byName = paramsBlob.split("&");
		for (String p : byName) {
			String[] kv = p.split("=");
			if (kv.length < 2)
				throw new IllegalArgumentException("unable parse param-string \""+p+"\"");
			params.put(kv[0], kv[1]);
		}
		return params;
	}
}