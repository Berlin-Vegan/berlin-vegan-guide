package org.berlin_vegan.bvapp;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by georgi on 4/14/15.
 */
public class UiUtils {

    public static MaterialDialog showMaterialDialog(Context context, String title, String content) {
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
        dialog.show();
        return dialog;
    }
}
