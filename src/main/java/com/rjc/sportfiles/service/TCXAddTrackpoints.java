package com.rjc.sportfiles.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rjc.sportfiles.model.TrainingCenterDatabase;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track.Trackpoint;
import com.rjc.sportfiles.utils.MyDateTimeUtils;
import com.rjc.sportfiles.utils.MyFileUtils;
import com.rjc.sportfiles.utils.TrainingCenterDatabaseUtils;

@Component
public class TCXAddTrackpoints {
	
	@Autowired
	private MyFileUtils myfileutils;
	
	@Autowired
	private MyDateTimeUtils mydatetimeutils;
	
	@Autowired
	private TrainingCenterDatabaseUtils tcdUtils;

	public void process(String folderSource, String dateProcess, String folderDest) {
		
		try {
			File file = myfileutils.getFileFromIdDateTime(folderSource, dateProcess);
			System.out.println(file.getPath());

			TrainingCenterDatabase tcd = tcdUtils.getTrainingCenterDatabase(file);

			for (Lap lap : tcd.getActivities().getActivity().getLap()) {
				Track track = lap.getTrack();
				
				XMLGregorianCalendar startTime = lap.getStartTime();
				BigDecimal totalSecs = lap.getTotalTimeSeconds();
		
				Trackpoint trackpointFirst = lap.getTrack().getTrackpoint().get(0);
	
				Trackpoint trackpointNewFirst = new Trackpoint();
	
				trackpointNewFirst.setDistanceMeters(BigDecimal.ZERO);
				trackpointNewFirst.setTime(lap.getStartTime());
				trackpointNewFirst.setCadence(trackpointFirst.getCadence());
				trackpointNewFirst.setExtensions(trackpointFirst.getExtensions());
				trackpointNewFirst.setHeartRateBpm(trackpointFirst.getHeartRateBpm());
	
	//			System.out.println("LapFirst.size B4="+lapFirst.getTrack().getTrackpoint().size());
				lap.getTrack().getTrackpoint().add(0, trackpointNewFirst);
	//			System.out.println("LapFirst.size AF="+lapFirst.getTrack().getTrackpoint().size());
	
				int lapTrackpointCount = lap.getTrack().getTrackpoint().size();
				Trackpoint trackpointLast = lap.getTrack().getTrackpoint().get(lapTrackpointCount - 1);
	
				XMLGregorianCalendar finishTime = (XMLGregorianCalendar) startTime.clone();
				mydatetimeutils.addSeconds(finishTime, totalSecs);
				
				System.out.println("Start=" + startTime);
				System.out.println("  End=" + finishTime);
				System.out.println("TotSecs=" + totalSecs);
				System.out.println("Dist =" + lap.getDistanceMeters());
	
				if (lap.getDistanceMeters().compareTo(trackpointLast.getDistanceMeters()) > 0) {
					System.out.println("Add Last Trackpoint");
					Trackpoint trackpointNewLast = new Trackpoint();
		
					trackpointNewLast.setDistanceMeters(lap.getDistanceMeters());
					trackpointNewLast.setTime(finishTime);
					trackpointNewLast.setCadence(trackpointLast.getCadence());
					trackpointNewLast.setExtensions(trackpointLast.getExtensions());
					trackpointNewLast.setHeartRateBpm(trackpointLast.getHeartRateBpm());
		
					lap.getTrack().getTrackpoint().add(trackpointNewLast);
				} else {
					System.out.println("No Add. Last Trackpoint ="+trackpointLast.getDistanceMeters());
				}
				
				Iterator<Trackpoint> itr = track.getTrackpoint().iterator();
			    while (itr.hasNext()) {
			     
					Trackpoint tp = itr.next();
					
					if (tp.getTime().toGregorianCalendar().compareTo(finishTime.toGregorianCalendar()) > 0) {
						System.out.println("Remove Time="+tp.getTime() + " HR="+tp.getHeartRateBpm().getValue());
						itr.remove();
//						System.out.println("Removed");
					}
				}
			}

			myfileutils.saveFile(tcd, folderDest, dateProcess);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
