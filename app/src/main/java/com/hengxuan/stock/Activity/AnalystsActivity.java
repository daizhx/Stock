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

public class AnalystsActivity extends SubActivity {
    private ListView listView;
    private AnalystListAdapter analystListAdapter;
    //数据
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
        analyst.content = "铭诺股票策略由铭诺投资有限公司老师" +
                "提供操作策略，灵活操控短中线，短线快进快出，中线波段操" +
                "作，顺势而为，力求做到收益最大化。" +
                "年收益率最高达<font color=red>645.78%</font>,月收益率超过<font color=red>20%</font>，" +
                "账户资产超过<font color=red>5000000元</font>。";
        data.add(analyst);

        Analyst analyst2 = new Analyst();
        analyst2.name = getString(R.string.niuniuchaogu);
        analyst2.content = "牛牛炒股策略牛牛网资深财经专家提过股票操作" +
                "策略，根据基本面分析，中长线操作，风险低，收益可观。" +
                "年收益率可超过<font color=red>200%</font>,月收益率可超过<font color=red>15%</font>";
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
