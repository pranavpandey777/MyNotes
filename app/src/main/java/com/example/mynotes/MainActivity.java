package com.example.mynotes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior mBottomSheetBehaviour;
    FloatingActionButton floatingActionButton;
    EditText title, note;
    Button save;
    RecyclerView rv;
    // RecyclerView.LayoutManager layoutManager;
    MyViewModel viewModel;
    DataDao dataDao;
    Database database;
    Data data;
    String ititle, inote;
    long id;
    MyRecyclerAdapter adapter;
    ArrayList<Data> arrayList;
    ArrayList<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2,1,false);

        // layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        database = Database.getUserDatabaseInstance(this);
        dataDao = database.dao();

        View nestedScrollView = findViewById(R.id.nestedScrollView);
        floatingActionButton = findViewById(R.id.fab);
        title = findViewById(R.id.title);
        note = findViewById(R.id.data);
        save = findViewById(R.id.save);
        mBottomSheetBehaviour = BottomSheetBehavior.from(nestedScrollView);

        //   mBottomSheetBehaviour.setPeekHeight(100);
        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            // Toast.makeText(getActivity(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        viewModel.getSavedData(this).observe(this, new Observer<ArrayList<Data>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Data> projects) {


                //    Toast.makeText(MainActivity.this, ""+projects, Toast.LENGTH_SHORT).show();

                adapter = new MyRecyclerAdapter(MainActivity.this, projects);
                rv.setAdapter(adapter);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (title.getText().toString().isEmpty()) {

                    title.setError("Fill");
                    return;
                }

                if (note.getText().toString().isEmpty()) {
                    note.setError("fill");
                    return;
                }


                ititle = title.getText().toString();
                inote = note.getText().toString();
                new Insertion().execute(ititle, inote);
                title.setText(null);
                note.setText(null);


                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


            }
        });


    }

    class Insertion extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            String ititle = strings[0];
            String inote = strings[1];


            data = new Data(ititle, inote);
            id = dataDao.insert(data);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id == -1) {


                        //  Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();


                    } else {

                        //    Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();

                        new Viewuser().execute();
                    }
                }
            });

            return null;
        }
    }

    class Viewuser extends AsyncTask<Void, Void, ArrayList<Data>> {


        @Override
        protected ArrayList<Data> doInBackground(Void... voids) {

            dataList = (ArrayList<Data>) dataDao.getdata();

            return dataList;
        }

        @Override
        protected void onPostExecute(ArrayList<Data> idata) {
            super.onPostExecute(idata);

            adapter.update(idata);


        }
    }

}
