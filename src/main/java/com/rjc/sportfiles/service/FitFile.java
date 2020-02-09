package com.rjc.sportfiles.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.garmin.fit.Decode;
import com.garmin.fit.FitRuntimeException;
import com.rjc.sportfiles.utils.MyFileUtils;

@Component
public class FitFile {

//private static String filePath = "20100501-094759-1-1018-ANTFS-4.FIT";
//private static String filePath = "2010-08-12-17-44-48.fit";
//private static String filePath = "FR310/20110109-110521-1-1018-ANTFS-4.FIT";
	private String filePath = "Activity.fit";
	private File fitFile;
	private FileInputStream fitFileInputStream;
	private Decode fitFileDecode;
	
	private List<String> folders;
	
	private final String iCLOUD_MOVES = "/Users/richardchapman/Library/Mobile Documents/com~apple~CloudDocs/Moves/";

	@Autowired
	private MyFileUtils myfileutils;
	
	public FitFile() {
			
		folders = new ArrayList<String>();
		
		folders.add(iCLOUD_MOVES + "xTrackpoints/");
		folders.add(iCLOUD_MOVES + "xMerged/");
		folders.add(iCLOUD_MOVES + "Concept2/");
		folders.add(iCLOUD_MOVES + "Movescount_Converted/");
	}

	/**
	 * @param args
	 */
	public void process(String dateProcess) {
// TODO Auto-generated method stub
		try {
			File fitFile = myfileutils.getFitFileFromDateSport(iCLOUD_MOVES+"Movescount_191225/Moves/", dateProcess, "rowing");
			System.out.println(fitFile.getName());
		
			fitFileInputStream = new FileInputStream(fitFile);
			
//			if (!fitFile.canRead())
//				throw new Exception("Unable to read the input file: " + filePath);
			
//			if (!fitFileDecode.isFileFit(fitFileInputStream))
//				throw new Exception("The following file is not a FIT file: " + filePath);
			/*
			 * if (!Decode.checkIntegrity(fitFileInputStream)) throw new
			 * Exception("There are integrity issues with the following FIT file: " +
			 * filePath);
			 */
			fitFileDecode = new Decode();
			System.out.println(fitFileDecode.read(fitFileInputStream));
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (FitRuntimeException fre) {
			System.out.println("FitRuntimeExeption: " + fre.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
		}
	}
}