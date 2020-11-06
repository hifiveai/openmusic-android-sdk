package com.hifive.sdk.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hifive.sdk.R;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;

import java.util.List;

/**
 * 歌曲搜索适配器
 */
public class HifiveMusicSearchAdapter extends RecyclerView.Adapter<HifiveMusicSearchAdapter.MusicSearchHolder> {
    private List<HifiveMusicModel> dataList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnAddLikeClickListener OnAddLikeClickListener;
    private OnAddkaraokeClickListener onAddkaraokeClickListener;

    private boolean showNumber;//判断是否显示序号
    //item点击回调
    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
    //添加喜欢点击回调
    public interface OnAddLikeClickListener {
        void onClick(View v, int position);
    }
    //添加K歌点击回调
    public interface OnAddkaraokeClickListener {
        void onClick(View v, int position);
    }
    //ty
    public HifiveMusicSearchAdapter(Context ctx, List<HifiveMusicModel> news,boolean showNumber) {
        this.mContext = ctx;
        this.dataList = news;
        this.showNumber = showNumber;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MusicSearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hifive_item_music_sheet_detail, viewGroup, false);
        return new MusicSearchHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MusicSearchHolder holder, final int position) {
        final HifiveMusicModel model = dataList.get(position);
        holder.tv_num.setText(String.valueOf(position+1));
        if(showNumber){//判断是否显示歌曲序号搜索页面不显示
            holder.tv_num.setVisibility(View.VISIBLE);
        }else{
            holder.tv_num.setVisibility(View.GONE);
        }
        if(HifiveDialogManageUtil.getInstance().getKaraokeList() != null
                && HifiveDialogManageUtil.getInstance().getKaraokeList().contains(model)){
            holder.iv_add_karaoke.setImageResource(R.mipmap.hifivesdk_icon_add_karaoke_select);
        }else{
            holder.iv_add_karaoke.setImageResource(R.mipmap.hifivesdk_icon_add_karaoke);
        }
        if(HifiveDialogManageUtil.getInstance().getLikeList() != null
                && HifiveDialogManageUtil.getInstance().getLikeList().contains(model)){
            holder.iv_add_like.setImageResource(R.mipmap.hifivesdk_icon_add_like_select);
        }else{
            holder.iv_add_like.setImageResource(R.mipmap.hifivesdk_icon_add_like);
        }
        holder.tv_name.setText(model.getName());
        holder.tv_detail.setText(model.getAuthor()+"-"+model.getAlbum()+"-"+model.getIntroduce());
        //点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(v,position);
                }
            }
        });
        holder.iv_add_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnAddLikeClickListener != null){
                    OnAddLikeClickListener.onClick(v,position);
                }
            }
        });
        holder.iv_add_karaoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddkaraokeClickListener != null){
                    onAddkaraokeClickListener.onClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size()  : 0;
    }

    public void updateDatas(List<HifiveMusicModel> newDatas) {
        dataList = newDatas;
        notifyDataSetChanged();
    }

    public void addDatas(List<HifiveMusicModel> addDatas) {
        dataList.addAll(addDatas);
        notifyDataSetChanged();
    }

    public List<HifiveMusicModel> getDatas() {
        return dataList;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnAddLikeClickListener(HifiveMusicSearchAdapter.OnAddLikeClickListener onAddLikeClickListener) {
        OnAddLikeClickListener = onAddLikeClickListener;
    }

    public void setOnAddkaraokeClickListener(OnAddkaraokeClickListener onAddkaraokeClickListener) {
        this.onAddkaraokeClickListener = onAddkaraokeClickListener;
    }

    public class MusicSearchHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tv_num;
        TextView tv_name;
        TextView tv_detail;
        ImageView iv_add_like;
        ImageView iv_add_karaoke;
        public MusicSearchHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            iv_add_like = itemView.findViewById(R.id.iv_add_like);
            iv_add_karaoke = itemView.findViewById(R.id.iv_add_karaoke);
        }
    }
}
