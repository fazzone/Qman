package handlers;

import game.Album;
import game.GameRuntime;
import game.Scoreboard;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import server.PageHandler;

//TODO: fix (i.e. make pretty)

public class ShowHandler implements PageHandler {
	public void handleRequest(String args, PrintStream out) throws IOException {
		String[] spl = URLDecoder.decode(args, "UTF-8").split(" ");
		final Scoreboard scb = getScores(spl[0]);
		final boolean ratingOrder = spl[1].equals("r");
		Comparator<Album> c = new Comparator<Album>() {
				//kill me
				//it hurts to live
				public int compare(Album a, Album b) {
					if (ratingOrder)
						return scb.getRating(b) - scb.getRating(a);
					else return GameRuntime.listeners.get(b) - GameRuntime.listeners.get(a);
				}
			};
			writeTable(c, scb, out);
	}
	public void writeTable(Comparator<Album> cmp, Scoreboard scb, PrintStream out) throws IOException {
		long start = System.currentTimeMillis();
		
		List<Album> e = scb.allAlbums;
		synchronized (scb) {
			Collections.sort(e, cmp);
		}
		out.println("<html><head>");
		out.println("<meta charset=\"utf-8\"/></head><body> ");
		out.println("<table border=\"1\">");
		out.println("<tr><td>rank</td><td>ID</td><td>name                        </td><td>rating</td><td>listeners</td></tr>");
		int max = Math.min(512, e.size());
		for (int i=0; i<max; i++) {
			Album a=e.get(i);
			out.println("<tr><td>"+(i+1)+"</td><td>"+a.serial+"</td><td>"+a+"</td><td>"+scb.getRating(a)+"</td><td>"+GameRuntime.listeners.get(a)+"</td><tr>");
		}
		out.println("</table>");
		long elapsed = System.currentTimeMillis() - start;
		
		out.println("generated in "+elapsed+"ms</body></html>");
	}
	private Scoreboard getScores(String arg) {
		if (arg.equals("global"))			//last.fm user "GLOBAL" has had 0 plays since 2004, so this hack should be safe.
			return GameRuntime.globalRankings;
		return GameRuntime.users.get(arg).scoreboard;
	}
}
