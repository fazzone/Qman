package serial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

import game.Match;
import game.Album;
import game.User;
import game.GameRuntime;
import util.Pair;


public class DecisionLog {
	private String filename;
	private BufferedWriter writer;
	
	public DecisionLog(String filename) {
		this.filename = filename;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public void writeDecision(Match m, Album winner) {
		try {
			synchronized (writer) {
				writer.write(m.owner.toString());
				writer.write(" ");
				writer.write(Integer.toString(m.first.serial));
				writer.write(" ");
				writer.write(Integer.toString(m.second.serial));
				writer.write(" ");
				writer.write(Integer.toString(winner.serial));
				writer.newLine();
				writer.flush();
			}
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	public ArrayList<Pair<Match, Album>> readback() {
		ArrayList<Pair<Match, Album>> ds = new ArrayList<>();
		try {
			synchronized (writer) {
				writer.flush();
				Scanner sc = new Scanner(new File(filename));
				while (sc.hasNextLine()) {
					User owner = GameRuntime.users.get(sc.next());
					Album first = Album.getAlbumByID(sc.nextInt());
					Album second = Album.getAlbumByID(sc.nextInt());
					Album winner = Album.getAlbumByID(sc.nextInt());
					sc.nextLine();
					ds.add(new Pair<Match, Album>(new Match(first, second, owner), winner));
				}
				sc.close();
			}
		} catch (Exception e) {
			throw new Error(e);
		}
		return ds;
	}
}