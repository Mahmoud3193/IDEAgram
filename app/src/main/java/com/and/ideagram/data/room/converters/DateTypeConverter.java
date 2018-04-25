package com.and.ideagram.data.room.converters;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by file1 on 08/04/2018.
 */


public class DateTypeConverter {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);


        @TypeConverter
        public static Date toDate(String value) {
            Date convertedDate = null;
            if(value != null) {
                convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return convertedDate;
        }

        @TypeConverter
        public static String toString(Date value) {
            String date = null;
            if(value != null) {
                date = dateFormat.format(value);
            }
            return date;
        }

}
