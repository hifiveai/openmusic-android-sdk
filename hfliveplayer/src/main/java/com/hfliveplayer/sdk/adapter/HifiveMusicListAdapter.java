package com.hfliveplayer.sdk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.model.HifiveMusicAuthorModel;
import com.hfliveplayer.sdk.model.HifiveMusicModel;

import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;

import java.util.List;

/**
 * 歌曲列表
 */
public class HifiveMusicListAdapter extends BaseRecyclerViewAdapter{
    private final Context mContext;

    private OnItemDeleteClickListener onItemDeleteClickListener;
    private OnEmptyViewClickListener onEmptyViewClickListener;


    public HifiveMusicListAdapter(Context mContext, List<HifiveMusicModel> news) {
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

    @Override
    public void onBindContentViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final HifiveMusicModel model = (HifiveMusicModel) getDatas().get(position);

        holder.setText(R.id.tv_num,String.valueOf(position+1));
        holder.setText(R.id.tv_name,model.getMusicName());

        if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                && HifiveDialogManageUtil.getInstance().getPlayMusic().getMusicId().equals(model.getMusicId())){
            holder.setVisible(R.id.tv_num, View.GONE);
            holder.setVisible(R.id.iv_play, View.VISIBLE);
            Glide.with(mContext).asGif().load(R.drawable.hifive_music_play).into((ImageView) holder.get(R.id.iv_play));
        }else{
            holder.setVisible(R.id.tv_num, View.VISIBLE);
            holder.setVisible(R.id.iv_play, View.GONE);

        }
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
