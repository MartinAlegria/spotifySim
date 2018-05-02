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
						
							boolean logOut = false;
							do{
								menu();
								int eleccion = input.nextInt();

								switch(eleccion){
									case 1: //CANCIONES

										canciones();

											int cancionesDec = input.nextInt();
											input.nextLine();

											switch(cancionesDec){
												
												case 0:
													createSong();
													break;

											}

										break;

									case 2: //ALBUMS
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
		song.write(currentUser);

	}//CREATE_SONG


}//Class MainSpotify

//System.out.println("\t");