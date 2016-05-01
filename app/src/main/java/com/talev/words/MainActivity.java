package com.talev.words;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

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

        File file = new File(getFilesDir(), "test.txt");
        OutputStream outputStream = null;
        try {
            outputStream = openFileOutput("text.txt", Context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tvWord.setText(String.valueOf(file.getName()));

        Log.d(TAG, String.valueOf(file.isFile()));
        new DownloadFile().execute();
//        tvWord.setText(readFile());
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
