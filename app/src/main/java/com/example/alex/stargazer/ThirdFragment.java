package com.example.alex.stargazer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThirdFragment extends Fragment implements RecyclerViewAdapter.ClickListener{
    View mView;

    RecyclerView recyclerView;
    SecondRecyclerViewAdapter adapter;
    JSONObject mJson;
    JSONArray jsonArray;
    TextToSpeech t1;
    List<Constellation> constellations;


    public void onClick(View view, int position) {
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
        Intent i = new Intent(getActivity(), InfoActivity.class  );
        i.putExtra("name", constellations.get(position).getName());
        i.putExtra("abbr", constellations.get(position).getAbbr());
        i.putExtra("genitive", constellations.get(position).getGenitive());
        i.putExtra("eng", constellations.get(position).getEng());


        startActivity(i);

    }
    public void onLongClick(View view, final int position) {
        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    t1.speak(constellations.get(position).getName(), TextToSpeech.QUEUE_FLUSH,null, null);
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initRecyclerView();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.third_fragment, container, false);
        constellations = new ArrayList<Constellation>();
        new DownloadJSON(ThirdFragment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        initRecyclerView();


        return mView;    }

    private void initRecyclerView() {
        recyclerView = mView.findViewById(R.id.recyclerView2);
        adapter = new SecondRecyclerViewAdapter(constellations, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


    }

    public class DownloadJSON extends AsyncTask<Void, Integer, Void> {
        public RecyclerViewAdapter.ClickListener c;  //need to get main

        public DownloadJSON(RecyclerViewAdapter.ClickListener c){ this.c = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        // parse JSON info and put it in Caricature object
        @Override
        protected Void doInBackground(Void... params) {

            //nameAndJob = new ArrayList<String>();

            try {
               //mJson = JsonReader.readJsonFromUrl("http://api.open-notify.org/astros.json");
                //mJson = JsonReader.readJsonFromUrl("http://www.cs.bc.edu/~signoril/people.json");
                mJson = JsonReader.readJsonFromUrl("https://api.myjson.com/bins/sywpi");
                jsonArray = mJson.getJSONArray("constellations");
                for (int i = 0; i< jsonArray.length(); i++) {
                    mJson = jsonArray.getJSONObject(i);
                    Constellation cons = new Constellation("","","","");
                    cons.setName(mJson.optString("name"));
                    cons.setAbbr(mJson.optString("abbr"));
                    cons.setGenitive(mJson.optString("genitive"));
                    cons.setEng(mJson.optString("en"));
                    constellations.add(cons);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // put results in ListView
        @Override
        protected void onPostExecute(Void args) {
            //ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
            //TextView loading = (TextView) findViewById(R.id.textView2);
            //loading.setVisibility(View.INVISIBLE);

            // create an Object for Adapter
            adapter = new SecondRecyclerViewAdapter(constellations,getActivity());

            adapter.notifyDataSetChanged();

            // set the adapter object to the Recyclerview
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(c); //this is important since need MainActivity.this


        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }


}
