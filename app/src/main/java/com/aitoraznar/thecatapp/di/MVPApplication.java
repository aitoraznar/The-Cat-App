package com.aitoraznar.thecatapp.di;

import android.app.Application;
import android.content.Context;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.di.components.AppComponent;
import com.aitoraznar.thecatapp.di.modules.AppModule;
import com.aitoraznar.thecatapp.di.components.DaggerAppComponent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;


/**
 * @author aitoraznar
 * @since 08/09/15
 */
public class MVPApplication extends Application {

    private AppComponent mComponent;
    private static Context mContext;

    // Google analytics
    private static GoogleAnalytics analytics;
    HashMap<MVPApplication.TrackerName, Tracker> mTrackers = new HashMap();

    public static GoogleAnalytics getAnalytics() {
        return analytics;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // Google analytics
        analytics = GoogleAnalytics.getInstance(this);
        getAnalytics().setLocalDispatchPeriod(getResources().getInteger(R.integer.ga_dispatchPeriod));

        // DaggerAppComponent is a method that creates once you rebuild the project
        //      and the name is Dagger%COMPONENT_NAME%, in this case AppComponent is the
        //      name of the component.
        // The method appModule, also depends on the name of the module.
        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return mComponent;
    }

    public static enum TrackerName {
        APP_TRACKER(mContext.getResources().getString(R.string.ga_trackingId));

        private final String text;

        TrackerName(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public synchronized Tracker getTracker(MVPApplication.TrackerName trackerId) {
        if(!this.mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            Tracker t = analytics.newTracker(trackerId.toString());
            t.enableExceptionReporting(true);
            t.enableAutoActivityTracking(getResources().getBoolean(R.bool.ga_autoActivityTracking));

            // Enable Advertising Features.
            t.enableAdvertisingIdCollection(true);

            this.mTrackers.put(trackerId, t);
        }

        return (Tracker)this.mTrackers.get(trackerId);
    }

}