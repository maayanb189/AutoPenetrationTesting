package GUI;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Project.Rule;
import Project.User;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.SystemColor;

public class Register extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldOrganization;
	private JTextField textFieldPassword;
	public static PrintStream outputStream;
	public static Register register;
	public static  User user ;


	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					register.setSize(692, 569);
					register.setVisible(true);
					register.setResizable(false);
					register.setLocationRelativeTo(null);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Register(PrintStream outputStream) {

		Register.outputStream = outputStream;
		initialize();
	}

	private void initialize() {

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Client.exitBtn.doClick();
				dispose();
			}
		});

		
		setSize(681, 554);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);





		textFieldName = new JTextField();
		textFieldName.setBounds(315, 155, 192, 38);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);

		textFieldOrganization = new JTextField();
		textFieldOrganization.setBounds(315, 209, 192, 38);
		contentPane.add(textFieldOrganization);
		textFieldOrganization.setColumns(10);

		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(315, 263, 192, 38);
		contentPane.add(textFieldPassword);
		textFieldPassword.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setForeground(Color.BLACK);
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblName.setBounds(108, 155, 90, 41);
		contentPane.add(lblName);

		JLabel lblOrganization = new JLabel("Organization");
		lblOrganization.setForeground(Color.BLACK);
		lblOrganization.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblOrganization.setBounds(108, 202, 192, 38);
		contentPane.add(lblOrganization);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblPassword.setBounds(108, 256, 178, 38);
		contentPane.add(lblPassword);

		JButton Back = new JButton("Back");
		Back.setForeground(SystemColor.controlShadow);
		Back.setBackground(new Color(0, 0, 0));
		Back.setBounds(28, 435, 132, 43);
		Back.setFont(new Font("Segoe UI", Font.BOLD, 16));
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream.println("back");
				Client.register.dispose();
				Client.window.frame.setVisible(true);

			}
		});
		
		contentPane.add(Back);
		
	
		Image img = new ImageIcon(this.getClass().getResource("/Background2.jpg")).getImage();
		
				JButton btnSubmit = new JButton("Submit");
				btnSubmit.setBackground(SystemColor.activeCaption);
				btnSubmit.setForeground(Color.BLACK);
				btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 25));
				btnSubmit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						user = new User(textFieldName.getText(), textFieldOrganization.getText(), textFieldPassword.getText(), new ArrayList<Rule>());
						String jsonUser = "";
						ObjectMapper mapper = new ObjectMapper();
						try {
							jsonUser = mapper.writeValueAsString(user);
						} catch (JsonProcessingException e1) {
							e1.printStackTrace();
						}
						outputStream.println(jsonUser);
						dispose();
					}
				});
				
						btnSubmit.setBounds(339, 338, 133, 72);
						contentPane.add(btnSubmit);
		JLabel lblimg = new JLabel("");
		lblimg.setForeground(Color.BLACK);
		lblimg.setBounds(0, 0, 689, 527);
		
		lblimg.setHorizontalAlignment(SwingConstants.CENTER);
		lblimg.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblimg);
		image = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
		lblimg.setIcon(new ImageIcon(image));


	}
}
