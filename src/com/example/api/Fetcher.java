package com.example.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

class Fetcher {
    private ArrayList<String> items;
    private String url;

    Fetcher(String url) {
        this.url = url;
    }

    Fetcher fetch() throws IOException, ParseException {
            items = new ArrayList<>();
            URL myUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("HttpResponseCode: " + code);
            }

            Scanner scanner = new Scanner(myUrl.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(inline.toString());
            for (Object object : array) {
                if (object instanceof JSONObject) {
                    JSONObject attackerReward = (JSONObject) ((JSONObject) object).get("attackerReward");
                    parseJson(attackerReward);
                    JSONObject defenderReward = (JSONObject) ((JSONObject) object).get("defenderReward");
                    parseJson(defenderReward);
                }
            }
        return this;
    }

    private void parseJson(JSONObject reward) {
        JSONArray countedItemsArray = (JSONArray) ((reward).get("countedItems"));
        for (Object item : countedItemsArray) {

            JSONObject jsonObject = (JSONObject) item;
            if (jsonObject.get("type").equals(jsonObject.get("key"))) {
                items.add(jsonObject.get("type").toString());
            } else {
                items.add(jsonObject.get("type").toString());
                items.add(jsonObject.get("key").toString());
            }

        }
    }

    ArrayList<String> getUniqueItems() {
        ArrayList<String> uniqueItems = new ArrayList<>();
        for (String item : items) {
            if (!uniqueItems.contains(item))
                uniqueItems.add(item);
        }
        return uniqueItems;
    }

    ArrayList<String> getItems() {
        return items;
    }
}
