package LibraryFiles.CoreLibrary;

import java.awt.Window;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
import com.sun.jna.platform.win32.Guid.GUID;

import jxl.read.biff.BiffException;
import jxl.Sheet;
import jxl.Workbook;

public class MapGenerator {

	public static FileInputStream fileInput;
	public static Workbook workBook;
	public static Sheet sheetName;
	public static LinkedHashMap<String, String> testExecutionList = new LinkedHashMap<String, String>();
	public static List< LinkedHashMap<String, String> > testData = new ArrayList< LinkedHashMap<String, String> >();
	public static LinkedHashMap<String, String> notestcaseid = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> commonData = new LinkedHashMap<String, String>();
	public static String current = System.getProperty("user.dir");
	public static String excelCommonDataPath = current.concat("\\Framework\\DataSets\\CommonData.xls");
	//public static String excelTestScriptExecutionPath = current.concat("\\Framework\\DataSets\\TestScriptExecution.xls");
	public static String excelTestDataPath;
	public static String couchdbTestDatabase;
	
	
	// function to generate maps to store the excel data
	public static void GenerateTestExecutionList(String text) throws BiffException, IOException{
						
		String[] commaSeperatedList = null;
		String[] tiltSeperatedList = null;
		String[] pipeSeperatedList = null;
		
		// generate Map for UI Frame execution 
		commaSeperatedList = text.split(",");
		for (int iComma=0; iComma<commaSeperatedList.length; iComma++){
			if (commaSeperatedList[iComma].contains("~")){
				tiltSeperatedList = commaSeperatedList[iComma].split("~");				
				for (int iTilt=0; iTilt<tiltSeperatedList.length; iTilt++){
						testExecutionList.put(tiltSeperatedList[iTilt], tiltSeperatedList[iTilt]);
				}
			}
			else if(commaSeperatedList[iComma].contains("|")){
				pipeSeperatedList = commaSeperatedList[iComma].split("\\|");				
				for (int iPipe=Integer.parseInt(pipeSeperatedList[0]); iPipe<=Integer.parseInt(pipeSeperatedList[1]); iPipe++){
						testExecutionList.put(Integer.toString(iPipe), Integer.toString(iPipe));
				}				
			}
			else{
				testExecutionList.put(commaSeperatedList[0], commaSeperatedList[0]);
			}
		}
				
	}
	
	public static void GenerateTestData(String TestScriptID) throws BiffException, IOException{

		int totalNoOfCols;
		int totalNoOfRows;
		int dataRow=1;
		int flowCount = 0;
		boolean multipleRowOccurance = false; 
		boolean specificRowData = false;
		LinkedHashMap<String, String> singleTestDataMap = null;
		
		// generate Map for test data for a particular test script
		fileInput = new FileInputStream(excelTestDataPath);
		workBook = Workbook.getWorkbook(fileInput);
		String [] sheetnames = workBook.getSheetNames();
		List<String> multipleOccurance = new ArrayList<String>();
		List<String> tempmultipleOccurance = new ArrayList<String>();
		for (int sheetNumber=0; sheetNumber<sheetnames.length; sheetNumber++){
			multipleOccurance.add(sheetnames[sheetNumber]);
		}
		
		while(!multipleOccurance.isEmpty()){
			singleTestDataMap = new LinkedHashMap<String, String>();
			multipleRowOccurance = false;
			for (int sheetNumber=0; sheetNumber<multipleOccurance.size(); sheetNumber++){
				if (multipleOccurance.get(sheetNumber).contains("~")){
					String [] sheetAndRows = multipleOccurance.get(sheetNumber).split("~");
					sheetName = workBook.getSheet(sheetAndRows[0]);
					dataRow = Integer.parseInt(sheetAndRows[1]);
					specificRowData = true;
				}
				else {
					sheetName = workBook.getSheet(multipleOccurance.get(sheetNumber));
					multipleRowOccurance = false;
					dataRow = 1;
				}
				totalNoOfCols = sheetName.getColumns();
				totalNoOfRows = sheetName.getRows();
				for (int i=dataRow; i<totalNoOfRows; i++){
					if (sheetName.getCell(0, i).getContents().toString().equalsIgnoreCase(TestScriptID)){
						if (multipleRowOccurance == false){
							for (int j=1; j<totalNoOfCols; j++){
								if (!sheetName.getCell(j, i).getContents().equals("")){
									singleTestDataMap.put(sheetName.getCell(j, 0).getContents(), sheetName.getCell(j, i).getContents());
									if (sheetNumber == 0 && sheetName.getCell(j, 0).getContents().contains("Flow")){
										flowCount++;
									}
								}
							}
							specificRowData = false;
							multipleRowOccurance = true;
						}
						else {
							multipleRowOccurance = false;
							tempmultipleOccurance.add( multipleOccurance.get(sheetNumber) + "~" + i );
							//multipleOccurance.remove(sheetNumber);
						}
					}
				}
				if (multipleRowOccurance == true){
					//multipleOccurance.remove(sheetNumber);
				}
			}
			testData.add(singleTestDataMap);
			if (!tempmultipleOccurance.isEmpty()){
				multipleOccurance.clear();
				multipleOccurance.addAll(tempmultipleOccurance);
			}
			if (tempmultipleOccurance.isEmpty()){
				multipleOccurance.clear();
			}
			tempmultipleOccurance.clear();
		}
		testData.get(0).put("FlowCounts",Integer.toString(flowCount));
	}
	
	public static String mapForTCID() throws BiffException, IOException{
		if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("Excel")){
			int totalNoOfRows;
			fileInput = new FileInputStream(excelTestDataPath);
			workBook = Workbook.getWorkbook(fileInput);
			sheetName = workBook.getSheet("Business Flow");
			if (sheetName != null){
				totalNoOfRows = sheetName.getRows();
				for (int i=1; i<totalNoOfRows; i++){
					if (!sheetName.getCell(0, i).getContents().equals("")){
						notestcaseid.put(Integer.toString(i), sheetName.getCell(0, i).getContents());
					}
				}
			}
			else{
				return "Businees Flow Sheet is not found in "+excelTestDataPath;
			}
		}else if (MapGenerator.commonData.get("DataSource").equalsIgnoreCase("CouchDB")){
			Session testDbSession = new Session("localhost", 5984);
			Database testDatabase = testDbSession.getDatabase(couchdbTestDatabase);
			ViewResults testDataViewResults = testDatabase.getAllDocuments();
			List<Document> testDocuments = testDataViewResults.getResults();
			int i = 1;
			for(Document document: testDocuments){				
				String id = document.getJSONObject().getString("id");
				notestcaseid.put(Integer.toString(i), id);
				i++;
			}
		}
		return null;
	}
	
	public static void mapCommonData() throws BiffException, IOException{
		int totalNoOfRows;
		fileInput = new FileInputStream(excelCommonDataPath);
		workBook = Workbook.getWorkbook(fileInput);
		sheetName = workBook.getSheet("Data");
		totalNoOfRows = sheetName.getRows();
		for (int i=1; i<totalNoOfRows; i++){
			if (!sheetName.getCell(0, i).getContents().equals("")){
				commonData.put(sheetName.getCell(0, i).getContents(), sheetName.getCell(1, i).getContents());
			}
		}
	}
	
	public static void GenerateCouchDBTestData(String TestScriptID) throws BiffException, IOException{
		
		LinkedHashMap<String, String> singleTestDataMap = new LinkedHashMap<String, String>();
		//singleTestDataMap = null;
		int flowCount = 0;
		
		Session testDbSession = new Session("localhost", 5984);
		Database testDatabase = testDbSession.getDatabase(couchdbTestDatabase);
		Document testDataDocument = testDatabase.getDocument(TestScriptID);
		Set<?> CouchKey =  testDataDocument.keySet();
		Iterator<?> iterator = CouchKey.iterator();
		while (iterator.hasNext()){
			String key = iterator.next().toString();
			String value = testDataDocument.get(key).toString();
/*			System.out.println("Key "+key );
			System.out.println("Value "+testDataDocument.get(key) );*/
			if (!key.equalsIgnoreCase("BusinessFlows")){
				singleTestDataMap.put(key, value);
			}
			if (key.equalsIgnoreCase("BusinessFlows")){
				String flowValues = (String) testDataDocument.get(key);
				String [] temp = flowValues.split(";");
				flowCount = temp.length;
				for (int i = 0; i < temp.length; i++){
					singleTestDataMap.put("Flow"+(i+1), temp[i]);
				}
			}
		}
		testData.add(singleTestDataMap);
		testData.get(0).put("FlowCounts",Integer.toString(flowCount));
	}
	
}
