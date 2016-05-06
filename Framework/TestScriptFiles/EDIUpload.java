package TestScriptFiles;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import LibraryFiles.CoreLibrary.Reporter;
import LibraryFiles.CoreLibrary.ScriptRunner;

public class EDIUpload {
	@Test 
	public void EDIdropdownlist()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	  //Click EDI Palette	
		WebElement ToolBar = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[3]/div/div[6]/div"));
        ToolBar.click();
        Reporter.detailedReportEvent("EDI Palette Clicked", "Pass");
		
      //Click Upload
		WebElement uploadEDI = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[3]/div/div[6]/ul/li[1]/span/a"));
		uploadEDI.click();
        Reporter.detailedReportEvent("Upload Option Clicked", "Pass");
    
      //Verify and Report - EDI Upload profile displayed 
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement EDIUploadprofile = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[1]"));
        if (EDIUploadprofile.isDisplayed()){
        	Reporter.detailedReportEvent("EDI Upload Profile for the particular port is displayed", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("EDI Upload Profile for the particular port is not displayed ", "Fail");
        }
		}
        catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
			       
}
        Thread.sleep(5000);
}

@Test 
public void EDIuploadprofile()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
	
	try{
	ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
  //Click EDI Session	
	WebElement EDISession = ScriptRunner.driver.findElement(By.xpath(".//*[@id='ediSession']"));
	EDISession.click();
    Reporter.detailedReportEvent("EDISession Clicked", "Pass");
	
  //Click Any one EDI Session (e.g - BAPLIE_15_IN)
	WebElement uploadEDI = ScriptRunner.driver.findElement(By.xpath(".//*[@id='ediSession']/option[3]"));
	uploadEDI.click();
    Reporter.detailedReportEvent("Any one EDI Session (e.g - BAPLIE_15_IN) Clicked", "Pass");

  //Verify and Report - EDI Upload profile displayed 
    ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    WebElement EDIUploadprofile = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[1]"));
    if (EDIUploadprofile.isDisplayed()){
    	Reporter.detailedReportEvent("EDI Upload Profile for the particular port is displayed", "Pass");
    }
    else{
    	Reporter.detailedReportEvent("EDI Upload Profile for the particular port is not displayed ", "Fail");
    }
	}
    catch(Exception e){
		Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
		       
}
    Thread.sleep(5000);
}
@Test 
public void upload()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
	
	try{
	ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
  //Click EDI Session	
	WebElement EDISession = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[1]/div[2]/div/div[1]/div[2]/div[2]/input"));
	EDISession.click();
    Reporter.detailedReportEvent("Upload Button Clicked", "Pass");
	
 

  //Verify and Report - EDI Upload profile displayed 
    ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    WebElement EDIUploadprofile = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[1]"));
    if (EDIUploadprofile.isDisplayed()){
    	Reporter.detailedReportEvent("EDI File Upload Window is displayed", "Pass");
    }
    else{
    	Reporter.detailedReportEvent("EDI File Upload Window is not displayed ", "Fail");
    }
}
catch(Exception e){
	Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
	       
}
    
}
}

