package com.hiro.hirotexttospeechapp;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private static final String TAG = "TestTTS";
    private String[] speechArray = new String[]{"Hi", "Hello", "Nice to meet you", "How is going?", "Hope everything is great!"};
    private int speechNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                // TTS 初期化
                if (TextToSpeech.SUCCESS == status){
                    Log.d(TAG, "initialized");
                } else {
                    Log.e(TAG, "failed to initialize");
                }
            }
        });

        Button ttsButton = (Button)findViewById(R.id.ttsButton);
        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechText();
                if(speechNo == 4){
                    speechNo = 0;
                } else {
                    ++speechNo;
                }
            }
        });
//        ttsButton.setOnClickListener((View.OnClickListener) this);
    }

    private void shutDown() {
        if (null != tts) {
            // to release the resource to TextToSpeech
            tts.shutdown();
        }
    }

    private void speechText(){
//        EditText editor = (EditText)findViewById(R.id.editText);
//        editor.selectAll();
        // EditText からテキスト取得
        // ここが自分の App の場合は取得した文字列になるんだと思う
        // String string = editor.getText().toString();

        String string = "This is a test speaking";

        if(0 < string.length()){
            if (tts.isSpeaking()){
                tts.stop();
                return;
            }

            setSpeechRate(1.0f);
            setSpeechPitch(1.0f);

            tts.speak(speechArray[speechNo], TextToSpeech.QUEUE_FLUSH, null);
//            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
//            tts.speak(string, TextToSpeech.QUEUE_FLUSH, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            setTtsListner();

            // tts.speak(text, TextToSpeech.QUEUE_FLUSH, null) に
            // KEY_PARAM_UTTERANCE_ID を HashMap で設定
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
//
//            tts.speak(string, TextToSpeech.QUEUE_FLUSH, map);
//            setTtsListner();
        }
    }

    // 読み上げのスピード
    private void setSpeechRate(float rate){
        if (null != tts) {
            tts.setSpeechRate(rate);
        }
    }

    // 読み上げのピッチ
    private void setSpeechPitch(float pitch){
        if (null != tts) {
            tts.setPitch(pitch);
        }
    }

    // 読み上げの始まりと終わりを取得
    private void setTtsListner(){
        // android version more than 15
        if(Build.VERSION.SDK_INT >= 15)
        {
            int listnerResult = tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.d(TAG,"progress on Start " + utteranceId);
                }

                @Override
                public void onDone(String utteranceId) {
                    Log.d(TAG,"progress on Done " + utteranceId);
                }

                @Override
                public void onError(String utteranceId) {
                    Log.d(TAG,"progress on Error " + utteranceId);
                }
            });
        }
        else
        {
            // less than 15th
            int listenerResult = tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener()
            {
                @Override
                public void onUtteranceCompleted(String utteranceId)
                {
                    Log.d(TAG,"progress on Completed " + utteranceId);
                }
            });

            if (listenerResult != TextToSpeech.SUCCESS)
            {
                Log.e(TAG, "failed to add utterance completed listener");
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        shutDown();
    }

}
