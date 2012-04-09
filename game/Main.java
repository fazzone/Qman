package game;

import handlers.BanHandler;
import handlers.FileHandler;
import handlers.GameHandler;
import handlers.ShowHandler;
import handlers.VictoryHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

import server.DynamicGuts;
import server.Server;

public class Main {
	public static void main(String[] __args) throws Exception {
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
		new Thread(new Server(1337)).start();
		
		Scanner in = new Scanner(System.in);
		do {
			System.out.print("> ");
			String args[] = in.nextLine().split(" "), cmd = args[0];
			switch (cmd) {
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
			//TODO: move this into a different file in the 'serial' package, with all the rest of the persistence nonsense
			case "write-album-defs":
				if (args.length > 1) {
					try {
						String filename = "data/"+args[1];
						BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
						for (int i=0; i<Album.getTotalAlbums(); i++) {
							Album a = Album.getAlbumByID(i);
							writer.write(Integer.toString(i));
							writer.newLine();
							writer.write(a.getTitle());
							writer.newLine();
							writer.write(a.getArtist());
							writer.newLine();
							writer.write(a.getImageURL());
							writer.newLine();
						}					
						writer.close();
						System.out.println("OK; wrote album defs to "+filename);
					} catch (Exception e) {
						System.out.println("exception in write-album-defs: "+e);
						e.printStackTrace();
					}
				}
				break;
			case "rebase":
				GameRuntime.rebase();
				break;
			}
		} while (in.hasNextLine());
	}
}