////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol
// devices only and remains the copyrighted property of Garmin Canada Inc.
// The software is being provided on an "as-is" basis and as an accommodation,
// and therefore all warranties, representations, or guarantees of any kind
// (whether express, implied or statutory) including, without limitation,
// warranties of merchantability, non-infringement, or fitness for a particular
// purpose, are specifically disclaimed.
//
// Copyright 2020 Garmin Canada Inc.
////////////////////////////////////////////////////////////////////////////////
// ****WARNING****  This file is auto-generated!  Do NOT edit this file.
// Profile Version = 21.22Release
// Tag = production/akw/21.22.00-0-g5065eaa
////////////////////////////////////////////////////////////////////////////////


package com.garmin.fit;

import java.util.HashMap;
import java.util.Map;

public class LegCurlExerciseName {
    public static final int LEG_CURL = 0;
    public static final int WEIGHTED_LEG_CURL = 1;
    public static final int GOOD_MORNING = 2;
    public static final int SEATED_BARBELL_GOOD_MORNING = 3;
    public static final int SINGLE_LEG_BARBELL_GOOD_MORNING = 4;
    public static final int SINGLE_LEG_SLIDING_LEG_CURL = 5;
    public static final int SLIDING_LEG_CURL = 6;
    public static final int SPLIT_BARBELL_GOOD_MORNING = 7;
    public static final int SPLIT_STANCE_EXTENSION = 8;
    public static final int STAGGERED_STANCE_GOOD_MORNING = 9;
    public static final int SWISS_BALL_HIP_RAISE_AND_LEG_CURL = 10;
    public static final int ZERCHER_GOOD_MORNING = 11;
    public static final int INVALID = Fit.UINT16_INVALID;

    private static final Map<Integer, String> stringMap;

    static {
        stringMap = new HashMap<Integer, String>();
        stringMap.put(LEG_CURL, "LEG_CURL");
        stringMap.put(WEIGHTED_LEG_CURL, "WEIGHTED_LEG_CURL");
        stringMap.put(GOOD_MORNING, "GOOD_MORNING");
        stringMap.put(SEATED_BARBELL_GOOD_MORNING, "SEATED_BARBELL_GOOD_MORNING");
        stringMap.put(SINGLE_LEG_BARBELL_GOOD_MORNING, "SINGLE_LEG_BARBELL_GOOD_MORNING");
        stringMap.put(SINGLE_LEG_SLIDING_LEG_CURL, "SINGLE_LEG_SLIDING_LEG_CURL");
        stringMap.put(SLIDING_LEG_CURL, "SLIDING_LEG_CURL");
        stringMap.put(SPLIT_BARBELL_GOOD_MORNING, "SPLIT_BARBELL_GOOD_MORNING");
        stringMap.put(SPLIT_STANCE_EXTENSION, "SPLIT_STANCE_EXTENSION");
        stringMap.put(STAGGERED_STANCE_GOOD_MORNING, "STAGGERED_STANCE_GOOD_MORNING");
        stringMap.put(SWISS_BALL_HIP_RAISE_AND_LEG_CURL, "SWISS_BALL_HIP_RAISE_AND_LEG_CURL");
        stringMap.put(ZERCHER_GOOD_MORNING, "ZERCHER_GOOD_MORNING");
    }


    /**
     * Retrieves the String Representation of the Value
     * @return The string representation of the value, or empty if unknown
     */
    public static String getStringFromValue( Integer value ) {
        if( stringMap.containsKey( value ) ) {
            return stringMap.get( value );
        }

        return "";
    }

    /**
     * Retrieves a value given a string representation
     * @return The value or INVALID if unkwown
     */
    public static Integer getValueFromString( String value ) {
        for( Map.Entry<Integer, String> entry : stringMap.entrySet() ) {
            if( entry.getValue().equals( value ) ) {
                return entry.getKey();
            }
        }

        return INVALID;
    }

}