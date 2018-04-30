import java.util.*;

public class Album extends Playlist{

	private String artist; //Artista del album

	Album(String artist, String name, ArrayList<Cancion> songList){
	super(name,songList);
	this.artist = artist; 
	}
	

}
