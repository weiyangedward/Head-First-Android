package com.example.weiyang.starbuzz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        // get the drink from intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);

        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            // create cursor
            Cursor cursor = db.query(
                    "DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkNo)},
                    null, null, null);

            // move cursor and read date
            if (cursor.moveToFirst()){
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);

                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }

            // close cursor and db
            cursor.close();
            db.close();
        }catch (SQLiteException e){
            e.printStackTrace();
            // show an error msg on screen
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view){
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("FAVORITE", favorite.isChecked());
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try{
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
            db.close();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
