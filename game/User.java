package game;

import java.util.HashSet;

import util.Pair;

public class User {
	public String username;
	public Scoreboard scoreboard;
	public HashSet<Album> banned = new HashSet<Album>();
	public User(String un, int N) {
		username = un;
		try {
			scoreboard = new Scoreboard(LastAPI.topAlbums(username, N));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	public User(String un, Scoreboard scb) {
		username = un;
		scoreboard = scb;
	}
	public Match createMatch() {
		Pair<Album, Album> p = scoreboard.pick2(banned);
		return new Match(p.first, p.second, this);
	}
	public void banAlbum(Album a) {
		banned.add(a);
	}
	public String toString() {
		return username;
	}
}
