package com.waracle.androidtest;

import android.support.annotation.NonNull;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpUtils {

    public static final int UNAVAILABLE_CONTENT_LENGTH = -1;

    private static final String HEADER_NAME_CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(\\S+)");
    private static final String DEFAULT_CHARSET = "ISO-8859-1";


    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String getCharset(@NonNull HttpURLConnection connection) {
        final String contentType = getHeaderValueOrEmptyString(connection, HEADER_NAME_CONTENT_TYPE);
        final Matcher matcher = CHARSET_PATTERN.matcher(contentType);
        return matcher.find() ? matcher.group(1) : DEFAULT_CHARSET;
    }

    /**
     * Get the value of the Content-Length header, or 0 if it can't be found/parsed
     */
    public static int getContentLength(@NonNull HttpURLConnection connection) {
        final String contentLengthString = getHeaderValueOrEmptyString(connection, HEADER_NAME_CONTENT_LENGTH);
        int contentLength;
        try {
            contentLength = Integer.valueOf(contentLengthString);
        }
        catch (NumberFormatException e) {
            contentLength = UNAVAILABLE_CONTENT_LENGTH;
        }
        return contentLength;
    }

    private static String getHeaderValueOrEmptyString(
            @NonNull HttpURLConnection connection,
            @NonNull final String headerName) {
        final String value = connection.getHeaderField(headerName);
        return (value == null) ? "" : value;
    }

    private HttpUtils() {
    }
}
