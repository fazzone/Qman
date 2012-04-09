package handlers;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;

import game.Album;
import game.GameRuntime;
import game.Scoreboard;
import game.User;

import server.PageHandler;

import util.PageTemplate;
import util.URLParameters;

public class ManagerHandler implements PageHandler {
	static PageTemplate mTemplate;
	static {
		try {
			mTemplate = PageTemplate.create(new File("manager_template.html"));
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	public void handleRequest(String encodedArgs, PrintStream out) throws IOException {
		Map<String, String> args = URLParameters.parseParameters(encodedArgs);
		String action = args.get("action"), sUser = args.get("user");
		if (action == null || sUser == null)  {
			System.out.println("ManagerHandler passed nonsense params: "+args);
			return;
		}
		User user = GameRuntime.users.get(sUser);
		StringBuilder sb = new StringBuilder();
		for (Album a : user.scoreboard.allAlbums)
			sb.append(tableRow(user.banned.contains(a), a.serial, a.getTitle(), a.getArtist()));
		out.println(mTemplate.filledIn("%rest-of-table%", sb.toString()));
	}
	static String tableRow(boolean banned, int id, String title, String artist) {
		String chkAttribute = banned ? "checked" : "";
		return "<tr><td>"+id+"</td><td><input type=\"checkbox\" "+chkAttribute+"/></td><td>"+title
			+"</td><td>"+artist+"</td></tr>";
	}
}