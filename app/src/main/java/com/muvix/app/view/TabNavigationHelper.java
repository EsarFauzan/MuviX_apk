package com.muvix.app.view;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.muvix.app.R;

public final class TabNavigationHelper {
    private TabNavigationHelper() {}

    public static void navigate(AppCompatActivity activity, Class<?> current, Class<?> target) {
        if (current == target) return;

        activity.startActivity(new Intent(activity, target));

        int currentIndex = tabIndex(current);
        int targetIndex = tabIndex(target);

        if (targetIndex > currentIndex) {
            activity.overridePendingTransition(R.anim.page_enter, R.anim.page_exit);
        } else {
            activity.overridePendingTransition(R.anim.page_back_enter, R.anim.page_back_exit);
        }
    }

    private static int tabIndex(Class<?> screen) {
        if (screen == MainActivity.class) return 0;
        if (screen == ScheduleActivity.class) return 1;
        if (screen == HistoryActivity.class) return 2;
        if (screen == SubscribeActivity.class) return 3;
        if (screen == ProfileActivity.class) return 4;
        return 0;
    }
}

