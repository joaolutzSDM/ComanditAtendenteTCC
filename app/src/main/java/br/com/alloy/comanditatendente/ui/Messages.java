package br.com.alloy.comanditatendente.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.service.exception.APIException;

import androidx.appcompat.app.AlertDialog;

public class Messages {

    public static void showDialogMessage(Context context, String message){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.btnOK), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
