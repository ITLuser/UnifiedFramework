package TestScriptFiles;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import LibraryFiles.CoreLibrary.Reporter;
import LibraryFiles.CoreLibrary.ScriptRunner;


public class AppSwitcherdropdownToggle{
	

	@Test
	public void AppSwitcher() throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{	
		
		try{	
    //Click App Container Button on the top left corner of the Menu Bar		
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement loggedUser = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[1]/div/div[1]/button"));
        loggedUser.click();
        Reporter.detailedReportEvent("App Switcher Button Clicked", "Pass");
    //Click ShipViewer icon/option   
        WebElement shipViewer = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[1]/div/div[1]/ul/li[3]/a"));
        shipViewer.click();
        Reporter.detailedReportEvent("Ship Viewer Option Clicked", "Pass");
        
    
        
    //Verify and Report - 'Managed Ships' panel - In which resulted total number of vessel should matches with the number of rows in the panel    
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement managedShips = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[1]/div/div"));
        if (managedShips.isDisplayed()){
        	Reporter.detailedReportEvent("Managed Ships Panel is displayed and Results(in top right corner of the panel) is matching with the no of rows in the panel ", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Managed Ships Panel is not displayed and Results(in top right corner of the panel) is not matching with the no of rows in the panel", "Fail");
        }
		
		}
		catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");        
}
}	
}



