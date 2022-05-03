package chatapplication;

import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class server extends JFrame{
	// constructor
	ServerSocket Server;
	Socket socket;

	BufferedReader br;
	PrintWriter out;
	
	// declare components
	private JLabel heading = new JLabel("Server Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN, 20);

	private void createGUI() {
		this.setTitle("Server Messager[END]");
		this.setSize(600, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// coding for components
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("D:\\chat\\chatapplication\\src\\chatapplication\\logo.jpg"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);

		// setting the layout of frame
		this.setLayout(new BorderLayout());

		// adding the components to frame
		this.add(heading, BorderLayout.NORTH);
		JScrollPane jscrollpane = new JScrollPane(messageArea);
		this.add(jscrollpane, BorderLayout.CENTER);
		this.add(messageInput, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("key released: "+e.getKeyCode());
				if (e.getKeyCode() == 10) {
					// System.out.println("enter button");
					String contentToSend = messageInput.getText();
					messageArea.append("Me: " + contentToSend + "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}

		});
	}

	//constructor
	public server() {
		try {
			Server = new ServerSocket(7770);
			System.out.println("Server is ready to accept connection.");
			System.out.println("waiting..");
			socket = Server.accept();

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			createGUI();
			handleEvents();
			startReading();
			//startWriting();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startReading() {
		// thread will read the data
		Runnable r1 = () -> {
			System.out.println("Reader started...");
			try {
				while (true) {
					String msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Client terminated the chat.");
						JOptionPane.showMessageDialog(this, "Client Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
					//System.out.println("Client : " + msg);
					messageArea.append("Client : " + msg + "\n");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Connection closed");
			}
		};
		new Thread(r1).start();
	}

	public void startWriting() {
		// thread will take the data from user and send to client
		Runnable r2 = () -> {
			System.out.println("Writer started...");
			try {
				while (!socket.isClosed()) {

					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush();
					if (content.equals("exit")) {
						socket.close();
						break;
					}
				}
				
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Connection closed");

			}
		};
		new Thread(r2).start();
	}

	public static void main(String[] args) {
		System.out.println("This is server...going to sart server");
		new server();
	}
}
