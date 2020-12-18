package com.hfliveplayer.sdk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hifive.sdk.entity.HifiveMusicAuthorModel;
import com.hifive.sdk.entity.HifiveMusicModel;

import java.util.List;

public class HifiveMusicSheetListAdapter extends BaseRecyclerViewAdapter{
    private final Context mContext;
    private OnAddLikeClickListener OnAddLikeClickListener;
    private OnAddkaraokeClickListener onAddkaraokeClickListener;

    private final boolean showNumber;//判断是否显示序号

    public HifiveMusicSheetListAdapter(Context mContext, List<HifiveMusicModel> news, boolean showNumber) {
        super(mContext, news);
        this.mContext = mContext;
        this.showNumber = showNumber;
    }

    @Override
    public void onBindHeaderViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.setText(R.id.tv_number,mContext.getString(R.string.hifivesdk_music_all_play, getDatas().size()));
    }

    @Override
    public void onBindContentViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final HifiveMusicModel model = (HifiveMusicModel) getDatas().get(position);

        holder.setText(R.id.tv_num,String.valueOf(position+1));
        if(showNumber){//判断是否显示歌曲序号搜索页面不显示
            holder.setVisible(R.id.tv_num,View.VISIBLE);
        }else{
            holder.setVisible(R.id.tv_num,View.GONE);
        }
        if(HifiveDialogManageUtil.getInstance().getKaraokeList() != null
                && HifiveDialogManageUtil.getInstance().getKaraokeList().contains(model)){
            holder.setImageResource(R.id.iv_add_karaoke,R.mipmap.hifivesdk_icon_add_karaoke_select);
        }else{
            holder.setImageResource(R.id.iv_add_karaoke,R.mipmap.hifivesdk_icon_add_karaoke);
        }
        if (HifiveDialogManageUtil.getInstance().getLikeList() != null &&
                HifiveDialogManageUtil.getInstance().getLikeList().contains(model)) {
            holder.setImageResource(R.id.iv_add_like,R.mipmap.hifivesdk_icon_add_like_select);
        } else {
            holder.setImageResource(R.id.iv_add_like,R.mipmap.hifivesdk_icon_add_like);
        }
        holder.setText(R.id.tv_name,model.getMusicName());
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
