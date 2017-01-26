package com.talev.words;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DUPLICATION_WORDS_LOG_TAG = "Duplication";
    public static final String KEY_URL = "http://80.72.69.142/MyNewWord.kvtml";
    public static final String KEY_FILE_NAME = "UnknownWords";
    public static final String COUNT = "Count";
    public static final String COUNT_KNOWN = "CountKnown";
    public static final String SPACE = " ";

    private TextView tvWord;
    private Button btnIKnowIt;
    private Button btnCheck;
    private Button btnNext;
    private Button btnBack;

    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private String duplicatedWords;
    private int count = 0;
    private int countKnown = 0;
    private boolean isTranslated = false;
    private boolean isShowUnknownWords = true;

    private List<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = (TextView) findViewById(R.id.tv_word);
        btnIKnowIt = (Button) findViewById(R.id.btn_i_know_it);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack = (Button) findViewById(R.id.btn_back);

        btnIKnowIt.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getPreferences(Context.MODE_PRIVATE);
        count = sharedpreferences.getInt(COUNT, 0);
        countKnown = sharedpreferences.getInt(COUNT_KNOWN, 0);

        loadFileWords();
        refresh();
    }

    public void saveFileWords() {
        try {
            FileOutputStream outputStream = openFileOutput(KEY_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(words);
            objectOutputStream.writeObject(duplicatedWords);
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
            duplicatedWords = (String) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        if (isShowUnknownWords) {
            if (words.size() > 0) {
                setTitle(getString(R.string.app_name) + SPACE + String.valueOf(count + 1)
                        + "/" + words.size() + SPACE + "(" + getUnknownWordNumbers() + ")");
            } else {
                setTitle(getString(R.string.app_name));
            }
            showFirstWord();
        } else {
            if (words.size() > 0) {
                setTitle(getString(R.string.known_words) + SPACE + String.valueOf(countKnown + 1)
                        + "/" + words.size() + SPACE + "(" + getKnownWordNumbers() + ")");
            } else {
                setTitle(getString(R.string.known_words));
            }
            showFirstWord();
        }
    }

    private int getKnownWordNumbers() {
        int result = 0;
        for (Word word : words) {
            if (word.isLearned()) {
                result++;
            }
        }

        return result;
    }

    private int getUnknownWordNumbers() {
        int result = 0;
        for (Word word : words) {
            if (!word.isLearned()) {
                result++;
            }
        }

        return result;
    }

    private void nextWord() {
        if (isShowUnknownWords) {
            if (words != null && words.size() > 0) {
                if (count + 1 < words.size()) {
                    count++;
                    if (words.get(count).isLearned()) {
                        for (int i = count; i < words.size(); i++) {
                            if (words.get(i).isLearned() && i + 1 < words.size()) {
                                count++;
                                if (!words.get(i + 1).isLearned()) {
                                    break;
                                }
                            } else {
                                count = 0;
                                refresh();
                                if (words.get(count).isLearned()) {
                                    nextWord();
                                }
                            }
                        }
                    }
                    refresh();
                } else {
                    count = 0;
                    refresh();
                    if (words.get(count).isLearned()) {
                        nextWord();
                    }
                }
            }
        } else {
            if (words != null && words.size() > 0) {
                if (countKnown + 1 < words.size()) {
                    countKnown++;
                    if (!words.get(countKnown).isLearned()) {
                        for (int i = countKnown; i < words.size(); i++) {
                            if (!words.get(i).isLearned() && i + 1 < words.size()) {
                                countKnown++;
                                if (words.get(i + 1).isLearned()) {
                                    break;
                                }
                            } else {
                                countKnown = 0;
                                refresh();
                                if (!words.get(countKnown).isLearned()) {
                                    nextWord();
                                }
                            }
                        }
                    }
                    refresh();
                } else {
                    countKnown = 0;
                    refresh();
                    if (!words.get(countKnown).isLearned()) {
                        nextWord();
                    }
                }
            }
        }
    }

    private void backWord() {
        if (isShowUnknownWords) {
            if (count > 0) {
                count--;
                if (words.get(count).isLearned()) {
                    if (count > 0) {
                        for (int i = count; i > 0; i--) {
                            if (words.get(i).isLearned()) {
                                count--;
                                if (count > 0) {
                                    if (!words.get(i - 1).isLearned()) {
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        count = words.size() - 1;
                        if (words.get(count).isLearned()) {
                            backWord();
                        }
                    }
                }
                refresh();
            } else {
                count = words.size() - 1;
                refresh();
            }
        } else {
            if (countKnown > 0) {
                countKnown--;
                if (!words.get(countKnown).isLearned()) {
                    if (countKnown > 0) {
                        for (int i = countKnown; i > 0; i--) {
                            if (!words.get(i).isLearned()) {
                                countKnown--;
                                if (countKnown > 0) {
                                    if (words.get(i - 1).isLearned()) {
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        countKnown = words.size() - 1;
                        if (!words.get(countKnown).isLearned()) {
                            backWord();
                        }
                    }
                }
                refresh();
            } else {
                countKnown = words.size() - 1;
                refresh();
            }
        }
    }

    private void showFirstWord() {
        if (isShowUnknownWords) {
            if (words.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
                } else {
                    tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                tvWord.setText(words.get(count).getWord());
                isTranslated = false;
            } else {
                tvWord.setText("");
            }
        } else {
            if (words.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvWord.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
                } else {
                    tvWord.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                tvWord.setText(words.get(countKnown).getWord());
                isTranslated = false;
            } else {
                tvWord.setText("");
            }
        }
    }

    private void showSecondWord() {
        if (isShowUnknownWords) {
            if (words.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
                } else {
                    tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                tvWord.setText(words.get(count).getWordTranslated());
                isTranslated = true;
            } else {
                tvWord.setText("");
            }
        } else {
            if (words.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
                } else {
                    tvWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                tvWord.setText(words.get(countKnown).getWordTranslated());
                isTranslated = true;
            } else {
                tvWord.setText("");
            }
        }
    }

    private void alertDialogDownload() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.download_message));

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.downloading), "");
                new DownloadWordsFromServer().execute(KEY_URL);
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

    private void alertDialogClear() {
        AlertDialog.Builder alertDialogBulder = new AlertDialog.Builder(this);
        alertDialogBulder.setMessage(getString(R.string.clear_message));

        alertDialogBulder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refresh();
            }
        });

        alertDialogBulder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBulder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFileWords();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(COUNT, count);
        editor.putInt(COUNT_KNOWN, countKnown);
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
        if (id == R.id.test) {
            String sentence = "knownWords.size(): "
                    + "\nwords.size(): " + words.size() + "\ntotalWords: ";
            Toast.makeText(this, sentence, Toast.LENGTH_LONG).show();

            if (duplicatedWords != null) {
                Toast.makeText(getApplicationContext(), duplicatedWords, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_know) {
            if (isShowUnknownWords) {
                isShowUnknownWords = false;
                refresh();
                item.setTitle("Unknown words");
                btnIKnowIt.setText("I forget it");
            } else {
                isShowUnknownWords = true;
                refresh();
                item.setTitle(getString(R.string.known_words));
                btnIKnowIt.setText(getString(R.string.i_know_it));
            }
            return true;
        }
        if (id == R.id.action_downloads) {
            alertDialogDownload();
            return true;
        }
        if (id == R.id.action_clear) {
            alertDialogClear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (isShowUnknownWords) {
            if (v.getId() == R.id.btn_i_know_it) {
                if (words.size() > 0) {
                    Word word = words.get(count);
                    word.setDate(new Date());
                    word.setLearned(true);
                    nextWord();
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
                backWord();
            }
            if (v.getId() == R.id.btn_next) {
                nextWord();
            }
        } else {
            if (v.getId() == R.id.btn_i_know_it) {
                refresh();
            }
            if (v.getId() == R.id.btn_check) {
                if (isTranslated) {
                    showFirstWord();
                } else {
                    showSecondWord();
                }
            }
            if (v.getId() == R.id.btn_back) {
                if (countKnown > 0) {
                    backWord();
                }
            }
            if (v.getId() == R.id.btn_next) {
                nextWord();
            }
        }
    }

    private class DownloadWordsFromServer extends AsyncTask<String, String, Void> {

        private DefaultHttpClient client = new DefaultHttpClient();

        @Override
        protected Void doInBackground(String... params) {
            for (String url : params) {
                HttpGet getRequest = new HttpGet(url);
                try {
                    HttpResponse getResponse = client.execute(getRequest);
                    final int statusCode = getResponse.getStatusLine().getStatusCode();

                    if (statusCode == HttpStatus.SC_OK) {
                        HttpEntity getResponseEntity = getResponse.getEntity();
                        if (getResponseEntity != null) {
                            publishProgress("Connecting...");
                            try {
                                String xmlData = new String(EntityUtils.toString(getResponseEntity)
                                        .getBytes("ISO-8859-1"), "UTF-8");
                                Serializer serializer = new Persister();

                                Reader reader = new StringReader(xmlData);
                                Kvtml kvtml = serializer.read(Kvtml.class, reader, false);

                                if (kvtml != null) {
                                    words.clear();
                                    for (int i = 0; i < kvtml.entries.size(); i++) {
                                        if (kvtml.entries.get(i).translations.get(0).text != null
                                                && kvtml.entries.get(i).translations.get(1).text != null) {
                                            words.add(new Word(
                                                    kvtml.entries.get(i).translations.get(0).text,
                                                    kvtml.entries.get(i).translations.get(1).text));
                                        }
                                        publishProgress("Done: " + String.valueOf(i + 1)
                                                + " from " + String.valueOf(kvtml.entries.size()));
                                    }

                                    // Search and remove all duplicated words and show it in Toast
                                    for (int i = 0; i < words.size(); i++) {
                                        for (int j = i + 1; j < words.size(); j++) {
                                            if (words.get(i).getWord().equals(words.get(j).getWord())
                                                    && words.get(i).getWordTranslated()
                                                    .equals(words.get(j).getWordTranslated())) {
                                                duplicatedWords += words.get(j).getWord() + " <=> "
                                                        + words.get(j).getWordTranslated() + "\n";
                                                Log.i(DUPLICATION_WORDS_LOG_TAG, "Duplication word: "
                                                        + words.get(j).getWord() + " <=> "
                                                        + words.get(j).getWordTranslated() + "\n"
                                                );
                                                words.remove(j);
                                                j--;
                                            }
                                        }
                                    }

                                    // Search all duplicated words and concatenation their translation
                                    for (int i = 0; i < words.size(); i++) {
                                        for (int j = i + 1; j < words.size(); j++) {
                                            if (words.get(i).getWord().equals(words.get(j).getWord())) {

                                                Log.i(DUPLICATION_WORDS_LOG_TAG, "Concatenations word: "
                                                        + words.get(j).getWord() + " <=> "
                                                        + words.get(j).getWordTranslated() + ", "
                                                        + words.get(i).getWordTranslated()
                                                        + "\n"
                                                );
//                                                words.get(j).setWordTranslated(words.get(j).getWordTranslated());
//                                                j--;
                                            }
                                        }
                                    }

                                    /*
                                    // Search for known words
                                    publishProgress("Check for known words...");
                                    List<Word> tempWords = new ArrayList<>();
                                    for (Word knownWord : knownWords) {
                                        for (Word word : words) {
                                            if (knownWord.getWord().equals(word.getWord())
                                                    && knownWord.getWordTranslated().equals(
                                                    word.getWordTranslated())) {

                                                tempWords.add(word);
                                            }
                                        }
                                    }
                                    // Remove all known words
                                    publishProgress("Remove all known words...");
                                    for (Word word : tempWords) {
                                        words.remove(word);
                                    }*/
                                }
                                count = 0;
                            } catch (Exception e) {
                                Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                            }
                        }
                    }
                } catch (IOException e) {
                    getRequest.abort();
                    Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.hide();
            refresh();
        }
    }
}
