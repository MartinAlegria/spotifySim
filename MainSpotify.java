import java.util.*;
import javax.swing.*;
import java.io.*;

public class MainSpotify{

	//Variables
	static Scanner input = new Scanner(System.in);
	
	static ArrayList<Cancion> recentList = new ArrayList<Cancion>();	//A.L. donde se encuentran las canciones recientes
	static ArrayList<Cancion> albumList = new ArrayList<Cancion>(); 	//A.L. para inicializar un Album
	static ArrayList<Cancion> playList = new ArrayList<Cancion>(); 		//A.L. para inicializar una Playlist
	static ArrayList<Usuario> usersList = new ArrayList<Usuario>(); 	//ArrayList donde se encuentran todos los usuarios

	static ArrayList<Album> albumLib = new ArrayList<Album>();
	static ArrayList<Playlist> plistLib = new ArrayList<Playlist>();
	static ArrayList<Cancion> songLib = new ArrayList<Cancion>();

	static Usuario user;
	static Usuario currentUser;
	static Cancion song;	
	static Album album;
    static BufferedReader br;
    static BufferedReader br2;
    static BufferedWriter bw;
	static File f = new File("users.csv");

	public static void main(String[] args) {

		System.out.println("\t --------- SPOTIFY ---------");
		System.out.println("\t Bienvenido!");

		if(f.length() == 0) {
			System.out.println("\t No tienes ningun usuario creado");
			createUser();
		}

		read();
		do{

			System.out.println("\t 1.- Log-In");
			System.out.println("\t 2.- Crear Cuenta");
			System.out.println("\t 3.- Salir");		
			System.out.println("\t Ingrea el número de la opcion deseada");
			int launch = input.nextInt();

			switch(launch){
				case 1:
					boolean correcto = false;

					do{

						if(logIn()) {
							System.out.println("////////// Bienvenido " + currentUser.getName() + " //////////");
							correcto = true;
							readSongLib();
							readAlbumLib();
						
							boolean logOut = false;
							do{
								menu();
								int eleccion = input.nextInt();

								switch(eleccion){
									case 1: //CANCIONES

										canciones();

											int cancionesDec = input.nextInt();
											input.nextLine();

											if(cancionesDec >= 1 && cancionesDec <= songLib.size()) {
												
												System.out.println("\t " + songLib.get(cancionesDec - 1).getName() + ":");
												System.out.println("\t 1.- Reproducir Cancion"); //TIMER TASK
												System.out.println("\t 2.- Editar Cancion");
												System.out.println("\t 3.- Eliminar Cancion");
												System.out.println("\t 0.- Regresar");
												int specificSongDec = input.nextInt();

													switch(specificSongDec){

														case 0:
															break;

														case 1:
															System.out.println("\t////////// AHORA REPRODUCIENDO " + songLib.get(cancionesDec-1).getName().toUpperCase() + " ... //////////"  );
															break;

														case 2: //EDTIAR CANCION
															songLib.get(cancionesDec-1).editData();
															writeOUTSongLib();
															writeSongLib();
															readSongLib();
															break;

														case 3: //BORRAR CANCION
															songLib.remove(songLib.get(cancionesDec-1));
															writeOUTSongLib();
															writeSongLib();
															readSongLib();
															break;	

													}

											}else if (cancionesDec == 0){
												createSong();
											}else if (cancionesDec == -1){
												break;
											}

										break;

									case 2: //ALBUMS
											albums();
											int albumsDec = input.nextInt();
											input.nextLine();

											if(albumsDec >= 1 && albumsDec <= albumList.size()) {
												
												System.out.println("\t " + albumLib.get(cancionesDec - 1).getName() + ":");
												System.out.println("\t 1.- Reproducir Album"); //TIMER TASK
												System.out.println("\t 2.- Editar Album");
												System.out.println("\t 3.- Eliminar Album");
												System.out.println("\t 0.- Regresar");
												int specificAlbumDec = input.nextInt();

											}else if(albumsDec == 0){ //CREAR ALBUM
												createAlbum();
											}else if (albumsDec == -1){
												break;
											}

										break;

									case 3: //PLAYLISTS
										break;

									case 4: //LOGOUT
										logOut = true;
										break;
								}

							}
							while(!logOut);

						}else{
							System.out.println("\t Usuario o contraseña incorrecta, intenta de nuevo");
							correcto = false;
						}

					}while(!correcto);

					break;
				case 2:
					createUser();
					break;

				case 3:
					System.exit(0);
					break;

				default:
					break;

			}//SWITCH

		}while(true);
		

	}//MAIN

	public static void createUser(){

		boolean s;
		System.out.println("\t ¿Cual es tu nombre?");
		String n = input.next();
		System.out.println("\t ¿Cual es tu edad?");
		int a = input.nextInt();
		System.out.println("\t ¿Cual va a ser tu nombre de usaurio?");
		String u = input.next();
		System.out.println("\t ¿Cual va a ser tu contraseña?");
		String p = input.next();
		System.out.println("\t ¿Que tipo de subscripción vas a tener? [gratis/premium]");
		String l = input.next();
			if(l.equalsIgnoreCase("gratis")) {
				 s = false;
			}else{  s = true;}
		user = new Usuario(n,a,u,p,s);
		user.write();
		usersList.add(user);

	}//CREATE_USER

	public static void menu(){
		System.out.println("\t 1.- Canciones");
		System.out.println("\t 2.- Albums");
		System.out.println("\t 3.- Playlist");
		System.out.println("\t 4.- Log-Out");
		System.out.println("\t Ingresa el numero de la opcion deseada");
	}//MENU

	public static void read(){

		String[] u = new String[5];
		boolean loop = true;

			try{
				

				br = new BufferedReader(new FileReader(f));
				String read;
				
				while((read = br.readLine() ) != null){
				
					u = read.split(",");
					loop = true;

				String name = u[0];
				int age = Integer.parseInt(u[1]);
				String usr = u[2];
				String pass = u[3];
				boolean sub = Boolean.valueOf(u[4]);

				user = new Usuario(name,age,usr,pass,sub);
				usersList.add(user);

			}

			}catch(IOException e){e.printStackTrace();}

	}//READ

	public static void readSongLib(){

		String[] sL = new String[5];
		boolean loop = true;
		songLib.clear();	

		try{
			

			br = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/" + "songLib.csv"));
			String read;
			
			while((read = br.readLine() ) != null){
			
				sL = read.split(",");
				loop = true;

				String name = sL[0];
				String artist = sL[1];
				String album = sL[2];
				int year = Integer.parseInt(sL[3]);
				int runtime = Integer.parseInt(sL[4]);

				song = new Cancion(name, artist, album, year, runtime);
				songLib.add(song);
			}		

		}catch(IOException e){e.printStackTrace();}	

	}//READ_SONG_LIBRARY


	public static void readAlbumLib(){

		String[] sL = new String[5];
		String[] aL = new String[5];
		boolean loop = true;
		songLib.clear();	

		try{
			

			br = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/" + "albumLib.csv"));
			String read;
			
			while((read = br.readLine() ) != null){
			
				aL = read.split(",");
				loop = true;

				String name = aL[0];
				String artist = aL[1];


					br2 = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/Albums/" +  name +".csv"));

					String read2;
					while((read2 = br2.readLine() ) != null){
			
						sL = read2.split(",");
						loop = true;

						String nameSong = sL[0];
						String artistSong = sL[1];
						String albumSong = sL[2];
						int yearSong = Integer.parseInt(sL[3]);
						int runtimeSong = Integer.parseInt(sL[4]);

						song = new Cancion(nameSong, artistSong, albumSong, yearSong, runtimeSong);
						albumList.add(song);
					}	

				album = new Album(name, artist, albumList);
				albumLib.add(album);
			}		

		}catch(IOException e){e.printStackTrace();}	

	}//READ_SONG_LIBRARY

	public static boolean logIn(){

		boolean log = true;
		int indexUser = 0;

		System.out.println("\t Ingresa el usuario:");
		String logUser = input.next();

		System.out.println("\t Ingresa la contraseña: [Case sensitive]");
		String logPass = input.next();

		for(int i = 0; i<usersList.size(); i++) {
			if(logUser.equalsIgnoreCase(usersList.get(i).getUser()) &&  logPass.equalsIgnoreCase(usersList.get(i).getPass())) {
				log = true;
				indexUser = i;
				break;
			}else{
				log = false;
			}
		}//FOR

		currentUser = usersList.get(indexUser);

		if(log) {
			return true;
		}else{
			return false;
		}
	
	}//LOGIN

	public static void canciones(){

		System.out.println("\t 0.- Añadir cancion");
		System.out.println("\t -1.- Regresar\n\n\t ///////// TU LIBRERIA DE CANCIONES: /////////");

		for(int i = 0; i<songLib.size(); i++) {
			System.out.println("\t "+ (i+1) + ".- " + songLib.get(i).getName() + "\t\t" + songLib.get(i).getArtist());
		}

	}//CANCIONES

	public static void createSong(){


		System.out.println("\t CREAR CANCION");
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

		song = new Cancion(nombreCancion, artistaCancion, n_albumCancion, yearCancion, rtCancion);
		songLib.add(song);
		writeOUTSongLib();
		writeSongLib();
	
	}//CREATE_SONG

	public static void writeSongLib(){


		if(songLib.size() == 0) {
		try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 

		}else{
			for(int i = 0; i<songLib.size(); i++) {
			songLib.get(i).write(currentUser);
			}
		}
	
	} //WRITE SONG LIB

	public static void writeOUTSongLib(){

		if(songLib.size() == 0) {
		try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 

		}else{
			for(int i = 0; i<songLib.size(); i++) {
			songLib.get(i).writeOut(currentUser);
			}
		}
	
	} //WRITEOUT SONG LIB

	public static void albums(){

		System.out.println("\t 0.- Añadir Album");
		System.out.println("\t -1.- Regresar\n\n\t ///////// TU LIBRERIA DE ALBUMS: /////////");

		for(int i = 0; i<albumLib.size(); i++) {
			System.out.println("\t "+ (i+1) + ".- " + albumLib.get(i).getName() + "\t\t" + albumLib.get(i).getArtist());
		}
	
	}//ALBUMS

	public static void createAlbum(){

		boolean seguir = true;

		System.out.println("\t CREAR ALBUM");
		System.out.println("\t Ingresa el nombre del album");
		String nombreAlbum = input.nextLine();
		System.out.println("\t Ingresa el artista del album");
		String artistaAlbum = input.nextLine();

		do{

			System.out.println("\t AÑADIR CANCION A ALBUM");
			System.out.println("\t Ingresa el nombre de la cancion");
			String nombreCancion = input.nextLine();
			System.out.println("\t Ingresa el año de la cancion");
			int yearCancion = input.nextInt();
			System.out.println("\t Ingresa el runtime de la cancion en segundos \n \t [Ej. 1:30 min = 90 seg ]" );
			int rtCancion = input.nextInt();

			song = new Cancion(nombreCancion, artistaAlbum, nombreAlbum, yearCancion, rtCancion);
			albumList.add(song);

			System.out.println("\t ¿Quieres añadir otra cancion ? [Y/N]");
			char otra = input.next().charAt(0);
			input.nextLine();

			if(Character.toLowerCase(otra) == 'y'){
				seguir = true;
			}
			else{
				seguir = false;
			}

		}while(seguir);

		album = new Album(nombreAlbum, artistaAlbum, albumList);
		albumLib.add(album);
		writeOUTAlbumLib();
		writeAlbumLib();

	}//CREATE ALBUM

	public static void writeOUTAlbumLib(){

		if(albumLib.size() == 0) {
		try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 

		}else{
			for(int i = 0; i<albumLib.size(); i++) {
			albumLib.get(i).writeOut(currentUser);
			}
		}

	}//WRITE OUT ALBUM LIB

	public static void writeAlbumLib(){

		if(albumLib.size() == 0) {
		try{
            bw = new BufferedWriter(new FileWriter(user.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
            bw.write("");
            bw.flush();
            bw.close();
        }catch(IOException e){e.printStackTrace();} 

		}else{
			for(int i = 0; i<albumLib.size(); i++) {
			albumLib.get(i).write(currentUser);
			}
		}

	}//WRITE ALBUM LIB

}//Class MainSpotify

//System.out.println("\t");