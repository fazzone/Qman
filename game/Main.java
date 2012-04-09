package game;

import handlers.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

import server.DynamicGuts;
import server.Server;

import util.PageTemplate;

import serial.ProfileReader;
import serial.ProfileWriter;

public class Main {
	public static void main(String[] __args) {
		//try {
		//	Persist.readAppState();
		//} catch (IOException e) {
		//	System.out.println(e.getMessage()+" while reading state, ignoring");
		//}
		DynamicGuts.addHandler("GET", "front", new FileHandler("front_template.html"));
		DynamicGuts.addHandler("GET", "showpage", new GameHandler());
		DynamicGuts.addHandler("POST", "victory", new VictoryHandler());
		DynamicGuts.addHandler("POST", "ban", new BanHandler());
		DynamicGuts.addHandler("GET", "show", new ShowHandler());
		DynamicGuts.addHandler("GET", "manage", new ManagerHandler());
		
		Thread serverThread = null;
		try {
			serverThread = new Thread(new Server(1337));
		} catch (Exception e) {
			System.out.println("exception in starting server: "+e);
			e.printStackTrace();
		}
		Scanner in = new Scanner(System.in);
		System.out.print("> ");
		do {
			String args[] = in.nextLine().split(" "), cmd = args[0];
			switch (cmd) {
			case "start":
				serverThread.start();
				break;
			case "stop":
				serverThread.stop();
				break;
			case "quit":
				System.exit(0);
				break;
			case "query-all-albums":
				synchronized (Album.albumConstructionLock) {
					for (int i=0; i<Album.getTotalAlbums(); i++)
						System.out.printf("%d (%d): %s\n", i, Album.getAlbumByID(i).serial, Album.getAlbumByID(i).toString());
				}
				break;
			case "query-album-by-id":
				if (args.length > 1) {
					try {
						int id = Integer.parseInt(args[1]);
						System.out.println(id+"\t->\t"+Album.getAlbumByID(id));
					} catch (NumberFormatException ex) {
						System.out.println("string \""+args[1]+"\" cannot be parsed as a number");
					}
				} else System.out.println("query-album-by-id :: AlbumID -> Album");
				break;
			case "merge-into":
				if (args.length > 2) {
					try {
						int idA = Integer.parseInt(args[1]), idB = Integer.parseInt(args[2]);
						System.out.println("merging <#"+idB+" - "+Album.getAlbumByID(idB)+"> into <#"+idA+" - "+Album.getAlbumByID(idA)+">");
						System.out.println("Are you absolutely, positively, sure? (type \"yes\" if you are)");
						if (in.nextLine().equals("yes")) {
							GameRuntime.merge(idA, idB);
							System.out.println("OK, merged");
						} else System.out.println("OK, merge aborted");
					} catch (NumberFormatException ex) {
						System.out.println("unable to parse both "+args[1]+" and "+args[2]+" as numbers");
					}
				} else System.out.println("merge-into :: AlbumID -> AlbumID -> IO ()");
				break;
			case "query-all-users":
				for (String u : GameRuntime.users.keySet())
					System.out.println(u);
				break;
			case "write-album-defs":
				if (args.length > 1) {
					try {
						writeAlbumDefs("data/"+args[1]);
						System.out.println("OK; wrote album defs");
					} catch (Exception e) {
						System.out.println("exception in write-album-defs: "+e);
						e.printStackTrace();
					}
				} else System.out.println("write-album-defs :: FilePath -> IO ()");
				break;
			case "read-album-defs":
				if (args.length > 1) {
					try {
						readAlbumDefs("data/"+args[1]);
						System.out.println("OK; read album defs");
					} catch (Exception e) {
						System.out.println("exception in read-album-defs: "+e);
						e.printStackTrace();
					}
				} else System.out.println("read-album-defs :: FilePath -> IO ()");
				break;
			case "reload-templates":
				PageTemplate.preloaded.clear();
				UserHandler.reloadTemplates();
				ManagerHandler.reloadTemplates();
				DynamicGuts.addHandler("GET", "front", new FileHandler("front_template.html"));
				System.out.println("OK, templates reloaded");
				break;
			case "write-profiles":
				try {
					writeProfiles("data/profiles");
				} catch (IOException e) {
					System.out.println("exception in write-profiles: "+e);
					e.printStackTrace();
				}
				System.out.println("OK, wrote profiles");
				break;
			case "read-profiles":
				if (args.length > 1) {
					try {
						readProfiles("data/profiles", Boolean.parseBoolean(args[1]));
					} catch (Exception e) {
						System.out.println("exception in read-profiles: "+e);
						e.printStackTrace();
					}
				} else System.out.println("read-profiles :: Bool -> IO ()");
				System.out.println("OK, read profiles");
				break;
			case "rebase":
				GameRuntime.rebase();
				break;
			case "":
				break;
			default:
				System.out.println("no case for "+cmd);
			}
			System.out.print("\n>");
		} while (in.hasNextLine());
	}
	//TODO: move this into a different file in the 'serial' package, with all the rest of the persistence nonsense
	static void writeAlbumDefs(String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (int i=0; i<Album.getTotalAlbums(); i++) {
			Album a = Album.getAlbumByID(i);
			writer.write(a.getTitle());
			writer.newLine();
			writer.write(a.getArtist());
			writer.newLine();
			writer.write(a.getImageURL());
			writer.newLine();
		}					
		writer.close();		
	}
	static void readAlbumDefs(String filename) throws IOException {
		Scanner sc = new Scanner(new File(filename));
		while (sc.hasNextLine()) {
			String title = sc.nextLine(), artist = sc.nextLine(), imgURL = sc.nextLine();
			Album.getAlbum(title, artist, imgURL);
		}
	}

	static void writeProfiles(String folder) throws IOException {
		new File(folder).mkdirs();
		for (String u : GameRuntime.users.keySet())
			new ProfileWriter(new File(folder+"/"+u)).write(GameRuntime.users.get(u));
		new ProfileWriter(new File(folder+"/global")).write(new User("global", GameRuntime.globalRankings));
	}
	static void readProfiles(String folder, boolean preserve) throws IOException {
		for (File f : new File(folder).listFiles()) {
			User u = new ProfileReader(f).read(preserve);
			if (f.getName().equals("global"))
				GameRuntime.globalRankings = u.scoreboard;
			else GameRuntime.addUser(u);
		}
	}
}