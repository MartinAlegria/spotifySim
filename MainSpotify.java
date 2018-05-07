import java.util.*;
import javax.swing.*;
import java.io.*;

public class MainSpotify{

	//Variables
	static Scanner input = new Scanner(System.in);
	
	static ArrayList<Cancion> albumList;								//A.L. para inicializar un Album
	static ArrayList<Cancion> playList = new ArrayList<Cancion>(); 		//A.L. para inicializar una Playlist
	static ArrayList<Usuario> usersList = new ArrayList<Usuario>(); 	//ArrayList donde se encuentran todos los usuarios

	static ArrayList<Album> albumLib = new ArrayList<Album>();
	static ArrayList<Playlist> plistLib = new ArrayList<Playlist>();
	static ArrayList<Cancion> songLib = new ArrayList<Cancion>();

	static Usuario user;
	static Usuario currentUser;
	static Cancion song;	
	static Album album;
	static Playlist plist;
    static BufferedReader br;
    static BufferedReader br2;
    static BufferedWriter bw;
	static File f = new File("users.csv");

	public static void main(String[] args) {

		System.out.println("\n\t --------- SPOTIFY ---------");
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
							System.out.println("\n\t ////////// Bienvenido " + currentUser.getName() + " //////////");
							correcto = true;
							readSongLib();
							readAlbumLib();
							readPlistLib();
						
							boolean logOut = false;//VARIABLE QUE SE USA COMO CONDICIONAL DEL DOWHILE
							do{
								menu();
								int eleccion = input.nextInt();

								switch(eleccion){

									case 1: //CANCIONES ------------------------------------------------------------------------------------------------
										
										canciones();

											int cancionesDec = input.nextInt();
											input.nextLine();

											if(cancionesDec >= 1 && cancionesDec <= songLib.size()) {
												removeDuplicates();
												System.out.println("\t --- " + songLib.get(cancionesDec - 1).getName() + " ---");
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

															System.out.println("\t Estas Apunto De Eliminar " + songLib.get(cancionesDec-1).getName() + "\n\t ¿Quieres Continuar? [Y/N]" );
															char delSong = input.next().charAt(0);

															if(Character.toLowerCase(delSong) == 'y') {

																System.out.println("\t Eliminaste " + songLib.get(cancionesDec-1).getName());
																songLib.remove(songLib.get(cancionesDec-1));
																writeOUTSongLib();
																writeSongLib();
																readSongLib();
																
															}else{
																System.out.println("\t Eliminacion Cancelada\n\n");
															}
														
															break;	

													}

											}else if (cancionesDec == 0){
												createSong();
											}else if (cancionesDec == -1){
												break;
											}

										break;

									case 2: //ALBUMS ------------------------------------------------------------------------------------------------
											albums();
											int albumsDec = input.nextInt();
											input.nextLine();

											if(albumsDec >= 1 && albumsDec <= albumLib.size()) { //UN ALBUM ESPECIFICO FUE SELECCIONADO
												
												System.out.println("\t --- " + albumLib.get(albumsDec - 1).getName() + " --- ");
												albumLib.get(albumsDec-1).printAlbumSongs();
												System.out.println("\t 1.- Reproducir Album"); //TIMER TASK
												System.out.println("\t 2.- Editar Album");
												System.out.println("\t 3.- Eliminar Album");
												System.out.println("\t 0.- Regresar");
												int specificAlbumDec = input.nextInt();
												input.nextLine();

													switch(specificAlbumDec){

														case 0: //REGRESAR
															break;

														case 1: //REPRODUCIR ALBUM
																System.out.println("\t////////// AHORA REPRODUCIENDO " + albumLib.get(albumsDec-1).getName().toUpperCase() + " ... //////////"  );														
															break;

														case 2: //EDITAR ALBUM
																editAlbum(albumsDec-1);
																writeOUTSongLib();
																writeSongLib();
																writeOUTAlbumLib();
																writeAlbumLib();
																readAlbumLib();
															break;

														case 3://ELIMINAR ALBUM
															System.out.println("\t Estas Apunto De Eliminar " + albumLib.get(albumsDec-1).getName() + "\n\t ¿Quieres Continuar? [Y/N]" );
															char delAlbum = input.next().charAt(0);
															
															if(Character.toLowerCase(delAlbum) == 'y') {
															
																System.out.println("\t Eliminaste " + albumLib.get(albumsDec-1).getName());
																removeSongsAlbum(albumsDec-1);
																albumLib.get(albumsDec-1).deleteFile(currentUser);
																albumLib.remove(albumLib.get(albumsDec-1));
																writeOUTAlbumLib();
																writeAlbumLib();
																readAlbumLib();

															}else{
																System.out.println("\t Eliminacion Cancelada\n\n");
															}

															break;

													}//ALBUM SPECIFIC MENU

											}else if(albumsDec == 0){ //CREAR ALBUM
												createAlbum();
												writeOUTAlbumLib();
												writeAlbumLib();
												writeOUTSongLib();
												writeSongLib();

											}else if (albumsDec == -1){ //REGRESAR
												break;
											}

										break;

									case 3: //PLAYLISTS

											plists();
											int plistDec = input.nextInt();
											input.nextLine();

											if(plistDec >= 1 && plistDec <= plistLib.size()){ //PLAYLIST ESPECIFICA FUE SELECCIONADA

												System.out.println("\t --- " + plistLib.get(plistDec - 1).getName() + " --- ");
												plistLib.get(plistDec - 1).printSongs();
												System.out.println("\t 1.- Reproducir Playlist"); //TIMER TASK
												System.out.println("\t 2.- Editar Playlist");
												System.out.println("\t 3.- Eliminar Playlist");
												System.out.println("\t 0.- Regresar");
												int specificPlistDec = input.nextInt();
												input.nextLine();

												switch(specificPlistDec){

														case 0: //REGRESAR
															break;

														case 1: //REPRODUCIR PLAYLIST
																System.out.println("\t////////// AHORA REPRODUCIENDO " + plistLib.get(plistDec-1).getName().toUpperCase() + " ... //////////"  );														
															break;

														case 2: //EDITAR PLAYLIST
																editPlaylist(plistDec-1);
																writeOutPlaylistLib();
																writePlaylistLib();
																readPlistLib();
															break;

														case 3://ELIMINAR PLAYLIST
															System.out.println("\t Estas Apunto De Eliminar " + plistLib.get(plistDec-1).getName() + "\n\t ¿Quieres Continuar? [Y/N]" );
															char delPlist = input.next().charAt(0);
															
															if(Character.toLowerCase(delPlist) == 'y') {
															
																System.out.println("\t Eliminaste " + plistLib.get(plistDec-1).getName());
															
																plistLib.get(plistDec-1).deleteFile(currentUser);
																plistLib.remove(plistLib.get(plistDec-1));
																writeOutPlaylistLib();
																writePlaylistLib();
																readPlistLib();

															}else{
																System.out.println("\t Eliminacion Cancelada\n\n");
															}

															break;

													}//PLAYLIST SPECIFIC MENU

											}else if(plistDec == 0){ //CREAR PLAYLIST
												createPlist();
											}else if (plistDec == -1){ //REGRESAR
												break;
											}

										break;

									case 4: //LOGOUT
										writeOUTSongLib();
										writeSongLib();
										writeOUTAlbumLib();
										writeAlbumLib();
										writeOutPlaylistLib();
										writePlaylistLib();
											logOut = true;//SE VUELVE VERDADERO Y ROMPE EL LOOP
										break;
								}

							}
							while(!logOut);

						}else{
							System.out.println("\t Usuario o contraseña incorrecta, intenta de nuevo");
							correcto = false; //HACE QUE EL LOOP SIGA CORRIENDO
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


	// FUNCTIONS ------------------------------------------------------------------------------------------------


	//UI FUNCTIONS		*******************************************

		public static void menu(){
			System.out.println("\t 1.- Canciones");
			System.out.println("\t 2.- Albums");
			System.out.println("\t 3.- Playlist");
			System.out.println("\t 4.- Log-Out");
			System.out.println("\t [Ingresa el numero de la opcion deseada]");
		}//MENU

		public static boolean logIn(){

			System.out.println("\t //////// LOG-IN ////////");
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

	//USER FUNCTIONS	*******************************************

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

		public static void canciones(){

			System.out.println("\t ///////// TU LIBRERIA DE CANCIONES: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			for(int i = 0; i<songLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + songLib.get(i).getName() + "\t\t" + songLib.get(i).getArtist());
			}

			System.out.println("\t ---------------------------------");
			System.out.println("\t    ***  0.- Añadir Cancion *** ");
			System.out.println("\t    *** -1.- Regresar ***");

		}//CANCIONES

		public static void albums(){

			System.out.println("\t ///////// TU LIBRERIA DE ALBUMS: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			for(int i = 0; i<albumLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + albumLib.get(i).getName() + "\t" + albumLib.get(i).getArtist());
			}

			System.out.println("\t -----------------------------------");
			System.out.println("\t      ***  0.- Añadir Album *** ");
			System.out.println("\t      *** -1.- Regresar ***");
		
		}//ALBUMS

		public static void plists(){

			System.out.println("\t ///////// TU LIBRERIA DE PLAYLISTS: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			for(int i = 0; i<plistLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + plistLib.get(i).getName() + "\t" + plistLib.get(i).getDesc());
			}

			System.out.println("\t -----------------------------------");
			System.out.println("\t      ***  0.- Crear Playlist *** ");
			System.out.println("\t      *** -1.- Regresar ***");
		
		}//PLAYLISTS


	//SONG FUNCTIONS	*******************************************

		public static void readSongLib(){

			String[] sL = new String[5];
			boolean loop = true;
			songLib.clear();	

			try{
				

				br = new BufferedReader(new FileReader(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv"));
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
					removeDuplicates();
				}		

			}catch(IOException e){e.printStackTrace();}	

		}//READ_SONG_LIBRARY

		public static void createSong(){


			System.out.println("\t CREAR CANCION");
			System.out.println("\t Ingresa el nombre de la cancion");
			String nombreCancion = input.nextLine();
			System.out.println("\t Ingresa el artista de la cancion");
			String artistaCancion = input.nextLine();
			System.out.println("\t Ingresa el album de la cancion \n \t[Ingresa un guion (-) para descartar ]");
			String n_albumCancion = input.nextLine();
			System.out.println("\t Ingresa el año de la cancion \n \t[Ingresa un cero (0) para descartar ]");
			int yearCancion = input.nextInt();
			System.out.println("\t Ingresa el runtime aproximado de la cancion en segundos \n \t [Ej. 1m = 60s; 2m = 120s; 3m= 180s; 4m = 240s]" );
			int rtCancion = input.nextInt();

			song = new Cancion(nombreCancion, artistaCancion, n_albumCancion, yearCancion, rtCancion);
			songLib.add(song);
			writeOUTSongLib();
			writeSongLib();
		
		}//CREATE_SONG

		public static void writeSongLib(){

			if(songLib.size() == 0) {
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
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
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
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

		public static void removeDuplicates(){

			if(songLib.size() > 1) {
				for(int i = 0; i<songLib.size(); i++) {
				
				for (int j = 1; j<songLib.size()-1; j++) {
					if(songLib.get(i).isEqual(songLib.get(j))) {
						songLib.remove(j);
					}
				}

			}
			}

		}//REMOVE DUPLICATES


	//ALBUM FUNCTIONS 	*******************************************

		public static void readAlbumLib(){

			String[] sL = new String[5];
			String[] aL = new String[5];
			boolean loop = true;
			albumLib.clear();	

			try{
				

				br = new BufferedReader(new FileReader(currentUser.getUser().toLowerCase() + "_Library/" + "albumLib.csv"));
				String read;
				
				while((read = br.readLine() ) != null){
				
					aL = read.split(",");
					loop = true;

					String name = aL[0];
					String artist = aL[1];


						br2 = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/Albums/" +  name +".csv"));

						String read2;
						albumList = new ArrayList<Cancion>(); 
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
		
		public static void createAlbum(){

			boolean seguir = true;

			System.out.println("\t CREAR ALBUM");
			System.out.println("\t Ingresa el nombre del album");
			String nombreAlbum = input.nextLine();
			System.out.println("\t Ingresa el artista del album");
			String artistaAlbum = input.nextLine();

			albumList = new ArrayList<Cancion>(); 

			do{

				System.out.println("\t AÑADIR CANCION A ALBUM");
				System.out.println("\t Ingresa el nombre de la cancion");
				String nombreCancion = input.nextLine();
				System.out.println("\t Ingresa el año de la cancion");
				int yearCancion = input.nextInt();
				System.out.println("\t Ingresa el runtime aproximado de la cancion en segundos \n \t [Ej. 1m = 60s; 2m = 120s; 3m= 180s; 4m = 240s]" );
				int rtCancion = input.nextInt();

				song = new Cancion(nombreCancion, artistaAlbum, nombreAlbum, yearCancion, rtCancion);
				albumList.add(song);
				songLib.add(song);

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
			album.write(currentUser);
			albumLib.add(album);
			
		}//CREATE ALBUM
		
		public static void editAlbum(int index){

			boolean seguir = true;

			System.out.println("\t EDITAR ALBUM");
			System.out.println("\t Ingresa el nombre del album");
			String nombreAlbum = input.nextLine();
			System.out.println("\t Ingresa el artista del album");
			String artistaAlbum = input.nextLine();

			removeSongsAlbum(index);
			albumLib.get(index).getSongList().clear();

			do{

				System.out.println("\t AÑADIR CANCION A ALBUM");
				System.out.println("\t Ingresa el nombre de la cancion");
				String nombreCancion = input.nextLine();
				System.out.println("\t Ingresa el año de la cancion");
				int yearCancion = input.nextInt();
				System.out.println("\t Ingresa el runtime aproximado de la cancion en segundos \n \t [Ej. 1m = 60s; 2m = 120s; 3m= 180s; 4m = 240s]" );
				int rtCancion = input.nextInt();

				song = new Cancion(nombreCancion, artistaAlbum, nombreAlbum, yearCancion, rtCancion);
				albumLib.get(index).getSongList().add(song);
				songLib.add(song);

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

			albumLib.get(index).deleteFile(currentUser);
			albumLib.get(index).editData(nombreAlbum,artistaAlbum);
			albumLib.get(index).updateFile(currentUser);

		}//EDIT ALBUM

		public static void writeOUTAlbumLib(){

			if(albumLib.size() == 0) {
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
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
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

			}else{
				for(int i = 0; i<albumLib.size(); i++) {
				albumLib.get(i).update(currentUser);
				}
			}

		}//WRITE ALBUM LIB

		public static void removeSongsAlbum(int index){

			for(int i = 0; i<songLib.size(); i++) {
				
				for(int j = 0; j<albumLib.get(index).getSongList().size(); j++) {
					if(songLib.get(i).equals(albumLib.get(index).getSongList().get(j))) {
						songLib.remove(i);
					}
				}

			}

			writeOUTSongLib();
			writeSongLib();

		}//REMOVE SONGS ALBUM

	//PLAYLIST FUNCTIONS 	*******************************************

		public static void readPlistLib(){

			String[] sL = new String[5];
			String[] pL = new String[5];
			boolean loop = true;
			plistLib.clear();	

			try{
				

				br = new BufferedReader(new FileReader(currentUser.getUser().toLowerCase() + "_Library/" + "plistLib.csv"));
				String read;
				
				while((read = br.readLine() ) != null){
				
					pL = read.split(",");
					loop = true;

					String name = pL[0];
					String desc = pL[1];


						br2 = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/Playlists/" +  name +".csv"));

						String read2;
						playList = new ArrayList<Cancion>(); 
						while((read2 = br2.readLine() ) != null){
				
							sL = read2.split(",");
							loop = true;

							String nameSong = sL[0];
							String artistSong = sL[1];
							String albumSong = sL[2];
							int yearSong = Integer.parseInt(sL[3]);
							int runtimeSong = Integer.parseInt(sL[4]);

							song = new Cancion(nameSong, artistSong, albumSong, yearSong, runtimeSong);
							playList.add(song);
						}	

					plist = new Playlist(name, desc, playList);
					plistLib.add(plist);
					
				}		

			}catch(IOException e){e.printStackTrace();}	

		}//READ PLAYLIST LIB

		public static void createPlist(){

			boolean seguir = true;

			System.out.println("\t CREAR PLAYLIST");
			System.out.println("\t Ingresa el nombre de la playList");
			String nombrePlist = input.nextLine();
			System.out.println("\t Ingresa una descripcion pequeña de la playList");
			String descPlist = input.nextLine();

			playList = new ArrayList<Cancion>(); 

			do{

				System.out.println("\t AÑADIR CANCION A PLAYLIST");
				System.out.println("\t Ingresa el nombre de la cancion");
				String nombreCancion = input.nextLine();
				System.out.println("\t Ingresa el artista de la cancion");
				String artistaCancion = input.nextLine();
				System.out.println("\t Ingresa el album de la cancion \n \t[Ingresa un guion (-) para descartar ]");
				String n_albumCancion = input.nextLine();
				System.out.println("\t Ingresa el año de la cancion \n \t[Ingresa un cero (0) para descartar ]");
				int yearCancion = input.nextInt();
				System.out.println("\t Ingresa el runtime aproximado de la cancion en segundos \n \t [Ej. 1m = 60s; 2m = 120s; 3m= 180s; 4m = 240s]" );
				int rtCancion = input.nextInt();

				song = new Cancion(nombreCancion, artistaCancion, n_albumCancion, yearCancion, rtCancion);
				playList.add(song);

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

			plist = new Playlist(nombrePlist, descPlist, playList);
			plist.write(currentUser);
			plistLib.add(plist);
			
		}//CREATE ALBUM

		public static void editPlaylist(int index){

			boolean seguir = true;

			System.out.println("\t CREAR PLAYLIST");
			System.out.println("\t Ingresa el nombre de la playList");
			String nombrePlist = input.nextLine();
			System.out.println("\t Ingresa una descripcion pequeña de la playList");
			String descPlist = input.nextLine();

			playList = new ArrayList<Cancion>(); 

			do{

				System.out.println("\t AÑADIR CANCION A PLAYLIST");
				System.out.println("\t Ingresa el nombre de la cancion");
				String nombreCancion = input.nextLine();
				System.out.println("\t Ingresa el artista de la cancion");
				String artistaCancion = input.nextLine();
				System.out.println("\t Ingresa el album de la cancion");
				String n_albumCancion = input.nextLine();
				System.out.println("\t Ingresa el año de la cancion");
				int yearCancion = input.nextInt();
				System.out.println("\t Ingresa el runtime aproximado de la cancion en segundos \n \t [Ej. 1m = 60s; 2m = 120s; 3m= 180s; 4m = 240s]" );
				int rtCancion = input.nextInt();

				song = new Cancion(nombreCancion, artistaCancion, n_albumCancion, yearCancion, rtCancion);
				playList.add(song);

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



			plistLib.get(index).deleteFile(currentUser);
			plistLib.get(index).editData(nombrePlist,descPlist);
			plistLib.get(index).updateFile(currentUser);

		}//EDIT PLAYLIST

		public static void writeOutPlaylistLib(){

			if(plistLib.size() == 0) {
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "plistLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

			}else{
				for(int i = 0; i<plistLib.size(); i++) {
				plistLib.get(i).writeOut(currentUser);
				}
			}

		}//WRITE OUT ALBUM LIB

		public static void writePlaylistLib(){

			if(albumLib.size() == 0) {
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "plistLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

			}else{
				for(int i = 0; i<plistLib.size(); i++) {
				plistLib.get(i).update(currentUser);
				}
			}

		}//WRITE ALBUM LIB


}//Class MainSpotify

//System.out.println("\t");