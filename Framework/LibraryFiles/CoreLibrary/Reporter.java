package LibraryFiles.CoreLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message; 
import javax.mail.MessagingException; 
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.elca.el4j.services.xmlmerge.AbstractXmlMergeException;
import ch.elca.el4j.services.xmlmerge.Mapper;
import ch.elca.el4j.services.xmlmerge.MergeAction;
import ch.elca.el4j.services.xmlmerge.XmlMerge;

import com.sun.xml.internal.ws.api.server.Module;

public class Reporter {
	
	public static String current = System.getProperty("user.dir");
	public static String exceutionReportFolder;
	public static String exceutionScreenshotsFolder;
	public static String failedTestFolderpPath;
	public static String summaryReportPath;
	public static String detailedReportsPath;
	public static String screenshotReportsPath;
	public static String testStatus = "Fail";
	public static Integer stepNo = 1;
	public static Integer testNo = 1;
	public static String TestID;
	public static String TestDescription;
	public static String testStartTime;
	public static Date startTime;
	public static Date endTime;
	public static String testEndTime;
	public static String HTMLReportPath;
	public static String HTMLSummaryReportPath;
	public static String screenshotreportpath;
	public static Boolean skipTestScript = false;
	public static Integer totalpassedScripts = 0;
	public static Integer totalfailedScripts = 0;
	public static Integer failedDueToExceptionScripts = 0;
	public static String summaryDescription = null;
	static String singleFailure = "Pass";
	
	Reporter(){
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		exceutionReportFolder = current.concat("\\Framework\\TestReports\\") + dateFormat.format(date);
		exceutionScreenshotsFolder = current.concat("\\Framework\\Screenshots\\") + dateFormat.format(date);
		File reportsFolder = new File(exceutionReportFolder);
		File ScreenshotsFolder = new File(exceutionScreenshotsFolder);
		ScreenshotsFolder.mkdirs();
		reportsFolder.mkdirs();
		stepNo = 1;
		testStatus = "Fail";
		singleFailure = "Pass";
	}

	public static void generateDetailedReport(String TCID,String TCDescription) throws ParserConfigurationException, TransformerException, DOMException, UnknownHostException{

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("TestSteps");				
		doc.appendChild(rootElement);

		Element testDetails = doc.createElement( "Details" );
		
		Element testUser = doc.createElement("TestUser");
		testUser.appendChild(doc.createTextNode(System.getProperty("user.name")));
		testDetails.appendChild(testUser);
		
		Element testMachineName = doc.createElement("testMachineName");
		testMachineName.appendChild(doc.createTextNode(InetAddress.getLocalHost().getHostName()));
		testDetails.appendChild(testMachineName);

		Element teststarttime = doc.createElement("testStartTime");
		teststarttime.appendChild(doc.createTextNode(dateFormat.format(date)));
		testDetails.appendChild(teststarttime);
		testStartTime = dateFormat.format(date);		
		
		Element testduration = doc.createElement("testDuration");
		testduration.appendChild(doc.createTextNode("Need to incorporate"));
		testDetails.appendChild(testduration);
		
		Element testId = doc.createElement("TestId");
		testId.appendChild(doc.createTextNode(TCID));
		testDetails.appendChild(testId);
		
		Element testdescription = doc.createElement("TestDescription");
		testdescription.appendChild(doc.createTextNode(TCDescription));
		testDetails.appendChild(testdescription);
		
		rootElement.appendChild(testDetails);
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		detailedReportsPath = (exceutionReportFolder + "\\DetailedReport-" +TCID + ".xml");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(detailedReportsPath));

		transformer.transform(source, result);
		TestID = TCID;
		TestDescription = TCDescription;
		
		File file = new File(detailedReportsPath); 
		file.delete();
		
	}
	
	public static void detailedReportEvent(String stepDescription, String stepStatus) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// changed the path for detailed xml file to summary report xml file to have single file
		Document doc = docBuilder.parse(summaryReportPath);
		
		Node root = doc.getFirstChild();
		
		NodeList nodes = doc.getElementsByTagName("Tests");
		
		for (int i = 0; i<nodes.getLength(); i++){
			Element node = (Element) nodes.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("Id");
			if (TestID.equalsIgnoreCase(nodeAttr.getTextContent())){
				root = nodes.item(i);
				break;
			}
		}
		
		Element stepDetails = doc.createElement( "Steps" );
		
		Element StepNumber = doc.createElement("StepNumber");
		StepNumber.appendChild(doc.createTextNode(stepNo.toString()));
		stepDetails.appendChild(StepNumber);
		
		Element StepDesc = doc.createElement("StepDesc");
		StepDesc.appendChild(doc.createTextNode(stepDescription));
		stepDetails.appendChild(StepDesc);
			
		Element StepStatus = doc.createElement("StepStatus");
		StepStatus.appendChild(doc.createTextNode(stepStatus));
		stepDetails.appendChild(StepStatus);
		
		Element StepTime = doc.createElement("StepTime");
		StepTime.appendChild(doc.createTextNode(dateFormat.format(date)));
		//StepTime.appendChild(doc.createTextNode(String.valueOf(date.getTime())));
		stepDetails.appendChild(StepTime);
		
		
		// call method to take screen shot and save it wth filename and location
		if (stepStatus.equals("Fail") || stepStatus.equals ("Exception")){	
			failedTestFolderpPath = exceutionScreenshotsFolder + "\\"+ TestID;		
			File failedTestFolder = new File(failedTestFolderpPath);
			if(!failedTestFolder.exists()){
				failedTestFolder.mkdirs();
			}
			captureScreenshot(stepNo.toString());
		}
				
		if (stepStatus.equals ("Fail") || stepStatus.equals ("Exception")){
			Element ScreenshotimagePath = doc.createElement("ScreenshotimagePath");
			ScreenshotimagePath.appendChild(doc.createTextNode(screenshotreportpath));
			stepDetails.appendChild(ScreenshotimagePath);
		}
        
		if (stepStatus.equals ("Exception")){
			skipTestScript = true;
		}
		
		testEndTime = dateFormat.format(date);
		
		root.appendChild(stepDetails);
		
		stepNo++;
		
		if(stepStatus.equalsIgnoreCase("Fail") || stepStatus.equals ("Exception")){
			singleFailure = "Fail";
		}
		
		if(singleFailure.equalsIgnoreCase("Fail")){
			testStatus = "Fail";
		}
		else{
			testStatus = "Pass";
		}		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(summaryReportPath));
		transformer.transform(source, result);
		
	}
	
	public static void generateDetailedHTML () throws FileNotFoundException, TransformerException{
		
		TransformerFactory tFactory = TransformerFactory.newInstance();

	    Transformer transformer = tFactory.newTransformer 
	    		(new javax.xml.transform.stream.StreamSource(current.concat("\\Framework\\TestReports\\DetailedReport.xsl")));

	    transformer.transform (new javax.xml.transform.stream.StreamSource (detailedReportsPath),
	    		new javax.xml.transform.stream.StreamResult ( new FileOutputStream(exceutionReportFolder + "\\DetailedReport-" +TestID + ".html")));
	    
	    HTMLReportPath = exceutionReportFolder + "\\DetailedReport-" +TestID + ".html";
	    
		File file = new File(detailedReportsPath); 
		file.delete();
		
	}
	
	public static void generateSummaryReport() throws ParserConfigurationException, TransformerException, DOMException, UnknownHostException{
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("TestSuite");
		
		doc.appendChild(rootElement);
	
		Element suiteDetails = doc.createElement( "Details" );
		
		Element testUser = doc.createElement("TestUser");
		testUser.appendChild(doc.createTextNode(System.getProperty("user.name")));
		suiteDetails.appendChild(testUser);
		
		Element testMachineName = doc.createElement("testMachineName");
		testMachineName.appendChild(doc.createTextNode(InetAddress.getLocalHost().getHostName()));
		suiteDetails.appendChild(testMachineName);

		Element teststarttime = doc.createElement("testStartTime");
		teststarttime.appendChild(doc.createTextNode(dateFormat.format(date)));
		suiteDetails.appendChild(teststarttime);

		Element testduration = doc.createElement("testDuration");
		testduration.appendChild(doc.createTextNode("Need to incorporate"));
		suiteDetails.appendChild(testduration);
		
		rootElement.appendChild(suiteDetails);		
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		summaryReportPath = (exceutionReportFolder + "\\SummaryReport.xml");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(summaryReportPath));

		transformer.transform(source, result);
		
	}
	
	public static void insertTestInSummaryReport() throws ParserConfigurationException, TransformerException, DOMException, UnknownHostException,SAXException,IOException{

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(summaryReportPath);
		
		Node root = doc.getFirstChild(); 
		
		Element test = doc.createElement( "Tests" );
		test.setAttribute("Id", TestID);

		root.appendChild(test);		

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(summaryReportPath));
		transformer.transform(source, result);
		
	}
	
	public static void summaryReportEvent() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(summaryReportPath);
		
		Node root = doc.getFirstChild(); 
		
		NodeList nodes = doc.getElementsByTagName("Tests");
		
		for (int i = 0; i<nodes.getLength(); i++){
			Element node = (Element) nodes.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("Id");
			if (TestID.equalsIgnoreCase(nodeAttr.getTextContent())){
				root = nodes.item(i);
				break;
			}
		}
		
		Element testDetails = doc.createElement( "TestsDetails" );
		
		Element sNo = doc.createElement("sNo");
		sNo.appendChild(doc.createTextNode(testNo.toString()));
		testDetails.appendChild(sNo);

		Element TestNumber = doc.createElement("TestNumber");
		TestNumber.appendChild(doc.createTextNode(TestID));
		testDetails.appendChild(TestNumber);
		
		Element TestDesc = doc.createElement("TestDesc");
		TestDesc.appendChild(doc.createTextNode(TestDescription));
		testDetails.appendChild(TestDesc);
		
		Element TestStatus = doc.createElement("TestStatus");
		TestStatus.appendChild(doc.createTextNode(testStatus));
		testDetails.appendChild(TestStatus);
		
		Element TestTime = doc.createElement("TestTime");
		TestTime.appendChild(doc.createTextNode(testStartTime));
		testDetails.appendChild(TestTime);
		
/*		Element TestHtmlPath = doc.createElement("HTMLPath");
		TestHtmlPath.appendChild(doc.createTextNode(HTMLReportPath));
		testDetails.appendChild(TestHtmlPath);*/

		root.appendChild(testDetails);
		
		endTime = date;		
		
		long durationTemp = endTime.getTime() - startTime.getTime();
		long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationTemp);

		nodes = doc.getElementsByTagName("Details");
		
		Node node = nodes.item(0);
		NodeList childs = node.getChildNodes();
		for (int i = 0; i<childs.getLength(); i++){
			node = childs.item(i);
			if ("testDuration".equals(node.getNodeName())) {
				node.setTextContent(Long.toString(durationInMinutes)+" Minutes");
				break;
			}
		}
		
		testNo++;
		if (testStatus.equals("Pass")){
			totalpassedScripts++;
		}
		if (testStatus.equals("Fail")){
			totalfailedScripts++;
		}
		if (testStatus.equals("Exception")){
			failedDueToExceptionScripts++;
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(summaryReportPath));
		transformer.transform(source, result);
		
	}
		
	public static void generateSummaryHTML () throws FileNotFoundException, TransformerException{
		
		TransformerFactory tFactory = TransformerFactory.newInstance();

	    Transformer transformer = tFactory.newTransformer 
	    		(new javax.xml.transform.stream.StreamSource(current.concat("\\Framework\\TestReports\\SummaryReport.xsl")));

	    transformer.transform (new javax.xml.transform.stream.StreamSource (summaryReportPath),
	    		new javax.xml.transform.stream.StreamResult ( new FileOutputStream(exceutionReportFolder + "\\SummaryReport.html")));

	    HTMLSummaryReportPath = exceutionReportFolder + "\\SummaryReport.html";	    
		
	}
	
	public static void reportsCleanUp (){
		File file = new File(summaryReportPath); 
		file.delete();
	}
	
	public static void captureScreenshot(String StepNo) throws IOException{		
	
		TakesScreenshot ts =(TakesScreenshot)ScriptRunner.driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(source, new File(failedTestFolderpPath + "\\Step-" +StepNo + ".png"));
		
		screenshotreportpath = failedTestFolderpPath + "\\Step-" +StepNo + ".png";
		
	}

	public static void sendMail(){
		//change accordingly	
		final String username = "vasantham.puyalnithi@idealtechlabs.com"; 
		final String password = "Kingsword_2";
		//Set properties 
		Properties props = new Properties(); 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.host", MapGenerator.commonData.get("SMTP_HOST"));  
		props.put("mail.smtp.port", MapGenerator.commonData.get("SMTP_PORT"));
		// Get the Session object. 
		Session session = Session.getInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password); } 
			});   
			try { 
				//Compose the message 
				Message message = new MimeMessage(session); 
				message.setFrom(new InternetAddress(MapGenerator.commonData.get("FROM"))); 
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MapGenerator.commonData.get("TO")));
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(MapGenerator.commonData.get("CC")));
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(MapGenerator.commonData.get("BCC")));
				message.setSubject(MapGenerator.commonData.get("Summary"));
				String msg = "Hi, \n\n\n Please find the Test Execution Report Below for '"+summaryDescription+"'";
				msg = msg + "\n\n\n Total Scripts Executed : "+ (testNo-1);
				msg = msg + "\n Passed Scripts               : "+ totalpassedScripts;
				msg = msg + "\n Failed Scripts                : "+ totalfailedScripts + "  (including valid/invalid failures)";
				msg = msg + "\n\n\n Please find the attached document for more details";
				
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(msg);
				
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				
				messageBodyPart = new MimeBodyPart();
		        DataSource source = new FileDataSource(HTMLSummaryReportPath);
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        messageBodyPart.setFileName(HTMLSummaryReportPath);
		        multipart.addBodyPart(messageBodyPart);
		         
		        message.setContent(multipart);
				//send the message 
				Transport.send(message);   
				System.out.println("Email send successfully.");   
			} 
			catch (MessagingException e) { 
				throw new RuntimeException(e); 
			}
		}

	public static void mergeXMLReports()throws FileNotFoundException, TransformerException, ParserConfigurationException,SAXException,IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document summaryDoc = db.parse(summaryReportPath);
        Document detailedDoc = db.parse(detailedReportsPath);
        System.out.println("Before Copy...");
        //prettyPrint(doc2);
        NodeList list = detailedDoc.getElementsByTagName("TestSteps");
        Element element = (Element) detailedDoc.getFirstChild();
        Node copiedNode = summaryDoc.importNode(detailedDoc.getFirstChild(), true);
        summaryDoc.getFirstChild().appendChild(copiedNode);
        System.out.println("After Copy...");
        //prettyPrint(doc2);

	}
	
}
