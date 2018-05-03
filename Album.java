import java.util.*;
import java.io.*;

public class Album extends Playlist{

	private String artist; //Artista del album
	static BufferedWriter bw;
	static BufferedWriter bw2;
    static BufferedReader br;
    static File f;

	Album(String name, String artist, ArrayList<Cancion> songList){
		super(name,songList);
		this.artist = artist; 
	}

	public String getArtist(){ return artist;}
	
	@Override
	public void write(Usuario user){
		try{
        bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "albumLib.csv",true)); 
        bw.write(name + "," + artist );
        bw.newLine();

        f = new File(user.getUser().toLowerCase() + "_Library/Albums/" + name + ".csv");
		f.getParentFile().mkdir();

        try{
            f.createNewFile();
        }catch(IOException e){}

        bw2 = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/Albums" + name +".csv",true)); 

        for(int i = 0; i<songList.size(); i++) {
        	bw2.write(songList.get(i).getName() + "," + artist + "," + name + "," + songList.get(i).getYear() + "," + songList.get(i).getRuntime());
        	bw2.newLine();
        }
  	
        bw2.flush();
        bw2.close();

        bw.flush();
        bw.close();
        }catch(IOException e){e.printStackTrace();} 
	}

	@Override
	public void writeOut(Usuario user){

        try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 
    }

}
