package com.example.codechallenge;

import androidx.test.filters.SmallTest;
import com.example.codechallenge.activities.MainActivity;
import junit.framework.TestCase;

public class MainActivityUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testFailedCase(){
        MainActivity mainActivity = new MainActivity();
        mainActivity.mMainActivityViewModel.getDataList().observe(mainActivity, artworkList -> {
            assertFalse(artworkList.isEmpty());
        });
    }
}
