package game;

import java.util.HashMap;
import java.util.ArrayList;

import util.NoncePool;
import util.Pair;
import serial.DecisionLog;

public class GameRuntime {
	public static NoncePool<Match> outstandingMatches = new NoncePool<Match>();
	public static HashMap<String, User> users = new HashMap<String, User>();
	public static Scoreboard globalRankings = new Scoreboard();
	public static HashMap<Album, Integer> listeners = new HashMap<Album, Integer>();

	
	public static DecisionLog dLog = new DecisionLog("data/decisionlog");
	private static final int ALBUMS_TO_LOAD = 328;
	

	public static void decideMatch(Match m, Album winner) {
		decideMatch(m, winner, true);
	}
	public static void decideMatch(Match m, Album winner, boolean log) {	//this needs to live here so global scores work
		if (m.owner.username.equals("NMH1998")) //nope
			return;
		synchronized (globalRankings) {				//UNTESTED
			synchronized (m.owner.scoreboard) {		//TODO: does this fix the strange scoreboard bug?
				int oldR = globalRankings.getRating(winner);
				m.decide(winner, m.owner.scoreboard);
				m.decide(winner, globalRankings);
				if (log)
					dLog.writeDecision(m, winner);
			}
		}
	}
	
	public static void merge(int idA, int idB) {
		//warning: likely slow
		for (String u : users.keySet()) {
			Scoreboard sb = users.get(u).scoreboard;
			synchronized (sb) {
				sb.allAlbums.remove(Album.getAlbumByID(idB));
				sb.ratings.remove(Album.getAlbumByID(idB));
			}
		}
		synchronized (globalRankings) {
			globalRankings.allAlbums.remove(Album.getAlbumByID(idB));
			globalRankings.ratings.remove(Album.getAlbumByID(idB));
		}
		Album.merge(idA, idB);
	}

	public static void rebase() {
		System.out.println("GameRuntime.rebase() {");
		
		//no matches can be decided while we do this
		synchronized (globalRankings) {
			System.out.print("\tResetting all scoreboards...");
			globalRankings.reset();
			for (String u : users.keySet())
				users.get(u).scoreboard.reset();
			ArrayList<Pair<Match, Album>> ds = dLog.readback();
			System.out.println("\tsuccessfully read back");
			for (Pair<Match, Album> p : ds)
				decideMatch(p.first, p.second);
			System.out.println("}");
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
		users.get(u.username).banned = u.banned;
	}
	public static void addListener(Album a) {
		if (!listeners.containsKey(a))
			listeners.put(a, 0);
		listeners.put(a, listeners.get(a)+1);
	}
}
