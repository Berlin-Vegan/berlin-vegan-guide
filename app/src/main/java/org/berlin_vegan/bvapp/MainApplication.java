package org.berlin_vegan.bvapp;

import android.app.Application;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.berlin_vegan.bvapp.acra.ACRAPostSender;

@ReportsCrashes(
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.something_went_wrong
)

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Log.i(TAG, "initialize application crash reporting");
            ACRA.init(this);
            ACRA.getErrorReporter().setReportSender(new ACRAPostSender());
        }
    }
}
