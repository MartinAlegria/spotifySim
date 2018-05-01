import java.util.*;
import java.io.*;

public class Usuario{

	private String name;	//Nombre del Usuario
	private int age;		//Edad del Usuario
	private String username;//Username del Usuario
	private String password;//Password del Usuario
	private boolean sub;	//Tipo de suscripción del usuario (true = premium, false = gratis)

	static BufferedWriter bw;
    static BufferedReader br;
    static File f;

	//Constructor 
	Usuario(String name, int age, String username, String password, boolean sub){
		this.name = name;
		this.age = age;
		this.username = username;
		this.password = password;
		this.sub = sub;
	}

	public String getName(){return name;}
	public String getUser(){return username;}
	public String getPass(){return password;}

	public String print(){
		String a;
		if(sub) {
			a = "Premium";
		}else{
			a = "Gratis";
		}
		return "\n\t " + name + "\n\t " + age + "\n\t " + username + "\n\t "+ a;
	}

	public void write(){

		try{
		bw = new BufferedWriter(new FileWriter("users.csv",true)); 
		bw.write(name + "," + age + "," + username + "," + password + "," + sub);
		bw.newLine();
		bw.flush();
		bw.close();
		}catch(IOException e){e.printStackTrace();}	

		f = new File(username.toLowerCase() + "_Library");
		f.mkdir();

	}

}//Class Usuario