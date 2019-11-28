package example.com.catatankeuangan;

import android.support.v7.app.AppCompatActivity;

public class Base extends AppCompatActivity {

    public boolean is_required(String value) {
        return (value.equalsIgnoreCase("")) ? true : false;
    }

    public String is_required_message(String key) {
        return key + " is required.\n";
    }

    public boolean is_int(String value) {
        boolean result = false;
        try {
            int var_int = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            result = true;
        }
        return result;
    }

    public String is_int_message(String key) {
        return key + " is not number.\n";
    }
}
