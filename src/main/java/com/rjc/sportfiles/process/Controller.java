package com.rjc.sportfiles.process;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rjc.sportfiles.model.TrainingCenterDatabase;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track.Trackpoint;
import com.rjc.sportfiles.service.FITConvertToTCX;
import com.rjc.sportfiles.service.TCXAddTrackpoints;
import com.rjc.sportfiles.service.TCXMergeDistAndHR;
import com.rjc.sportfiles.utils.MyDateTimeUtils;
import com.rjc.sportfiles.utils.MyFileUtils;
import com.rjc.sportfiles.utils.TrainingCenterDatabaseUtils;

@Component
public class Controller {

	@Autowired
	private MyFileUtils myfileutils;

	@Autowired
	private TrainingCenterDatabaseUtils tcdUtils;

	@Autowired
	private MyDateTimeUtils mydatetimeUtils;
	
	@Autowired
	private FITConvertToTCX FITconverttoTCX;
	
	@Autowired
	private TCXMergeDistAndHR TCXmergedistandHR;
	
	@Autowired
	private TCXAddTrackpoints TCXaddtrackpoints;

	
	private final String iCLOUD_MOVES = "/Users/richardchapman/Library/Mobile Documents/com~apple~CloudDocs/Moves/";

	private final String CONCEPT2 = iCLOUD_MOVES + "Concept2";
	private final String MOVESCOUNT = iCLOUD_MOVES + "Movescount_191225/Moves/";
	private final String CONVERTED = iCLOUD_MOVES + "Movescount_Converted/";
	private final String MERGED = iCLOUD_MOVES + "Merged/";
	private final String TRACKPOINTS = iCLOUD_MOVES + "Trackpoints/";
	
	private List<String> folders;
	
	public Controller() {
		
		folders = new ArrayList<String>();
		
		folders.add(iCLOUD_MOVES + "xTrackpoints/");
		folders.add(iCLOUD_MOVES + "xMerged/");
		folders.add(iCLOUD_MOVES + "xConcept2/");
		folders.add(iCLOUD_MOVES + "Movescount_Converted/");
	}
	
	public void process () {
		String dateToProcess="2017-04-02";
		
//		FITconverttoTCX.process(MOVESCOUNT, dateToProcess, CONVERTED);
    	     
//		TCXmergedistandHR.process(CONCEPT2, CONVERTED, dateToProcess, MERGED);
//    	controller.joinFiles("19-07-16");
    	
//    	controller.addTrackPoints(dateToProcess);
		TCXaddtrackpoints.process(CONCEPT2, dateToProcess, TRACKPOINTS);
//		TCXaddtrackpoints.process(MERGED, dateToProcess, TRACKPOINTS);
//        controller.displayLapDetails(dateToProcess);
//        fitFile.process("2018_04_16");
	}

	public void joinFiles(String dateProcess) {
		TrainingCenterDatabase tcdA;
		TrainingCenterDatabase tcdB;

		try {
			String dateTimeToFind = dateProcess + "T11:27";

			File fileA = myfileutils.getFileFromIdDateTime(iCLOUD_MOVES + "Concept2", dateTimeToFind);

			tcdA = tcdUtils.getTrainingCenterDatabase(fileA);
			LocalDateTime dateTimeEndA = tcdUtils.getEndDateTime(tcdA);

			System.out.println(fileA.getName() + " End=" + dateTimeEndA);

			dateTimeToFind = dateProcess + "T11:57";

			File fileB = myfileutils.getFileFromIdDateTime(iCLOUD_MOVES + "Concept2", dateTimeToFind);

			tcdB = tcdUtils.getTrainingCenterDatabase(fileB);
			LocalDateTime dateTimeStartB = tcdUtils.getStartDateTime(tcdB);

			System.out.println(fileB.getName() + " Start=" + dateTimeStartB);

			int diff = (int) dateTimeEndA.until(dateTimeStartB, ChronoUnit.SECONDS) - 2;

			System.out.println("diff=" + diff);

			BigDecimal distA = tcdUtils.getDistanceMeters(tcdA);

			Activity activityA = tcdA.getActivities().getActivity();
			List<Trackpoint> trackpointsA = activityA.getLap().get(0).getTrack().getTrackpoint();

			for (Lap lap : tcdB.getActivities().getActivity().getLap()) {
				Track track = lap.getTrack();

				for (Trackpoint trackpointB : track.getTrackpoint()) {
					XMLGregorianCalendar xgctimeB = mydatetimeUtils.minusSeconds(trackpointB.getTime(), diff);

					BigDecimal distB = trackpointB.getDistanceMeters();

					trackpointB.setTime(xgctimeB);
					trackpointB.setDistanceMeters(distB.add(distA));

					trackpointsA.add(trackpointB);
				}
			}

			myfileutils.saveFile(tcdA, iCLOUD_MOVES + "Merged/", dateProcess);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTrackPoints(String dateProcess) {
		File file ;
		
		try {
			
			try {
				file = myfileutils.getFileFromIdDateTime(iCLOUD_MOVES + "Concept2/", dateProcess);
			} catch (Exception e1) {
				try {
					file = myfileutils.getFileFromIdDateTime(iCLOUD_MOVES + "Merged/", dateProcess);
				} catch (Exception e2) {
					file = myfileutils.getFileFromIdDateTime(iCLOUD_MOVES + "Movescount_Converted/", dateProcess);
				}
			}
			
			System.out.println(file.getPath());

			TrainingCenterDatabase tcd = tcdUtils.getTrainingCenterDatabase(file);

			Lap lapFirst = tcd.getActivities().getActivity().getLap().get(0);
			XMLGregorianCalendar startTime = lapFirst.getStartTime();
			BigDecimal totalSecs = lapFirst.getTotalTimeSeconds();

			int lapCount = tcd.getActivities().getActivity().getLap().size();
			Lap lapLast = tcd.getActivities().getActivity().getLap().get(lapCount - 1);

			Trackpoint trackpointFirst = lapFirst.getTrack().getTrackpoint().get(0);

			Trackpoint trackpointNewFirst = new Trackpoint();

			trackpointNewFirst.setDistanceMeters(BigDecimal.ZERO);
			trackpointNewFirst.setTime(lapFirst.getStartTime());
			trackpointNewFirst.setCadence(trackpointFirst.getCadence());
			trackpointNewFirst.setExtensions(trackpointFirst.getExtensions());
			trackpointNewFirst.setHeartRateBpm(trackpointFirst.getHeartRateBpm());

//			System.out.println("LapFirst.size B4="+lapFirst.getTrack().getTrackpoint().size());
			lapFirst.getTrack().getTrackpoint().add(0, trackpointNewFirst);
//			System.out.println("LapFirst.size AF="+lapFirst.getTrack().getTrackpoint().size());

			int lapLastTrackpointCount = lapLast.getTrack().getTrackpoint().size();
			Trackpoint trackpointLast = lapLast.getTrack().getTrackpoint().get(lapLastTrackpointCount - 1);

			XMLGregorianCalendar finishTime = (XMLGregorianCalendar) startTime.clone();
			mydatetimeUtils.addSeconds(finishTime, totalSecs);
			System.out.println("TotSecs=" + totalSecs);
			System.out.println("Start=" + startTime);
			System.out.println("  End=" + finishTime);
			System.out.println("Dist =" + lapFirst.getDistanceMeters());

			Trackpoint trackpointNewLast = new Trackpoint();

			trackpointNewLast.setDistanceMeters(lapFirst.getDistanceMeters());
			trackpointNewLast.setTime(finishTime);
			trackpointNewLast.setCadence(trackpointLast.getCadence());
			trackpointNewLast.setExtensions(trackpointLast.getExtensions());
			trackpointNewLast.setHeartRateBpm(trackpointLast.getHeartRateBpm());

//			System.out.println("LapLast.size B4=" + lapLast.getTrack().getTrackpoint().size());
			lapLast.getTrack().getTrackpoint().add(trackpointNewLast);
//			System.out.println("LapLast.size AF=" + lapLast.getTrack().getTrackpoint().size());
			
			for (Lap lap : tcd.getActivities().getActivity().getLap()) {
				Track track = lap.getTrack();
				
				Iterator<Trackpoint> itr = track.getTrackpoint().iterator();
			    while (itr.hasNext()) {
			     
					Trackpoint tp = itr.next();
					
					if (tp.getTime().toGregorianCalendar().compareTo(finishTime.toGregorianCalendar()) > 0) {
						System.out.println("Remove Time="+tp.getTime());// + " HR="+tp.getHeartRateBpm().getValue());
						itr.remove();
//						System.out.println("Removed");
					}
				}
			}

			myfileutils.saveFile(tcd, iCLOUD_MOVES + "Trackpoints/", dateProcess);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayLapDetails(String dateProcess) {
		File file ;
		BigDecimal distActive = BigDecimal.ZERO;
		BigDecimal distResting = BigDecimal.ZERO;
		
		try {
//			file=myfileutils.getFileFromFolderList(folders, dateProcess);
			file=myfileutils.getFileFromIdDate(iCLOUD_MOVES+"Concept2/", dateProcess);
			System.out.println(file.getPath());

			TrainingCenterDatabase tcd = tcdUtils.getTrainingCenterDatabase(file);
			Activity act=tcd.getActivities().getActivity();
			
			System.out.println("Sport="+act.getSport());
			System.out.println("Id="+act.getId());
			System.out.println("isEmpty="+act.getLap().isEmpty());
			System.out.println("Lap Count=" + act.getLap().size());
			
			for (Lap lap : act.getLap()) {
		
				Trackpoint tpFirst = lap.getTrack().getTrackpoint().get(0);
	
				int lapTrackpointCount = lap.getTrack().getTrackpoint().size();
				Trackpoint tpLast = lap.getTrack().getTrackpoint().get(lapTrackpointCount - 1);
				
//				System.out.println("Lap.Start =" + lap.getStartTime());
//				System.out.println("Lap.Secs  =" + lap.getTotalTimeSeconds());
//				System.out.println("Lap.Dist  =" + lap.getDistanceMeters());
//				System.out.println("Intensity =" + lap.getIntensity());
//				System.out.println("First.Time=" + tpFirst.getTime());
//				System.out.println("First.Dist=" + tpFirst.getDistanceMeters());
//				System.out.println("Last.Time =" + tpLast.getTime());
//				System.out.println("Last.Dist =" + tpLast.getDistanceMeters());
//				System.out.println();
				
				if ("Active".equals(lap.getIntensity())) {
					distActive=distActive.add(lap.getDistanceMeters());
				} else {
					distResting=distResting.add(lap.getDistanceMeters());
				}
			}
			
			System.out.println("Total Active="+distActive);
			System.out.println("Total Rest  ="+distResting);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
