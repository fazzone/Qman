package handlers;

import game.Album;
import game.User;

import java.io.IOException;
import java.io.PrintStream;

import server.PageHandler;
import util.NoncePool;
import util.Pair;

public class BanHandler implements PageHandler {
	public static NoncePool<Pair<User, Album>> nsBan = new NoncePool<Pair<User, Album>>(); 
	public void handleRequest(String args, PrintStream out) throws IOException {
		Pair<User, Album> p = nsBan.get(Long.parseLong(args));
		if (p!=null)
			p.first.banAlbum(p.second);
	}
	public static String createBanLink(User u, Album a) {
		return "/ban/"+nsBan.bind(new Pair<User, Album>(u, a));
	}
}
