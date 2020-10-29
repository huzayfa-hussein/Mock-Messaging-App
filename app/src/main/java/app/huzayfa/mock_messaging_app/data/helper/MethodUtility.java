package app.huzayfa.mock_messaging_app.data.helper;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MethodUtility {

    public static void toast(Context context, String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isEven(int num) {
        return (num % 2) == 0;
    }

    public static String dateToString(Date date) {
        Locale locale = new Locale("en");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", locale);
        Date newDate = date;
        try {
            newDate = sdf.parse(String.valueOf(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(checkIfToday(newDate) ? "HH:mm" : "dd/MM/yyyy", locale);

        return sdf.format(newDate);

    }

    public static boolean checkIfToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        return today.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR);
    }
}
