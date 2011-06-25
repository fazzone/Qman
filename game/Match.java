package game;

public class Match {
	public Album first, second;
	public User owner;
	public Match(Album a, Album b, User u) {
		first = a;
		second = b;
		owner = u;
	}
	public void decide(Album e, Scoreboard sb) {
		synchronized (sb) {
			if (e.equals(first))
				sb.updateRatings(first, second);
			else if (e.equals(second))
				sb.updateRatings(second, first);
			else throw new IllegalArgumentException("\""+e+"\" can't win a match between \""+first+"\" and \""+second+"\"");
		}
	}
}
