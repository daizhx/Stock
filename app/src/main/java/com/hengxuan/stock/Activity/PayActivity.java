package com.hengxuan.stock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.application.Constants;


public class PayActivity extends SubActivity{
    private TextView tvId;
    private TextView tvHint;
    private ImageView image;
    private EditText etInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvHint = (TextView) findViewById(android.R.id.text1);
        image = (ImageView) findViewById(R.id.iv);
        etInput = (EditText) findViewById(android.R.id.input);
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",-1);
        if(type == Constants.PAY_TYPE_ZFB){
            setTitle(getString(R.string.zfbzf));
            tvHint.setText("ͨ��΢�ſͻ��˹����볤ʱ�䰴ס��ͼ��ά�룬ѡ�񴢴�ͼ��Ȼ��ʹ��΢��ɨһɨ���ܣ������Ļ�Ϸ�����ᣬ���������ѡ��ձ���Ķ�ά�룬�Ϳ���֧��");
            etInput.setHint("������֧�����˺�");
        }else if(type == Constants.PAY_TYPE_WX){
            setTitle(getString(R.string.wxzf));
            tvHint.setText("ͨ��΢�ſͻ��˹����볤ʱ�䰴ס��ͼ��ά�룬ѡ�񴢴�ͼ��Ȼ��ʹ��΢��ɨһɨ���ܣ������Ļ�Ϸ�����ᣬ���������ѡ��ձ���Ķ�ά�룬�Ϳ���֧��");
            etInput.setHint("������΢���˺�");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
