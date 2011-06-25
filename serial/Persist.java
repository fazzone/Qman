package serial;

import game.GameRuntime;
import game.User;

import java.io.File;
import java.io.IOException;

public class Persist {
	static File defaultDir = new File("data");
	public static void writeAppState(File saveDir) throws IOException {
		System.out.println("\n\nWriting state...............");
		if (!saveDir.exists())
			saveDir.mkdir();
		new ProfileWriter(new File(saveDir, "global")).write(new User("global", GameRuntime.globalRankings));
		for (String un : GameRuntime.users.keySet()) {
			File f = new File(saveDir, un);
			new ProfileWriter(f).write(GameRuntime.users.get(un));
		}
		System.out.println("done");
	}
	public static void readAppState(File saveDir) throws IOException {
		GameRuntime.globalRankings = new ProfileReader(new File(saveDir, "global")).read().scoreboard;
		for (File f : saveDir.listFiles())
			if (!f.getName().equals("global") && !f.getName().equals("listeners"))
				GameRuntime.addUser(new ProfileReader(f).read());
	}
	public static void writeAppState() throws IOException {
		writeAppState(defaultDir);
	}
	public static void readAppState() throws IOException {
		readAppState(defaultDir);
	}
}
