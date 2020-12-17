package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface ViewPagerAdapter {
    @NonNull
    Fragment getItem(int position);

    int getCount();

    void addFragment(Fragment fragment, String title);

    @Nullable
    CharSequence getPageTitle(int position);
}
