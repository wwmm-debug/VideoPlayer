package njust.dzh.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {
    ListView mainLv;
    String url="http://baobab.kaiyanapp.com/api/v4/tabs/selected?udid=11111&vc=168&vn=3.3.1&deviceModel=Huawei6&first_channel=eyepetizer_baidu_market&last_channel=eyepetizer_baidu_market&system_version_code=20";
    private List<VideoBean.ItemListBean> mDatas;
    private VideoAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                String json=(String)msg.obj;
                //解析数据
                VideoBean videoBean=new Gson().fromJson(json,VideoBean.class);
                List<VideoBean.ItemListBean> itemList=videoBean.getItemList();
                for(int i=0;i<itemList.size();i++){
                    VideoBean.ItemListBean listBean=itemList.get(i);
                    if(listBean.getType().equals("video")){
                        mDatas.add(listBean);
                    }
                }
                //提示适配器更新数据
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("趣味视频");
        mainLv=findViewById(R.id.main_lv);
        //数据源
        mDatas=new ArrayList<>();
        //创建适配器
        adapter=new VideoAdapter(this,mDatas);
        //设置适配器
        mainLv.setAdapter(adapter);
        //加载网络数据
        loadData();

    }
    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonContent=HttpUtils.getJsonContent(url);
                Message message=new Message();
                message.what=1;
                message.obj=jsonContent;
                handler.sendMessage(message);
            }
        }).start();
    }
    @Override
    public void onBackPressed() {
        if (JzvdStd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    //释放正在被播放的视频信息

    protected void onStop(){
        super.onStop();
        JzvdStd.releaseAllVideos();
    }

}