package util;

public class HTML {
	private static final String[] entities = {"lt","gt","amp","cent","pound","yen","euro","sect","copy","reg","trade"};
	private static final String[] sp_chars = {"<", ">", "&",  "¢",   "£",    "¥",  "€",   "§",   "©",   "®",  "™"};
	public static String unescape(String s) {
		for (int i=0; i<entities.length; i++)
			s = s.replace("&"+entities[i]+";", sp_chars[i]);
		return s;
	}
}
