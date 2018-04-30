import java.util.*;

public class Playlist{

	//Variables
	protected String name; //Nombre de la Playlist
	protected String description; //Descripción de la playlist
	protected ArrayList<Cancion> songList; //ArrayList de canciones
	protected int numSongs; //Cantidad de canciones en la lista

	//El constructor que se usaria para incializar una playlist
	Playlist(String name, String description, ArrayList<Cancion> songList){ 
		this.name = name;
		this.description = description;
		this.songList = songList;
	}

	//El constructor que se usaria para inicialziar un Album mediante herencia
	Playlist(String name, ArrayList<Cancion> songList){

	}

}//class Playlist