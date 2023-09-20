package com.example.newver3.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newver3.Database.Item;
import com.example.newver3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    Context context;
    List<Item> listItem;
    String category;
    int layoutID;

    public CustomListViewAdapter(Context context, int layoutID, List<Item> listItem, String category) {
        this.context = context;
        this.listItem = listItem;
        this.category = category;

    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class ViewHolder {
        ImageView imgHinh, imgNguonBao;
        TextView title;
        TextView theLoai, ngayDang;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Item item = (Item) getItem(position);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false);
        holder = new ViewHolder();
        holder.imgHinh = convertView.findViewById(R.id.iconNews);
        holder.title = convertView.findViewById(R.id.textTitle);
        holder.ngayDang = convertView.findViewById(R.id.id_date);
        holder.theLoai = convertView.findViewById(R.id.id_theloai);
        holder.imgNguonBao = convertView.findViewById(R.id.img_nguon);

        holder.title.setText(item.getTitle());
        Picasso.with(convertView.getContext()).load(Uri.parse(item.getImageUrl())).into(holder.imgHinh);
        holder.theLoai.setText(category);
        holder.ngayDang.setText(item.getDate());
        holder.imgNguonBao.setImageResource(item.getNguon());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        convertView.startAnimation(animation);
        convertView.setTag(holder);

        return convertView;
    }
}
