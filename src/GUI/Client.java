package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Project.Rule;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.BorderLayout;

public class Client   {
	public JFrame frame;
	private static Socket socket = null;
	public static PrintStream outputStream;
	private static BufferedReader inputStream;
	public static Client window;
	public static Login login;
	public static Register register;
	public static InsertPolicy insertPolicy;
	public static boolean wait = true;
	private Image image = null;
	public static JButton exitBtn;
	public static File file = new File("test.txt");

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, InterruptedException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					window =  new Client();
					window.frame.setSize(681, 554);
					window.frame.setVisible(true);


				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});

		connect();


		String line = "";
		try {
			line = inputStream.readLine();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}

		while (!line.equals("goodbye")) {
			switch (line) {
			case "DONE":
				window.frame.setVisible(true);
			
				JOptionPane.showMessageDialog(window.frame, "Registration successful. " +"\n"+ "Please Press Login","Information",JOptionPane.INFORMATION_MESSAGE);
				break;
			case "exists":
				window.frame.setVisible(true);
				JOptionPane.showMessageDialog(window.frame, "The User Already Exists." +"\n"+ "Please Press Login","Error",JOptionPane.ERROR_MESSAGE);
				break;
			case "verified":
				JOptionPane.showMessageDialog(window.frame,"Login successful :)","Information",JOptionPane.INFORMATION_MESSAGE);
				login.dispose();
				window.frame.setVisible(false);
				String name = inputStream.readLine();
				ArrayList<Rule> policy = new ArrayList<Rule>();
				ObjectMapper mapper = new ObjectMapper();
				try {
					line = inputStream.readLine();
					while (!line.equals("finished")) {
						policy.add(mapper.readValue(line, Rule.class));
						line = inputStream.readLine();
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}				
				insertPolicy = new InsertPolicy(name, policy);
				insertPolicy.setVisible(true);
				outputStream.println("insert");
				InsertPolicy.outputStream = Client.outputStream;
				break;
			case "notVerified":
				JOptionPane.showMessageDialog(null, "The password or username is incorrect","Error",JOptionPane.ERROR_MESSAGE);
				outputStream.println("Login");
				break;
			case "wait":
				while(wait) {}
				break;
			}
			line = inputStream.readLine();
		}
		
		outputStream.println("Results");
		line = inputStream.readLine();
		while (!line.equals("End")) {
			InsertPolicy.jsonPolicy.add(line);
			line = inputStream.readLine();
		}
		outputStream.println("goodbye");
		System.out.println("Client disconnected.");

	}

		public  Client() throws IOException, HeadlessException, URISyntaxException {
		initialize();
	}

	private void initialize() throws HeadlessException, IOException, URISyntaxException {


		
        frame = new JFrame();
		frame.setResizable(false);
		
		frame.setSize(681, 554);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().setForeground(Color.BLACK);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JLabel AppLabel = new JLabel("Auto Penetration Testing");
		AppLabel.setBackground(Color.CYAN);
		AppLabel.setBounds(63, 16, 550, 70);
		AppLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 40));
		AppLabel.setHorizontalAlignment(SwingConstants.CENTER);
		AppLabel.setForeground(Color.RED);
		panel.add(AppLabel);


		


		JLabel lblNewInSystyem = new JLabel("Not already registered?");
		lblNewInSystyem.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewInSystyem.setForeground(Color.WHITE);
		lblNewInSystyem.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewInSystyem.setBackground(Color.WHITE);
		lblNewInSystyem.setBounds(219, 318, 238, 25);
		panel.add(lblNewInSystyem);
		
		
		JButton LoginButton = new JButton("Login");
		LoginButton.setIcon(null);
		LoginButton.setFont(new Font("Segoe UI", Font.BOLD, 25));
		LoginButton.setForeground(Color.BLACK);
		LoginButton.setBackground(Color.CYAN);
		LoginButton.setBounds(288, 198, 128, 63);
		panel.add(LoginButton);
		
		LoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream.println("Login");
				window.frame.setVisible(false);
				login = new Login(outputStream);
				login.setVisible(true);
			}
		});
		
		
		JButton RegisterButton = new JButton("Sign up");
		RegisterButton.setFont(new Font("Segoe UI", Font.BOLD, 25));
		RegisterButton.setBackground(Color.CYAN);
		RegisterButton.setBounds(288, 359, 128, 43);
		panel.add(RegisterButton);
		
		RegisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream.println("Register");
				window.frame.setVisible(false);
				register = new Register(outputStream);
				register.setVisible(true);
			}
		});
		
	
		
		
		
		
		exitBtn = new JButton("Exit");
		exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 25));
		exitBtn.setBackground(Color.CYAN);
		exitBtn.setForeground(Color.BLACK);
		exitBtn.setBounds(41, 427, 77, 43);
		panel.add(exitBtn);
		
		
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream.println("Exit");
				JOptionPane.showMessageDialog(null, "The application will now close.");
				window.frame.dispose();
				System.exit(0);
			}
		});
		
		JLabel lblimg = new JLabel("");
		lblimg.setForeground(Color.BLACK);
		lblimg.setBounds(0, 0, 686, 530);
		lblimg.setHorizontalAlignment(SwingConstants.CENTER);
		lblimg.setVerticalAlignment(SwingConstants.TOP);
		Image img = new ImageIcon(this.getClass().getResource("/Background.jpg")).getImage();
		image = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
		lblimg.setIcon(new ImageIcon(image));
		panel.add(lblimg);
		
		frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        exitBtn.doClick();
		      }
		    });
		
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

	public static void connect(){

		try {
			socket = new Socket("192.168.14.96", 7000);//52.47.63.252
			outputStream = new PrintStream(socket.getOutputStream());
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream.println("Client");
		}
		catch (Exception e) {
			System.err.println("Failed to connect to server.");
		}

	}
}
