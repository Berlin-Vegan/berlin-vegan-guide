package org.berlin_vegan.bvapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by georgi on 4/14/15.
 */
public class UiUtils {

    public static MaterialDialog showMaterialDialog(Context context, String title, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        MaterialDialog dialog = showMaterialDialog(context, title, spannableStringBuilder);
        dialog.show();
        return dialog;
    }

    public static MaterialDialog showMaterialDialogCheckboxes(Context context, String title,
                                                              String[] checkBoxItems, int selected,
                                                              MaterialDialog.ListCallbackSingleChoice checkCallBack) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(checkBoxItems)
                .itemsCallbackSingleChoice(selected, checkCallBack)
                .autoDismiss(true)
                .contentColorRes(R.color.material_dialog_content)
                .titleColorRes(R.color.material_dialog_title)
                .backgroundColorRes(R.color.material_dialog_background)
                .negativeText(android.R.string.cancel)
                .negativeColorRes(R.color.material_dialog_buttons)
                .build();
        dialog.show();
        return dialog;
    }

    public static MaterialDialog showMaterialDialog(Context context, String title, SpannableStringBuilder content) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .autoDismiss(true)
                .contentColorRes(R.color.material_dialog_content)
                .titleColorRes(R.color.material_dialog_title)
                .backgroundColorRes(R.color.material_dialog_background)
                .positiveText(android.R.string.ok)
                .positiveColorRes(R.color.material_dialog_buttons)
                .build();
        dialog.show();
        return dialog;
    }

    private static SpannableStringBuilder createAboutContent(final Context context) {
        // get app version
        String versionName = BuildConfig.VERSION_GIT_DESCRIPTION;

        // build the about body view and append the link to see OSS licenses
        String title = context.getString(R.string.app_name) + " " + context.getString(R.string.guide);
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(Html.fromHtml(context.getString(R.string.about_body, title, versionName)));

        SpannableString licensesLink = new SpannableString(context.getString(R.string.about_licenses));
        licensesLink.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                showOpenSourceLicenses((Activity) context);
            }
        }, 0, licensesLink.length(), 0);
        aboutBody.append("\n\n");
        aboutBody.append(licensesLink);
        return aboutBody;
    }

    public static void showOpenSourceLicenses(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog_licenses");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        new OpenSourceLicensesDialog().show(fragmentTransaction, "dialog_licenses");
    }

    public static MaterialDialog showMaterialAboutDialog(Context context, String title) {
        SpannableStringBuilder content = createAboutContent(context);
        MaterialDialog dialog = showMaterialDialog(context, title, content);
        dialog.show();
        return dialog;
    }

    public static MaterialDialog showMaterialProgressDialog(Context context, String title, String content) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .autoDismiss(true)
                .contentColorRes(R.color.material_dialog_content)
                .titleColorRes(R.color.material_dialog_title)
                .backgroundColorRes(R.color.material_dialog_background)
                .progress(true, 0)
                .build();
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static class OpenSourceLicensesDialog extends DialogFragment {

        public OpenSourceLicensesDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            WebView webView = new WebView(getActivity());
            webView.loadUrl("file:///android_asset/licenses.html");
            return new AlertDialogWrapper.Builder(getActivity())
                    .setTitle(R.string.about_licenses)
                    .setView(webView)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }
}
