/*
 * ActivityDiary
 *
 * Copyright (C) 2018 Raphael Mack http://www.raphael-mack.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.rampro.activitydiary.ui;

import android.database.Cursor;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.rampro.activitydiary.BuildConfig;
import de.rampro.activitydiary.ui.history.HistoryActivity;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HistoryActivityTest {
    HistoryActivity activity;

    @Before
    public void setUp() throws Exception
    {
        activity = Robolectric.buildActivity( HistoryActivity.class )
                .create()
                .resume()
                .get();
    }

    @Test
    public void insertSameSuggestionTest() throws Exception{

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test1");
        }
        Uri suggestionUri = Uri.parse("content://de.rampro.activitydiary.debug.provider/history/search_suggest_query/?limit=50");
        Cursor cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);

        assertEquals(1, cursor.getCount() );

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test2");
        }

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test1");
        }
        cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);
        assertEquals(2, cursor.getCount() );

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test3");
        }
        cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);
        assertEquals(3, cursor.getCount() );

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test4");
        }
        cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);
        assertEquals(4, cursor.getCount() );

        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test5");
        }
        cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);
        assertEquals(5, cursor.getCount() );
    }

    @Test
    public void searchSuggestionDisplayCountTest() throws Exception{
        for (int i = 0; i < 100; i ++){
            activity.suggestionHandle("test" + i);
        }

        Uri suggestionUri = Uri.parse("content://de.rampro.activitydiary.debug.provider/history/search_suggest_query/?limit=50");
        Cursor cursor = activity.getContentResolver().query(suggestionUri, null, null, null, null);

        assertEquals(5, cursor.getCount() );

    }
}
