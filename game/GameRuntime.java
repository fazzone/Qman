package game;

import java.util.HashMap;

import util.NoncePool;

public class GameRuntime {
	public static NoncePool<Match> outstandingMatches = new NoncePool<Match>();
	public static HashMap<String, User> users = new HashMap<String, User>();
	public static Scoreboard globalRankings = new Scoreboard();
	public static HashMap<Album, Integer> listeners = new HashMap<Album, Integer>();
	private static final int ALBUMS_TO_LOAD = 328;
	
	public static void decideMatch(Match m, Album winner) {	//this needs to live here so global scores work
		synchronized (globalRankings) {				//UNTESTED
			synchronized (m.owner.scoreboard) {		//TODO: does this fix the strange scoreboard bug?
				int oldR = globalRankings.getRating(winner);
				m.decide(winner, m.owner.scoreboard);
				m.decide(winner, globalRankings);
				System.out.println(winner+": "+oldR+" -> "+globalRankings.getRating(winner));
			}
		}
	}
	public static void addUser(String name) {
		try {
			addUser(name, new Scoreboard(LastAPI.topAlbums(name, ALBUMS_TO_LOAD)));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	public static void addUser(String name, Scoreboard scb) {
		System.out.println("adduser "+name+" with "+scb.allAlbums.size()+" albums");
		if (name.equalsIgnoreCase("global"))
			return;							//Can't do that, messes it up.
		if (!users.containsKey(name)) {
			users.put(name, new User(name, scb));
			globalRankings.addAlbums(scb.allAlbums);
		}
		for (Album a : scb.allAlbums)
			addListener(a);
		System.out.println("global now contains "+globalRankings.allAlbums.size());
		outstandingMatches.cleanup();	//why not?
	}
	public static void addUser(User u) {
		addUser(u.username, u.scoreboard);
	}
	public static void addListener(Album a) {
		if (!listeners.containsKey(a))
			listeners.put(a, 0);
		listeners.put(a, listeners.get(a)+1);
	}
}
