package com.hengxuan.stock.Activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hengxuan.stock.AnalystListAdapter;
import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.model.Analyst;

import java.util.ArrayList;
import java.util.List;

public class AnalystsActivity extends Activity {
    private ListView listView;
    private AnalystListAdapter analystListAdapter;
    //����
    private List<Analyst> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysts);
        initData();
        listView = (ListView) findViewById(android.R.id.list);
        analystListAdapter = new AnalystListAdapter(this,data);
        listView.setAdapter(analystListAdapter);
        listView.setEnabled(false);
    }

    private void initData(){
        Analyst analyst = new Analyst();
        analyst.name = getString(R.string.jubaoyiqi);
        analyst.content = "�۱�һ�ڲ�����ԭ��Ѷ��Ʊ���Ʒ���ʦС����ʦ��" +
                "�ӣ����ٿض����ߣ����߿����������߲��β�" +
                "����˳�ƶ�Ϊ����������������󻯡�������Ѷ��" +
                "Ʊ��һ������������<font color=red>645.78%</font>,��������<font color=red>15.45%</font>��" +
                "�˻��ʲ�<font color=red>523006.53Ԫ</font>��";
        data.add(analyst);

        Analyst analyst2 = new Analyst();
        analyst2.name = getString(R.string.niuniuchaogu);
        analyst2.content = "ţţ���ɲ�����ԭ��Ѷ��Ʊ���Ʒ���ʦС����ʦ��" +
                "�ӣ����ٿض����ߣ����߿����������߲��β�" +
                "����˳�ƶ�Ϊ����������������󻯡�������Ѷ��" +
                "Ʊ��һ������������<font color=red>645.78%</font>,��������<font color=red>15.45%</font>��" +
                "�˻��ʲ�<font color=red>523006.53Ԫ</font>��";
        data.add(analyst2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysts, menu);
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
