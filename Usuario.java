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
	public void setName(String name){
		this.name=name;
	}
	public void setAge(int age){
		this.age=age;
	}
	public void setUserName(String username){
		this.username=username;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setSub(boolean sub){
		this.sub=sub;
	}
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

		f = new File(username.toLowerCase() + "_Library/songLib.csv");
		f.getParentFile().mkdir();

		try{
            f.createNewFile();
        }catch(IOException e){}

		f = new File(username.toLowerCase() + "_Library/plistLib.csv");
		f.getParentFile().mkdir();

        try{
            f.createNewFile();
        }catch(IOException e){}

        f = new File(username.toLowerCase() + "_Library/albumLib.csv");
		f.getParentFile().mkdir();

        try{
            f.createNewFile();
        }catch(IOException e){}

	}

}//Class Usuario