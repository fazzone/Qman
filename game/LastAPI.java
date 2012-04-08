package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import util.HTML;

//This is really, really ugly.  Perhaps I should use a real XML parser.
//TODO: un-uglify
public class LastAPI {
	static final String key="efcda4efc433fb1745f83de9ede36514";
	static String[] imgtypes=new String[]{"jpg", "png", "gif", "bmp"};
	public static ArrayList<Album> topAlbums(String username, int N) throws IOException {
		System.out.println("topalbums "+username+" "+N);
		
		String uenc = URLEncoder.encode(username, "UTF-8");
		URLConnection urlc = new URL("http://ws.audioscrobbler.com/2.0/?method=user.gettopalbums&user="+uenc+"&limit="+N+"&api_key="+key).openConnection();
		urlc.setRequestProperty("User-Agent", "Qman2");		//TODO: change to the eventual real name of this thing
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
		String line=null, nMarker = "<name>";
		String title=null, artist=null;
		ArrayList<Album> found = new ArrayList<Album>();
		while ((line=reader.readLine())!=null)
			if (line.contains(nMarker)) {
				if (title==null)
					title = insideTag(line, line.indexOf(nMarker)+nMarker.length());
				else artist = insideTag(line, line.indexOf(nMarker)+nMarker.length());
			}
			else if (line.contains("<image size=\"extralarge\"")) {
				String imgURL = insideTag(line, line.indexOf('.'));	//the filename has a '.' in it
				found.add(Album.getAlbum(HTML.unescape(title), HTML.unescape(artist), imgURL));
				title=artist=null;
			}
		return found;
	}
	static String insideTag(String s, int idx) {
		int b,e;
		for (b=idx; b>=0 && !"<>".contains(""+s.charAt(b)); b--) {}
		for (e=idx; e<s.length() && !"<>".contains(""+s.charAt(e)); e++) {}
		return s.substring(b+1,e);
	}
}
