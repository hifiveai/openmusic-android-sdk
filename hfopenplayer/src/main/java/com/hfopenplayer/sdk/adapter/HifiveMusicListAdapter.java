package com.hfopenplayer.sdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hfopen.sdk.entity.Author;
import com.hfopen.sdk.entity.Composer;
import com.hfopen.sdk.entity.Desc;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenplayer.sdk.R;

import com.hfopenplayer.sdk.util.HifiveDialogManageUtil;
import com.hifive.sdk.entity.HifiveMusicAuthorModel;
import com.hifive.sdk.entity.HifiveMusicModel;

import java.util.List;

/**
 * 歌曲列表
 */
public class HifiveMusicListAdapter extends BaseRecyclerViewAdapter{
    private final Context mContext;

    private OnItemDeleteClickListener onItemDeleteClickListener;
    private OnEmptyViewClickListener onEmptyViewClickListener;


    public HifiveMusicListAdapter(Context mContext, List<MusicRecord> news) {
        super(mContext, news);
        this.mContext = mContext;
    }

    @Override
    public void onBindHeaderViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.setText(R.id.tv_number,mContext.getString(R.string.hifivesdk_music_all_play, getDatas().size()));
    }

    @Override
    public void onBindEmptyViewHolder(BaseRecyclerViewHolder holder, final int position) {
        holder.setOnClickListener(R.id.tv_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onEmptyViewClickListener){
                    onEmptyViewClickListener.onClick(v,position);
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindContentViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final MusicRecord model = (MusicRecord) getDatas().get(position);

        holder.setText(R.id.tv_num,String.valueOf(position+1));
        holder.setText(R.id.tv_name,model.getMusicName());

        if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                && HifiveDialogManageUtil.getInstance().getPlayMusic().getMusicId().equals(model.getMusicId())){
            holder.setVisible(R.id.tv_num, View.GONE);
            holder.setVisible(R.id.iv_play, View.VISIBLE);

            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(mContext).asGif().load(R.drawable.hifive_music_play)
                    .apply(options)
                    .into((ImageView) holder.get(R.id.iv_play));
        }else{
            holder.setVisible(R.id.tv_num, View.VISIBLE);
            holder.setVisible(R.id.iv_play, View.GONE);

        }
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
        if(model.getAuthor() != null && model.getAuthor().size() >0){
            for(Author authorModel:model.getAuthor()){
                if(stringBuffer.length() >0){
                    stringBuffer.append("-");
                }
                stringBuffer.append(authorModel.getName());
            }
        }

        if(model.getArranger()!= null &&  model.getArranger().size() >0){
            for(Desc authorModel:model.getArranger()){
                if(stringBuffer.length() >0){
                    stringBuffer.append("-");
                }
                stringBuffer.append(authorModel.getName());
            }
        }

        holder.setText(R.id.tv_detail,stringBuffer.toString());


        //点击事件
        holder.setOnClickListener(R.id.iv_delete,new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(onItemDeleteClickListener != null){
                    onItemDeleteClickListener.onClick(v,position);
                }
            }
        });

    }

    //item删除回调
    public interface OnItemDeleteClickListener {
        void onClick(View v, int position);
    }
    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    //空布局点击添加
    public interface OnEmptyViewClickListener {
        void onClick(View v, int position);
    }
    public void setOnEmptyViewClickListener(OnEmptyViewClickListener onEmptyViewClickListener) {
        this.onEmptyViewClickListener = onEmptyViewClickListener;
    }


    @Override
    protected int setContentLayout() {
        return R.layout.hifive_item_music_list;
    }
}
