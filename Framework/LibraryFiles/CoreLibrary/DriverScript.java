package LibraryFiles.CoreLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DriverScript {
	
	public static String current = System.getProperty("user.dir");
	public static String testngXmlPath = current.concat("\\Framework\\LibraryFiles\\CoreLibrary\\testing.xml");
	public static LinkedHashMap<String, String> testDataMap = null;
	public static List< LinkedHashMap<String, String> > testDataList = null;	
	
	@SuppressWarnings({ "static-access" })
	public static void main(String args[]) throws BiffException, IOException, ParserConfigurationException, TransformerException, SAXException{
				
		MapGenerator MapGen = new MapGenerator();
		Reporter Report = new Reporter();
		
		// Calling to Create Map for the list of Test Script Executions
		MapGen.GenerateTestExecutionList();
		Report.generateSummaryReport();
		Set setOfKeys = MapGen.testExecutionList.keySet();
		Iterator iterator = setOfKeys.iterator();
		while (iterator.hasNext()){
			// Store the test script id
			String TestID = (String) iterator.next();
			// Store the test description for the given test id
			String TestDescription = (String) MapGen.testExecutionList.get(TestID);
			// Generate Detailed Report temp file
			Report.generateDetailedReport(TestID,TestDescription);
			// generate test data map inside a list for multiple iteration
			MapGen.GenerateTestData(TestID);			
			
			testDataList = MapGen.testData;
			testDataMap = testDataList.get(0);
			ScriptRunner.driverSetup();
			
			for (int i=1; i<Integer.parseInt(testDataMap.get("FlowCounts"))+1; i++){				
				String tempKey = "Flow"+i; 
				if(testDataMap.containsKey(tempKey)){
					generateTestngXML(tempKey);
					ScriptRunner.beginTest();
				}
			}
			ScriptRunner.tearDown();
			Report.generateDetailedHTML();
			Report.summaryReportEvent();
			cleanup();
		}
		Report.generateSummaryHTML();
	}
	
	private static void cleanup() {
		Reporter.stepNo = 1;
		MapGenerator.testData.clear();
	}

	public static void generateTestngXML(String tempKey) throws ParserConfigurationException, SAXException, IOException, TransformerException {
	
		String tempValue;
		String tempClass;
		String tempMethod;
		String tempArgument;
		String tempXMLClass;
		String [] flowDetails = null;
		
		File file = new File(testngXmlPath); 
		file.delete();
		
		tempValue = testDataMap.get(tempKey);
		Reporter.detailedReportEvent("Business Flow "+tempValue+ " has started", " -- ");
		
		if (tempValue.contains("~")) {
			flowDetails = tempValue.split("~");
		}
		tempClass = flowDetails[0];
		tempMethod = flowDetails[1];
		if (flowDetails.length == 3 ){
			tempArgument = flowDetails[2];
			if (tempArgument != "1"){
				ScriptRunner.dataIterationNumber = Integer.parseInt(flowDetails[2]);
			}			
		}
		else{
			ScriptRunner.dataIterationNumber = 1;
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Element rootElement = doc.createElement("suite");
		rootElement.setAttribute("name", "TestScriptFiles");
		rootElement.setAttribute("verbose", "1");
		doc.appendChild(rootElement);
		
		Element test = doc.createElement( "test" );		
		test.setAttribute("name", "framework");
		
		Element classes = doc.createElement("classes");
		test.appendChild(classes);

		Element class1 = doc.createElement("class");
		
		tempXMLClass = "TestScriptFiles.".concat(tempClass);
		class1.setAttribute("name", tempXMLClass);
		classes.appendChild(class1);

		Element methods = doc.createElement("methods");
		class1.appendChild(methods);
		
		Element include1 = doc.createElement("include");
		include1.setAttribute("name", tempMethod);
		methods.appendChild(include1);

		rootElement.appendChild(test);
					
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("suite",
		    "SYSTEM",
		    "http://testng.org/testng-1.0.dtd");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
		//detailedReportsPath = (exceutionReportFolder + "\\DetailedReport - " +TCID + ".xml");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(testngXmlPath));

		transformer.transform(source, result);		
			    
	}
	
}
