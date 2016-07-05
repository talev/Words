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
    private Button btnReadSimple;
    private Button btnReadSimpleLast;

    public static final String url = "http://dl.dropbox.com/u/7215751/JavaCodeGeeks/AndroidFullAppTutorialPart03/Transformers+2007.xml";
    private DefaultHttpClient client = new DefaultHttpClient();

    public static final String urlTest = "http://80.72.69.142/Test.kvtml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnReadSimple = (Button) findViewById(R.id.btn_read_new);
        btnReadSimpleLast = (Button) findViewById(R.id.btn_read_last);

        tvWord.setText(String.valueOf(""));
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile().execute();
                tvWord.setText("File from server is download");
            }
        });

        btnReadSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSimpleFrameWork();
                tvWord.setText("Read form Simple Framework XML");
            }
        });

        btnReadSimpleLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTestSimpleFrameWork();
//                tvWord.setText("Reading...");
            }
        });

    }

    public void testSimpleFrameWork() {
        try {
//            String xmlData = retrieve(url);
            String xmlData = new TestDownloadFile().execute().get();
            Serializer serializer = new Persister();
            if (xmlData != null) {
                Reader reader = new StringReader(xmlData);
                OpenSearchDescription osd =
                        serializer.read(OpenSearchDescription.class, reader, false);
                Log.d(MainActivity.class.getSimpleName(), osd.toString());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
        }
    }

    public void myTestSimpleFrameWork() {
        try {
            String xmlData = new MyTestDownloadFile().execute().get();
            Serializer serializer = new Persister();

            if (xmlData != null) {
                Reader reader = new StringReader(xmlData);
                Kvtml kvtml =
                        serializer.read(Kvtml.class, reader, false);
                Log.d(MainActivity.class.getSimpleName(), kvtml.toString());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
        }

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

    private class TestDownloadFile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    private class MyTestDownloadFile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpGet getRequest = new HttpGet(urlTest);
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
                Log.w(getClass().getSimpleName(), "Error for URL " + KEY_URL, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

}
