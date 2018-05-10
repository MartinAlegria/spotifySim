import java.util.*;
import javax.swing.*;
import java.io.*;

public class MainSpotify{

	//Variables
	static Scanner input = new Scanner(System.in);						//Scanner para el input del usuario
	
	static ArrayList<Cancion> albumList;								//A.L. para inicializar un Album
	static ArrayList<Cancion> playList = new ArrayList<Cancion>(); 		//A.L. para inicializar una Playlist
	static ArrayList<Usuario> usersList = new ArrayList<Usuario>(); 	//ArrayList donde se encuentran todos los usuarios

	static ArrayList<Album> albumLib = new ArrayList<Album>();			//A.L. donde esta la libreria de Albums del usuario
	static ArrayList<Playlist> plistLib = new ArrayList<Playlist>();	//A.L. donde esta la libreria de PLaylists del usuario
	static ArrayList<Cancion> songLib = new ArrayList<Cancion>();		//A.L. donde esta la libreria de Canciones del usuario

	static Usuario user;			//Instancia de Usuario que se usa crear un nuevo usuario
	static Usuario currentUser;		//Instancia de Usuario donde se guarda el usuario que hizo LogIn
	static Cancion song;			//Instancia de Usuario que se usa para crear una nueva cancion
	static Album album;				//Instancia de Album que se usa para crear un nuevo album
	static Playlist plist;			//Instancia de Playlist que se usa para crear una nueva playlist
    static BufferedReader br;		//BufferedReader 1
    static BufferedReader br2;		//BufferedReader 2, usado para la creacion de archivos de canciones de albums/playlists
    static BufferedWriter bw;		//BufferedWriter
	static File f = new File("users.csv"); //File donde se guardan todos los usuarios creados

	public static void main(String[] args) {

		System.out.println("\n\t --------- SPOTIFY ---------");
		System.out.println("\t Bienvenido!");

		/*
			Si el archivo de users.csv está vacio significa que 
			el programa esta re100 salido del horno (nadie lo ha usado).
			Entonces se crear el primer usuario y se pasa al menú principal
			del programa.
		*/
		if(f.length() == 0) {
			System.out.println("\t No tienes ningun usuario creado");
			createUser();
		}

		read(); //Lee todos los usarios existentes para poder hacer LogIn

		do{

			System.out.println("\t 1.- Log-In");
			System.out.println("\t 2.- Crear Cuenta");
			System.out.println("\t 3.- Salir");	
			System.out.println("\t Ingrea el número de la opcion deseada");
			int launch = input.nextInt(); 

			switch(launch){
				case 1: //LOGIN
					boolean correcto = false; //Condicional para el for loop. Mientras sea falso regresara al Menu

					do{

						if(logIn()) {
							System.out.println("\n\t ////////// Bienvenido " + currentUser.getName() + " //////////");
							correcto = true; //Se hizo LogIn, 
							readSongLib(); //Se lee el archivo songLib.csv
							readAlbumLib(); //Se lee el achivo albumLib.csv
							readPlistLib(); //Se lee el archivo plistLib.csv
						
							boolean logOut = false;//VARIABLE QUE SE USA COMO CONDICIONAL DEL DO WHILE
							do{
								menu(); //Se muestra el menu
								int eleccion = input.nextInt();

								switch(eleccion){

									case 1: //CANCIONES ------------------------------------------------------------------------------------------------
										
										canciones();//Se muestran las canciones

											int cancionesDec = input.nextInt();
											input.nextLine();

											/*
												Se muestra el menu especifico para canciones, al igual que sus
												opciones.
											*/

											if(cancionesDec >= 1 && cancionesDec <= songLib.size()) {
												removeDuplicates(); //Quita los duplicados de la libreria de canciones
												System.out.println("\t --- " + songLib.get(cancionesDec - 1).getName() + " ---");
												System.out.println("\t 1.- Reproducir Cancion"); //TIMER TASK
												System.out.println("\t 2.- Editar Cancion");
												System.out.println("\t 3.- Eliminar Cancion");
												System.out.println("\t 0.- Regresar");
												int specificSongDec = input.nextInt();

													switch(specificSongDec){

														case 0: //REGRESAR
															break;

														case 1: //REPRODUCIR CANCION
															System.out.println("\t////////// AHORA REPRODUCIENDO " + songLib.get(cancionesDec-1).getName().toUpperCase() + " ... //////////"  );
															break;

														case 2: //EDTIAR CANCION
															songLib.get(cancionesDec-1).editData();
															writeOUTSongLib();
															writeSongLib();
															readSongLib();
															break;

														case 3: //BORRAR CANCION

															/*
																Se muestra que cancion se va a eliminar, se pregunta al usuario si quiere eliminarla
																Si el input es Y, se elimina (lasinstancia de esa cancion en el index "cancionesDec-1" 
																[la eleccion de cancion del usuario] es removida del A.L. sonLib)
															*/

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

											}else if (cancionesDec == 0){ //CREAR CANCION
												createSong(); 
											}else if (cancionesDec == -1){ //REGRESAR
												break;
											}

										break;

									case 2: //ALBUMS ------------------------------------------------------------------------------------------------
											albums();
											int albumsDec = input.nextInt();
											input.nextLine();

											if(albumsDec >= 1 && albumsDec <= albumLib.size()) { //UN ALBUM ESPECIFICO FUE SELECCIONADO
												
												/*
												Se muestra el menu especifico para albums, al igual que sus
												opciones.
												*/

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
																//Se actualizan los archivos albumLib.csv y songLib.csv
																writeOUTSongLib();
																writeSongLib();
																writeOUTAlbumLib();
																writeAlbumLib();
																readAlbumLib();
															break;

														case 3://ELIMINAR ALBUM

															/*
																Se muestra que ALBUM se va a eliminar, se pregunta al usuario si quiere eliminarlo
																Si el input es Y, se elimina (lasinstancia de ese album en el index "albumsDec-1" 
																[la eleccion de album del usuario] es removido del A.L. albumLib).
															*/

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
												
												//Se actualizan los archivos albumLib.csv y songLib.csv
												
												writeOUTAlbumLib(); 
												writeAlbumLib();
												writeOUTSongLib();
												writeSongLib();

											}else if (albumsDec == -1){ //REGRESAR
												break;
											}

										break;

									case 3: //PLAYLISTS ------------------------------------------------------------------------------------------------

											plists();
											int plistDec = input.nextInt();
											input.nextLine();

											if(plistDec >= 1 && plistDec <= plistLib.size()){ //PLAYLIST ESPECIFICA FUE SELECCIONADA

												/*
												Se muestra el menu especifico para playlists, al igual que sus
												opciones.
												*/

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
																// Se actualiza el archivo plisLib.csv
																writeOutPlaylistLib();
																writePlaylistLib();
																readPlistLib();
															break;

														case 3://ELIMINAR PLAYLIST

															/*
																Se muestra que Playlist se va a eliminar, se pregunta al usuario si quiere eliminarlo
																Si el input es Y, se elimina (la instancia de ese playlist en el index "plistDec-1" 
																[la eleccion de playlist del usuario] es removido del A.L. plistLib).
															*/

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

									case 4: //SETTINGS ------------------------------------------------------------------------------------------------

										System.out.println("\t " + currentUser.print());
										settings();
										int specificSettings = input.nextInt();
										input.nextLine();
									
										switch(specificSettings){
											
											case 1:
												System.out.println("\t ¿Cual es tu nombre?");
												String n = input.next();
												currentUser.setName(n);
												break;
											
											case 2: 
												System.out.println("\t ¿Cual es tu edad?");
												int a = input.nextInt();
												currentUser.setAge(a);
												break;

											case 3:
												System.out.println("\t ¿Cual va a ser tu nueva contraseña?");
												String p = input.next();
												currentUser.setPassword(p);
												break;

											case 4:
												boolean s;
												System.out.println("\t ¿Que tipo de subscripción vas a tener? [gratis/premium]");
												String l = input.next();
												if(l.equalsIgnoreCase("gratis")) {
													 s = false;
												}else{  s = true;}
												currentUser.setSub(s);
												break;

											case 0:
												break;

										}

											writeOutUser();
											writeUser();

										break;

									case 5: //LOGOUT ------------------------------------------------------------------------------------------------

											//Se acutalizan todos los archivos *lib.csv namas para estar seguros
											writeOutUser();
											writeUser();
											writeOUTSongLib();
											writeSongLib();
											writeOUTAlbumLib();
											writeAlbumLib();
											writeOutPlaylistLib();
											writePlaylistLib();
											logOut = true;
										break;
								}

							}
							while(!logOut); //Si !logOut regresa el menu, si no regresa al menu principal.

						}else{ //La funcion logIn regresó falso
							System.out.println("\t Usuario o contraseña incorrecta, intenta de nuevo");
							correcto = false; //HACE QUE EL LOOP SIGA CORRIENDO
						}

					}while(!correcto);//Si no se hizo logIn regresa al logIn, si no al menu principal

					break;

				case 2: //CREAR USUARIO
					createUser(); 
					break;

				case 3: //SALIR DEL PROGRAMA
					System.exit(0); 
					break;

				default:
					break;

			}//SWITCH

		}while(true); //Loop infinito, ya que para salir del prgorama se necesita la opcion
		

	}//MAIN


	// FUNCTIONS ------------------------------------------------------------------------------------------------


	//UI FUNCTIONS		*******************************************

		/*
			Imprime el Menu de opciones una vez que el usuario haga
			LogIn
		*/
		public static void menu(){

			System.out.println("\t 1.- Canciones");
			System.out.println("\t 2.- Albums");
			System.out.println("\t 3.- Playlist");
			System.out.println("\t 4.- Settings");	
			System.out.println("\t 5.- Log-Out");
			System.out.println("\t [Ingresa el numero de la opcion deseada]");
		}//MENU

		/*
			Funcion de LogIn. El usuario ingresa un potencial usuario y contraseña.
			La funcion busca que esos dos inputs pertenezcan a algun usuario.
			Si pertenecen a algun usuario, la funcion regresa verdadero, de lo 
			contrario regresa falso. De igual forma, el usuario que matchea los
			inputs es igualado a currentUser.
		*/
		public static boolean logIn(){

			System.out.println("\t //////// LOG-IN ////////");
			boolean log = true;
			int indexUser = 0;

			System.out.println("\t Ingresa el usuario:");
			String logUser = input.next(); 

			System.out.println("\t Ingresa la contraseña: [Case sensitive]");
			String logPass = input.next();

			//Loop donde busca que los inputs pertenezcan al username y pass de algun usuario
			//en el A.L. de users (userList)
			for(int i = 0; i<usersList.size(); i++) { 
				if(logUser.equalsIgnoreCase(usersList.get(i).getUser()) &&  logPass.equalsIgnoreCase(usersList.get(i).getPass())) {
					log = true;
					indexUser = i; //Se guarda el index donde se encuentra el usuario que matcheo los inputs
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

		/*
			Imprime la libreria de canciones.
		*/

		public static void canciones(){

			System.out.println("\t ///////// TU LIBRERIA DE CANCIONES: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			//For que imprime nombre y artista de todas las canciones
			//en la libreria de canciones (songLib)
			for(int i = 0; i<songLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + songLib.get(i).getName() + "\t\t" + songLib.get(i).getArtist());
			}

			System.out.println("\t ---------------------------------");
			System.out.println("\t    ***  0.- Añadir Cancion *** ");
			System.out.println("\t    *** -1.- Regresar ***");

		}//CANCIONES

		/*
			Imprime la libreria de albums.
		*/

		public static void albums(){

			System.out.println("\t ///////// TU LIBRERIA DE ALBUMS: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			//For que imprime nombre y artista de todos los Albums
			//en la libreria de albums (albumLib)
			for(int i = 0; i<albumLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + albumLib.get(i).getName() + "\t" + albumLib.get(i).getArtist());
			}

			System.out.println("\t -----------------------------------");
			System.out.println("\t      ***  0.- Añadir Album *** ");
			System.out.println("\t      *** -1.- Regresar ***");
		
		}//ALBUMS

		/*
			Imprime la libreria de playlists.
		*/

		public static void plists(){

			System.out.println("\t ///////// TU LIBRERIA DE PLAYLISTS: /////////");
			System.out.println("\t [Escribe el numero de tu seleccion]");

			//For que imprime nombre y artista de todas las playlists
			//en la libreria de playlist (plistLib)
			for(int i = 0; i<plistLib.size(); i++) {
				System.out.println("\t "+ (i+1) + ".- " + plistLib.get(i).getName() + "\t" + plistLib.get(i).getDesc());
			}

			System.out.println("\t -----------------------------------");
			System.out.println("\t      ***  0.- Crear Playlist *** ");
			System.out.println("\t      *** -1.- Regresar ***");
		
		}//PLAYLISTS

		/*
			Imprime las opciones de settings
		*/

		public static void settings(){
			System.out.println("\t [Ingresa el numero de la opcion deseada]");
			System.out.println("\t 1.- Editar nombre ");
			System.out.println("\t 2.- Editar edad ");
			System.out.println("\t 3.- Cambiar contraseña ");
			System.out.println("\t 4.- Cambiar tipo de subscripcion ");
			System.out.println("\t 0.- Regresar ");
		}

	//USER FUNCTIONS	*******************************************

		/*
			Funcion que crea un nuevo usuario. Pide todos los parametros necesarios,
			crea una nueva instancia de Usuario y la añade a usersList.
		*/

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
			user = new Usuario(n,a,u,p,s); //Crea instancia de Usuario
			user.write(); //Llama a write
			usersList.add(user); //La añade a usersList

		}//CREATE_USER

		/*
			Funcion que lee el archivo users.csv y guarda cada usuario
			en usersList
		*/

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

					user = new Usuario(name,age,usr,pass,sub); //Crea instancia de Usuario a partir de lo escrito en el archivo
					usersList.add(user); //Lo añade a usersList

				}

				}catch(IOException e){e.printStackTrace();}

		}//READ

		public static void writeOutUser(){
			try{
	            bw = new BufferedWriter(new FileWriter("users.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 
		
		} //WRITEOUT SONG LIB

		public static void writeUser(){

				for(int i = 0; i<usersList.size(); i++) {
					usersList.get(i).update();
				}

		}

	//SONG FUNCTIONS	*******************************************

		/*
			Funcion que lee el archivo songLib.csv, crea instancias de Cancion
			y las guarda en songLib (ArrayList)
		*/

		public static void readSongLib(){

			String[] sL = new String[5];
			boolean loop = true;
			songLib.clear();	

			try{
				
				//Para leer albumLib.csv
				br = new BufferedReader(new FileReader(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv"));
				String read;
				
				while((read = br.readLine() ) != null){ //Mientras la siguiente linea no sea null(vacia)
				
					sL = read.split(",");
					loop = true;

					String name = sL[0];
					String artist = sL[1];
					String album = sL[2];
					int year = Integer.parseInt(sL[3]);
					int runtime = Integer.parseInt(sL[4]);

					song = new Cancion(name, artist, album, year, runtime); //Crea instancia de Cancion
					songLib.add(song); //La guarda en songLib
				}

			}catch(IOException e){e.printStackTrace();}	

		}//READ_SONG_LIBRARY

		/*
			Funcion que crea una cancion. Pide los parametros para una cancion
			crea la instancia de Cancion y la guarda en songLib.
		*/

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

			song = new Cancion(nombreCancion, artistaCancion, n_albumCancion, yearCancion, rtCancion); //Crea instancia de Cancion
			songLib.add(song);//Añade la cancion a songLib
			//Actualiza el archivo songLib.csv
			writeOUTSongLib();
			writeSongLib();
		
		}//CREATE_SONG

		/*
			Escribe en el archivo songLib.csv todas las canciones que estan en 
			songLib (ArrayList)
		*/

		public static void writeSongLib(){

			//Si songLib está vacio no guarda nada ( --> "")
			if(songLib.size() == 0) {
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

			}else{ //Si songLib tiene algo
				//Para cada cancion en songLib, llama la funcion wr
				for(int i = 0; i<songLib.size(); i++) {
				songLib.get(i).write(currentUser);
				}
			}
		
		} //WRITE SONG LIB

		/*
			Vacia el archivo songLib.csv
		*/

		public static void writeOUTSongLib(){
			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "songLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 
		
		} //WRITEOUT SONG LIB

		/*
			Remueve los duplicados que puede haber en la librearia de 
			canciones
		*/

		public static void removeDuplicates(){

			/*
				La funcion compara cada index de songLib con los demás
				si es igual (Nombre y Artista), se elimina.
			*/

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

		/*
			Funcion que lee albumLib.csv y los archivos individuales de
			cada album.
		*/

		public static void readAlbumLib(){

			String[] sL = new String[5]; //Canciones de archivos individuales
			String[] aL = new String[5]; //Albums 
			boolean loop = true;
			albumLib.clear();	

			try{
				

				br = new BufferedReader(new FileReader(currentUser.getUser().toLowerCase() + "_Library/" + "albumLib.csv"));
				String read;
				
				//Lee el archivo linea por linea hasta que la siguiente linea este vacía
				while((read = br.readLine() ) != null){ 
				
					aL = read.split(",");
					loop = true;

					String name = aL[0]; //guarda el nombre del album
					String artist = aL[1];// guarda el archivo del album

						//Lee el archivo individual del album, donde se encuentran las canciones del album
						br2 = new BufferedReader(new FileReader(user.getUser().toLowerCase() + "_Library/Albums/" +  name +".csv"));

						String read2;
						albumList = new ArrayList<Cancion>(); //Crea nuevo A.L. para el Song List del album

						//Lee el archivo linea por linea hasta que la siguiente linea este vacía
						while((read2 = br2.readLine() ) != null){
				
							sL = read2.split(",");
							loop = true;

							String nameSong = sL[0];
							String artistSong = sL[1];
							String albumSong = sL[2];
							int yearSong = Integer.parseInt(sL[3]);
							int runtimeSong = Integer.parseInt(sL[4]);

							song = new Cancion(nameSong, artistSong, albumSong, yearSong, runtimeSong); //Crea instancia de Cancion
							albumList.add(song);//La añade a albumList
						
						}	

					album = new Album(name, artist, albumList); //Crea instancia de Album
					albumLib.add(album); //Lo añade a la libreariade albums
					
				}		

			}catch(IOException e){e.printStackTrace();}	

		}//READ_SONG_LIBRARY
		
		/*
			Funcion para crear album. Pide parametros de Album, crea instancia de Album. 
			Las canciones creadas en el album tambien son añadidas a la libreria de canciones.
		*/
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

				song = new Cancion(nombreCancion, artistaAlbum, nombreAlbum, yearCancion, rtCancion);//Crea instancia de Cancion
				albumList.add(song); //La añade a albumList
				songLib.add(song); //Añade la cancion a la librearia de canciones

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

			album = new Album(nombreAlbum, artistaAlbum, albumList); //Crea instancia de Album
			album.write(currentUser); //Llama a funcion Write
			albumLib.add(album); //Añade el album a la libreria de Albums
			
		}//CREATE ALBUM
		
		/*
			Funcion para editar album. Cuando lo edita, elimina el archivo individual de 
			canciones del album y crea uno nuevo con las canciones editadas. (Esto debido
			a que no se pueden editar lineas específicas en java)
		*/

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

			albumLib.get(index).deleteFile(currentUser); //Borra archivo individual de canciones
			albumLib.get(index).editData(nombreAlbum,artistaAlbum); //Edita los parametros de Album. No necesita parametro de songList por el paso por referencia.
			albumLib.get(index).updateFile(currentUser); //Escribe el archivo individual con las nuevas canciones

		}//EDIT ALBUM

		/*
			Vacia el archivo albumLib.csv
		*/

		public static void writeOUTAlbumLib(){

			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "albumLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

		}//WRITE OUT ALBUM LIB


		/*
			Escribe los albums en albumLib (ArrayList) en el archivo albumLib.csv
		*/

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

		/*
			Cuando un album es eliminado, sus canciones tambien. Es por esto
			que esta funcion elimina las canciones que estaban en el album, de 
			la libreria de canciones
		*/

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

		/*
			Lee el archivo plistLib.csv, al igual que su archivo individual de 
			canciones. Despues, crea instancias de playlist y las guarda en el 
			ArrayList plistLib.
		*/

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

		/*
			Funcion que pide los parametros de una playlist para la creacion de
			la misma.
		*/

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

		/*
			Funcion para editar una playlist. Cuando la edita, elimina el archivo individual de 
			canciones de ella y crea uno nuevo con las canciones editadas. (Esto debido
			a que no se pueden editar lineas específicas en java)
		*/

		public static void editPlaylist(int index){

			boolean seguir = true;

			System.out.println("\t EDITAR PLAYLIST");
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

		/*
			Vacia el archivo plistLib.csv
		*/

		public static void writeOutPlaylistLib(){

			try{
	            bw = new BufferedWriter(new FileWriter(currentUser.getUser().toLowerCase() + "_Library/" + "plistLib.csv")); 
	            bw.write("");
	            bw.flush();
	            bw.close();
	        }catch(IOException e){e.printStackTrace();} 

		}//WRITE OUT ALBUM LIB

		/*
			Escribe las playlists en plistLib (ArrayList) en el archivo plistLib.csv
		*/

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