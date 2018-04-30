public class Cancion{

    //Variables 
    private String name;    //Nombre de la cancion
    private String artist;  //Artista
    private String album;   //Album al que pertenece
    private int year;       //Año en el que fue lanzada 
    private int rating;     //Rating que da el usuario (1 al 5)
    private int runtime;    //Runtime de la cancion (segundos)

    //Constructor
    Cancion(String name, String artist, String album, int year, int rating, int runtime){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.rating = rating;
        this.runtime = runtime;
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
