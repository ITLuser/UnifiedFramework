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

public class UsersPalette {
	@Test
	public void Users() throws ParserConfigurationException, SAXException,
			IOException, TransformerException, InterruptedException {

		try {
			ScriptRunner.driver.manage().timeouts()
					.implicitlyWait(30, TimeUnit.SECONDS);

			// Click Users
			WebElement ToolBar = ScriptRunner.driver.findElement(By
					.xpath("html/body/div[4]/div[2]/div[1]/a[1]"));
			ToolBar.click();
			// Reporter.detailedReportEvent("Users Clicked", "Pass");

			// Verify and Report - User Workspace displayed
			ScriptRunner.driver.manage().timeouts()
					.implicitlyWait(10, TimeUnit.SECONDS);
			WebElement Userworkspace = ScriptRunner.driver.findElement(By
					.xpath(".//*[@id='workspace']/div[2]/div/div"));
			if (Userworkspace.isDisplayed()) {
				// Reporter.detailedReportEvent("User Workspace is displayed",
				// "Pass");
			} else {
				// Reporter.detailedReportEvent("User Workspace is not displayed ",
				// "Fail");
			}
		} catch (Exception e) {
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150),
					"Exception");
		}

	}

	@Test
	public void searchOption() throws ParserConfigurationException,
			SAXException, IOException, TransformerException,
			InterruptedException {

		try {
			// Enter any Vessel's name in the search bar at the top right
			// corner, For Example - Horizon Pacific, in 'Managed Ships' panel
			ScriptRunner.driver.manage().timeouts()
					.implicitlyWait(30, TimeUnit.SECONDS);
			WebElement search = ScriptRunner.driver
					.findElement(By
							.xpath(".//*[@id='workspace']/div[2]/div/div/div[3]/div/div[1]/div[1]/div[3]/input"));
			search.sendKeys("Hapag-Lloyd");
			Reporter.detailedReportEvent("Vessel Name Entered", "Pass");

			// Verify And Report - Particular Ship is filtered in a single row
			// in the 'Managed Ships' panel
			ScriptRunner.driver.manage().timeouts()
					.implicitlyWait(10, TimeUnit.SECONDS);

			WebElement shipName = ScriptRunner.driver.findElement(By
					.xpath(".//*[@id='workspace']/div[1]/div/div"));
			if (shipName.isDisplayed()) {
				Reporter.detailedReportEvent("Shipname, along with other informations for the respective ship, is displayed in only one row",
				 "Pass");
			} else {
				 Reporter.detailedReportEvent("Shipname, along with other informations for the respective ship, is not displayed in only one row",
				 "Fail");
			}
		} catch (Exception e) {
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150),
					"Exception");
		}

	}

	@Test
	public void vesselsearched() throws ParserConfigurationException,
			SAXException, IOException, TransformerException,
			InterruptedException {

		try {
			// Enter any Vessel's name in the search bar at the top right
			// corner, For Example - Horizon Pacific, in 'Managed Ships' panel
			// Click the filtered Vessel Profile
			WebElement shipViewer = ScriptRunner.driver
					.findElement(By
							.xpath("html/body/div[4]/div[4]/div[2]/div/div/div[3]/div/div[1]/div[3]/div/div/div[1]/div[2]/div[1]/div/div/div[1]/div"));
			shipViewer.click();
			Reporter.detailedReportEvent("Filtered Vessel clicked ", "Pass");

			// Click Hamburger a top left cornert
			WebElement Hamburger = ScriptRunner.driver
					.findElement(By
							.xpath("html/body/div[4]/div[4]/div[2]/div/div/div[3]/div/div[1]/div[1]/div[1]/button"));
			Hamburger.click();
			Reporter.detailedReportEvent("Hamburger Button clicked ",
			"Pass");

			// Click Impersonate
			WebElement dropdownoption = ScriptRunner.driver
					.findElement(By
							.xpath("html/body/div[4]/div[4]/div[2]/div/div/div[3]/div/div[1]/div[1]/div[1]/ul/li[2]/a"));
			dropdownoption.click();
			Reporter.detailedReportEvent("Impersonate clicked ", "Pass");

			WebElement admin = ScriptRunner.driver.findElement(By
					.xpath(".//*[@id='workspace']"));
			if (admin.isDisplayed()) {
				Reporter.detailedReportEvent("Admin(e.g- Hapag-Lloyd) Profile is displayed",
				"Pass");
			} else {
				Reporter.detailedReportEvent("Admin(e.g- Hapag-Lloyd) Profile is not displayed",
				"Fail");
			}
		} catch (Exception e) {
			Reporter.detailedReportEvent(e.getMessage().substring(0, 150),
					"Exception");
		}
		Thread.sleep(5000);

	}
}
