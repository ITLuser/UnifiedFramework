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

public class ContainerSearchType3 {
	
	@Test
	public void ShipVisualizerProfile()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	//Enter Container ID (For Eg- CZZU0400185)
        WebElement ListedOptions = ScriptRunner.driver.findElement(By.xpath(".//*[@id='equipmentTypeIsoCode']/div/div/div/button"));
        ListedOptions.click();
        
        WebElement ListOptions = ScriptRunner.driver.findElement(By.xpath("html/body/ul[1]"));
        if (ListOptions.isDisplayed()){
        	Reporter.detailedReportEvent("Search Based on Listed option(e.g - Equip Type) is displayed ", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Search Based on Listed option(e.g - Equip Type) is not displayed", "Fail");
        }
       
WebElement AnyoneOption = ScriptRunner.driver.findElement(By.xpath("html/body/ul[1]/li[3]/button"));        
AnyoneOption.click();
Reporter.detailedReportEvent("Any One Option(e.g- 22G1) from the displayed List is Clicked ", "Pass");


WebElement AnyoneOption1 = ScriptRunner.driver.findElement(By.xpath("html/body/ul[1]/li[4]/button"));
AnyoneOption1.click();
Reporter.detailedReportEvent("Any One Option(e.g- 22T0) from the displayed List is Clicked ", "Pass");


      //Click 'List' button  
        WebElement list = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[3]/div[2]/button[1]"));
        list.click();
        Reporter.detailedReportEvent("List Button Clicked", "Pass");
        
      //Verify and Report - 'Un-Saved Search 1' panel - In which resulted total number of Containers should match with the number of rows in the panel    
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement Panel = ScriptRunner.driver.findElement(By.id("workspace"));
        if (Panel.isDisplayed()){
        	Reporter.detailedReportEvent("Un-Saved Search 1 Panel is displayed ", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Un-Saved Search 1 Panel is not displayed", "Fail");
        }
       
	
}
}


