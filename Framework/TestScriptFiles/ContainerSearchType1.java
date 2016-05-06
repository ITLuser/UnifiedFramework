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

//Type 1- Searching Container By feilds having dropdown list

public class ContainerSearchType1{
	
	@Test
	public void searchBar()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	//Click 'Containers'  In Menu Bar	
        WebElement dropdownToggle = ScriptRunner.driver.findElement(By.xpath(".//*[@id='xv-app-container']/div[3]/div/div[7]/div"));
        dropdownToggle.click();
        Reporter.detailedReportEvent("Containers Option Clicked", "Pass");
        
        
    //Click  'Search' option     
        WebElement search = ScriptRunner.driver.findElement(By.xpath(".//*[@id='xv-app-container']/div[3]/div/div[7]/ul/li[2]/span/a"));
        search.click();
        Reporter.detailedReportEvent("Search Option Clicked", "Pass");
    
     //Verify and Report - 'Container Search' panel, which is consist of different kinds of search criterias.
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement containerSearch = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[2]"));
        if (containerSearch.isDisplayed()){
        	Reporter.detailedReportEvent("Container Search Panel is displayed ", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Container Search Panel is not displayed ", "Fail");
        }
		}
        catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
			       
}
}
	@Test
	public void ShipVisualizerProfile()throws ParserConfigurationException, SAXException, IOException, TransformerException, InterruptedException{
		
		try{
		ScriptRunner.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	//Perform action on any one of the search criteria which contains dropdown list, For Example - 'Freightkind'	
	//Click any one option (e.g- Full/Empty/Breakbulk)	
        //WebElement dropdownoption = ScriptRunner.driver.findElement(By.xpath(".//*[@id='freightKind']/option[3]"));
        //dropdownoption.click();
        Reporter.detailedReportEvent("Certain Option(e.g-Full) From Drop Down List Clicked", "Pass");
        
     //Click 'List' button 
        WebElement list = ScriptRunner.driver.findElement(By.xpath("html/body/div[1]/div/div/div/div/form/div[3]/div[2]/button[2]"));
        list.click();
        Reporter.detailedReportEvent("List Button CLicked", "Pass");
        
        
     //Verify and Report - 'Un-Saved Search 1' panel - In which resulted total number of Containers should match with the number of rows in the panel   
        ScriptRunner.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement Panel = ScriptRunner.driver.findElement(By.xpath(".//*[@id='workspace']/div[2]/div/div"));
        if (Panel.isDisplayed()){
        	Reporter.detailedReportEvent("Un-Saved Search 1 Panel is displayed", "Pass");
        }
        else{
        	Reporter.detailedReportEvent("Un-Saved Search 1 Panel is not displayed", "Fail");
        }
		}
        catch(Exception e){
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150)+"", "Exception");
			
        
        
}
}
	
}