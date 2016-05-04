package TestScriptFiles;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;



import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.thread.TestNGThread;
import org.xml.sax.SAXException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import LibraryFiles.CoreLibrary.DriverScript;
import LibraryFiles.CoreLibrary.Reporter;
import LibraryFiles.CoreLibrary.ScriptRunner;


public class UserScenario {

	@Test
	public void loginPage() throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
			ScriptRunner.driver.get(ScriptRunner.fetchData("Url"));
			
			ScriptRunner.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					
	        // Enter user id
	        WebElement userName = ScriptRunner.driver.findElement(By.id("login-user-email"));
	        userName.sendKeys(ScriptRunner.fetchData("User Name"));
	        Reporter.detailedReportEvent("Username entered", "Pass");
			
	        WebElement passWord = ScriptRunner.driver.findElement(By.id("login-password"));
	        passWord.sendKeys(ScriptRunner.fetchData("Password"));
	        Reporter.detailedReportEvent("Password entered", "Pass");
	        
	        WebElement logIn = ScriptRunner.driver.findElement(By.id("login"));
	        logIn.submit();		
	        Reporter.detailedReportEvent("login button clicked", "Pass");
	        
	        
	        ScriptRunner.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	        WebElement loggedInUser = ScriptRunner.driver.findElement(By.id("workspace"));
	        if(loggedInUser.isDisplayed()){
	        	Reporter.detailedReportEvent("User name is displayed as " + ScriptRunner.fetchData("User Name") + " in home page since login successful", "Fail");
	        }
	        else{
	        	Reporter.detailedReportEvent("User name is not displayed as " + ScriptRunner.fetchData("User Name") + " in home page since login successful", "Fail");
	        }
		}
		catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150), "Exception");
		}
        
	}
	
	@Test
	public void logoutPage() throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
        // Enter user id
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement loggedUser = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[1]/div/div[5]/div/div/span[2]"));
        loggedUser.click();
        Reporter.detailedReportEvent("Admin Carrier clicked", "Pass");
        
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement signOut = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[1]/div/div[5]/div/ul/li[6]/a"));
        signOut.click();
        Reporter.detailedReportEvent("logout clicked", "Pass");
        
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement loginuserNamefield = ScriptRunner.driver.findElement(By.id("login-user-email"));
        if (loginuserNamefield.isDisplayed()){
        	Reporter.detailedReportEvent("Username edit box is displayed and enabled in opening page since logout successful", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Username edit box is not displayed and enabled in opening page since logout successful", "Fail");
        }
	}
		catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
}

	}
}

