package handlers;

import game.GameRuntime;
import game.Match;

import java.io.IOException;
import java.io.PrintStream;

import server.PageHandler;
import util.NoncePool;

public class GameHandler implements PageHandler {
	static NoncePool<Match> outstandingMatches = new NoncePool<Match>();
	public void handleRequest(String args, PrintStream out) throws IOException {
		String user = args.substring(0, args.length()-1);
		if (!GameRuntime.users.containsKey(user))
			GameRuntime.addUser(user);
		new UserHandler(GameRuntime.users.get(user)).handleRequest(null, out);
	}
}
