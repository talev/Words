package com.talev.words;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_URL = "http://80.72.69.142/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "UnknownWords";
    public static final String TOTAL_WORDS = "TotalWords";
    public static final String COUNT = "Count";
    public static final String SPACE = " ";
    public static final String CURRENT_STATE = "CurrentState";

    private TextView tvWord;
    private Button btnKnow;
    private Button btnCheck;
    private Button btnNext;
    private Button btnBack;

    private DefaultHttpClient client = new DefaultHttpClient();
    private SharedPreferences sharedpreferences;
    private int count = 0;
    private int totalWords = 0;
    private boolean isTranslated = false;

    private List<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnKnow = (Button) findViewById(R.id.btn_know);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack = (Button) findViewById(R.id.btn_back);

        btnKnow.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(CURRENT_STATE, Context.MODE_PRIVATE);
//        xmlData = sharedpreferences.getString(TOTAL_WORDS, null);
        totalWords = sharedpreferences.getInt(TOTAL_WORDS, 0);
        count = sharedpreferences.getInt(COUNT, 0);

        loadFileWords();
        refresh();
    }

    public void simpleFrameWork() {
        try {
            String xmlData = new String(new DownloadFile().execute(KEY_URL).get().getBytes("ISO-8859-1"), "UTF-8");
            Serializer serializer = new Persister();

            Reader reader = new StringReader(xmlData);
            Kvtml kvtml = serializer.read(Kvtml.class, reader, false);

            if (kvtml != null) {
                words.clear();
                for (int i = 0; i < 10; i++) {
                    if (kvtml.entries.get(i).translations.get(0).text != null && kvtml.entries.get(i).translations.get(1).text != null) {
                        words.add(new Word(kvtml.entries.get(i).translations.get(0).text, kvtml.entries.get(i).translations.get(1).text));
                    }
                }
                totalWords = words.size();
            }
            count = 0;
            refresh();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
        }
    }

    public void saveFileWords() {
        try {
            FileOutputStream outputStream = openFileOutput(KEY_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(words);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFileWords() {
        try {
            FileInputStream fileInputStream = openFileInput(KEY_FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            words = (List<Word>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        if (words.size() > 0) {
            setTitle(getString(R.string.app_name) + SPACE + String.valueOf(count + 1) + "/" + words.size() + SPACE + "(" + totalWords + ")");
        } else {
            setTitle(getString(R.string.app_name));
        }
        showFirstWord();
    }

    private void showFirstWord() {
        if (words.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
            } else {
                tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            tvWord.setText(words.get(count).getWord1());
            isTranslated = false;
        } else {
            tvWord.setText("");
        }
    }

    private void showSecondWord() {
        if (words.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
            } else {
                tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            tvWord.setText(words.get(count).getWord2());
            isTranslated = true;
        } else {
            tvWord.setText("");
        }
    }

    private void alertDialogDownload() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.download_message));

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simpleFrameWork();
            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFileWords();
        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString(TOTAL_WORDS, xmlData);
        editor.putInt(TOTAL_WORDS, totalWords);
        editor.putInt(COUNT, count);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_downloads) {
            alertDialogDownload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_know) {
            if (words.size() > 0) {
                words.remove(count);
                if (count + 1 < words.size()) {
                    refresh();
                } else {
                    count = words.size() - 1;
                    refresh();
                }
            } else {
                refresh();
            }
        }
        if (v.getId() == R.id.btn_check) {
            if (isTranslated) {
                showFirstWord();
            } else {
                showSecondWord();
            }
        }
        if (v.getId() == R.id.btn_back) {
            if (count > 0) {
                count--;
                refresh();
            } else {
                count = words.size() - 1;
                refresh();
            }
        }
        if (v.getId() == R.id.btn_next) {
            if (words != null) {
                if (count + 1 < words.size()) {
                    count++;
                    refresh();
                } else {
                    count = 0;
                    refresh();
                }
            }
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (String url : params) {
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
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
