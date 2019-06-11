package com.example.zhantuoer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhantuoer.Fragment.GoalAdapter;
import com.example.zhantuoer.Fragment.RecyclerGoal;

import java.util.ArrayList;
import java.util.List;

import static com.example.zhantuoer.Fragment.GoalAdapter.recyclerGoals;

public class SearchActivity extends AppCompatActivity  implements View.OnClickListener,TextView.OnEditorActionListener
{
    private RecyclerView recyclerView;
    private static final String RECYCLER_JSON = JsonTools.CONSTURL+"zhantuoer/findTextTopic";
    EditText edit_sou;
    InputMethodManager manager;//输入法管理器
    //找不到时弹出提示
    LinearLayout fail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        fail = (LinearLayout)findViewById(R.id.fail);
        //软键盘管理器
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        //layoutManager用于指定布局方式，这里为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        click_init();
    }

    private void click_init(){
        ImageView back = (ImageView)findViewById(R.id.back);
        TextView nav_sou = (TextView) findViewById(R.id.nav_sou);
        edit_sou = (EditText)findViewById(R.id.edit_sou);
        back.setOnClickListener(this);
        nav_sou.setOnClickListener(this);
        edit_sou.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.nav_sou:
                //先隐藏键盘
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow(edit_sou.getApplicationWindowToken(), 0);
                }
                getText();
                break;
            default:
        }
    }

    private void getText(){
        if(edit_sou.getText().toString().length()==0){
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
        }else{
            String searchUri = RECYCLER_JSON + "?text=" + edit_sou.getText().toString();
            // 启动 AsyncTask 以获取数据
            new GoalAsyncTask().execute(searchUri);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            getText();
        }
        return false;
    }

    private class GoalAsyncTask extends AsyncTask<String, Void, List<RecyclerGoal>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fail.setVisibility(View.GONE);
        }

        @Override
        protected List<RecyclerGoal> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<RecyclerGoal> result = JsonTools.fetchData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<RecyclerGoal> data) {
            if (data != null && !data.isEmpty()) {
                GoalAdapter mAdapter = new GoalAdapter(SearchActivity.this,new ArrayList<RecyclerGoal>());
                recyclerGoals.addAll(data);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else if(data != null &&data.size()==0){
                fail.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(SearchActivity.this,"网络有问题",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
