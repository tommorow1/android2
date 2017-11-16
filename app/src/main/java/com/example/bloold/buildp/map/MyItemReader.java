/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bloold.buildp.map;

import android.graphics.drawable.Icon;

import com.example.bloold.buildp.R;
import com.example.bloold.buildp.model.MyItem;
//import com.google.maps.android.utils.demo.model.MyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

    public List<MyItem> read(JSONArray finalJson) throws JSONException {
        List<MyItem> items = new ArrayList<MyItem>();
        for (int i = 0; i < finalJson.length(); i++) {
            String title = null;
            String snippet = null;
            String ObjId =null;
            JSONObject object = finalJson.getJSONObject(i);
            double lat = object.getDouble("LAT");
            double lng = object.getDouble("LNG");
            title = object.getString("ADDRESS");
            ObjId = object.getString("OBJECT_ID");
            snippet = object.getString("NAME");

            items.add(new MyItem(lat, lng, title, snippet,ObjId));
        }
        return items;
    }

}
