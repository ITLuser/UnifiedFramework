package LibraryFiles.CoreLibrary;

import java.io.IOException;

import jxl.read.biff.BiffException;

public class EntryPoint {

	public static void main(String[] args) throws BiffException, IOException {

		MapGenerator.mapCommonData();
		//creating object for UI Simulator class 
		UISimulator uiSimulator = new UISimulator();
		uiSimulator.setVisible(true);
		
	}
}