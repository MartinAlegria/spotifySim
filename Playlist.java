import java.util.*;
import java.io.*;

public class Playlist{

	//Variables
	protected String name; //Nombre de la Playlist
	protected String description; //Descripción de la playlist
	protected ArrayList<Cancion> songList; //ArrayList de canciones

	static BufferedWriter bw;
	static BufferedWriter bw2;
    static BufferedReader br;
    static File f;

	//El constructor que se usaria para incializar una playlist
	Playlist(String name, String description, ArrayList<Cancion> songList){ 
		this.name = name;
		this.description = description;
		this.songList = songList;
	}

	//El constructor que se usaria para inicialziar un Album mediante herencia
	Playlist(String name, ArrayList<Cancion> songList){
		this.name = name;
		this.songList = songList;
	}

	public String getName(){ return name;}
	public String getDesc() {return description;}

	public void write(Usuario user){
		try{
        bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "plistLib.csv",true)); 
        bw.write(name + "," + description );
        bw.newLine();

        f = new File(user.getUser().toLowerCase() + "_Library/Playlists/" + name + ".csv");
		f.getParentFile().mkdir();

        try{
            f.createNewFile();
        }catch(IOException e){}

        bw2 = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/Playlists/" + name +".csv",true)); 

        for(int i = 0; i<songList.size(); i++) {
        	bw2.write(songList.get(i).getName() + "," + songList.get(i).getArtist() + "," + songList.get(i).getAlbum() + "," + songList.get(i).getYear() + "," + songList.get(i).getRuntime());
        	bw2.newLine();
        }
  	
        bw2.flush();
        bw2.close();

        bw.flush();
        bw.close();
        }catch(IOException e){e.printStackTrace();} 
	}

	/*
		Actualiza el archivo plistLib.csv
	*/

    public void update(Usuario user){
		try{
        bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "plistLib.csv",true)); 
        bw.write(name + "," + description );
        bw.newLine();
        bw.flush();
        bw.close();
        }catch(IOException e){e.printStackTrace();} 
	}

    public void deleteFile(Usuario user){

    	f = new File(user.getUser().toLowerCase() + "_Library/Playlists/" + name + ".csv");
    	f.delete();
    }

   	public void editData(String name, String desc){

		this.name = name;
		this.description = desc;

	}//EDIT DATA

	/*
		Actualiza el archivo individual de canciones de una playlist
	*/

	public void updateFile(Usuario user){


		try{

			f = new File(user.getUser().toLowerCase() + "_Library/Playlists/" + name + ".csv");
			f.getParentFile().mkdir();

	        try{
	            f.createNewFile();
	        }catch(IOException e){}

	        bw2 = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/Playlists/" + name +".csv",true)); 

	        for(int i = 0; i<songList.size(); i++) {
	        	bw2.write(songList.get(i).getName() + "," + songList.get(i).getArtist() + "," + songList.get(i).getName() + "," + songList.get(i).getYear() + "," + songList.get(i).getRuntime());
	        	bw2.newLine();
	        }
	  	
	        bw2.flush();
	        bw2.close();

		}catch(IOException e){e.printStackTrace();}

	}//UPDATE FILE

	public void printSongs(){

		for(int i = 0; i<songList.size(); i++) {
			System.out.println("\t\t * " + songList.get(i).getName());
		}		

	}//PRINT ALBUM SONGS


}//class Playlist