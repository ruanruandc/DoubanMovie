package com.example.ruandongchuan.doubanmovie.util;

import android.content.SearchRecentSuggestionsProvider;

/**SearchView的搜索历史记录
 * Created by ruandongchuan on 15-11-16.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.ruandongchuan.doubanmovie.util.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
