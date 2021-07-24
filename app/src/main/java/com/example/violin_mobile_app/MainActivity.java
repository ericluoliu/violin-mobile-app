package com.example.violin_mobile_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int NUMBER_OF_NOTES = 15;
    private double[] frequencies = new double[NUMBER_OF_NOTES];     //store precise frequencies of notes in scale
    private String[] notes = new String[NUMBER_OF_NOTES];           //store note name of each note in scale
    private String scale;                                           //store scale name
    private String direction = "Ascending";
    private final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    //Creation of main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showRecordAudioPermission();                                //request user for recording permission
    }

    //permission request
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //permission request
    private void showRecordAudioPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                showExplanation("Permission Needed", "Record Audio to Capture Violin Playing", Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            } else {
                requestPermission(Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            }
        } else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    //permission request
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    //permission request
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    //shows user which scale was selected and direction of scale
    public void changeText(View view) {
        if (direction == null) {
            ((TextView) findViewById(R.id.userinfo)).setText(scale);
        } else if (scale == null) {
            ((TextView) findViewById(R.id.userinfo)).setText(direction);
        } else {
            ((TextView) findViewById(R.id.userinfo)).setText(direction + " " + scale);
        }
    }

    //onClick: ascending button
    public void ascending(View view) {
        if (frequencies != null && direction.equals("Descending")) {
            frequencies = reverseDoubleArray(frequencies);
            notes = reverseStrArray(notes);
        }
        direction = "Ascending";
        changeText(view);
    }

    //onClick: descending button
    public void descending(View view) {
        if (frequencies != null && direction.equals("Ascending")) {
            frequencies = reverseDoubleArray(frequencies);
            notes = reverseStrArray(notes);
        }
        direction = "Descending";
        changeText(view);
    }

    //start the next activity, transfer data to next activity
    public void play(View view) {
        EditText mEdit = findViewById(R.id.bpm);
        if (scale == null) {
            Toast.makeText(this, "PLEASE SELECT A SCALE", Toast.LENGTH_SHORT).show();
        } else if (mEdit.getText().toString().matches("")) {
            Toast.makeText(this, "PLEASE SELECT A BPM", Toast.LENGTH_SHORT).show();
        } else {
            int bpm = Integer.parseInt(mEdit.getText().toString());
            Intent intent = new Intent(this, recordAudio.class);
            intent.putExtra("frequencies", frequencies);
            intent.putExtra("scale", scale);
            intent.putExtra("bpm", bpm);
            intent.putExtra("notes", notes);
            startActivity(intent);
        }
    }

    //helper method to reverse a String array
    public String[] reverseStrArray(String[] inputArr) {
        String[] ret = new String[NUMBER_OF_NOTES];
        for (int i = 0; i < inputArr.length; i++) {
            ret[14 - i] = inputArr[i];
        }
        return ret;
    }

    //helper method to reverse a double array
    public double[] reverseDoubleArray(double[] inputArr) {
        double[] ret = new double[NUMBER_OF_NOTES];
        for (int i = 0; i < inputArr.length; i++) {
            ret[14 - i] = inputArr[i];
        }
        return ret;
    }

    /*

    NOTES AND FREQUENCIES FOR EACH SCALE

     */

    public void fmajor(View view) {
        scale = "F Major";
        frequencies[0] = 349.228;
        frequencies[1] = 391.995;
        frequencies[2] = 440;
        frequencies[3] = 466.164;
        frequencies[4] = 523.251;
        frequencies[5] = 587.33;
        frequencies[6] = 659.255;
        frequencies[7] = 698.456;
        frequencies[8] = 783.991;
        frequencies[9] = 880;
        frequencies[10] = 932.328;
        frequencies[11] = 1046.502;
        frequencies[12] = 1174.659;
        frequencies[13] = 1318.51;
        frequencies[14] = 1396.913;
        notes[0] = "F";
        notes[1] = "G";
        notes[2] = "A";
        notes[3] = "B♭";
        notes[4] = "C";
        notes[5] = "D";
        notes[6] = "E";
        notes[7] = "F";
        notes[8] = "G";
        notes[9] = "A";
        notes[10] = "B♭";
        notes[11] = "C";
        notes[12] = "D";
        notes[13] = "E";
        notes[14] = "F";
        changeText(view);
    }

    public void cmajor(View view) {
        scale = "C Major";
        frequencies[0] = 261.626;
        frequencies[1] = 293.665;
        frequencies[2] = 329.628;
        frequencies[3] = 349.228;
        frequencies[4] = 391.995;
        frequencies[5] = 440;
        frequencies[6] = 493.883;
        frequencies[7] = 523.251;
        frequencies[8] = 587.33;
        frequencies[9] = 659.255;
        frequencies[10] = 698.456;
        frequencies[11] = 783.991;
        frequencies[12] = 880;
        frequencies[13] = 987.767;
        frequencies[14] = 1046.502;
        notes[0] = "C";
        notes[1] = "D";
        notes[2] = "E";
        notes[3] = "F";
        notes[4] = "G";
        notes[5] = "A";
        notes[6] = "B";
        notes[7] = "C";
        notes[8] = "D";
        notes[9] = "E";
        notes[10] = "F";
        notes[11] = "G";
        notes[12] = "A";
        notes[13] = "B";
        notes[14] = "C";
        changeText(view);
    }

    public void gmajor(View view) {
        scale = "G Major";
        frequencies[0] = 195.998;
        frequencies[1] = 220;
        frequencies[2] = 246.942;
        frequencies[3] = 261.626;
        frequencies[4] = 293.665;
        frequencies[5] = 329.628;
        frequencies[6] = 369.994;
        frequencies[7] = 391.995;
        frequencies[8] = 440;
        frequencies[9] = 493.883;
        frequencies[10] = 523.251;
        frequencies[11] = 587.33;
        frequencies[12] = 659.255;
        frequencies[13] = 739.989;
        frequencies[14] = 783.991;
        notes[0] = "G";
        notes[1] = "A";
        notes[2] = "B";
        notes[3] = "C";
        notes[4] = "D";
        notes[5] = "E";
        notes[6] = "F#";
        notes[7] = "G";
        notes[8] = "A";
        notes[9] = "B";
        notes[10] = "C";
        notes[11] = "D";
        notes[12] = "E";
        notes[13] = "F#";
        notes[14] = "G";
        changeText(view);
    }

    public void dmajor(View view) {
        scale = "D Major";
        frequencies[0] = 293.665;
        frequencies[1] = 329.628;
        frequencies[2] = 369.994;
        frequencies[3] = 391.995;
        frequencies[4] = 440;
        frequencies[5] = 493.883;
        frequencies[6] = 554.365;
        frequencies[7] = 587.33;
        frequencies[8] = 659.255;
        frequencies[9] = 739.989;
        frequencies[10] = 783.991;
        frequencies[11] = 880;
        frequencies[12] = 987.767;
        frequencies[13] = 1108.731;
        frequencies[14] = 1174.659;
        notes[0] = "D";
        notes[1] = "E";
        notes[2] = "F#";
        notes[3] = "G";
        notes[4] = "A";
        notes[5] = "B";
        notes[6] = "C#";
        notes[7] = "D";
        notes[8] = "E";
        notes[9] = "F#";
        notes[10] = "G";
        notes[11] = "A";
        notes[12] = "B";
        notes[13] = "C#";
        notes[14] = "D";
        changeText(view);
    }

    public void amajor(View view) {
        scale = "A Major";
        frequencies[0] = 220;
        frequencies[1] = 246.942;
        frequencies[2] = 277.183;
        frequencies[3] = 293.665;
        frequencies[4] = 329.628;
        frequencies[5] = 369.994;
        frequencies[6] = 415.305;
        frequencies[7] = 440;
        frequencies[8] = 493.883;
        frequencies[9] = 554.365;
        frequencies[10] = 587.33;
        frequencies[11] = 659.255;
        frequencies[12] = 739.989;
        frequencies[13] = 830.609;
        frequencies[14] = 880;
        notes[0] = "A";
        notes[1] = "B";
        notes[2] = "C#";
        notes[3] = "D";
        notes[4] = "E";
        notes[5] = "F#";
        notes[6] = "G#";
        notes[7] = "A";
        notes[8] = "B";
        notes[9] = "C#";
        notes[10] = "D";
        notes[11] = "E";
        notes[12] = "F#";
        notes[13] = "G#";
        notes[14] = "A";
        changeText(view);
    }

    public void emajor(View view) {
        scale = "E Major";
        frequencies[0] = 329.628;
        frequencies[1] = 369.994;
        frequencies[2] = 415.305;
        frequencies[3] = 440;
        frequencies[4] = 493.883;
        frequencies[5] = 554.365;
        frequencies[6] = 622.254;
        frequencies[7] = 659.255;
        frequencies[8] = 739.989;
        frequencies[9] = 830.609;
        frequencies[10] = 880;
        frequencies[11] = 987.767;
        frequencies[12] = 1108.731;
        frequencies[13] = 1244.508;
        frequencies[14] = 1318.51;
        notes[0] = "E";
        notes[1] = "F#";
        notes[2] = "G#";
        notes[3] = "A";
        notes[4] = "B";
        notes[5] = "C#";
        notes[6] = "D#";
        notes[7] = "E";
        notes[8] = "F#";
        notes[9] = "G#";
        notes[10] = "A";
        notes[11] = "B";
        notes[12] = "C#";
        notes[13] = "D#";
        notes[14] = "E";
        changeText(view);
    }

    public void bmajor(View view) {
        scale = "B Major";
        frequencies[0] = 246.942;
        frequencies[1] = 277.183;
        frequencies[2] = 311.127;
        frequencies[3] = 329.628;
        frequencies[4] = 369.994;
        frequencies[5] = 415.305;
        frequencies[6] = 466.164;
        frequencies[7] = 493.883;
        frequencies[8] = 554.365;
        frequencies[9] = 622.254;
        frequencies[10] = 659.255;
        frequencies[11] = 739.989;
        frequencies[12] = 830.609;
        frequencies[13] = 932.328;
        frequencies[14] = 987.767;
        notes[0] = "B";
        notes[1] = "C#";
        notes[2] = "D#";
        notes[3] = "E";
        notes[4] = "F#";
        notes[5] = "G#";
        notes[6] = "A#";
        notes[7] = "B";
        notes[8] = "C#";
        notes[9] = "D#";
        notes[10] = "E";
        notes[11] = "F#";
        notes[12] = "G#";
        notes[13] = "A#";
        notes[14] = "B";
        changeText(view);
    }

    public void bflatmajor(View view) {
        scale = "B♭ Major";
        frequencies[0] = 233.082;
        frequencies[1] = 261.626;
        frequencies[2] = 293.665;
        frequencies[3] = 311.127;
        frequencies[4] = 349.228;
        frequencies[5] = 391.995;
        frequencies[6] = 440;
        frequencies[7] = 466.164;
        frequencies[8] = 523.251;
        frequencies[9] = 587.33;
        frequencies[10] = 622.254;
        frequencies[11] = 698.456;
        frequencies[12] = 783.991;
        frequencies[13] = 880;
        frequencies[14] = 932.328;
        notes[0] = "B♭";
        notes[1] = "C";
        notes[2] = "D";
        notes[3] = "E♭";
        notes[4] = "F";
        notes[5] = "G";
        notes[6] = "A";
        notes[7] = "B♭";
        notes[8] = "C";
        notes[9] = "D";
        notes[10] = "E♭";
        notes[11] = "F";
        notes[12] = "G";
        notes[13] = "A";
        notes[14] = "B♭";
        changeText(view);
    }

    public void eflatmajor(View view) {
        scale = "E♭ Major";
        frequencies[0] = 311.127;
        frequencies[1] = 349.228;
        frequencies[2] = 391.995;
        frequencies[3] = 415.305;
        frequencies[4] = 466.164;
        frequencies[5] = 523.251;
        frequencies[6] = 587.33;
        frequencies[7] = 622.254;
        frequencies[8] = 698.456;
        frequencies[9] = 783.991;
        frequencies[10] = 830.609;
        frequencies[11] = 932.328;
        frequencies[12] = 1046.502;
        frequencies[13] = 1174.659;
        frequencies[14] = 1174.659;
        notes[0] = "E♭";
        notes[1] = "F";
        notes[2] = "G";
        notes[3] = "A♭";
        notes[4] = "B♭";
        notes[5] = "C";
        notes[6] = "D";
        notes[7] = "E♭";
        notes[8] = "F";
        notes[9] = "G";
        notes[10] = "A♭";
        notes[11] = "B♭";
        notes[12] = "C";
        notes[13] = "D";
        notes[14] = "E♭";
        changeText(view);
    }

    public void aflatmajor(View view) {
        scale = "A♭ Major";
        frequencies[0] = 207.652;
        frequencies[1] = 233.082;
        frequencies[2] = 261.626;
        frequencies[3] = 277.183;
        frequencies[4] = 311.127;
        frequencies[5] = 349.228;
        frequencies[6] = 391.995;
        frequencies[7] = 415.305;
        frequencies[8] = 466.164;
        frequencies[9] = 523.251;
        frequencies[10] = 554.365;
        frequencies[11] = 622.254;
        frequencies[12] = 698.456;
        frequencies[13] = 783.991;
        frequencies[14] = 830.609;
        notes[0] = "A♭";
        notes[1] = "B♭";
        notes[2] = "C";
        notes[3] = "D♭";
        notes[4] = "E♭";
        notes[5] = "F";
        notes[6] = "G";
        notes[7] = "A♭";
        notes[8] = "B♭";
        notes[9] = "C";
        notes[10] = "D♭";
        notes[11] = "E♭";
        notes[12] = "F";
        notes[13] = "G";
        notes[14] = "A♭";
        changeText(view);
    }

    public void dflatmajor(View view) {
        frequencies[0] = 277.183;
        frequencies[1] = 311.127;
        frequencies[2] = 349.228;
        frequencies[3] = 369.994;
        frequencies[4] = 415.305;
        frequencies[5] = 466.164;
        frequencies[6] = 523.251;
        frequencies[7] = 554.365;
        frequencies[8] = 622.254;
        frequencies[9] = 698.456;
        frequencies[10] = 739.989;
        frequencies[11] = 830.609;
        frequencies[12] = 932.328;
        frequencies[13] = 1046.502;
        frequencies[14] = 1108.731;
        scale = "D♭ Major";
        notes[0] = "D♭";
        notes[1] = "E♭";
        notes[2] = "F";
        notes[3] = "G♭";
        notes[4] = "A♭";
        notes[5] = "B♭";
        notes[6] = "C";
        notes[7] = "D♭";
        notes[8] = "E♭";
        notes[9] = "F";
        notes[10] = "G♭";
        notes[11] = "A♭";
        notes[12] = "B♭";
        notes[13] = "C";
        notes[14] = "D♭";
        changeText(view);
    }
}