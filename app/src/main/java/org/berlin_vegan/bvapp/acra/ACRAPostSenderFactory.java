package org.berlin_vegan.bvapp.acra;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;


public class ACRAPostSenderFactory implements ReportSenderFactory {
    // NB requires a no arg constructor.
    @NonNull
    public ReportSender create(Context context, ACRAConfiguration config) {
        return new ACRAPostSender();
    }
}
