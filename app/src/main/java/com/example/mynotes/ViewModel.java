package com.example.mynotes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;


class MyViewModel extends ViewModel {


    DataDao userDao;
    Database myDatabase;
    Context context;
    ArrayList dataList;


    private MutableLiveData<ArrayList<Data>> projectList;


    public LiveData<ArrayList<Data>> getProjects() {


        if (projectList == null) {
            projectList = new MutableLiveData<>();

            loadProjects();


        }

        return projectList;
    }

    public void loadProjects() {


        myDatabase = Database.getUserDatabaseInstance(context);
        userDao = myDatabase.dao();
        new Viewuser().execute();


    }

    public LiveData<ArrayList<Data>> getSavedData(Context context) {

        if (projectList == null) {
            projectList = new MutableLiveData<>();
            myDatabase = Database.getUserDatabaseInstance(context);
            userDao = myDatabase.dao();


            new Viewuser().execute();


        }

        return projectList;


    }

    class Viewuser extends AsyncTask<Void, Void, ArrayList<Data>> {


        @Override
        protected ArrayList<Data> doInBackground(Void... voids) {

            dataList = (ArrayList<Data>) userDao.getdata();

            return dataList;
        }

        @Override
        protected void onPostExecute(ArrayList<Data> idata) {
            super.onPostExecute(idata);

            projectList.setValue(idata);


        }
    }


}
