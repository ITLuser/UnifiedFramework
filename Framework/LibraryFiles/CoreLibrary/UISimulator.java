package LibraryFiles.CoreLibrary;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTextPane;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
import com.gargoylesoftware.htmlunit.TextPage;

public class UISimulator extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_Summary;
	private JComboBox comboBox = new JComboBox();
	String current = System.getProperty("user.dir");
	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UISimulator() {
		String tempDataSetPath = current.concat("\\Framework\\DataSets");
		setTitle("Unified Framework");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblExecutionDetails = new JLabel("Execution Details");
		lblExecutionDetails.setFont(new Font("Calibri", Font.BOLD, 13));
		lblExecutionDetails.setBounds(10, 24, 574, 14);
		lblExecutionDetails.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblExecutionDetails);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				try {
					if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("Excel")){
						MapGenerator.excelTestDataPath = current.concat("\\Framework\\DataSets\\").concat((String) comboBox.getSelectedItem());
					}else if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("CouchDB")){
						MapGenerator.couchdbTestDatabase = (String) comboBox.getSelectedItem();
					}
					String warning = MapGenerator.mapForTCID();
					if (warning != null){
						JOptionPane
						.showMessageDialog(
								contentPane,
										warning,
								"Error", JOptionPane.WARNING_MESSAGE);						
					}
					else{
						Reporter.summaryDescription = textField_Summary.getText();
						String testScripts = textField_1.getText();
						MapGenerator.testExecutionList.clear();
						MapGenerator.GenerateTestExecutionList(testScripts);
						String temp = findMissingTCID(testScripts);
						if (temp != null) {
							JOptionPane
									.showMessageDialog(
											contentPane,
											"The mentioned TCIDs "
													+ temp
													+ " is/are not available in the "+ comboBox.getSelectedItem() +" test data. \nPlease enter valid TCIDs",
											"Error", JOptionPane.WARNING_MESSAGE);
							temp = null;
						} else {
							setVisible(false);
							DriverScript.startExecution(testScripts);
							System.exit(0);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			public String findMissingTCID(String testScripts) {
				boolean isNotAvailable = true;
				String notAvailable = null;
				//String[] userEnteredList = testScripts.split("~");
				Collection<String> userEnteredSetOfValues = MapGenerator.testExecutionList.values();
				List<String> userEnteredList = new ArrayList<String>(userEnteredSetOfValues);
				Collection<String> availableSetOfValues = MapGenerator.notestcaseid.values();
				List<String> availableList = new ArrayList<String>(availableSetOfValues);

				for (int i = 0; i < userEnteredList.size(); i++) {
					for (int j = 0; j < availableList.size(); j++) {
						if (availableList.get(j).equalsIgnoreCase(userEnteredList.get(i))) {
							isNotAvailable = false;
							break;
						}
						isNotAvailable = true;
					}
					if (isNotAvailable == true) {
						if (notAvailable == null) {
							notAvailable = userEnteredList.get(i);
						} else {
							notAvailable = notAvailable + "~" + userEnteredList.get(i);
						}
					}
				}
				return notAvailable;
			}
			
		});
		btnExecute.setFont(new Font("Calibri", Font.BOLD, 13));
		btnExecute.setBounds(198, 370, 89, 23);
		contentPane.add(btnExecute);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});
		btnCancel.setFont(new Font("Calibri", Font.BOLD, 13));
		btnCancel.setBounds(314, 370, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblDataSheet = new JLabel("Data Sheet:");
		lblDataSheet.setFont(new Font("Calibri", Font.BOLD, 13));
		lblDataSheet.setBounds(66, 140, 89, 23);
		contentPane.add(lblDataSheet);
		
		
		comboBox.setFont(new Font("Calibri", Font.ITALIC, 13));
		List<String> results = new ArrayList<String>();
		if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("Excel")){
			File[] files = new File(tempDataSetPath).listFiles();
			//If this pathname does not denote a directory, then listFiles() returns null. 
			for (File file : files) {
			    if (file.isFile()) {
			    	results.add(file.getName());
			    }
			}
		}else if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("CouchDB")){
			Session testDbSession = new Session("localhost", 5984);
			//Database testDatabase = testDbSession.getDatabase("xvelasystems");
			results = testDbSession.getDatabaseNames();
	/*		ViewResults testDataViewResults = testDatabase.getAllDocuments();
			List<Document> studentDocuments = testDataViewResults.getResults();
			for(Document couchDocument: studentDocuments){
				String id = couchDocument.getJSONObject().getString("id");
				results.add(id);
			}*/
		}
		comboBox.setModel(new DefaultComboBoxModel(results.toArray()));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		comboBox.setBounds(156, 133, 345, 28);
		contentPane.add(comboBox);
		
		JLabel label = new JLabel("Test IDs:");
		label.setFont(new Font("Calibri", Font.BOLD, 13));
		label.setBounds(84, 68, 46, 14);
		contentPane.add(label);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Calibri", Font.ITALIC, 13));
		textField_1.setBounds(156, 62, 345, 28);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("1~2~3... (or) 5|10... (or) 5|10,1~2~3,15|20...");
		lblNewLabel_1.setFont(new Font("Calibri", Font.ITALIC, 12));
		lblNewLabel_1.setBounds(156, 93, 345, 14);
		contentPane.add(lblNewLabel_1);
		
		final JTextArea mailingDetails = new JTextArea();
		mailingDetails.setFont(new Font("Calibri", Font.ITALIC, 14));
		mailingDetails.setBounds(156, 185, 345, 104);
		mailingDetails.setLineWrap(true);
		mailingDetails.setEnabled(false);
		contentPane.add(mailingDetails);
		
		final JCheckBox sendMail = new JCheckBox("Send Summary as a Mail");
		sendMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sendMail.isSelected()){
					textField_Summary.setText(MapGenerator.commonData.get("Summary"));
					mailingDetails.setText(
					"TO: " + MapGenerator.commonData.get("TO") + "\n" + 
					"CC: " + MapGenerator.commonData.get("CC") + "\n" + 
					"BCC: " + MapGenerator.commonData.get("BCC") + "\n" + 
					"Host: " + MapGenerator.commonData.get("SMTP_HOST")+ "\n" + "Port: " + MapGenerator.commonData.get("SMTP_PORT"));
					DriverScript.hasToSendMail = true;
				}
				else{
					textField_Summary.setText("");
					mailingDetails.setText("");
				}
			}
		});
		//sendMail.setBounds(156, 296, 218, 23);
		sendMail.setBounds(156, 330, 218, 23);
		contentPane.add(sendMail);
		
		
		JLabel labelSummary = new JLabel("Summary:");
		labelSummary.setFont(new Font("Calibri", Font.BOLD, 13));
		//labelSummary.setBounds(70, 335, 80, 14);
		labelSummary.setBounds(70, 301, 80, 14);
		contentPane.add(labelSummary);
		
		textField_Summary = new JTextField();
		textField_Summary.setFont(new Font("Calibri", Font.ITALIC, 13));
		//textField_Summary.setBounds(156, 330, 345, 28);
		textField_Summary.setBounds(156, 296, 345, 28);
		contentPane.add(textField_Summary);
		textField_Summary.setColumns(10);
		
/*		final JCheckBox killopenedBrowsers = new JCheckBox("Kill the Browsers before Run");
		killopenedBrowsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (killopenedBrowsers.isSelected()){
					ScriptRunner.killBrowsersBeforeRun = true;
				}
				else{
					ScriptRunner.killBrowsersBeforeRun = false;
				}
			}
		});
		//sendMail.setBounds(156, 296, 218, 23);
		killopenedBrowsers.setBounds(156, 360, 200, 23);
		contentPane.add(killopenedBrowsers);		

		final JCheckBox killopenedBrowsersEach = new JCheckBox("Kill the Browsers before Each Run");
		killopenedBrowsersEach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (killopenedBrowsersEach.isSelected()){
					ScriptRunner.killBrowsersBeforeEachRun = true;
				}
				else{
					ScriptRunner.killBrowsersBeforeEachRun = false;
				}
			}
		});
		//sendMail.setBounds(156, 296, 218, 23);
		killopenedBrowsersEach.setBounds(156, 390, 218, 23);
		contentPane.add(killopenedBrowsersEach);*/
		
	}
}
