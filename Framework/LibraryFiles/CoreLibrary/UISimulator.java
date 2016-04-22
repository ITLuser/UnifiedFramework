package LibraryFiles.CoreLibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import jxl.read.biff.BiffException;

public class UISimulator {


	public UISimulator() throws BiffException, IOException{
			
		 //creat UI frame 
			final JFrame guiFrame = new JFrame();
			guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			guiFrame.setTitle("Unified Framework");
			guiFrame.setSize(300,250);
			
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
			
			//Click on the execute button of the frame
			execute.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					String testScripts = tests.getText();
					guiFrame.dispose();
					try {
						DriverScript.startExecution(testScripts);
					} catch (BiffException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}						
			});

			guiFrame.add(comboPanel, BorderLayout.NORTH);
			guiFrame.add(execute, BorderLayout.SOUTH);
			
			guiFrame.setVisible(true);	
			 
		}		

}
