package LibraryFiles.CoreLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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
	public static String testEndTime;
	public static String HTMLReportPath;
	public static String screenshotreportpath;
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
		Element rootElement = doc.createElement("Test");
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

	}
	
	public static void detailedReportEvent(String stepDescription, String stepStatus) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(detailedReportsPath);
		
		Node root = doc.getFirstChild(); 
		
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
		StreamResult result = new StreamResult(new File(detailedReportsPath));
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
	
	public static void summaryReportEvent() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(summaryReportPath);
		
		Node root = doc.getFirstChild(); 
		
		Element testDetails = doc.createElement( "Tests" );
		
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
		
		Element TestHtmlPath = doc.createElement("HTMLPath");
		TestHtmlPath.appendChild(doc.createTextNode(HTMLReportPath));
		testDetails.appendChild(TestHtmlPath);

		root.appendChild(testDetails);
		
		testNo++;
		
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

		File file = new File(summaryReportPath); 
		file.delete();
		
	}
	
	public static void captureScreenshot(String StepNo) throws IOException{		
	
		TakesScreenshot ts =(TakesScreenshot)ScriptRunner.driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(source, new File(failedTestFolderpPath + "\\Step-" +StepNo + ".png"));
		
		screenshotreportpath = failedTestFolderpPath + "\\Step-" +StepNo + ".png";
		
	}
}
