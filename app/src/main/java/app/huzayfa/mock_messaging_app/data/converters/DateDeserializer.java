package app.huzayfa.mock_messaging_app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import androidx.room.TypeConverter;

/**
 * This class is used to convert and deserialize
 * the {@link Date} object to a specific date format {@link #DATE_FORMAT}
 *
 * @author Huzayfa
 */

public class DateDeserializer implements JsonDeserializer<Date> {
    private static String[] DATE_FORMAT = new String[]{"yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"};
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String date = json.getAsString();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            return df.parse(date);
        } catch (ParseException e) {
        }
        throw new JsonParseException("Unparseable date: \"" + json.getAsString()
                + "\". Supported formats: \n" + Arrays.toString(DATE_FORMAT));
    }

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("IST");
                df.setTimeZone(timeZone);
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @TypeConverter
    public static String dateToTimestamp(Date value) {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        df.setTimeZone(timeZone);
        return value == null ? null : df.format(value);
    }
}
