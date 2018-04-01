package com.chenggong.modelsearch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenggong.modelsearch.aty.WebPageActivity;
import com.chenggong.modelsearch.bean.Result;
import com.chenggong.modelsearch.R;
import com.chenggong.modelsearch.net.HttpUtil;
import com.chenggong.modelsearch.net.ImageUtil;
import com.chenggong.modelsearch.utils.Logger;

import java.util.List;

/**
 * Created by chenggong on 18-3-27.
 * 搜索结果的界面中的RecyclerView的adapter
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Context context;
    private static final String TAG = "ResultAdapter";
    private List<Result> resultList;

    /**
     * ViewHolder 内部类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tv_title;  //暂时为搜索物品+作者
        ImageView model_image;
        TextView tv_timePost;
        TextView tv_description;
        TextView tv_webpageURL;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_title = itemView.findViewById(R.id.tv_title);
            model_image = itemView.findViewById(R.id.model_image);
            tv_timePost = itemView.findViewById(R.id.tv_timePost);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_webpageURL = itemView.findViewById(R.id.tv_webpageURL);
        }

    }

    public ResultAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Result result = resultList.get(position);
//                holder.view.setBackgroundColor(Color.GRAY);
                WebPageActivity.start(context, result.getWebpageURL());
            }
        });
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Result result = resultList.get(position);
                WebPageActivity.start(context, result.getWebpageURL());
            }
        });
        holder.tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Result result = resultList.get(position);
                WebPageActivity.start(context, result.getWebpageURL());
            }
        });
        holder.tv_webpageURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Result result = resultList.get(position);
                WebPageActivity.start(context, result.getWebpageURL());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = resultList.get(position);
        holder.tv_title.setText(result.getName() + "-" + result.getAuthor() + "-" + result.getSource());
        holder.tv_title.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        ImageUtil.loadImage(context, result.getImgURL(), holder.model_image);

        holder.tv_description.setText(result.getDescription());
        holder.tv_timePost.setText(result.getTimePost());
        holder.tv_webpageURL.setText(result.getWebpageURL());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

}
