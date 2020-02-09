package com.rjc.sportfiles.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.rjc.sportfiles.model.TrainingCenterDatabase;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track.Trackpoint;

@Component
public class TrainingCenterDatabaseUtils {

	public TrainingCenterDatabase getTrainingCenterDatabase(File file) {

		try {
//			System.out.println(file.length());

			JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (TrainingCenterDatabase) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	public TrainingCenterDatabase getTrainingCenterDatabase(String fileName) {

		try {
			File file = ResourceUtils.getFile("classpath:xml/" + fileName);

//			System.out.println(file.length());

			JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (TrainingCenterDatabase) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Activity getActivity(String fileName) {
		try {
			File file = ResourceUtils.getFile("classpath:xml/" + fileName);

//			System.out.println(file.length());

			JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			TrainingCenterDatabase tcd = (TrainingCenterDatabase) jaxbUnmarshaller.unmarshal(file);

			Activities activities = tcd.getActivities();
			return activities.getActivity();
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public LocalDate getIdDate(File file) {
		
		try {
			TrainingCenterDatabase tcd = getTrainingCenterDatabase(file);
			Activity activity = tcd.getActivities().getActivity();
			return activity.getId().toGregorianCalendar().toZonedDateTime().toLocalDate();
		} catch (Exception e) {
			System.out.println("Error getIdDate: File="+file.getName());
			e.printStackTrace();
			return null;
		}
	}
	
	public LocalDateTime getIdDateTime(File file) {
		
		try {
			TrainingCenterDatabase tcd = getTrainingCenterDatabase(file);
			Activity activity = tcd.getActivities().getActivity();
			return activity.getId().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
		} catch (Exception e) {
			System.out.println("Error getIdDate: File="+file.getName());
			e.printStackTrace();
			return null;
		}
	}
	
	public LocalDate getStartDate(TrainingCenterDatabase tcd) {
		return getStartDate(tcd.getActivities().getActivity());
	}

	public LocalDate getStartDate(Activity activity) {
		Lap lap = activity.getLap().get(0);
		Track track = lap.getTrack();
		return track.getTrackpoint().get(0).getTime().toGregorianCalendar().toZonedDateTime().toLocalDate();
	}
	
	public LocalDateTime getStartDateTime(TrainingCenterDatabase tcd) {
		
		Lap lap = tcd.getActivities().getActivity().getLap().get(0);
		Track track = lap.getTrack();
		return track.getTrackpoint().get(0).getTime().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
	}
	

	public LocalTime getStartTime(TrainingCenterDatabase tcd) {
		return getStartTime(tcd.getActivities().getActivity());
	}

	public LocalTime getStartTime(Activity activity) {
		Lap lap = activity.getLap().get(0);
		Track track = lap.getTrack();
		return track.getTrackpoint().get(0).getTime().toGregorianCalendar().toZonedDateTime().toLocalTime();
	}
	
	public LocalDateTime getEndDateTime(TrainingCenterDatabase tcd) {
		LocalDateTime dateTime = null;
		
		for (Lap lap : tcd.getActivities().getActivity().getLap()) {
			Track track = lap.getTrack();
			
			for (Trackpoint trackpoint : track.getTrackpoint()) {
				dateTime=trackpoint.getTime().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
			}
		}
		
		return dateTime;
	}
	
	public BigDecimal getDistanceMeters(TrainingCenterDatabase tcd) {
		Lap lap = tcd.getActivities().getActivity().getLap().get(0);
		return lap.getDistanceMeters();
	}

	public Short getHR(TrainingCenterDatabase tcd, LocalTime timeDist, long offset) {
		return getHR(tcd.getActivities().getActivity(), timeDist, offset);
	}
	
	public Short getHR(Activity activity, LocalTime timeDist, long offset) {
		LocalTime timeHR;

		Lap lap = activity.getLap().get(0);
		Track track = lap.getTrack();

		for (Trackpoint trackpoint : track.getTrackpoint()) {

			timeHR = trackpoint.getTime().toGregorianCalendar().toZonedDateTime().toLocalTime().minusSeconds(offset);

//			System.out.println("timeHR="+timeHR);

			if (timeHR.equals(timeDist)) {
				return trackpoint.getHeartRateBpm().getValue();
			}
		}
		return 0;
	}

	
}
