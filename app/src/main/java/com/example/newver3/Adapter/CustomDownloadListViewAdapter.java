package com.example.newver3.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newver3.Database.BaiBao;
import com.example.newver3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomDownloadListViewAdapter extends BaseAdapter {

    Context context;
    List<BaiBao> listBaiBao;
    int layoutID;

    public CustomDownloadListViewAdapter(Context context, int layoutID, List<BaiBao> listBaiBao) {
        this.context = context;
        this.listBaiBao = listBaiBao;
    }

    @Override
    public int getCount() {
        return listBaiBao.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listBaiBao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView title, ngayDang;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaiBao baiBao = (BaiBao) getItem(position);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_downloadlistview, parent, false);
        ViewHolder holder;
        //Ánh xạ
        holder = new ViewHolder();
        holder.imgHinh = convertView.findViewById(R.id.iconNews);
        holder.title = convertView.findViewById(R.id.textTitle);
        holder.ngayDang = convertView.findViewById(R.id.id_date);
        //Set giá trị
        holder.title.setText(baiBao.getTitle());
        Picasso.with(convertView.getContext()).load(Uri.parse(baiBao.getImage())).into(holder.imgHinh);
        holder.ngayDang.setText(baiBao.getDatePost());
        convertView.setTag(holder);

        return convertView;
    }
}
