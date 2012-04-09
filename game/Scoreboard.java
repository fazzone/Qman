package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import util.Pair;

public class Scoreboard {
	public ArrayList<Album> allAlbums = new ArrayList<Album>();
	public HashMap<Album, Integer> ratings = new HashMap<Album, Integer>();
	public static final int K = 32, DEFAULT_RATING = 1400;
	public Scoreboard() {}
	public Scoreboard(List<Album> as) {
		addAlbums(as);
	}
	public synchronized void updateRatings(Album winner, Album loser) {
		double wexpected = 1/(1+Math.pow(10, (getRating(loser) - getRating(winner))/400));
		double lexpected = 1/(1+Math.pow(10, (getRating(winner) - getRating(loser))/400));
		ratings.put(winner, (int)(getRating(winner) + K*(1-wexpected)));
		ratings.put(loser,  (int)(getRating(loser)  - K*(1-lexpected)));
	}
	public synchronized void updateRatings(Pair<Album, Album> match, boolean firstWon) {
		if (firstWon)
			updateRatings(match.first, match.second);
		else updateRatings(match.second, match.first);
	}
	public synchronized void reset() {
		for (Album e : ratings.keySet())
			ratings.put(e, DEFAULT_RATING);
	}
	public synchronized void addAlbums(List<Album> as) {
		for (Album a : as)
			if (!ratings.containsKey(a)) {
				ratings.put(a, DEFAULT_RATING);
				allAlbums.add(a);
			}
	}
	public Pair<Album, Album> pick2() {
		int firstIdx=(int)(Math.random()*allAlbums.size()), secondIdx;
		do
			secondIdx = (int)(Math.random()*allAlbums.size());
		while (firstIdx == secondIdx);
		return new Pair<Album, Album>(allAlbums.get(firstIdx), allAlbums.get(secondIdx));
	}
	public Pair<Album, Album> pick2(Set<Album> exclude) {
		Pair<Album, Album> match = pick2();
		while (exclude.contains(match.first) || exclude.contains(match.second))
			match = pick2();
		return match;
	}
	public synchronized int getRating(Album a) {
		return ratings.get(a);
	}
}