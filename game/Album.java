package game;


public class Album {
	public String title, artist;
	public String imageURL;
	public Album(String title, String artist, String imageURL) {
		this.title = title;
		this.artist = artist;
		this.imageURL = imageURL;
	}
	public String toString() {
		return title+" by "+artist;
	}
	public boolean equals(Object o) {
		return (o instanceof Album) && toString().equals(""+o);
	}
	public int hashCode() {
		return toString().hashCode();
	}
}
