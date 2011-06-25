package handlers;

import game.GameRuntime;
import game.Match;
import game.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import server.PageHandler;
import util.PageTemplate;

public class UserHandler implements PageHandler {
	User user;
	static PageTemplate mTemplate;
	static {
		try {
			mTemplate = PageTemplate.create(new File("match_template.html"));
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	public UserHandler(User user) {
		this.user = user;
	}
	@Override
	public void handleRequest(String args, PrintStream out) throws IOException {
		Match match = user.createMatch();
		HashMap<String, String> params = new HashMap<String, String>();
		
		long matchNonce = GameRuntime.outstandingMatches.bind(match);
		
		params.put("title1", match.first.title);
		params.put("title2", match.second.title);
		params.put("artist1", match.first.artist);
		params.put("artist2", match.second.artist);
		params.put("imglink1", ""+match.first.imageURL);
		params.put("imglink2", ""+match.second.imageURL);
		params.put("victorylink1", "/victory/"+matchNonce+"f");
		params.put("victorylink2", "/victory/"+matchNonce+"s");
		params.put("banlink1", BanHandler.createBanLink(user, match.first));
		params.put("banlink2", BanHandler.createBanLink(user, match.second));
		params.put("USER", user.username);
			
		
		out.println(mTemplate.filledIn(params));
	}
}
