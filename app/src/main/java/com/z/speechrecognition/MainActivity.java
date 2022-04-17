package com.z.speechrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    public static final Integer RecordAudioRequestCode = 1;
    String[] language = {"af", "sq", "ar-dz", "ar-bh", "ar-eg", "ar-iq", "ar-jo", "ar-kw", "ar-lb", "ar-ly", "ar-ma", "ar-om", "ar-qa", "ar-sa", "ar-sy", "ar-tn", "ar-ae", "ar-ye", "eu", "be", "bg", "ca", "zh-hk", "zh-cn", "zh-sg", "zh-tw", "hr", "cs", "da", "nl-be", "nl", "en", "en-au", "en-bz", "en-ca", "en-ie", "en-jm", "en-nz", "en-za", "en-tt", "en-gb", "en-us", "et", "fo", "fa", "fi", "fr-be", "fr-ca", "fr-lu", "fr", "fr-ch", "gd", "de-at", "de-li", "de-lu", "de", "de-ch", "el", "he", "hi", "hu", "is", "id", "ga", "it", "it-ch", "ja", "ko", "ko", "ku", "lv", "lt", "mk", "ml", "ms", "mt", "no", "nb", "nn", "pl", "pt-br", "pt", "pa", "rm", "ro", "ro-md", "ru", "ru-md", "sr", "sk", "sl", "sb", "es-ar", "es-bo", "es-cl", "es-co", "es-cr", "es-do", "es-ec", "es-sv", "es-gt", "es-hn", "es-mx", "es-ni", "es-pa", "es-py", "es-pe", "es-pr", "es", "es-uy", "es-ve", "sv", "sv-fi", "th", "ts", "tn", "tr", "ua", "ur", "ve", "vi", "cy", "xh", "ji"};
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    Intent speechRecognizerIntent;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.button);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,language);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    micButton.setImageResource(R.drawable.ic_mic_black_24dp);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),language[position] , Toast.LENGTH_LONG).show();
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language[position]);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}