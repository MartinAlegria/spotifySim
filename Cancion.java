import java.io.*;
import java.util.*;

public class Cancion{

    //Variables 
    private String name;    //Nombre de la cancion
    private String artist;  //Artista
    private String album;   //Album al que pertenece
    private int year;       //Año en el que fue lanzada 
    private int runtime;    //Runtime de la cancion (segundos)

    static BufferedWriter bw;
    static BufferedReader br;
    static File f;


    //Constructor
    Cancion(String name, String artist, String album, int year, int runtime){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.runtime = runtime;
    }

    public void write(Usuario user){


        try{
        bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "songLib.csv",true)); 
        bw.write(name + "," + artist + "," + album + "," + year + "," + runtime);
        bw.newLine();
        bw.flush();
        bw.close();
        }catch(IOException e){e.printStackTrace();} 

    }


}//Class Cancion 

/*
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setArtista(String artista){this.artista = artista;}
    public void setAlubm(String album){this.album = album;}
    public void setYear(int year){this.year = year;}
    public void setRating(int rating){this.rating = rating;}
    public void intRuntime(int runtime){this.runtime = runtime;}
*/
