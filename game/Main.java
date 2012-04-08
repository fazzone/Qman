package game;

import handlers.BanHandler;
import handlers.FileHandler;
import handlers.GameHandler;
import handlers.ShowHandler;
import handlers.VictoryHandler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import serial.Persist;
import server.DynamicGuts;
import server.Server;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			Persist.readAppState();
		} catch (IOException e) {
			System.out.println(e.getMessage()+" while reading state, ignoring");
		}
		DynamicGuts.addHandler("GET", "front", new FileHandler("front_template.html"));
		DynamicGuts.addHandler("GET", "showpage", new GameHandler());
		DynamicGuts.addHandler("POST", "victory", new VictoryHandler());
		DynamicGuts.addHandler("POST", "ban", new BanHandler());
		DynamicGuts.addHandler("GET", "show", new ShowHandler());
		new Thread(new Server(1337)).start();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(60 * 1000);
						Persist.writeAppState(new File("data/backup "+System.currentTimeMillis()));
					}
				} catch (Exception e) {
					throw new Error(e);
				}
			}
		}).start();
		
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String l = in.nextLine();
			if (l.startsWith("w")) {
				synchronized (GameRuntime.class) {	//No idea if this actually works
					Persist.writeAppState();
				}
				if (l.equals("wq"))
					System.exit(0);
			}
		}
		
	}
}
