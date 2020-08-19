package network;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;

public class serv extends JFrame {
	static LinkedList<Socket> list=new LinkedList<Socket>();
	static LinkedList<DataInputStream> l_entrer=new LinkedList<DataInputStream>();
	static LinkedList<DataOutputStream> l_sortie=new LinkedList<DataOutputStream>();
	static LinkedList<String> users=new LinkedList<String>();
	static JPanel pa;
	static DefaultTableModel model=new DefaultTableModel();
	static Object [] row =new Object[1];
	static String name;
	static boolean select=false;
	static int n=0;
	static JTextArea message;
    static ServerSocket serveur;
    static Socket client ;
    static DataInputStream entrer;
    static DataOutputStream sortie;
	private JPanel contentPane;
	private JTextField out;
	public static JTable table;
	private JButton btnClose;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					serv frame = new serv();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			try {
				Thread.sleep(500);  //pour eviter "Null Pointer Exception on append" :message.append(text) avant la creation de JTexerea "message"
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			serveur =new ServerSocket(999);
			while(true){
			client =serveur.accept();
			entrer=new DataInputStream(client.getInputStream());
			sortie=new DataOutputStream(client.getOutputStream());
				n++;
				l_entrer.add(entrer);
				l_sortie.add(sortie);
				list.add(client);
				model= (DefaultTableModel) table.getModel();
				row[0]= "connection request ....";
				model.addRow(row);
				table.setModel(model);
				
				
			MyThread service=new MyThread(client,n);
			service.start();		
		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

	/**
	 * Create the frame.
	 */
	public serv() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 457, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//pa.add(sp2);
		out = new JTextField();
		out.setBounds(10, 214, 414, 25);
		contentPane.add(out);
		out.setColumns(10);
		
		JButton send = new JButton("send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			if(table.getRowCount()==0){
				JOptionPane.showMessageDialog(null,"There is no client","Attention",0);
			}
			else{
				try {
					if (select==false){   //Le client n'a pas selectionner
						JOptionPane.showMessageDialog(null,"you need to select the client first","Attention",0);
					}
					else{ // Le client est deja selectionner
					client=MyThread.getsocket(name);   //sélectionner la socket de le client selectionner
					sortie=new DataOutputStream(client.getOutputStream());
					String ms_out="";
					ms_out=out.getText();
					sortie.writeUTF("Server: "+ms_out);
					out.setText("");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		});
		send.setBounds(41, 243, 75, 23);
		contentPane.add(send);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(300, 11, 124, 192);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		
		JScrollPane sp = new JScrollPane();
		panel.add(sp);
		sp.setViewportView(table);
		
		table = new JTable(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Client"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setBackground(Color.WHITE);
			sp.setColumnHeaderView(table);
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(false);
			sp.setViewportView(table);
			
			 pa = new JPanel();
			pa.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pa.setBounds(10, 11, 280, 192);
			contentPane.add(pa);
			pa.setLayout(new GridLayout(1, 0, 0, 0));
			
	    message = new JTextArea();
	    message.setEditable(false);
	    message.setBounds(21, 155, 185, 28);
	    
	    JScrollPane scrollPane = new JScrollPane(message);
	    scrollPane.setBounds(10,60,780,500);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    pa.add(scrollPane);
	    
	    JButton btnClean = new JButton("Clean");
	    btnClean.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		message.setText("");
	    	}
	    });
	    btnClean.setBounds(184, 243, 75, 23);
	    contentPane.add(btnClean);
	    
	    btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    btnClose.setBounds(321, 243, 75, 23);
	    contentPane.add(btnClose);
	    
	    //contentPane.add(message);
		table.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    		    if (e.getClickCount() !=0) {
	    		      JTable target = (JTable)e.getSource();
	    		      int row = target.getSelectedRow();
	    		      //int col = target.getSelectedColumn();
	    		    if(!table.getValueAt(row, 0).toString().equals("connection request ....")){  
	    		   	name=(table.getValueAt(row, 0).toString());
	    		   	select=true;
	    		    }
	    		    else{
	    		    	select=false;	
	    		    }
	    		    }
	    		  }
	});
	}
}