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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_URL = "http://80.72.69.142/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "MyNewWord.kvtml";
    public static final String TAG = "dimko";
    private TextView tvWord;
    private Button btnDownload;

    public static final String urlTest = "http://80.72.69.142/Test.kvtml";
    private DefaultHttpClient client = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnDownload = (Button) findViewById(R.id.btn_download);

        tvWord.setText(String.valueOf(""));
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleFrameWork();
//                tvWord.setText("File from server is download");
            }
        });

    }

    public void simpleFrameWork() {
        try {
            String xmlData = new DownloadFile().execute().get();
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

    private class DownloadFile extends AsyncTask<Void, Void, String> {

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
