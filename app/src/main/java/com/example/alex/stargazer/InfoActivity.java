package com.example.alex.stargazer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_main);
        Bundle extras = getIntent().getExtras();
        TextView mName = (TextView) findViewById(R.id.const_name);
        TextView mAbbr = (TextView) findViewById(R.id.const_abbr);
        TextView mGen = (TextView) findViewById(R.id.const_gen);
        TextView mEng = (TextView) findViewById(R.id.const_eng);
        mName.setText(extras.getString("name"));
        mAbbr.setText(extras.getString("abbr"));
        mGen.setText(extras.getString("genitive"));
        mEng.setText(extras.getString("eng"));

        Button mButton = (Button) findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://en.wikipedia.org/wiki/" + getIntent().getStringExtra("name") + "_(constellation)";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
}
