package com.talev.words;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_URL = "http://192.168.1.30/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "MyNewWord.kvtml";
    public static final String TAG = "dimko";
    private TextView tvWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.word);

        tvWord.setText(String.valueOf("TEST"));

        new DownloadFile().execute();

        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
//            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();

//            InputStream in_s = getApplicationContext().getAssets().open(KEY_FILE_NAME);
            /*Context context = this;
            AssetManager assManager = context.getAssets();
            InputStream in_s = assManager.open("MyNewWord.kvtml");*/

            File file = new File(getFilesDir(), KEY_FILE_NAME);
            InputStream in_s = new FileInputStream(file);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

//            Log.d(TAG, String.valueOf(parser.getLineNumber()));
            Log.d(TAG, "GO GO GO!!!");

            List<String> list = new ArrayList<>();
            String text = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String name = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                            Log.d(TAG, "getText: " + String.valueOf(parser.getText()));
                            text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "getName: " + parser.getPrefix());
                        if (name.equals("translation")) {
//                            Log.d(TAG, String.valueOf(parser.getAttributeValue(null, "id")));
//                            temperature = parser.getAttributeValue(null,"value");
//                            Log.d(TAG, "text" + text);
//                            Log.d(TAG, "getText: " + parser.getText());

                        }
                        break;
                }
                eventType = parser.next();
            }

/*                if(eventType == XmlPullParser.START_DOCUMENT) {

//                        Log.d(TAG, String.valueOf(parser.getText()));
//                    Log.d("dimko", "Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
//                    Log.d("dimko", "Start tag " + parser.getName());
                    if (parser.getName().equals("text")) {
                        list.add(parser.getText());
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
//                    Log.d(TAG, "End tag " + parser.getName());
                } else if(eventType == XmlPullParser.TEXT) {
//                    tvWord.setText(parser.getText());

//                    Log.d(TAG, "Text " + parser.getText());
                }
                eventType = parser.next();
            }
            Log.d(TAG, "End document");
            tvWord.setText(list.toString());*/

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        File file = new File(getFilesDir(), KEY_FILE_NAME);
        StringBuilder text = null;

        if (file.exists()) {
            text = new StringBuilder();
            String line;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line + "\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return text.toString();
    }

    private class DownloadFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(KEY_URL);
                InputStream inputStream = url.openStream();

                DataInputStream dataInputStream = new DataInputStream(inputStream);

                byte[] buffer = new byte[1024];
                int length = buffer.length;

                File file = new File(getFilesDir(), KEY_FILE_NAME);
                file.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((length = dataInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                fileOutputStream.flush();
                fileOutputStream.close();

                dataInputStream.close();
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
