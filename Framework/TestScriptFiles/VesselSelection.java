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

public class VesselSelection{
	
	@Test
	public void searchOption()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
	//Enter any Vessel's name in the search bar at the top right corner, For Example - Horizon Pacific, in 'Managed Ships' panel 
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement search = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[1]/div/div/div[3]/div/div[1]/div[1]/div[2]/input"));
        search.sendKeys("HORIZON PACIFIC");
        Reporter.detailedReportEvent("Vessel Name Entered", "Pass");
        
    //Verify And Report - Particular Ship is filtered in a single row in the 'Managed Ships' panel
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  
        WebElement shipName = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[1]/div/div"));
        if (shipName.isDisplayed()){
        	Reporter.detailedReportEvent("Shipname, along with other informations for the respective ship, is displayed in only one row", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Shipname, along with other informations for the respective ship, is not displayed in only one row", "Fail");
        }
		}
        catch(Exception e){
        	Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
        }
        
}
	@Test
	public void searchedvessel()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
        ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //Click the filtered Vessel Profile  
        WebElement shipViewer = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[5]/div[1]/div/div/div[3]/div/div[1]/div[3]/div/div/div[1]/div[2]/div[1]/div/div/div[1]/div"));
        shipViewer.click();
        Reporter.detailedReportEvent("Filtered Vessel clicked ", "Pass");
        
   //Click Eye Shaped button on the top left corner      
        WebElement search = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[5]/div[1]/div/div/div[3]/div/div[1]/div[1]/div[1]/button"));
        search.click();
        Reporter.detailedReportEvent("Eyed Shaped Button clicked ", "Pass");
        
   
        
    //Verify And Report - Ship Profile Panel is displayed    
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
     
        WebElement shipProfile = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[1]/div/div/div[3]/div/div/canvas[2]"));
        if (shipProfile.isDisplayed()){
        	Reporter.detailedReportEvent("Ship Profile - 'Selected Vessel Name' is displayed", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Ship Profile - 'Selected Vessel Name' is not displayed", "Fail");
        }
	}
        catch(Exception e){
        	Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
        }
}
}