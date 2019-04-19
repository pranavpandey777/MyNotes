package com.example.mynotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Update extends AppCompatActivity {
    EditText title;
    EditText note;
    String ititle, inote;
    int id;
    Button update;
    Database database;
    DataDao dataDao;
    int resupdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        title = findViewById(R.id.title);
        note = findViewById(R.id.note);
        update = findViewById(R.id.update);
        database = Database.getUserDatabaseInstance(this);
        dataDao = database.dao();

        ititle = getIntent().getStringExtra("title");
        inote = getIntent().getStringExtra("note");
        id = getIntent().getIntExtra("id", 0);
        title.setText(ititle);
        note.setText(inote);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new UpdateUser().execute(String.valueOf(id),title.getText().toString(),note.getText().toString());
            }
        });
    }

    class UpdateUser extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            long id = Long.valueOf(strings[0]);
            String ititle = strings[1];
            String inote = strings[2];

            resupdate = dataDao.update(id, ititle, inote);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (resupdate == 0) {

                     //  Toast.makeText(Update.this, "Failed", Toast.LENGTH_SHORT).show();


                    } else {

                     //   Toast.makeText(Update.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Update.this,MainActivity.class));
                        finish();
                    }
                }
            });

            return null;
        }
    }


}
