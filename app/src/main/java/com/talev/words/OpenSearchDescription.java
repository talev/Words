package com.talev.words;

import android.app.DownloadManager;

import java.util.List;

/**
 * Created by Dimko Talev on 30.06.2016 г.
 */
public class OpenSearchDescription {
    public DownloadManager.Query query;
    public int totalResults;
    public List<Move> movies;
}
