package LibraryFiles.CoreLibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.bcel.generic.NEWARRAY;
import org.hamcrest.core.IsNot;
import org.xml.sax.SAXException;

import jxl.read.biff.BiffException;

public class UISimulator {

	public UISimulator() throws BiffException, IOException {

		// creat UI frame
		final JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Unified Framework");
		guiFrame.setSize(500, 250);

		guiFrame.setLocationRelativeTo(null);

		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("Test Script IDs:");
		final JTextArea tests = new JTextArea();
		tests.setFont(new Font("Serif", Font.BOLD, 16));
		tests.setLineWrap(true);
		tests.setWrapStyleWord(true);
		tests.setBorder(BorderFactory.createLineBorder(Color.black));

		comboPanel.add(comboLbl);
		comboPanel.add(tests);

		JButton execute = new JButton("Execute");

		// Click on the execute button of the frame
		execute.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				String testScripts = tests.getText();
				try {
					String temp = findMissingTCID(testScripts);
					if (temp != null) {
						JOptionPane
								.showMessageDialog(
										guiFrame,
										"TCIDs "
												+ temp
												+ " is/are not available in the test data.Please enter valid TCIDs",
										"Error", JOptionPane.WARNING_MESSAGE);
					} else {
						guiFrame.dispose();
						DriverScript.startExecution(testScripts);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public String findMissingTCID(String testScripts) {
				boolean isNotAvailable = false;
				String notAvailable = null;
				String[] userEnteredList = testScripts.split("~");
				Collection<String> setOfvalues = MapGenerator.notestcaseid
						.values();
				List<String> availableList = new ArrayList<String>(setOfvalues);

				for (int i = 0; i < userEnteredList.length; i++) {
					for (int j = 0; j < availableList.size(); j++) {
						if (availableList.get(j).equalsIgnoreCase(
								userEnteredList[i])) {
							isNotAvailable = true;
							break;
						}
						isNotAvailable = false;
					}
					if (isNotAvailable == false) {
						if (notAvailable == null) {
							notAvailable = userEnteredList[i];
						} else {
							notAvailable = notAvailable + "~" + userEnteredList[i];
						}
					}
				}
				return notAvailable;
			}

		});

		guiFrame.add(comboPanel, BorderLayout.NORTH);
		guiFrame.add(execute, BorderLayout.SOUTH);

		guiFrame.setVisible(true);

	}

}
