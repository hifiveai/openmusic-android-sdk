package com.hfopenmusic.sdk.adapter;

import android.content.Context;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfopen.sdk.entity.Author;
import com.hfopen.sdk.entity.Composer;
import com.hfopen.sdk.entity.Desc;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenmusic.sdk.R;

import java.util.List;

public class HifiveMusicSheetListAdapter extends BaseRecyclerViewAdapter{
    private final Context mContext;
    private OnAddLikeClickListener OnAddLikeClickListener;
    private OnAddkaraokeClickListener onAddkaraokeClickListener;


    public HifiveMusicSheetListAdapter(Context mContext, List<MusicRecord> news) {
        super(mContext, news);
        this.mContext = mContext;
    }

    @Override
    public void onBindHeaderViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.setText(R.id.tv_number,mContext.getString(R.string.hifivesdk_music_all_play, getDatas().size()));
    }

    @Override
    public void onBindContentViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final MusicRecord model = (MusicRecord) getDatas().get(position);

//        if(HFOpenMusic.getInstance().getKaraokeList() != null
//                && HFOpenMusic.getInstance().getKaraokeList().contains(model)){
//            holder.setImageResource(R.id.iv_add_karaoke,R.mipmap.hifivesdk_icon_add_karaoke_select);
//        }else{
//            holder.setImageResource(R.id.iv_add_karaoke,R.mipmap.hifivesdk_icon_add_karaoke);
//        }
//        if (HFOpenMusic.getInstance().getLikeList() != null &&
//                HFOpenMusic.getInstance().getLikeList().contains(model)) {
//            holder.setImageResource(R.id.iv_add_like,R.mipmap.hifivesdk_icon_add_like_select);
//        } else {
//            holder.setImageResource(R.id.iv_add_like,R.mipmap.hifivesdk_icon_add_like);
//        }
        holder.setText(R.id.tv_name,model.getMusicName());
        StringBuilder stringBuffer = new StringBuilder();
        if(model.getArtist() != null && model.getArtist().size() >0){
            for(Desc authorModel:model.getArtist()){
                if(stringBuffer.length() >0){
                    stringBuffer.append("-");
                }
                stringBuffer.append(authorModel.getName());
            }
        }else{
            if(model.getComposer()!= null && model.getComposer().size() >0){
                for(Composer authorModel:model.getComposer()){
                    if(stringBuffer.length() >0){
                        stringBuffer.append("-");
                    }
                    stringBuffer.append(authorModel.getName());
                }
            }
        }
//        if(model.getAuthor() != null && model.getAuthor().size() >0){
//            for(Author authorModel:model.getAuthor()){
//                if(stringBuffer.length() >0){
//                    stringBuffer.append("-");
//                }
//                stringBuffer.append(authorModel.getName());
//            }
//        }
//
//        if(model.getArranger()!= null &&  model.getArranger().size() >0){
//            for(Desc authorModel:model.getArranger()){
//                if(stringBuffer.length() >0){
//                    stringBuffer.append("-");
//                }
//                stringBuffer.append(authorModel.getName());
//            }
//        }

        stringBuffer.append(" - ").append(model.getAlbumName());
        holder.setText(R.id.tv_detail,stringBuffer.toString());


        holder.setOnClickListener(R.id.iv_add_like,new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(OnAddLikeClickListener != null){
                    OnAddLikeClickListener.onClick(v,position);
                }
            }
        });

        holder.setOnClickListener(R.id.iv_add_karaoke,new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(onAddkaraokeClickListener != null){
                    onAddkaraokeClickListener.onClick(v,position);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            //payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder((BaseRecyclerViewHolder) holder, position);
        } else {
            if((int)payloads.get(0) == 1){
                ((BaseRecyclerViewHolder)holder).setImageResource(R.id.iv_add_karaoke,R.mipmap.hifivesdk_icon_add_karaoke_select);
            }else{
                ((BaseRecyclerViewHolder)holder).setImageResource(R.id.iv_add_like,R.mipmap.hifivesdk_icon_add_like_select);
            }

        }
    }

    //添加喜欢点击回调
    public interface OnAddLikeClickListener {
        void onClick(View v, int position);
    }
    //添加K歌点击回调
    public interface OnAddkaraokeClickListener {
        void onClick(View v, int position);
    }

    public void setOnAddLikeClickListener(OnAddLikeClickListener onAddLikeClickListener) {
        this.OnAddLikeClickListener = onAddLikeClickListener;
    }

    public void setOnAddkaraokeClickListener(OnAddkaraokeClickListener onAddkaraokeClickListener) {
        this.onAddkaraokeClickListener = onAddkaraokeClickListener;
    }

    @Override
    protected int setContentLayout() {
        return R.layout.hifive_item_music_sheet_detail;
    }
}
