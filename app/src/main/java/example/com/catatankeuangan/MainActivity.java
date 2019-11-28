package example.com.catatankeuangan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Base {

    ListView main_listview;
    CursorAdapter adapter;
    long id = 0;
    SQLiteCursor transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup_table();
        main_listview.setClickable(true);
        main_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id_selection) {
                id = id_selection;
                transaction = (SQLiteCursor) main_listview.getAdapter().getItem(position);
//                String id = transaction.getString(3);
                Log.i("action", "Record: " + position + " - " + id);
                action_alert();
//                edit(transaction);
//                delete(id);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        setup_table();
        Log.i("activity", "MainActivity Resumed");
    }

    public void action_alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Do you want to edit or delete the record ?");
        alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (id != 0) {
                    delete(id);
                    id = 0;
                    delete_alert();
                }
            }
        });
        alertDialog.setButton2("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (transaction != null) {
                    edit(transaction);
                    transaction = null;
                }
            }
        });
        alertDialog.show();
    }

    public void setup_table() {
        // Create database helper
        TransactionTable transaction_table = new TransactionTable(getApplicationContext());
        String[] column_names = {"description", "date", "amount", BaseColumns._ID};
        main_listview = (ListView) findViewById(R.id.main_listview);
        int[] target_layout_ids = {R.id.main_label_description, R.id.main_label_date, R.id.main_label_amount};
        SQLiteDatabase db = transaction_table.getReadableDatabase();
        Cursor transactions = db.query("transactions", column_names, null, null, null, null, null);
//        if( transactions != null ){
//            while(transactions.moveToNext()){
//                Log.i("label", transactions.getString(0));
//            }
//        }
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.main_listview, transactions, column_names, target_layout_ids, 0);
//        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.main_listview, transactions, column_names, target_layout_ids);
        main_listview.setAdapter(adapter);
    }

    public void delete(long id) {
        TransactionTable transaction_table = new TransactionTable(getApplicationContext());
        SQLiteDatabase db = transaction_table.getWritableDatabase();
        String insertSQL = "DELETE FROM " + TransactionTable.TABLE_NAME + " WHERE _id=" + id + ";";
        db.execSQL(insertSQL);
        setup_table();
        Log.i("query", insertSQL);
    }

    public void delete_alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Data has been deleted successfully");
        alertDialog.show();
    }

    public void edit(SQLiteCursor transaction) {
        String description = transaction.getString(0);
        String date = transaction.getString(1);
        String amount = transaction.getString(2);
        String id = transaction.getString(3);
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("description", description);
        intent.putExtra("date", date);
        intent.putExtra("amount", amount);
        intent.putExtra("id", id);
        startActivity(intent);
        Log.i("action", "Edit");
    }

    public void create(View view) {
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        intent.putExtra("action", "create");
        startActivity(intent);
    }
}
