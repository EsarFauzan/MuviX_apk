package com.muvix.app.view;

import android.content.Context;

public final class ImageSourceResolver {
    private static final String LOCAL_PREFIX = "local:";

    private ImageSourceResolver() {}

    public static Object resolve(Context context, String source) {
        if (source == null || source.trim().isEmpty()) return null;

        if (!source.startsWith(LOCAL_PREFIX)) {
            return source;
        }

        String rawName = source.substring(LOCAL_PREFIX.length()).trim();
        if (rawName.isEmpty()) return null;

        String normalized = rawName.toLowerCase()
                .replace("-", "_")
                .replace(" ", "_");

        int resId = context.getResources().getIdentifier(rawName, "drawable", context.getPackageName());
        if (resId == 0) {
            resId = context.getResources().getIdentifier(normalized, "drawable", context.getPackageName());
        }

        if (resId == 0) {
            String withoutExt = normalized;
            int dot = normalized.lastIndexOf('.');
            if (dot > 0) {
                withoutExt = normalized.substring(0, dot);
                resId = context.getResources().getIdentifier(withoutExt, "drawable", context.getPackageName());
            }
        }

        return resId == 0 ? null : resId;
    }
}

