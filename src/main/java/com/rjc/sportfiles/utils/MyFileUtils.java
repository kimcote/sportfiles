package com.rjc.sportfiles.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rjc.sportfiles.model.TrainingCenterDatabase;

@Component
public class MyFileUtils {
	
	@Autowired
	private TrainingCenterDatabaseUtils tcdUtils;

	public String myReadFile(String fileName) throws IOException, URISyntaxException {
		
		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream(fileName);
		String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		return text;
	}

	public static String readFile(String fileName) throws IOException {
	    File file = new File(fileName);
	    return FileUtils.readFileToString(file);
	}
	
	public Collection<?> getFiles(File path, String[] extensions) {
		return FileUtils.listFiles(path, extensions, true);
	}
	
	public File getFileFromFolderList(List<String> folders, String dateToFind ) throws Exception {
		
		for (String folder: folders) {
		
			try {
				return getFileFromIdDateTime(folder, dateToFind);
			} catch (Exception e0) {}
		}
		throw new Exception("File Not Found");
	}
	
	public File getFileFromDate(String pathStr, String date) {
		String[] extensions={"tcx"};
		System.out.println("Search folder="+pathStr);
		
		File path = new File(pathStr);
		
		Collection<?> files = getFiles(path, extensions);
		
		for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			
			if (file.getName().contains(date)) {
//				System.out.println(file.getName());
				return file;
			}
		}
		
//		System.out.println("getFileFromDate: No File Found");
		return null;
    }
	
	public File getFitFileFromDateSport(String pathStr, String date, String sport) {

		date=date.replace("-","_");
		
		System.out.println("Search folder="+pathStr);
		System.out.println("Search params: Date="+ date + " Sport="+sport);
		
		File path = new File(pathStr);
		
		String[] extensions={"fit"};
		Collection<?> files = getFiles(path,extensions);
		
		for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();

			if (file.getName().contains(date) 
			 && file.getName().contains(sport)) {
//				System.out.println(file.getName());
				return file;
			}
		}
		
		System.out.println("getFitFileFromDateSport: No File Found");
		return null;
    }
	
	public File getFileFromIdDate(String pathStr, String date) throws Exception {
		String[] extensions={"tcx"};
		System.out.println("Search folder="+pathStr);
		
		File path = new File(pathStr);
		
		List<File> files = (List<File>) getFiles(path, extensions);
		files.sort(Comparator.comparing(File::getName));
		
		for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			
			String idDate = tcdUtils.getIdDate(file).toString();
//			System.out.println(file.getName()+ " Date=" + idDate);
			if (idDate.equals(date)) {
				return file;
			}
		}

		throw new Exception("getFileFromDate: No File Found");
    }
	
	public File getFileFromIdDateTime(String pathStr, String dateTime) throws Exception {
		String[] extensions={"tcx"};
		System.out.println("Search folder="+pathStr);
		
		File path = new File(pathStr);
		
		List<File> files = (List<File>) getFiles(path, extensions);
		files.sort(Comparator.comparing(File::getName));
		
		for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			
			String idDate = tcdUtils.getIdDateTime(file).toString();
//			System.out.println(file.getName()+ " Date=" + idDate);
			if (idDate.contains(dateTime)) {
				return file;
			}
		}

		throw new Exception("getFileFromDate: No File Found in folder="+pathStr);
    }
	
	public List<String> getResourceFiles(String path) throws IOException {
		System.out.println(path);
	    List<String> filenames = new ArrayList<>();

	    try (
	        InputStream in = getResourceAsStream(path);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
	        String resource;

	        while ((resource = br.readLine()) != null) {
	            filenames.add(resource);
	        }
	    }

	    return filenames;
	}

	private InputStream getResourceAsStream(String resource) {
	    final InputStream in
	            = getContextClassLoader().getResourceAsStream(resource);

	    return in == null ? getClass().getResourceAsStream(resource) : in;
	}
	
	private ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}
	
	public void saveFile(TrainingCenterDatabase tcd, String folder, String date) {
		
		try {
			String fileName=date + "_IndoorRowing.tcx";
			File file = new File(folder+fileName);
			JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);
		
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tcd, file);	
			
			System.out.println("File Saved: " +file.getPath() );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
