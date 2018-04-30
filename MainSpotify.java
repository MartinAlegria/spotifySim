import java.util.*;
import javax.swing.*;
import java.io.*;

public class MainSpotify{

	//Variables
	static Scanner input = new Scanner(System.in);
	static ArrayList<Cancion> recent = new ArrayList<Cancion>();
	static ArrayList<Cancion> album = new ArrayList<Cancion>();
	static ArrayList<Cancion> plist = new ArrayList<Cancion>();
	static ArrayList<Usuario> users = new ArrayList<Usuario>();
	static Usuario user;
    static BufferedReader br;
	static File f = new File("users.csv");

	public static void main(String[] args) {

		System.out.println("\t --------- SPOTIFY ---------");
		System.out.println("\t Bienvenido!");

		if(f.length() == 0) {
			System.out.println("\t No tienes ningun usuario creado");
			createUser();
			System.out.println(user.print());
		}

		else{

			do{

			System.out.println("\t 1.- Log-In");
			System.out.println("\t 2.- Crear Cuenta");
			System.out.println("\t 3.- Salir");		
			System.out.println("\t Ingrea el número de la opcion deseada");
			int launch = input.nextInt();

			switch(launch){
				case 1: 
					read();
					break;

				case 2:
					createUser();
					menu();
					break;

				case 3:
					System.exit(0);
					break;

				default:
					break;
			}//SWITCH

			}while(true);
			
		}//ELSE

	}//main

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
		users.add(user);
	}

	public static void menu(){
		System.out.println("\t 1.- Canciones");
		System.out.println("\t 2.- Albums");
		System.out.println("\t 3.- Playlist");
		System.out.println("\t 4.- Log-Out");
	}

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
				String user = u[2];
				String pass = u[3];
				boolean sub = Boolean.valueOf(u[4]);

				users.add(new Usuario(name,age,user,pass,sub));

			}

			}catch(IOException e){e.printStackTrace();}
	}

}//Class MainSpotify

//System.out.println("\t");