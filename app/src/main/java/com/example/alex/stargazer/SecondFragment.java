package com.example.alex.stargazer;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment implements RecyclerViewAdapter.ClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Spot> spots = new ArrayList<>();
    private View mView;

    public void onClick(View view, int position) {
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
;

    }
    public void onLongClick(View view, int position) {
        //show Alertdialog to edit or update the
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
        showActionsDialog(position);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new DatabaseAsync(SecondFragment.this).execute(null, -1, null, 0.0, 0.0, null);  //MainActivity.this explain this usage
        MySingletonClass.getInstance().setValue2(spots);

        //initRecyclerView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.add:
                // do stuff here
                //adding  new event
                showEventDialog(false, null, -1);
                return true;
            case R.id.deleteAll:
                // do stuff here
                new DatabaseAsync(SecondFragment.this).execute(null, -2, null, 0.0, 0.0, null);  //MainActivity.this explain this usage
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showEventDialog(final Boolean shouldUpdate, final Spot spot, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.event_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        final EditText edt_title = (EditText) view.findViewById(R.id.edt_title);

        final EditText edt_longitude = (EditText) view.findViewById(R.id.edt_longitude);

        final EditText edt_latitude = (EditText) view.findViewById(R.id.edt_latitude);

        final EditText edt_info = (EditText) view.findViewById(R.id.edt_info);

        edt_latitude.setText("" +MySingletonClass.getInstance().getValue1().latitude);
        edt_longitude.setText("" +MySingletonClass.getInstance().getValue1().longitude);






        dialog_title.setText(!shouldUpdate ? "New Spot" : "Edit Spot");

        //in case of update we want all the
        //fields  to be set by default with text


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Update or add data into the database only when both field are  filled(i.e title,description)
                if (!TextUtils.isEmpty(edt_title.getText().toString()) && !TextUtils.isEmpty(edt_longitude.getText().toString()) &&
                        !TextUtils.isEmpty(edt_latitude.getText().toString()) && !TextUtils.isEmpty(edt_info.getText().toString())) {

                    // check if user updating note

                    if (shouldUpdate && spot != null) {
                        // update event
                        new DatabaseAsync(SecondFragment.this).execute(shouldUpdate, position, edt_title.getText().toString(), Double.parseDouble(edt_longitude.getText().toString()), Double.parseDouble(edt_latitude.getText().toString()), edt_info.getText().toString());
                        MySingletonClass.getInstance().setValue2(spots);

                    } else {
                        // create new event
                        new DatabaseAsync(SecondFragment.this).execute(shouldUpdate, -1, edt_title.getText().toString(), Double.parseDouble(edt_longitude.getText().toString()), Double.parseDouble(edt_latitude.getText().toString()), edt_info.getText().toString());
                        MySingletonClass.getInstance().setValue2(spots);


                    }
                } else {
                    Toast.makeText(getActivity(),"Please fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {

                    //show Alertdialog to update the event
                    showEventDialog(true, spots.get(position), position);
                } else {

                    //delete event from database
                    new DatabaseAsync(SecondFragment.this).execute(null, position, null, 0.0, 0.0, null);
                }
            }
        });
        builder.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.second_fragment, container, false);

        initRecyclerView();

        return mView;    }

    private void initRecyclerView() {
        recyclerView = mView.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(spots,this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


    }


    private class DatabaseAsync extends AsyncTask<Object, Void, List<Spot>> {
        public RecyclerViewAdapter.ClickListener c;  //need to get main

        // Constructor providing a reference to the views in MainActivity
        public DatabaseAsync(RecyclerViewAdapter.ClickListener c) {
            this.c = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Spot> doInBackground(Object... params) {
            AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "production").allowMainThreadQueries().build();

            Boolean shouldUpdate = (Boolean) params[0];
            int position = (int) params[1];
            String name = (String) params[2];
            double longitude = (double) params[3];
            double latitude = (double) params[4];
            String info = (String) params[5];

            //check whether to add add or update event based on if shouldUpdate is null
            if (shouldUpdate != null) {
                //update event if shouldUpdate is true
                if (shouldUpdate) {
                    Spot spot = spots.get(position);
                    spot.setName(name);
                    spot.setLongitude(longitude);
                    spot.setLatitude(latitude);
                    spot.setInfo(info);



                    //update event into the database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().updateSpot(spot);
                    //db.textBookDao().updateTextBook(textBook);

                } else {
                    //add event if shouldUpdate is false
                    Spot spot = new Spot(name,longitude,latitude,info);


                    //add event into the database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().addSpot(spot);

                    //db.textBookDao().addTextBook(textBook);

                }

            } else { //so no update since shouldUpdate == null
                //delete all if postion is = -2, really bad, i should fix this
                if (position == -2)
                    //delete all events  from database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().dropTheTable();

                    //db.textBookDao().dropTheTable();
                    //delete event
                else if (position != -1) { //-1 means delete a specific event
                    Spot spot = spots.get(position);

                    //delete event from database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().deleteSpot(spot);

                    //db.textBookDao().deleteTextBook(textBook);
                }
            }

            //get events from database, also not a great way to do this
            List<Spot> spots = AppDatabase.getAppDatabase(getActivity()).spotDao().getAllSpots();
            return spots;

        }
        @Override
        protected void onPostExecute(List<Spot> items) {

            //get list of events from doInBackground()
            spots = items;

            adapter = new RecyclerViewAdapter(spots, getActivity());
            adapter.setClickListener(c); //this is important since need MainActivity.this

            recyclerView.setAdapter(adapter);

            //shows NO EVENTS FOUND when list is empty
        }
    }


}
