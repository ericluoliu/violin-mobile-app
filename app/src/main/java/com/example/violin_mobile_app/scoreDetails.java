package com.example.violin_mobile_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class scoreDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);
        Bundle bundle = getIntent().getExtras();
        double[] frequencies = bundle.getDoubleArray("frequencies");
        double[] inputFrequencies = bundle.getDoubleArray("inputFrequencies");
        String scale = bundle.getString("scale");
        String[] notes = bundle.getStringArray("notes");
        String[] screenText = new String[15];
        for (int i = 0; i < 15; i++) {
            double absDiff = Math.abs(frequencies[i] - inputFrequencies[i]);
            absDiff = Math.round(absDiff * 100.0) / 100.0;
            if (frequencies[i] > inputFrequencies[i]) {
                screenText[i] = "Play Higher " + (absDiff);
            } else {
                screenText[i] = "Play Lower " + (absDiff);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView scaleName = findViewById(R.id.scaleName);
        TextView inDepthDetails = findViewById(R.id.inDepthDetails);

        scaleName.setText(scale);
        inDepthDetails.setText(
                notes[0] + " : " + screenText[0] + " Hz\n" +
                        notes[1] + " : " + screenText[1] + " Hz\n" +
                        notes[2] + " : " + screenText[2] + " Hz\n" +
                        notes[3] + " : " + screenText[3] + " Hz\n" +
                        notes[4] + " : " + screenText[4] + " Hz\n" +
                        notes[5] + " : " + screenText[5] + " Hz\n" +
                        notes[6] + " : " + screenText[6] + " Hz\n" +
                        notes[7] + " : " + screenText[7] + " Hz\n" +
                        notes[8] + " : " + screenText[8] + " Hz\n" +
                        notes[9] + " : " + screenText[9] + " Hz\n" +
                        notes[10] + " : " + screenText[10] + " Hz\n" +
                        notes[11] + " : " + screenText[11] + " Hz\n" +
                        notes[12] + " : " + screenText[12] + " Hz\n" +
                        notes[13] + " : " + screenText[13] + " Hz\n" +
                        notes[14] + " : " + screenText[14] + " Hz"
        );

    }

    public void returnToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}