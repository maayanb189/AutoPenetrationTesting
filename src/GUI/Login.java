package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField UserNametextField;
	private JPasswordField PasswordtextField;
	public static PrintStream outputStream;
	public static Login loginWindow ;
	private Image image = null;
	private String userName;
	private String password;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					loginWindow.setSize(681, 554);
					loginWindow.setVisible(true);
					loginWindow.setResizable(false);
					loginWindow.setLocationRelativeTo(null);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login(PrintStream outputStream) {
		
		Login.outputStream = outputStream;
		init();
	
	}
	
	public void init() {
		
		addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        Client.exitBtn.doClick();
		        dispose();
		      }
		    });

		setSize(681, 554);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JLabel lbUserName = new JLabel("User Name");
		lbUserName.setFont(new Font("Segoe UI", Font.BOLD, 25));
		lbUserName.setBounds(139, 131, 159, 30);
		contentPane.add(lbUserName);

		JLabel lbpassword = new JLabel("Password");
		lbpassword.setFont(new Font("Segoe UI", Font.BOLD, 25));
		lbpassword.setBounds(139, 177, 113, 20);
		contentPane.add(lbpassword);

		UserNametextField = new JTextField();
		UserNametextField.setBounds(297, 134, 159, 32);
		contentPane.add(UserNametextField);
		UserNametextField.setColumns(10);


		PasswordtextField = new JPasswordField();
		PasswordtextField.setBounds(297, 182, 159, 32);
		contentPane.add(PasswordtextField);

		JButton Back = new JButton("Back");
		Back.setForeground(SystemColor.controlShadow);
		Back.setBackground(new Color(0, 0, 0));
		Back.setBounds(40, 424, 132, 43);
		Back.setFont(new Font("Segoe UI", Font.BOLD, 16));
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream.println("back");
				Client.login.dispose();
				Client.window.frame.setVisible(true);
				
			}
		});
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(SystemColor.activeCaption);
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 25));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				userName = UserNametextField.getText();
				password = String.valueOf(PasswordtextField.getPassword());
				

				String details =  userName+","+password;
				

				if (userName.length() < 1 || password.length() < 1) 
					JOptionPane.showMessageDialog(null, "Please enter user name and password", "Error", JOptionPane.WARNING_MESSAGE);
				else {
					outputStream.println(details);
				}
				
				UserNametextField.setText("");
				PasswordtextField.setText("");
			}
		});
		
				btnLogin.setBounds(253, 288, 140, 54);
				contentPane.add(btnLogin);
		
		contentPane.add(Back);
		
		
		
		JLabel lblimg = new JLabel("");
		lblimg.setForeground(Color.BLACK);
		lblimg.setBounds(0, 0, 675, 528);
		lblimg.setHorizontalAlignment(SwingConstants.CENTER);
		lblimg.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblimg);
		
		Image img = new ImageIcon(this.getClass().getResource("/Background2.jpg")).getImage();
		image = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
		lblimg.setIcon(new ImageIcon(image));
		
	}
}
