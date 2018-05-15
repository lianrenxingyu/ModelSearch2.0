package com.chenggong.modelsearch2_0.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenggong.modelsearch2_0.R;
import com.chenggong.modelsearch2_0.bean.ResultBean;
import com.chenggong.modelsearch2_0.net.HttpUtil;
import com.chenggong.modelsearch2_0.net.ImageUtil;
import com.chenggong.modelsearch2_0.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.Okio;

/**
 * Created by chenggong on 18-5-13.
 *
 * @author chenggong
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private static final String TAG = "DownloadAdapter";
    private Context context;
    private ResultBean resultBean;

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "保存到Download,下载完成", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public DownloadAdapter(Context context, ResultBean resultBean) {
        this.context = context;
        this.resultBean = resultBean;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_name.setText(getNameFromUrl(resultBean.getObjs().get(position)));
        holder.tv_category.setText(getCategoryFromUrl(resultBean.getObjs().get(position)));

        ImageUtil.loadImage(context, resultBean.getPics().get(position), holder.iv_objImage);

        final Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.dialog_image_view);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        ImageUtil.loadImage(dialog.getContext(), resultBean.getPics().get(position), imageView);
        dialog.setCanceledOnTouchOutside(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        holder.iv_objImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

            }
        });

        final AlertDialog isDownloadDialog = new AlertDialog.Builder(context)
                .setMessage("要下载"+getNameFromUrl(resultBean.getObjs().get(position))+"吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uiHandler.sendEmptyMessage(0);
                        //TODO 下载文件操作
                        HttpUtil.downloadObject(resultBean.getObjs().get(position), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                Logger.d(TAG, "下载失败");
                                uiHandler.sendEmptyMessage(2);

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                byte[] bytes = new byte[2048];
                                int len;
                                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                                File file = new File(filePath, getNameFromUrl(resultBean.getObjs().get(position)));
                                FileOutputStream fos = new FileOutputStream(file);

                                InputStream inputStream = response.body().byteStream();
                                while ((len = inputStream.read(bytes)) != -1) {
                                    fos.write(bytes, 0, len);

                                }
                                fos.flush();
                                fos.close();
                                inputStream.close();
                                uiHandler.sendEmptyMessage(1);
                            }
                        });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        holder.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDownloadDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return resultBean != null ? resultBean.getPics().size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_download;
        TextView tv_name;
        TextView tv_category;
        ImageView iv_objImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_download = itemView.findViewById(R.id.tv_download);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_category = itemView.findViewById(R.id.tv_category);
            iv_objImage = itemView.findViewById(R.id.iv_objImage);
        }
    }

    private String getNameFromUrl(String url) {
        String[] strings = url.split("/");
        String name = strings[strings.length - 1];
        return name;
    }

    private String getCategoryFromUrl(String url) {
        String[] strings = url.split("/");
        String category = strings[4] + "/" + strings[5];
        return category;
    }
}
