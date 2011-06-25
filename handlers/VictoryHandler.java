package handlers;

import game.Album;
import game.GameRuntime;
import game.Match;

import java.io.IOException;
import java.io.PrintStream;

import server.PageHandler;

public class VictoryHandler implements PageHandler {
	public void handleRequest(String args, PrintStream out) throws IOException {
		int wIdx = Math.max(args.indexOf('f'), args.indexOf('s'));
		long nonce = Long.parseLong(args.substring(0, wIdx));
		Match m = GameRuntime.outstandingMatches.get(nonce);
		if (m == null) {
			System.out.println("invalid nonce");
			return;
		}
		Album winner = (args.charAt(wIdx) == 'f') ? m.first : m.second;
		GameRuntime.decideMatch(m, winner);
		GameRuntime.outstandingMatches.remove(nonce);
	}
}
