package example.com.catatankeuangan;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends Base {

    EditText input_id;
    EditText input_description;
    EditText input_date;
    EditText input_amount;
    String validation_error_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction);
        String action = this.getIntent().getExtras().getString("action");
        if (action.equalsIgnoreCase("edit")) {
            setContentView(R.layout.transaction_edit);
            fill();
        } else {
            setContentView(R.layout.transaction_create);
        }
    }

    public void fill() {
        String id = this.getIntent().getExtras().getString("id");
        String description = this.getIntent().getExtras().getString("description");
        String date = this.getIntent().getExtras().getString("date");
        String amount = this.getIntent().getExtras().getString("amount");
        Log.i("values", description + "," + date + "," + amount);
        input_id = (EditText) findViewById(R.id.input_id);
        input_description = (EditText) findViewById(R.id.input_description);
        input_date = (EditText) findViewById(R.id.input_date);
        input_amount = (EditText) findViewById(R.id.input_amount);
        input_id.setText(id);
        input_description.setText(description);
        input_date.setText(date);
        input_amount.setText(amount);
//        input_description.setError("Description is required");
    }

    public void update(View view) {
        input_id = (EditText) findViewById(R.id.input_id);
        if (validate()) {
            try {
                TransactionTable transaction_table = new TransactionTable(getApplicationContext());
                SQLiteDatabase db = transaction_table.getWritableDatabase();
                String sql = "UPDATE " + TransactionTable.TABLE_NAME + " SET description='" +
                        input_description.getText() + "', date='" + input_date.getText() + "', amount=" +
                        input_amount.getText() + " WHERE " + BaseColumns._ID + "=" + input_id.getText() + ";";
                db.execSQL(sql);
                update_alert();
                Log.i("query", sql);
            } catch (Exception e) {
                error_alert();
            }
        }
    }

    public boolean validate() {
        int validation_errors = 0;
        validation_error_message = "";
        input_description = (EditText) findViewById(R.id.input_description);
        input_date = (EditText) findViewById(R.id.input_date);
        input_amount = (EditText) findViewById(R.id.input_amount);
        String description_text = input_description.getText().toString();
        if (is_required(description_text)) {
            input_description.setError(is_required_message("Description"));
            validation_errors++;
        }
        String date_text = input_date.getText().toString();
        if (is_required(date_text)) {
            input_date.setError(is_required_message("Date"));
            validation_errors++;
        }
        String amount_text = input_amount.getText().toString();
        if (is_required(amount_text)) {
            input_amount.setError(is_required_message("Amount"));
            validation_errors++;
        }
        if (validation_errors == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void save(View view) {
        if (this.validate()) {
            try {
                TransactionTable transaction_table = new TransactionTable(getApplicationContext());
                SQLiteDatabase db = transaction_table.getWritableDatabase();
                String sql = "INSERT INTO " + TransactionTable.TABLE_NAME + " (description, date, amount) VALUES" +
                        "('" + input_description.getText() + "', '" + input_date.getText() + "', " +
                        input_amount.getText() + ");";
                db.execSQL(sql);
                save_reset();
                save_alert();
                Log.i("query", sql);
            } catch (Exception e) {
                error_alert();
            }
        }
    }

    public void save_reset() {
        input_description.setText("");
        input_date.setText("");
        input_amount.setText("");
    }

    public void error_alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(TransactionActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("There's something error occur");
        alertDialog.show();
    }

    public void save_alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(TransactionActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Data has been saved successfully");
        alertDialog.show();
    }

    public void update_alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(TransactionActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Data has been updated successfully");
        alertDialog.show();
    }

}
