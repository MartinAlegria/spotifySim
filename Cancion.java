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
    static Scanner input = new Scanner(System.in);


    //Constructor
    Cancion(String name, String artist, String album, int year, int runtime){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.runtime = runtime;
    }

    public String getName(){ return name;}
    public String getArtist(){ return artist;}

    public void setName(String name){this.name = name;}
    public void setArtist(String artist){this.artist = artist;}
    public void setAlbum(String album){this.album = album;}
    public void setYear(int year){this.year = year;}
    public void setRuntime(int runtime){this.runtime = runtime;}

    public void write(Usuario user){

        try{
        bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "songLib.csv",true)); 
        bw.write(name + "," + artist + "," + album + "," + year + "," + runtime);
        bw.newLine();
        bw.flush();
        bw.close();
        }catch(IOException e){e.printStackTrace();} 
    }

    public void editData(){

        System.out.println("\t Ingresa el nombre de la cancion");
        String nombreCancion = input.nextLine();
        System.out.println("\t Ingresa el artista de la cancion");
        String artistaCancion = input.nextLine();
        System.out.println("\t Ingresa el album de la cancion");
        String n_albumCancion = input.nextLine();
        System.out.println("\t Ingresa el año de la cancion");
        int yearCancion = input.nextInt();
        System.out.println("\t Ingresa el runtime de la cancion en segundos \n \t [Ej. 1:30 min = 90 seg ]" );
        int rtCancion = input.nextInt();

        setName(nombreCancion);
        setArtist(artistaCancion);
        setAlbum(n_albumCancion);
        setYear(yearCancion);
        setRuntime(rtCancion);
    }

    public void writeOut(Usuario user){

        try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 
    }

 

}//Class Cancion 

