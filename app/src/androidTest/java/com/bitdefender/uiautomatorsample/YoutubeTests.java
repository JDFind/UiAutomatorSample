package com.bitdefender.uiautomatorsample;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 * Created by anicolae on 01-Aug-17.
 *
 *  ALL DEVICES MUST HAVE ANDROID API 18+
 *  ALL DEVICES MUST NOT HAVE A LOCkSCREEN, NOT EVEN SWIPE
 *
 *  THIS TEST DEPENDS ON:
 *  Internet Connection
 */

@SdkSuppress(minSdkVersion = 18)
public class YoutubeTests {

    private static int DEFAULT_TIMEOUT = 5000;
    private static String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";

    private Context mTestContext;
    private UiDevice mDevice;

    @Before
    public void setUp() {

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        mTestContext = instrumentation.getContext();
        mDevice = UiDevice.getInstance(instrumentation);
    }

    @After
    public void tearDown() {
        mDevice.pressHome();
    }

    @Test
    public void searchAndPlayVideo() throws UiObjectNotFoundException {

        // test should be ran only if the Youtube app can be launched
        assumeTrue(TestUtils.launchApp(mTestContext, YOUTUBE_PACKAGE_NAME));

        // wait for the app to be displayed
        UiSelector rootSelector = new UiSelector().resourceIdMatches(".*content").packageName(YOUTUBE_PACKAGE_NAME);
        UiObject root = mDevice.findObject(rootSelector);
        assertTrue(root.waitForExists(DEFAULT_TIMEOUT));

        // find and click search icon
        UiSelector searchIconSelector = new UiSelector().resourceIdMatches(".*menu_search");
        UiObject searchIcon = mDevice.findObject(searchIconSelector);
        assertTrue(searchIcon.clickAndWaitForNewWindow());

        // type the desired text into the search input field
        UiSelector inputFieldSelector = new UiSelector().resourceIdMatches(".*search_edit_text");
        UiObject inputField = mDevice.findObject(inputFieldSelector);
        inputField.setText("Android Testing Library");

        mDevice.pressEnter();

        /* NOT WORKING:
        UiSelector searchedTitleSelector = new UiSelector()
                    .resourceIdMatches(".*title")
                    .textContains("Android Testing Patterns #1")
                    .fromParent(listItemContainerSelector);
        */

        // Match the video title, knowing that is contained by an element of the Videos List
        UiSelector listItemContainerSelector = new UiSelector().resourceIdMatches(".*video_info_view");
        UiSelector searchedTitleSelector = listItemContainerSelector.childSelector(new UiSelector()
                .resourceIdMatches(".*title")
                .textContains("Android Testing Patterns #1"));

        // check if the desired video is listed
        UiObject searchedTitle = mDevice.findObject(searchedTitleSelector);
        assertTrue(searchedTitle.waitForExists(DEFAULT_TIMEOUT));

        // NOT WORKING: assertTrue(searchedTitle.clickAndWaitForNewWindow());
        searchedTitle.click();

        // check if a video is being played
        UiSelector videoPlayerSelector = new UiSelector().resourceIdMatches(".*watch_player");
        UiObject videoPlayer = mDevice.findObject(videoPlayerSelector);
        assertTrue(videoPlayer.waitForExists(DEFAULT_TIMEOUT));

        // Match the video title, knowing that is contained by an element of the Video Info
        UiSelector playedInfoRootSelector = new UiSelector().resourceIdMatches(".*video_info_fragment");
        UiSelector playedTitleSelector = playedInfoRootSelector.childSelector(new UiSelector()
                .resourceIdMatches(".*title")
                .textContains("Android testing Patterns #1"));

        // check if the chosen title appears as "Info" - the appropriate video is being played
        UiObject playedTitle = mDevice.findObject(playedTitleSelector);
        assertTrue(playedTitle.waitForExists(DEFAULT_TIMEOUT));
    }
}
