package com.hfopenplayer.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private static final String TAG = "AdvancedRecyclerViewAda";

    private int HEAD_COUNT = 0;
    private int FOOT_COUNT = 0;
    private int EMPTY_COUNT = 0;

    private int layout_header_view = -1;
    private int layout_empty_view = -1;
    private int layout_footer_view = -1;
    private int layout_content_view = setContentLayout();

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;
    private final static int TYPE_EMPTY = 3;

    private Context mContext;

    private OnRecyclerViewContentClick mOnRecyclerViewContentClick;
    private OnRecyclerViewHeaderClick mOnRecyclerViewHeaderClick;
    private OnRecyclerViewFooterClick mOnRecyclerViewFooterClick;
    private OnRecyclerViewEmptyClick mOnRecyclerViewEmptyClick;

    private List<T> data;

    public BaseRecyclerViewAdapter(Context mContext, List<T> data) {
        super();
        this.mContext = mContext;
        this.data = data;
    }

    private int getContentSize() {
        return data.size();
    }

    public boolean isHead(int position) {
        return HEAD_COUNT != 0 && position == 0;
    }

    public boolean isFoot(int position) {
        if (getContentSize() == 0) {
            return FOOT_COUNT != 0 && position == getContentSize() + HEAD_COUNT + EMPTY_COUNT;
        } else {
            return FOOT_COUNT != 0 && position == getContentSize() + HEAD_COUNT;
        }

    }

    public boolean isEmpty(int position) {
        if(getContentSize() == 0){
            return EMPTY_COUNT != 0 && position == 0 + HEAD_COUNT;
        }else {
            return false;
        }

    }


    @Override
    public int getItemViewType(int position) {
        int contentSize = getContentSize();

        if (contentSize == 0) {
            //没数据
            if (HEAD_COUNT != 0 && position == 0) {
                return TYPE_HEAD;
            } else if (EMPTY_COUNT != 0 && position == 0 + HEAD_COUNT) {
                return TYPE_EMPTY;
            } else {
                return TYPE_FOOTER;
            }
        } else {
            //有数据
            if (HEAD_COUNT != 0 && position == 0) {
                return TYPE_HEAD;
            } else if (FOOT_COUNT != 0 && position == HEAD_COUNT + contentSize) { // 尾部
                return TYPE_FOOTER;
            } else {
                return TYPE_CONTENT;
            }

        }

    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadHolder(LayoutInflater.from(mContext)
                    .inflate(layout_header_view, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            return new ContentHolder(LayoutInflater.from(mContext)
                    .inflate(layout_content_view, parent, false));
        } else if (viewType == TYPE_EMPTY) {
            return new EmptyHolder(LayoutInflater.from(mContext)
                    .inflate(layout_empty_view, parent, false));
        } else {
            return new FooterHolder(LayoutInflater.from(mContext)
                    .inflate(layout_footer_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof BaseRecyclerViewAdapter.HeadHolder) {
            onBindHeaderViewHolder(holder, position);
        } else if (holder instanceof BaseRecyclerViewAdapter.ContentHolder) {
            onBindContentViewHolder(holder, position - HEAD_COUNT);
        } else if (holder instanceof BaseRecyclerViewAdapter.EmptyHolder) {
            onBindEmptyViewHolder(holder, position);
        } else if (holder instanceof BaseRecyclerViewAdapter.FooterHolder) {
            onBindFooterViewHolder(holder, position);
        }
//        final int viewType = getItemViewType(position);
//        if (viewType == TYPE_HEAD) {
//            onBindHeaderViewHolder(holder, position);
//        } else if (viewType == TYPE_CONTENT) {
//            onBindContentViewHolder(holder, position - HEAD_COUNT);
//        } else if (viewType == TYPE_EMPTY) {
//            onBindEmptyViewHolder(holder, position);
//        } else {
//            onBindFooterViewHolder(holder, position);
//        }

    }

    public  void onBindHeaderViewHolder(BaseRecyclerViewHolder holder, int position){};

    public abstract void onBindContentViewHolder(BaseRecyclerViewHolder holder, int position);

    public  void onBindEmptyViewHolder(BaseRecyclerViewHolder holder, int position){};

    public  void onBindFooterViewHolder(BaseRecyclerViewHolder holder, int position){};

    protected abstract int setContentLayout();


    @Override
    public int getItemCount() {
        if (data.size() == 0) {
            return HEAD_COUNT + FOOT_COUNT + EMPTY_COUNT;
        } else {
            return data.size() + HEAD_COUNT + FOOT_COUNT;
        }
    }

    public void updateDatas(List<T> newDatas) {
        data = newDatas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> addDatas) {
        data.addAll(addDatas);
        notifyDataSetChanged();
    }


    public List<T> getDatas() {
        return data;
    }


    public void notifyContentItemChanged(int position){
        notifyItemChanged(position+HEAD_COUNT);
    }

    public void notifyContentItemChanged(int position,Object payload){
        notifyItemChanged(position+HEAD_COUNT,payload);
    }

    public void addHeaderView(@LayoutRes int id) {
        HEAD_COUNT = 1;
        layout_header_view = id;
        notifyDataSetChanged();
    }


    public void removeHeaderView() {
        HEAD_COUNT = 0;
        layout_header_view = -1;
        notifyDataSetChanged();
    }

    public void addEmptyView(@LayoutRes int id) {
        EMPTY_COUNT = 1;
        layout_empty_view = id;
        notifyDataSetChanged();
    }

    public void removeEmptyView() {
        EMPTY_COUNT = 0;
        layout_empty_view = -1;
        notifyDataSetChanged();
    }


    public void addFooterView(@LayoutRes int id) {
        FOOT_COUNT = 1;
        layout_footer_view = id;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        FOOT_COUNT = 0;
        layout_footer_view = -1;
        notifyDataSetChanged();
    }


    public class HeadHolder extends BaseRecyclerViewHolder {
        public View rootView;

        public HeadHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewHeaderClick != null) {
                        mOnRecyclerViewHeaderClick.OnHeaderClick();
                    }
                }
            });

        }
    }

    public class ContentHolder extends BaseRecyclerViewHolder {
        public View rootView;

        public ContentHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewContentClick != null) {
                        if (getLayoutPosition() < getContentSize() || getLayoutPosition() > 0) {
                            mOnRecyclerViewContentClick.OnContentClick(getLayoutPosition() - HEAD_COUNT);
                        }
                    }
                }
            });

        }
    }

    public class FooterHolder extends BaseRecyclerViewHolder {
        public View rootView;

        public FooterHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewFooterClick != null) {
                        mOnRecyclerViewFooterClick.OnFooterClick();
                    }
                }
            });
        }
    }

    public class EmptyHolder extends BaseRecyclerViewHolder {
        public View rootView;

        public EmptyHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewEmptyClick != null) {
                        mOnRecyclerViewEmptyClick.OnEmptyClick();
                    }
                }
            });

        }
    }


    public void setOnRecyclerViewContentClick(OnRecyclerViewContentClick mOnRecyclerViewContentClick) {
        this.mOnRecyclerViewContentClick = mOnRecyclerViewContentClick;
    }

    public void setOnRecyclerViewHeaderClick(OnRecyclerViewHeaderClick mOnRecyclerViewHeaderClick) {
        this.mOnRecyclerViewHeaderClick = mOnRecyclerViewHeaderClick;
    }

    public void setOnRecyclerViewFooterClick(OnRecyclerViewFooterClick mOnRecyclerViewFooterClick) {
        this.mOnRecyclerViewFooterClick = mOnRecyclerViewFooterClick;
    }

    public void setOnRecyclerViewEmptyClick(OnRecyclerViewEmptyClick mOnRecyclerViewEmptyClick) {
        this.mOnRecyclerViewEmptyClick = mOnRecyclerViewEmptyClick;
    }

    public interface OnRecyclerViewContentClick {
        void OnContentClick(int position);
    }

    public interface OnRecyclerViewHeaderClick {
        void OnHeaderClick();
    }

    public interface OnRecyclerViewFooterClick {
        void OnFooterClick();
    }

    public interface OnRecyclerViewEmptyClick {
        void OnEmptyClick();
    }


}