package game;

import java.util.HashMap;
import java.util.ArrayList;

class AlbumActual {
	public final String title, artist;
	public final String imageURL;
	private AlbumActual(String t, String a, String iurl) {
		title = t;
		artist = a;
		imageURL = iurl;
	}
	static AlbumActual create(String title, String artist, String iurl) {
		return new AlbumActual(title, artist, iurl);
	}
}

public class Album {
	public final int serial;
	public AlbumActual pActual;

	//now that each album has a unique serial number, we want to avoid constructing a lot of instances
	private static HashMap<String, Album> aNameMap = new HashMap<>();
	//used to get an album from its serial number
	private static ArrayList<Album> allAlbums = new ArrayList<>();

	//hold this lock to make sure no albums are created when you're not ready
	public static final Object albumConstructionLock = new Object();
	private static int nextSerial = 0;

	private Album(int serial, AlbumActual pA) {
		this.serial = serial;
		pActual = pA;
	}

	public static Album getAlbum(String title, String artist, String imageURL) {
		Album proto = aNameMap.get(title+" by "+artist);
		if (proto != null)
			return proto;

		synchronized (albumConstructionLock) {
			Album a = new Album(nextSerial++, AlbumActual.create(title, artist, imageURL));
			allAlbums.add(a);
			aNameMap.put(""+a, a);
			return a;
		}
	}

	public String getTitle() {
		return pActual.title;
	}

	public String getArtist() {
		return pActual.artist;
	}
	
	public String getImageURL() {
		return pActual.imageURL;
	}
	
	public static int getTotalAlbums() {
		return allAlbums.size();
	}

	public static Album getAlbumByID(int id) {
		if (id >= allAlbums.size())
			throw new IllegalArgumentException("ID #"+id+" not (yet) in existence");
		return allAlbums.get(id);
	}

	//merge B with A; that is, A is the one kept, and B redirects to A
	public static void merge(int idA, int idB) {
		allAlbums.get(idB).pActual = allAlbums.get(idA).pActual;
		allAlbums.set(idB, allAlbums.get(idA));		
	}
	
	public String toString() {
		return pActual.title+" by "+pActual.artist;
	}

	public boolean equals(Object o) {
		return (o instanceof Album) && ((Album)o).serial == serial;
	}

	public int hashCode() {
		return serial;
	}	
}
