package com.release.violin_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class recordAudio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        Bundle bundle = getIntent().getExtras();                                    //access data from previous activity
        final double[] frequencies = bundle.getDoubleArray("frequencies");
        final String scale = bundle.getString("scale");
        final int bpm = bundle.getInt("bpm");
        final String[] notes = bundle.getStringArray("notes");
        Intent intent = new Intent(this, realRecordAudio.class);
        TextView textView = findViewById(R.id.startPlaying);
        textView.postDelayed(() -> {                                                //this screen displays for 7 seconds
            intent.putExtra("frequencies", frequencies);
            intent.putExtra("scale", scale);
            intent.putExtra("bpm", bpm);
            intent.putExtra("notes", notes);
            startActivity(intent);
        }, 7000);
    }

}