package serial;

import game.Album;
import game.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ProfileWriter {
	PrintWriter out;
	public ProfileWriter(File f) throws IOException {
		out = new PrintWriter(f);
	}
	public void write(User u) {
		out.println(u.scoreboard.allAlbums.size());
		for (Album e : u.scoreboard.allAlbums)
			out.println(e.getTitle()+"\n"+e.getArtist()+"\n"+e.getImageURL()+"\n"+u.scoreboard.getRating(e));
		for (Album e : u.banned)
			out.println(e.getTitle()+"\n"+e.getArtist()+"\n"+e.getImageURL()+"\n"+u.scoreboard.getRating(e));
		out.close();
	}
}
