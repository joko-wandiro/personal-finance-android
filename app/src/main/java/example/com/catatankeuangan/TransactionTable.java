package example.com.catatankeuangan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionTable extends SQLiteOpenHelper {
    public static final int DB_VERSION = 8;
    public static final String TABLE_NAME = "transactions";

    public TransactionTable(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT," +
                "type INTEGER," +
                "date TEXT," +
                "amount INTEGER" +
                ");";
        sqLiteDatabase.execSQL(createSQL);
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (description, type, date, amount) VALUES" +
                "('Rokok', 0, '21 November 2019', 10000)," +
                "('Gaji', 1, '22 November 2019', 100000)," +
                "('Rokok', 0, '23 November 2019', 10000)," +
                "('Rokok', 0, '24 November 2019', 14000);";
        sqLiteDatabase.execSQL(insertSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
