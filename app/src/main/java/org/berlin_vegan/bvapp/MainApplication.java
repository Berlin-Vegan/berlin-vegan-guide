package org.berlin_vegan.bvapp;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.something_went_wrong
)

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new ACRAPostSender());
    }
}
