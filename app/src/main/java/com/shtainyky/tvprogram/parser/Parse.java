package com.shtainyky.tvprogram.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parse {
    public static String parseJSONtoString(String content)
    {
        try {
            JSONArray array = new JSONArray(content);
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                builder.append("{");
                builder.append("channel_id =").append(object.getInt("channel_id")).append("\n")
                        .append("date =").append(object.getString("date")).append("\n")
                        .append("time=").append(object.getString("time")).append("\n")
                        .append("title =").append(object.getString("title")).append("\n")
                        .append("}");

            }
            return builder.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
