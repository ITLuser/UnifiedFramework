package LibraryFiles.CoreLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.thoughtworks.selenium.Selenium;

public class ScriptRunner {

	public static WebDriver driver;
	public static int dataIterationNumber;
	public static boolean killBrowsersBeforeRun = false;
	public static boolean killBrowsersBeforeEachRun = false;
	
	public static void driverSetup() throws IOException{
/*		if (killBrowsersBeforeRun == true){
			Runtime.getRuntime().exec("taskkill /F /IM Chrome.exe");
		}
*/		System.setProperty("webdriver.chrome.driver",MapGenerator.commonData.get("ChromeDriverPath"));
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	
	}
	
	public static void beginTest(){

		List<String> suitesList = new ArrayList<String>();
		TestListenerAdapter listener = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setOutputDirectory("outputfoldername");
		suitesList.add(DriverScript.testngXmlPath);
		testng.setTestSuites(suitesList);
		testng.addListener(listener);
		testng.run();			
		
	}
	
	public static void tearDown() {
		if(driver!=null) {
			System.out.println("Closing chrome browser");
			driver.quit();
		}
	}
	
	public static String fetchData(String key){

		LinkedHashMap<String, String> tempTestDataMap = null;
		String value = null;
		
		tempTestDataMap = MapGenerator.testData.get(dataIterationNumber-1);
		if(tempTestDataMap.containsKey(key)){
			value = tempTestDataMap.get(key);
		}
		return value;
		
	}
	
}
