package com.talev.words;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
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
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_URL = "http://80.72.69.142/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "MyNewWord.kvtml";
    public static final String TAG = "dimko";
    private TextView tvWord;
    private Button btnDownload;
    private Button btnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnRead = (Button) findViewById(R.id.btn_read);

        tvWord.setText(String.valueOf(""));
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile().execute();
                tvWord.setText("File from server is download");
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmlPullParserFactory();
                tvWord.setText("Read");
            }
        });

    }

    public void testSimpleFrameWork() {
        String url =
                "http://dl.dropbox.com/u/7215751/JavaCodeGeeks/AndroidFullAppTutorialPart03/Transformers+2007.xml";

        DefaultHttpClient client = new DefaultHttpClient();

        try {
            String xmlData = retrieve(url);
            Serializer serializer = new Persister();
            Reader reader = new StringReader(xmlData);
            OpenSearchDescription osd =
                    serializer.read(OpenSearchDescription.class, reader, false);
            Log.d(MainActivity.class.getSimpleName(), osd.toString());
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
        }

    }

    private String retrieve(String url) {
        HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            if (getResponseEntity != null) {
                return EntityUtils.toString(getResponseEntity);
            }
        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }

        return null;
    }

    public void xmlPullParserFactory() {
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
