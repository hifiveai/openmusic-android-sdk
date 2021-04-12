package com.hfopenmusic.sdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hfopen.sdk.entity.Author;
import com.hfopen.sdk.entity.Composer;
import com.hfopen.sdk.entity.Desc;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenmusic.sdk.R;

import java.util.List;

public class HifiveHotMusicAdapter extends BaseRecyclerViewAdapter{
    private final Context mContext;

    public HifiveHotMusicAdapter(Context mContext, List<MusicRecord> news) {
        super(mContext, news);
        this.mContext = mContext;
    }

    @Override
    public void onBindContentViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final MusicRecord model = (MusicRecord) getDatas().get(position);

        TextView line = holder.get(R.id.tv_line);
        line.setTextColor(Color.parseColor(position>2? "#FFFFFF":"#D34747"));
        line.setText(position+1+"");

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
        stringBuffer.append(" - ").append(model.getAlbumName());

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
        holder.setText(R.id.tv_hot,stringBuffer.toString());

    }

    @Override
    protected int setContentLayout() {
        return R.layout.hifive_item_music_hot;
    }
}
