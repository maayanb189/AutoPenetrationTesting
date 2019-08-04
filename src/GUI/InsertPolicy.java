package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Project.Rule;
import Project.User;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.SystemColor;

public class InsertPolicy extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userName;
	private JLabel lblUserName;
	private JTable table;
	private ArrayList<Rule> policy;
	public static ArrayList<String> jsonPolicy;
	//private ArrayList<String> jsonPolicy;
	private Image image = null;
	public static InsertPolicy insertPolicy ;
	public static PrintStream outputStream;
	public static boolean visibleBtFile = false;
	public static User user;
	ObjectMapper mapper = new ObjectMapper();
	final static int colNumber = 5;
	public static JProgressBar progressBar = new JProgressBar(0, 100);
	private static int index = 0;
	





	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					insertPolicy.setSize(681, 554);
					insertPolicy.setVisible(true);
					insertPolicy.setResizable(false);
					insertPolicy.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}



	/**
	 * @wbp.parser.constructor
	 */
	public InsertPolicy(String name, ArrayList<Rule> policy) {
		this.userName = name;
		this.policy = policy;
		jsonPolicy = new ArrayList<String>();

		init();
	}


	public void init() {

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Client.wait = false;
				Client.exitBtn.doClick();
				dispose();
			}
		});
		
		
		setSize(681, 554);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.RED, 3, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar.setValue(0); 
		progressBar.setStringPainted(true);
		progressBar.setBounds(236, 469, 153, 23);
		progressBar.setVisible(false);
		contentPane.add(progressBar);
		
		JButton btnCreateFile = new JButton("Export File");
		btnCreateFile.setFont(new Font("Tahoma", Font.BOLD, 16));

		lblUserName = new JLabel();
		lblUserName.setForeground(new Color(240, 248, 255));
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblUserName.setBounds(15, 0, 234, 33);

		lblUserName.setText("Hello" +", "+ userName);
		contentPane.add(lblUserName);

		table = new JTable();
		Object[] columns = {"Number","Source","Destination","Port", "Allowed / Not Allowed"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);

		Image img = new ImageIcon(this.getClass().getResource("/Background2.jpg")).getImage();

		table.setModel(model);
		table.setBackground(Color.LIGHT_GRAY);
		table.setForeground(Color.black);
		Font font = new Font("",1,22);
		table.setFont(font);
		table.setRowHeight(30);


		Object [] singleRule = new Object[5];

		if(policy.size() != 0) {
			for(int i = 0 ;i<policy.size();i++) {
				singleRule[0] = table.getRowCount()+1;
				singleRule[1] = policy.get(i).getSource();
				singleRule[2] = policy.get(i).getDestination();
				singleRule[3] = policy.get(i).getPort();
				singleRule[4] = policy.get(i).getAllow();

				model.addRow(singleRule);
			}
		}

		DefaultTableCellRenderer centerrendar = new DefaultTableCellRenderer();
		centerrendar.setHorizontalAlignment(JLabel.CENTER);
		for(int i=0;i<table.getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerrendar); 
		}


		JTextField textSource = new JTextField();
		textSource.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField textDestination = new JTextField();
		textDestination.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField textPort = new JTextField();
		textPort.setHorizontalAlignment(SwingConstants.CENTER);
		JComboBox<String> comboBox = new JComboBox<String>();



		textSource.setBounds(15, 379, 110, 25);
		textDestination.setBounds(140, 379, 118, 25);
		textPort.setBounds(273, 379, 100, 25);


		JButton btnAdd = new JButton("Add New Rule");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// i = the index of the selected row
				int i = table.getSelectedRow();
				if(i >= 0){
					// remove a row from jtable
					policy.remove(i);
					model.removeRow(i);
				}
				else{
					JOptionPane.showMessageDialog(null, "No rule has been selected.", "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				

			}
		});
		JButton btnUpdate = new JButton("Update"); 
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));

		btnAdd.setBounds(15, 420, 130, 33);
		btnUpdate.setBounds(299, 420, 130, 33);
		btnDelete.setBounds(155, 420, 130, 33);

		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(15, 44, 644, 309);

		contentPane.setLayout(null);
		contentPane.add(pane,BorderLayout.CENTER);


		contentPane.add(textSource);
		contentPane.add(textDestination);
		contentPane.add(textPort);


		contentPane.add(btnAdd);
		contentPane.add(btnDelete);
		contentPane.add(btnUpdate);


		JLabel lblSource = new JLabel("Source");
		lblSource.setForeground(SystemColor.infoText);
		lblSource.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSource.setBounds(35, 354, 86, 20);
		contentPane.add(lblSource);

		JLabel lblDestination = new JLabel("Destination");
		lblDestination.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDestination.setForeground(SystemColor.infoText);
		lblDestination.setBounds(145, 354, 110, 20);
		contentPane.add(lblDestination);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBackground(SystemColor.controlLtHighlight);
		lblPort.setForeground(SystemColor.infoText);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPort.setBounds(278, 354, 69, 20);
		contentPane.add(lblPort);

		JLabel lblPermission = new JLabel("Allow/Not Allowed");
		lblPermission.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPermission.setForeground(SystemColor.infoText);
		lblPermission.setBounds(393, 354, 173, 20);
		contentPane.add(lblPermission);






		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnStart.setBounds(436, 420, 130, 33);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				if (policy.isEmpty())
					JOptionPane.showMessageDialog(null, "No rules have been specified", "Error", JOptionPane.WARNING_MESSAGE);
				else {
					
					String temp = "";

					//btnCreateFile.setVisible(true);
					lblSource.setVisible(false);
					lblDestination.setVisible(false);
					lblPort.setVisible(false);
					lblPermission.setVisible(false);

					textSource.setVisible(false);
					textDestination.setVisible(false);
					textPort.setVisible(false);
					comboBox.setVisible(false);

					btnAdd.setVisible(false);
					btnUpdate.setVisible(false);
					btnDelete.setVisible(false);
					btnStart.setVisible(false);

					for (int i = 0; i< policy.size(); i++) {
						try {
							temp = mapper.writeValueAsString(policy.get(i));
						} catch (JsonProcessingException e1) {
							System.out.println("listPolicy.get(i) issue");
							e1.printStackTrace();
						}
						outputStream.println(temp);
					}
					
					progressBar.setVisible(true);
				    new Thread(new Runnable() {
				      public void run() {
				        for (index = 0; index <= 100; index++) {
				          SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				              progressBar.setValue(index);
				            }
				          });

				          try {
				            java.lang.Thread.sleep(25);
				          }
				          catch(Exception e) { }
				        }
				        btnCreateFile.setVisible(true);
				      }
				    }).start();
					
					outputStream.println("goodbye");
					Client.wait = false;
				}
			}
		});
		



		comboBox.setEditable(true);
		comboBox.addItem("Allow");
		comboBox.addItem("Not Allowed");
		
		comboBox.setBounds(388, 380, 153, 25);
		comboBox.setSelectedItem(null);
		comboBox.setEditable(false);
		contentPane.add(comboBox);
		
		btnCreateFile.setBounds(15, 459, 130, 33);
		contentPane.add(btnCreateFile);
		
		btnCreateFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (.txt)", "txt");
				fc.setAcceptAllFileFilterUsed(false);
				fc.addChoosableFileFilter(filter);
				fc.setDialogTitle("Save as...");
				int result = fc.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {

					Calendar calendar = new GregorianCalendar();
					TimeZone tz = calendar.getTimeZone();
					Date now = new Date();
					DateFormat df = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");

					df.setTimeZone(tz);
					String currentTime = df.format(now);
					File file = new File(fc.getSelectedFile()+".txt");
					try {


						FileWriter fw = new FileWriter(file);
						PrintWriter writer = new PrintWriter(fw);


						//writer.write("---------- Report Log   " + currentTime + " ----------");
						writer.println("---------- Report Log   " + currentTime + " ----------");
						if(jsonPolicy.isEmpty()) {
							//writer.write("No rules received by the agents.");
							//writer.println();
							writer.println("No rules received by the agents.");
						}
						
						for (int i = 0; i < jsonPolicy.size(); i++) {
							//writer.write(jsonPolicy.get(i));
							writer.println(jsonPolicy.get(i));

						}
						writer.write("-------------- End --------------");
						writer.flush();
						writer.close();

					}catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "File Creation Error", "Error", JOptionPane.WARNING_MESSAGE);
					}

				}
			}
		});


		


		JLabel lblimg = new JLabel("");
		lblimg.setForeground(Color.BLACK);
		lblimg.setBounds(-6, 0, 1000, 700);
		lblimg.setHorizontalAlignment(SwingConstants.CENTER);
		lblimg.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblimg);
		image = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
		lblimg.setIcon(new ImageIcon(image));
		btnCreateFile.setVisible(false);

		if(visibleBtFile == true ) {
			btnCreateFile.setVisible(true);
		}


		Object [] row = new Object[5];

		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean empty = false;
				boolean portError = false;
				String temp;

				row[0] = table.getRowCount() + 1;
				row[1] = textSource.getText();
				row[2] = textDestination.getText();
				row[3] = textPort.getText();
				row[4] = comboBox.getSelectedItem();

				for (int i = 1; i < table.getColumnCount() - 1; i++) {
					temp = (String) row[i];
					if (temp.length() < 1)
						empty = true;
				}
				
				temp = textPort.getText();
				if (checkPort(temp) == false) {
					textPort.setText("");
					portError = true;
				}
				
				if (comboBox.getSelectedIndex() == -1)
					empty = true; 

				if (empty)
					JOptionPane.showMessageDialog(null, "Please make sure all required fields are filled.", "Error", JOptionPane.WARNING_MESSAGE);
				else if (portError)
					JOptionPane.showMessageDialog(null, "Port must contain numbers only", "Error", JOptionPane.WARNING_MESSAGE);
				else {
					// add row to the model
					model.addRow(row);

					textSource.setText("");
					textDestination.setText("");
					textPort.setText("");
					comboBox.setSelectedIndex(-1);
					
					policy.add(toRuleObject(getRowAt(table.getRowCount() - 1)));
				}
			}
		});

		table.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e){

				// i = the index of the selected row
				int i = table.getSelectedRow();


				textSource.setText(model.getValueAt(i, 1).toString());
				textDestination.setText(model.getValueAt(i, 2).toString());
				textPort.setText(model.getValueAt(i, 3).toString());
				comboBox.setSelectedItem(model.getValueAt(i, 4).toString());

			}
		});

		btnUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				// i = the index of the selected row
				int i = table.getSelectedRow();

				if(i >= 0){
					
					model.setValueAt(textSource.getText(), i, 1);
					model.setValueAt(textDestination.getText(), i, 2);
					model.setValueAt(textPort.getText(), i, 3);
					model.setValueAt(comboBox.getSelectedItem(), i, 4);
					
					String port = textPort.getText();
					if (checkPort(port) == false) {
						JOptionPane.showMessageDialog(null, "Port must contain numbers only", "Error", JOptionPane.WARNING_MESSAGE);
					}
					else {
					Rule temp = toRuleObject(getRowAt(i));
					policy.add(i, temp);
					System.out.println(temp);
					policy.remove(i + 1);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "No rule has been selected.", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	//get the rule as a row
	public String[] getRowAt(int row) {

		String[] result = new String[colNumber];

		for(int col = 1; col < colNumber; col++) {
			result[col] = table.getModel().getValueAt(row, col).toString();
		}

		return result;

	}

	public Rule toRuleObject(String [] rule) {
		int port = Integer.parseInt(rule[3]);
		return new Rule(rule[1], rule[2], port, rule[4]);
	}
	
	public boolean checkPort(String port) {
		boolean result = false;
		int tester;
		try {
			tester = Integer.parseInt(port);
			result = true;
		}
		catch (NumberFormatException e){
			System.out.println("parseInt failed.");
		}
		return result;
	}
	
}
