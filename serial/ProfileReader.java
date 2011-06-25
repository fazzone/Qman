package serial;

import game.Album;
import game.Scoreboard;
import game.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileReader {
	String fname;
	BufferedReader reader;
	public ProfileReader(File f) throws IOException {
		fname = f.getName();
		reader = new BufferedReader(new FileReader(f));
	}
	public User read() throws IOException {
		Scoreboard scb = new Scoreboard();
		HashMap<Album, Integer> rtg = new HashMap<Album, Integer>();
		int N = Integer.parseInt(reader.readLine()), i=0;
		ArrayList<Album> banned = new ArrayList<Album>();
		while (reader.ready()) {
			String title = reader.readLine(), artist = reader.readLine(), imgurl = reader.readLine();
			int rating = Integer.parseInt(reader.readLine());
			if (i < N)
				rtg.put(new Album(title, artist, imgurl), rating);
			else banned.add(new Album(title, artist, imgurl));
		}
		scb.allAlbums = new ArrayList<Album>(rtg.keySet());
		scb.ratings = rtg;
		User u = new User(fname, scb);
		u.banned.addAll(banned);
		return u;
	}
}
