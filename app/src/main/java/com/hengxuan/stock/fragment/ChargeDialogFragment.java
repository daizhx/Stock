package com.hengxuan.stock.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hengxuan.stock.R;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.user.User;

/**
 * Created by Administrator on 2015/12/5.
 */
public class ChargeDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.charge_dialog, null);
        view.findViewById(R.id.zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goIntent();
                dismiss();
            }
        });
        view.findViewById(R.id.wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"暂不支持,请先选择支付宝支付",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

    void goIntent(){
        User user = User.getUser(getActivity());
        Uri uri = Uri.parse(HttpAPI.ZFB_ZF_URL+ user.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        getActivity().startActivity(intent);
    }
}
