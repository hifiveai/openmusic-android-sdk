package com.hfopenplayer.sdk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfopenplayer.sdk.R;
import com.hfopenplayer.sdk.util.HifiveDisplayUtils;
import com.hifive.sdk.entity.HifiveMusicSheetModel;

import java.util.List;

/**
 * 歌单适配器
 */
public class HifiveMusicSheetAdapter extends RecyclerView.Adapter<HifiveMusicSheetAdapter.MusicSheetHolder> {
    private List<HifiveMusicSheetModel> dataList;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;
    //点击回调
    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
    public HifiveMusicSheetAdapter(Context ctx, List<HifiveMusicSheetModel> news) {
        this.mContext = ctx;
        this.dataList = news;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MusicSheetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hifive_item_music_sheet, viewGroup, false);
        return new MusicSheetHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MusicSheetHolder holder, final int position) {
        HifiveMusicSheetModel model = dataList.get(position);
        holder.tv_name.setText(model.getSheetName());
        if(position < 3){
            holder.vv_line.setVisibility(View.VISIBLE);
        }else{
            holder.vv_line.setVisibility(View.GONE);
        }
        if(model.getCover() != null && !TextUtils.isEmpty(model.getCover().getUrl())){
            Glide.with(mContext).load(model.getCover().getUrl())
                    .error(R.mipmap.hifvesdk_sheet_default)
                    .placeholder(R.mipmap.hifvesdk_sheet_default)
                    .into(holder.iv_image);//四周都是圆角的圆角矩形图片。
        }else{
            Glide.with(mContext).load(R.mipmap.hifvesdk_sheet_default)
                    .into(holder.iv_image);//四周都是圆角的圆角矩形图片。
        }
        //点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size()  : 0;
    }

    public void updateDatas(List<HifiveMusicSheetModel> newDatas) {
        dataList = newDatas;
        notifyDataSetChanged();
    }

    public void addDatas(List<HifiveMusicSheetModel> addDatas) {
        dataList.addAll(addDatas);
        notifyDataSetChanged();
    }

    public List<HifiveMusicSheetModel> getDatas() {
        return dataList;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    public class MusicSheetHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView iv_image;
        TextView tv_name;
        View vv_line;
        public MusicSheetHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            vv_line = itemView.findViewById(R.id.vv_line);
            iv_image = itemView.findViewById(R.id.iv_image);
            int pwidth = HifiveDisplayUtils.getScreenWidth(mContext)- HifiveDisplayUtils.dip2px(mContext, 60);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_image.getLayoutParams();
            params.height = pwidth/3;
            params.width = pwidth/3;
            iv_image.setLayoutParams(params);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
