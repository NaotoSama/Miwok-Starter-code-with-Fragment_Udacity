/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.example.android.miwok;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.example.android.miwokWithFragment.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);           // Set the content of the activity to use the activity_main.xml layout file

        // Find the view pager in activity_main.xml that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragmentViewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MiwokFragmentPagerAdaptor adapter = new MiwokFragmentPagerAdaptor(this, getSupportFragmentManager());      //It was originally:  MiwokFragmentPagerAdaptor adapter = new MiwokFragmentPagerAdaptor(getSupportFragmentManager());  Since we modified the MiwokFragmentPagerAdaptor constructor, we also need to update the MainActivity (which uses that constructor).
                                                                                                                             //When we create a MiwokFragmentPagerAdaptor, we pass in a Context (which is “this” or the activity) and the FragmentManager.

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout in activity_main.xml that will allow the user to swipe between tabs.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        // Set the adapter onto the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

    }


}
