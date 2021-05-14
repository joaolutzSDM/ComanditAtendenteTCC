package br.com.alloy.comanditatendente.ui.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import br.com.alloy.comanditatendente.R;

public class FragmentUtil {

    public static AlertDialog createGenericDialog(Context context, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setTitle(title);
        if(message != null) {
            alertDialog.setMessage(message);
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    //Método para mostrar uma mensagem genérica ao usuário quando um erro ocorrer. (o app é fechado ao clicar no OK)
    public static void showFinishDialog(Context context, @Nullable String message, FragmentActivity activity) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(message == null ? context.getString(R.string.msgError) : message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.btnOK), (dialog, which) -> activity.finish());
        alertDialog.show();
    }

}
