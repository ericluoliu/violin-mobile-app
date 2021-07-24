package com.example.violin_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;


public class realRecordAudio extends AppCompatActivity {

    private long timeDelay;
    private TextView beat;
    private TextView circle;
    private Handler UIHandler;                          //update UI-metronome
    private Handler startHandler;                       //start record
    private Handler stopHandler;                        //stop record
    private Handler resultHandler;                      //create UI for playing accuracy and grade
    private AudioDispatcher dispatcher;                 //part of Tarsos DSP audio analysis
    private List<Float> inputFrequencies;               //stores frequencies of Audio Event
    private double[] averages;                          //averages the inputFrequencies for 15 intervals (15 notes)
    private int count = -1;                             //metronome counter
    private int timer = -1;                             //metronome timer
    private double[] frequencies;                       //stores actual frequencies of each note
    private String scale;                               //scale name
    private String[] notes;                             //notes in given scale


    private static final int NUMBER_OF_NOTES = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_record_audio);
        Bundle bundle = getIntent().getExtras();
        frequencies = bundle.getDoubleArray("frequencies");
        scale = bundle.getString("scale");
        timeDelay = 60000 / bundle.getInt("bpm");
        notes = bundle.getStringArray("notes");

        beat = findViewById(R.id.beat);
        circle = findViewById(R.id.metronome);
        Button details = findViewById(R.id.details);
        Button restart = findViewById(R.id.restart);
        details.setVisibility(View.INVISIBLE);
        restart.setVisibility(View.INVISIBLE);

        startHandler = new Handler();
        startHandler.postDelayed(() -> {
            startRecording();
            stopHandler = new Handler();
            stopHandler.postDelayed(() -> {
                dispatcher.stop();
                Toast.makeText(realRecordAudio.this, "STOPPED RECORDING", Toast.LENGTH_SHORT).show();
                resultHandler = new Handler();
                resultHandler.postDelayed(() -> {
                    int[] indices = approximateIndex(inputFrequencies);
                    averages = new double[15];
                    int index = 0;
                    double size = 0;
                    double avgValue = 0;
                    for (int i = 0; i < inputFrequencies.size(); i++) {
                        if (i == indices[index]) {
                            averages[index] = avgValue / size;
                            avgValue = 0;
                            size = 0;
                            index++;
                        }
                        if (inputFrequencies.get(i) > 0) {
                            avgValue += inputFrequencies.get(i);
                            size++;
                        }
                    }
                    averages[index] = avgValue / size;
                    double percentError = 0;
                    for (int i = 0; i < averages.length; i++) {
                        percentError += Math.abs(averages[i] - frequencies[i]) / frequencies[i];
                    }
                    percentError *= 100.0 / 15.0;
                    percentError = 100 - percentError;
                    double roundedError = Math.round(percentError * 100.0) / 100.0;

                    TextView errorView = findViewById(R.id.errorView);
                    errorView.setText((roundedError) + "% Accuracy");
                    beat.setVisibility(View.VISIBLE);
                    beat.setText(getLetterGrade(roundedError));

                    Handler newActivity = new Handler();
                    newActivity.postDelayed(() -> {
                        details.setVisibility(View.VISIBLE);
                        restart.setVisibility(View.VISIBLE);
                    }, 2000);
                }, 1000);
            }, 15 * timeDelay);
        }, 5 * timeDelay);
        UIHandler = new Handler();
        UIHandler.post(updateTextRunnable);
    }

    //divide the number of frequency samples as evenly as possible by the number of notes
    //the recorded frequency of some notes will be the average of one more sample than others
    private int[] approximateIndex(List<Float> inputList) {
        double samplesPerNote = (double) inputList.size() / (double) NUMBER_OF_NOTES;
        int upper = (int) Math.ceil(samplesPerNote);
        int lower = (int) Math.floor(samplesPerNote);
        int roundedSummation = 0;
        double summation = 0;
        boolean bool = Math.round(samplesPerNote) == upper;
        int[] ret = new int[NUMBER_OF_NOTES];
        int i = 0;

        while (i < 15) {
            if (bool) {
                roundedSummation += upper;
            } else {
                roundedSummation += lower;
            }
            summation += samplesPerNote;
            ret[i] = roundedSummation;
            if (roundedSummation <= summation) {
                bool = true;
            } else {
                bool = false;
            }
            i++;

        }
        return ret;
    }

    //begin the recording using Tarsos DSP
    private void startRecording() {
        inputFrequencies = new ArrayList<>();
        PackageManager pmanager = this.getPackageManager();
        if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            dispatcher =
                    AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
            PitchDetectionHandler pdh = (res, e) -> {
                final float pitchInHz = res.getPitch();
                runOnUiThread(() -> inputFrequencies.add(pitchInHz));
            };
            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);
            Thread analysis = new Thread(dispatcher, "Audio Dispatcher");
            analysis.start();

            Toast.makeText(realRecordAudio.this, "STARTED RECORDING", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(realRecordAudio.this, "NO MICROPHONE", Toast.LENGTH_SHORT).show();
        }
    }

    //UI metronome created by Handler calling itself with delay
    Runnable updateTextRunnable = new Runnable() {
        public void run() {
            if (count == 4) {
                count = 1;
            } else {
                count++;
            }
            timer++;
            beat.setText(String.valueOf(count));
            if (timer < 20) {
                UIHandler.postDelayed(updateTextRunnable, timeDelay);
            } else {
                wait(500);
                beat.setVisibility(View.INVISIBLE);
            }
        }

        public void wait(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    };

    //assign letter grade to user based on their playing accuracy
    private String getLetterGrade(double input) {
        if (input > 99) {
            return "S";
        }
        if (input > 98) {
            return "A";
        }
        if (input > 96) {
            return "B";
        }
        if (input > 92) {
            return "C";
        }
        if (input > 85) {
            return "D";
        }
        return "F";
    }

    //moves to new activity that provides more in-depth information on the accuracy of the
    //user's playing
    public void detailsActivity(View view) {
        Intent intent = new Intent(this, scoreDetails.class);
        intent.putExtra("inputFrequencies", averages);
        intent.putExtra("frequencies", frequencies);
        intent.putExtra("scale", scale);
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    //returns to main activity where user can test another scale
    public void returnToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
