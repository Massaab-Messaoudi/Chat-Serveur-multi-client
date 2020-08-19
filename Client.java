package network;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;

public class Client extends JFrame {
	static JTextArea message;
    static Socket client;
    static DataInputStream entrer;
    static DataOutputStream sortie;
	private JPanel contentPane;
	private JTextField out;
	static JPanel pa;
	static JLabel con;
	private JButton btnClose;
	static JButton send;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
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
			client=new Socket("localhost",999);
			entrer=new DataInputStream(client.getInputStream());
			sortie=new DataOutputStream(client.getOutputStream());
			send.setEnabled(true);
			String message1="";
			while(true){
				message1=entrer.readUTF();
				message.append(message1+"\n");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Server connection failed ","Attention",0);
			con.setIcon(new ImageIcon(Client.class.getResource("/network/icon/wifi2.png")));
			
		}
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 278);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		pa = new JPanel();
		pa.setBounds(10, 11, 325, 189);
		contentPane.add(pa);
	    pa.setLayout(new GridLayout(0, 1, 0, 0));
		
	    message = new JTextArea();
	    message.setEditable(false);
		message.setBounds(10, 104, 403, 96);
		JScrollPane scrollPane = new JScrollPane(message);
	    scrollPane.setBounds(10,60,780,500);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    pa.add(scrollPane);
		//contentPane.add(message);
		
		out = new JTextField();
		out.setBounds(10, 211, 325, 23);
		contentPane.add(out);
		out.setColumns(10);
		
		 send = new JButton("Send");
		send.setEnabled(false);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String ms_out="";
					ms_out=out.getText();
					sortie.writeUTF(ms_out);
					out.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"Server connection failed ","Attention",0);
				}
			}
		});
		send.setBounds(345, 211, 64, 23);
		contentPane.add(send);
		
	     con = new JLabel("");
		con.setIcon(new ImageIcon(Client.class.getResource("/network/icon/wifi 1.png")));
		con.setBounds(367, 32, 32, 40);
		contentPane.add(con);
		
		JLabel lblConnection = new JLabel("Conection");
		lblConnection.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConnection.setBounds(355, 73, 68, 14);
		contentPane.add(lblConnection);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setBounds(345, 136, 79, 23);
		contentPane.add(btnClose);
		
	}
}
