package app.huzayfa.mock_messaging_app.data.helper;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MethodUtility {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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
        sdf = new SimpleDateFormat("HH:mm", locale);

        return sdf.format(newDate);

    }
}
