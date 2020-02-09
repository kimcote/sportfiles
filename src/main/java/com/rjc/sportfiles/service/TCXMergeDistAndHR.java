package com.rjc.sportfiles.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rjc.sportfiles.model.TrainingCenterDatabase;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track.Trackpoint;
import com.rjc.sportfiles.model.TrainingCenterDatabase.Activities.Activity.Lap.Track.Trackpoint.HeartRateBpm;
import com.rjc.sportfiles.utils.MyFileUtils;
import com.rjc.sportfiles.utils.TrainingCenterDatabaseUtils;

@Component
public class TCXMergeDistAndHR {
	
	@Autowired
	private MyFileUtils myfileutils;
	
	@Autowired
	private TrainingCenterDatabaseUtils tcdUtils;

	public void process(String folderDist, String folderHR, String dateToMerge, String folderDest) {
		TrainingCenterDatabase tcdDist;
		TrainingCenterDatabase tcdHR;

		LocalTime timeDist;

		Short prevHR = 0;

		try {
			File fileDist = myfileutils.getFileFromIdDate(folderDist, dateToMerge);
			System.out.println(fileDist.getName());

			File fileHR = myfileutils.getFileFromDate(folderHR, dateToMerge);
			System.out.println(fileHR.getName());

			tcdDist = tcdUtils.getTrainingCenterDatabase(fileDist);
			tcdHR = tcdUtils.getTrainingCenterDatabase(fileHR);

			LocalDate dateDist = tcdUtils.getStartDate(tcdDist);
			LocalDate dateHR = tcdUtils.getStartDate(tcdHR);

			if (!dateDist.equals(dateHR)) {
				System.out.println("Diff Dates");
			}

			LocalTime startTimeDist = tcdUtils.getStartTime(tcdDist);
			LocalTime startTimeHR = tcdUtils.getStartTime(tcdHR);

			long diff = startTimeDist.until(startTimeHR, ChronoUnit.SECONDS);
			System.out.println("Dist.Start=" + startTimeDist + " HR.Start=" + startTimeHR + " Diff=" + diff);

			for (Lap lap : tcdDist.getActivities().getActivity().getLap()) {
				Track track = lap.getTrack();

				for (Trackpoint trackpoint : track.getTrackpoint()) {
					timeDist = trackpoint.getTime().toGregorianCalendar().toZonedDateTime().toLocalTime();
					Short hr = tcdUtils.getHR(tcdHR, timeDist, diff);
//					System.out.println(i+" "+trackpoint.getTime() + " HR="+hr);

					if (hr == 0) {
						hr = prevHR;
					}

					HeartRateBpm hrbpm = new HeartRateBpm();
					hrbpm.setValue(hr);
					trackpoint.setHeartRateBpm(hrbpm);

					prevHR = hr;
				}
			}

			myfileutils.saveFile(tcdDist, folderDest, dateToMerge);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
