package com.waracle.androidtest.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public final class Cake {
    private static final String JSON_ELEMENT_TITLE = "title";
    private static final String JSON_ELEMENT_DESCRIPTION = "desc";
    private static final String JSON_ELEMENT_IMAGE_URL = "image";

    private final String title;
    private final String description;
    private final String imageUrl;

    /**
     * Construct a cake from its JSON representation
     *
     * @param jsonObject JSON representation of a cake
     * @throws IllegalArgumentException JSON object doesn't represent a cake
     */
    public Cake(@NonNull final JSONObject jsonObject) throws IllegalArgumentException {
        try {
            title = jsonObject.getString(JSON_ELEMENT_TITLE);
            description = jsonObject.getString(JSON_ELEMENT_DESCRIPTION);
            imageUrl = jsonObject.getString(JSON_ELEMENT_IMAGE_URL);
        }
        catch(JSONException e) {
            throw new IllegalArgumentException("The JSON object provided does not look like a cake");
        }
    }

    public @NonNull String getTitle() {
        return title;
    }

    public @NonNull String getDescription() {
        return description;
    }

    public @NonNull String getImageUrl() {
        return imageUrl;
    }
}
