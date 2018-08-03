package com.waracle.androidtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public final class Cake {
    private static final String JSON_ELEMENT_TITLE = "title";
    private static final String JSON_ELEMENT_DESCRIPTION = "desc";
    private static final String JSON_ELEMENT_IMAGE_URL = "image";

    private String title;
    private String description;
    private String imageUrl;

    /**
     * Construct a cake from its JSON representation
     *
     * @param jsonObject JSON representation of a cake
     * @throws IllegalArgumentException JSON object doesn't represent a cake
     */
    public Cake(final JSONObject jsonObject) throws IllegalArgumentException
    {
        try {
            title = jsonObject.getString(JSON_ELEMENT_TITLE);
            description = jsonObject.getString(JSON_ELEMENT_DESCRIPTION);
            imageUrl = jsonObject.getString(JSON_ELEMENT_IMAGE_URL);
        }
        catch(JSONException e) {
            throw new IllegalArgumentException("The JSON object provided does not look like a cake");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
