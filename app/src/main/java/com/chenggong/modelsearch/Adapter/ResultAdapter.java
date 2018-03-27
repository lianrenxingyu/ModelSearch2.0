package com.chenggong.modelsearch.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenggong.modelsearch.Bean.Result;
import com.chenggong.modelsearch.R;

import java.util.List;

/**
 * Created by chenggong on 18-3-27.
 * 搜索结果的界面中的RecyclerView的adapter
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Result> mResultList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;  //暂时为搜索物品+作者
        ImageView model_image;
        TextView tv_timePost;
        TextView tv_description;
        TextView tv_webpageURL;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            model_image = itemView.findViewById(R.id.model_image);
            tv_timePost = itemView.findViewById(R.id.tv_timePost);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_webpageURL = itemView.findViewById(R.id.tv_webpageURL);
        }

    }

    public ResultAdapter(List<Result> resultList) {
        mResultList = resultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = mResultList.get(position);
        holder.tv_title.setText(result.getName() + "-" + result.getAuthor());
        holder.model_image.setImageResource(R.drawable.ying_ting);
        holder.tv_description.setText(result.getDescription());
        holder.tv_timePost.setText(result.getTimePost());
        holder.tv_webpageURL.setText(result.getWebpageURL());
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }
}
