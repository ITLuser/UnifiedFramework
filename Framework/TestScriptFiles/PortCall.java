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

public class PortCall {
	@Test 
	public void ShipProfileToolbar()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	  //Click 'Select Call'	
		WebElement ToolBar = ScriptRunner.driver.findElement(By.xpath(".//*[@id='xv-app-container']/div[3]/div/div[2]/div/div"));
        ToolBar.click();
        Reporter.detailedReportEvent("Select Call Clicked", "Pass");
		
        Thread.sleep(5000);
        
      //Click any one Port in the dropdown list, For Example - HNL 512E
		WebElement PortId = ScriptRunner.driver.findElement(By.xpath("html/body/div[4]/div[3]/div/div[2]/ul/div[2]/ul/li[29]/span"));
        PortId.click();
        Reporter.detailedReportEvent("Port Call Clicked", "Pass");
    
      //Verify and Report - Change in Ship Profile panel
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement shipName = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[1]/div/div/div[3]/div/div/canvas[2]"));
        if (shipName.isDisplayed()){
        	Reporter.detailedReportEvent("Ship Profile for the particular port is displayed", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Ship Profile for the particular port is not displayed ", "Fail");
        }
        
        Thread.sleep(5000);
}
}

