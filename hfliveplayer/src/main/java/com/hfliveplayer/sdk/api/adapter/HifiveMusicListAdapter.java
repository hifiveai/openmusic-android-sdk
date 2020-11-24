package com.hfliveplayer.sdk.demo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.demo.model.HifiveMusicAuthorModel;
import com.hfliveplayer.sdk.demo.model.HifiveMusicModel;
import com.hfliveplayer.sdk.demo.util.HifiveDialogManageUtil;

import java.util.List;

/**
 * 歌曲列表适配器
 */
public class HifiveMusicListAdapter extends RecyclerView.Adapter<HifiveMusicListAdapter.MusicListHolder> {
    private List<HifiveMusicModel> dataList;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnItemDeleteClickListener onItemDeleteClickListener;
    //item点击回调
    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
    //item删除回调
    public interface OnItemDeleteClickListener {
        void onClick(View v, int position);
    }
    //ty
    public HifiveMusicListAdapter(Context ctx, List<HifiveMusicModel> news) {
        this.mContext = ctx;
        this.dataList = news;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MusicListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hifive_item_music_list, viewGroup, false);
        return new MusicListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MusicListHolder holder, final int position) {
        final HifiveMusicModel model = dataList.get(position);
        if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                && HifiveDialogManageUtil.getInstance().getPlayMusic().getMusicId().equals(model.getMusicId())){
            holder.tv_num.setVisibility(View.GONE);
            holder.iv_play.setVisibility(View.VISIBLE);
            Glide.with(mContext).asGif().load(R.drawable.hifive_music_play).into(holder.iv_play);
        }else{
            holder.iv_play.setVisibility(View.GONE);
            holder.tv_num.setVisibility(View.VISIBLE);
        }
        holder.tv_num.setText(String.valueOf(position+1));
        holder.tv_name.setText(model.getMusicName());
        StringBuilder stringBuffer = new StringBuilder();
        if(model.getArtist() != null && model.getArtist().size() >0){
            for(HifiveMusicAuthorModel authorModel:model.getArtist()){
                if(stringBuffer.length() >0){
                    stringBuffer.append("-");
                }
                stringBuffer.append(authorModel.getName());
            }
        }else{
            if(model.getComposer()!= null && model.getComposer().size() >0){
                for(HifiveMusicAuthorModel authorModel:model.getComposer()){
                    if(stringBuffer.length() >0){
                        stringBuffer.append("-");
                    }
                    stringBuffer.append(authorModel.getName());
                }
            }
        }
        if(model.getAlbum()!= null && !TextUtils.isEmpty(model.getAlbum().getName())){
            if(stringBuffer.length() >0){
                stringBuffer.append("-");
            }
            stringBuffer.append(model.getAlbum().getName());
        }
        if(!TextUtils.isEmpty(model.getIntro())){
            if(stringBuffer.length() >0){
                stringBuffer.append("-");
            }
            stringBuffer.append(model.getIntro());
        }
        holder.tv_detail.setText(stringBuffer.toString());
        //点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(v,position);
                }
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemDeleteClickListener != null){
                    onItemDeleteClickListener.onClick(v,position);
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
    public List<HifiveMusicModel> getDatas() {
        return dataList;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    public static class MusicListHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tv_num;
        ImageView iv_play;
        TextView tv_name;
        TextView tv_detail;
        ImageView iv_delete;
        public MusicListHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            iv_play = itemView.findViewById(R.id.iv_play);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
