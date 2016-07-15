package com.talev.words;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_URL = "http://80.72.69.142/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "filewords";
    public static final String TAG = "dimko";
    public static final String WORDS = "words";
    public static final String COUNT = "count";
    public static final String SPACE = " ";

    private TextView tvWord;
    private Button btnKnow;
    private Button btnWord1;
    private Button btnWord2;
    private Button btnNext;
    private Button btnBack;
    private Button btnDownload;

    private DefaultHttpClient client = new DefaultHttpClient();
    private Kvtml kvtml;
    private String xmlData;
    private int count = 0;
//    private SharedPreferences sharedpreferences;

    private List<Word> words;

    /*private Map<String, String> dontKnow = new HashMap();
    private Set set;
    private Iterator iterator;
    private Map.Entry mapEntry;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnKnow = (Button) findViewById(R.id.btn_know);
        btnWord1 = (Button) findViewById(R.id.btn_word1);
        btnWord2 = (Button) findViewById(R.id.btn_word2);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnDownload = (Button) findViewById(R.id.btn_download);

        tvWord.setText(String.valueOf(""));
        btnNext.setText(getString(R.string.next) + " " + String.valueOf(count));

        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                words.remove(count);
                // From HashMap
                /*if (mapEntry != null) {
                    dontKnow.remove(mapEntry.getKey());
                    refresh();
                    mapEntry = (Map.Entry) iterator.next();
                    showFirstWord();
                }
                Log.d(TAG, dontKnow.toString());*/
            }
        });

        btnWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showFirstWord();

                /*if (kvtml != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
                    } else {
                        tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    tvWord.setText(kvtml.entries.get(count).translations.get(0).text);
                }*/
                if (words != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
                    } else {
                        tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    tvWord.setText(words.get(count).getWord1());
                }
            }
        });

        btnWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From HashMap
                /*if (mapEntry != null && mapEntry.getValue() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
                    } else {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    tvWord.setText(mapEntry.getValue().toString());
                    Log.d(TAG, dontKnow.toString());
                }*/

                /*if (kvtml != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
                    } else {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    tvWord.setText(kvtml.entries.get(count).translations.get(1).text);
                }*/
                if (words != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
                    } else {
                        tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    tvWord.setText(words.get(count).getWord2());
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From HashMap
                /*if (mapEntry != null) {
                    mapEntry = (Map.Entry) iterator.next();
                    showFirstWord();
                    Log.d(TAG, dontKnow.toString());
                } else {
                    refresh();
                }*/

                /*if (kvtml != null) {
                    if (count <= kvtml.entries.size()) {
                        count++;
                        btnNext.setText(getString(R.string.next) + " " + String.valueOf(count));
                    }
                }*/
                if (words != null) {
                    if (count <= words.size()) {
                        count++;
                        btnNext.setText(getString(R.string.next) + SPACE + String.valueOf(count));
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (count > 0) {
                    count--;
                    btnNext.setText(getString(R.string.next) + " " + String.valueOf(count));
                }*/
                if (count > 0) {
                    count--;
                    btnNext.setText(getString(R.string.next) + SPACE + String.valueOf(count));
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleFrameWork();
            }
        });

    }

    private void showFirstWord() {
        // From HashMap
        /*if (mapEntry != null && mapEntry.getKey() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
            } else {
                tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            tvWord.setText(mapEntry.getKey().toString());
            Log.d(TAG, dontKnow.toString());
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

//        loadFileWords();
//        refresh();

//        set = dontKnow.entrySet();
//        iterator = set.iterator();

//        sharedpreferences = getSharedPreferences("ListWords", Context.MODE_PRIVATE);
//        xmlData = sharedpreferences.getString(WORDS, null);
//        count = sharedpreferences.getInt(COUNT, 0);

//        btnNext.setText(getString(R.string.next) + " " + String.valueOf(count));

//        Serializer serializer = new Persister();

//        if (xmlData != null) {
//            Reader reader = new StringReader(xmlData);
//            try {
//                kvtml = serializer.read(Kvtml.class, reader, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void simpleFrameWork() {
        try {
            xmlData = new String(new DownloadFile().execute(KEY_URL).get().getBytes("ISO-8859-1"), "UTF-8");
            Serializer serializer = new Persister();

            if (xmlData != null) {
                Reader reader = new StringReader(xmlData);
                kvtml = serializer.read(Kvtml.class, reader, false);

                // From HashMap
                /*dontKnow.clear();
                for (int i = 0; i < kvtml.entries.size(); i++) {
                    dontKnow.put(kvtml.entries.get(i).translations.get(0).text, kvtml.entries.get(i).translations.get(1).text);
                }
                saveFileWords();
//                btnNext.setText(getString(R.string.next) + " " + String.valueOf(dontKnow.size()));
                setTitle(getString(R.string.app_name) + " " + String.valueOf(dontKnow.size()));
                refresh();*/
                /*for (int i = 0; i < kvtml.entries.size(); i++) {
                    words.add(new Word(kvtml.entries.get(i).translations.get(0).text, kvtml.entries.get(i).translations.get(1).text));
                }*/
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
        }

    }

    public void saveFileWords() {
        try {
            FileOutputStream outputStream = openFileOutput(KEY_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(dontKnow);
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
//            dontKnow = (HashMap<String, String>) objectInputStream.readObject();
            words = (List<Word>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        /*set = dontKnow.entrySet();
        iterator = set.iterator();
        mapEntry = (Map.Entry) iterator.next();*/
//        btnNext.setText(getString(R.string.next) + " " + String.valueOf(dontKnow.size()));
//        setTitle(getString(R.string.app_name) + " " + String.valueOf(dontKnow.size()));

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFileWords();
        /*SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(WORDS, xmlData);
        editor.putInt(COUNT, count);
        editor.commit();*/
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
