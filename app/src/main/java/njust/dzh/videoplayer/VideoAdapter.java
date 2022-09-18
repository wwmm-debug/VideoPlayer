package njust.dzh.videoplayer;

import android.content.Context;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JzvdStd;

public class VideoAdapter extends BaseAdapter {
    Context context;

    List<VideoBean.ItemListBean> mDatas;

    public VideoAdapter(Context context,List<VideoBean.ItemListBean> mDatas) {
        this.context = context;
        this.mDatas=mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_mainlv,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }
        //获取指定位置的数据源
        VideoBean.ItemListBean.DataBean dataBean=mDatas.get(i).getData();
        //设置发布者信息
        VideoBean.ItemListBean.DataBean.AuthorBean author=dataBean.getAuthor();
        holder.nameTv.setText(author.getName());
        holder.descTv.setText(author.getDescription());
        String iconURL=author.getIcon();
        if(!TextUtils.isEmpty(iconURL)){
            Picasso.with(context).load(iconURL).into(holder.iconIv);
        }
        //获取点赞数和评论数
        VideoBean.ItemListBean.DataBean.ConsumptionBean consumptionBean=dataBean.getConsumption();
        holder.heartTv.setText(consumptionBean.getRealCollectionCount()+"");
        holder.replyTv.setText(consumptionBean.getReplyCount()+"");
        //设置播放器的信息
        holder.jzvdStd.setUp(dataBean.getPlayUrl(),dataBean.getTitle(),JzvdStd.SCREEN_NORMAL);
        String thumbUrl=dataBean.getCover().getFeed();
        Picasso.with(context).load(thumbUrl).into(holder.jzvdStd.thumbImageView);
        
        holder.jzvdStd.positionInList=i;

        return view;
    }
    class ViewHolder{
        JzvdStd jzvdStd;
        ImageView iconIv;
        TextView nameTv,descTv,heartTv,replyTv;
        public ViewHolder(View view){
            jzvdStd=view.findViewById(R.id.item_main_jzvd);
            iconIv=view.findViewById(R.id.item_main_iv);
            nameTv=view.findViewById(R.id.item_main_tv_name);
            descTv=view.findViewById(R.id.item_main_tv_des);
            heartTv=view.findViewById(R.id.item_main_tv_heart);
            replyTv=view.findViewById(R.id.item_main_tv_reply);
        }

    }


}
