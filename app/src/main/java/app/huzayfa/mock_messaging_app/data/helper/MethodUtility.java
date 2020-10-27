package app.huzayfa.mock_messaging_app.data.helper;

import android.content.Context;
import android.widget.Toast;

public class MethodUtility {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isEven(int num) {
        return (num % 2) == 0;
    }
}
