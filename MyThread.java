package network;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.serv;

public class MyThread extends Thread {
	String user;
	int n_msg=0;
	int number;
	private DefaultTableModel model=new DefaultTableModel();
	static Socket client ;
    public static DataOutputStream sortie;
    static DataInputStream entrer;

public MyThread(Socket s,int n){
	
	number=n-1;     //number = l'index de Socket dant las list des socket
	client =s;
}
@Override
public void run() {
	
	
	try {
			entrer=serv.l_entrer.get(number);
			sortie=serv.l_sortie.get(number);
			sortie.writeUTF("UserName :");
			while(true){
				
			if(n_msg==0){   //n_msg=0 --> le message conteint Username
				user=entrer.readUTF();
				serv.users.add(user);
				serv.table.setValueAt(user, number, 0);
				sortie=serv.l_sortie.get(number);
				entrer=serv.l_entrer.get(number);
				sortie.writeUTF("Password :");
				while(!entrer.readUTF().equals("rsd")){
					sortie=serv.l_sortie.get(number);
					sortie.writeUTF("Password :");
					entrer=serv.l_entrer.get(number);
					
				}
				sortie=serv.l_sortie.get(number);
				sortie.writeUTF("welcome you "+user);
				n_msg++;
			}
				
			entrer=serv.l_entrer.get(number);
			serv.message.append(user+" "+":" +entrer.readUTF()+ "\n");
			n_msg++;
			
		
			}
	} catch (IOException e) {
		int x=0;
		if(!serv.users.isEmpty()){  // Trouver le numéro de la line on doit supprimer x= numero de la line
		for(int j=0;j<serv.table.getRowCount();j++){
			if(serv.table.getValueAt(j, 0).equals(serv.users.get(number))){
				x=j;
			}
		}
		}
		
		model= (DefaultTableModel) serv.table.getModel();
		model.removeRow(x);
		serv.table.setModel(model);
		serv.list.remove(x);    //supprimé la Socket dont le  numeor =x
		serv.l_entrer.remove(x);  //supprimé entrer dont le numero numeor =x
		serv.l_sortie.remove(x);   //supprimé sortie dont le numero numeor =x
		if(!serv.users.isEmpty()){
		serv.users.set(number, null);  //suprimmé UserName
		}
		serv.n--;
		if(user!=null){
		JOptionPane.showMessageDialog(null,"connection with "+user+" is down ","Attention",0);
		}
		else{
			JOptionPane.showMessageDialog(null,"connection failed","Attention",0);
		}
		
	}

	
}
/*public static int getindex(DataInputStream e){
	int i=0;
	for (DataInputStream en:serv.l_entrer){
		i++;
		if (en==e){
			break;
		}
	}
	return 0;
}*/
public static Socket getsocket(String x){
	int p=0;

   while (p<serv.list.size()){   //p : index de Socket
	   if (x.equals(serv.users.get(p))){    //if(users(p)==x)
		   return serv.list.get(p);
	   }
	   p++;
   }
	
	return serv.list.get(p);       //return la socket dont l'index=p....(list(p))
}
}