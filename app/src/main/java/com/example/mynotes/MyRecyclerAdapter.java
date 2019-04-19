package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ProjectViewHolder> {
    private Context context;
    private List<Data> arraylist;
    DataDao dataDao;
    int res;
    Database database;
    ArrayList<Data> list;



    public MyRecyclerAdapter(Context context, List<Data> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        ProjectViewHolder viewHolder = new ProjectViewHolder(view, arraylist, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder holder, int position) {
        Data project = arraylist.get(position);

        holder.title.setText(project.getTitle());
        holder.note.setText(project.getNote());


    }

    public void update(ArrayList<Data> datas) {
        arraylist.clear();
        arraylist.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView note, title;
        MaterialCardView card;
        List<Data> arrayList;
        Context context;

        public ProjectViewHolder(View itemView, List<Data> arrayList, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            card = itemView.findViewById(R.id.card);
            this.arrayList = arrayList;
            this.context = context;
            itemView.setOnClickListener(this);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            PopupMenu popup = new PopupMenu(context, card);

            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //  Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                    switch (item.getItemId()) {
                        case R.id.delete:
                            database = Database.getUserDatabaseInstance(context);
                            dataDao = database.dao();
                            int pos=getAdapterPosition();
                            Data project = arraylist.get(pos);
                            int id=project.getId();
                            new deleteuser().execute(String.valueOf(id));
                            new Viewuser().execute();
                          //  Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();

                            break;
                        case R.id.update:

                            int ipos=getAdapterPosition();
                            Data iproject = arraylist.get(ipos);
                            Intent intent=new Intent(context,Update.class);
                            intent.putExtra("title",iproject.getTitle());
                            intent.putExtra("note",iproject.getNote());
                            intent.putExtra("id",iproject.getId());
                            context.startActivity(intent);

                           // Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
                            break;

                    }


                    return true;
                }
            });

            popup.show();


        }
    }
    class deleteuser extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            long id = Long.valueOf(strings[0]);

            res = dataDao.delete(id);


            return null;
        }
    }

    class Viewuser extends AsyncTask<Void, Void, ArrayList<Data>> {


        @Override
        protected ArrayList<Data> doInBackground(Void... voids) {

            list = (ArrayList<Data>) dataDao.getdata();

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Data> idata) {
            super.onPostExecute(idata);

            update(idata);


        }
    }
}
