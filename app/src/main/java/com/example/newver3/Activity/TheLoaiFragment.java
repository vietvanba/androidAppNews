package com.example.newver3.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.newver3.Adapter.CustomListViewAdapter;
import com.example.newver3.Database.Item;
import com.example.newver3.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TheLoaiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    //Khởi tạo biến cho Fragment
    ListView listView;
    CustomListViewAdapter customListViewAdapter;
    List<Item> listItem = new ArrayList<>();
    private String[] theloai;
    private TextView textTheloai;


    public TheLoaiFragment() {
        // Required empty public constructor
    }

    public class ReadRSS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                Item item = new Item();
                item.setTitle(parser.getValue(element, "title"));
                String[] listTag = {"image", "description", "content:encoded", "content"};

                for (String tag : listTag) {
                    if (!parser.getValue(element, tag).equals("")) {
                        String img = parser.getValue(element, tag);
                        String[] list = img.split("\"");
                        for (String a : list) {
                            String tam;
                            if (a.length() < 4) {
                                tam = "";
                            } else {
                                tam = a.substring(a.length() - 4, a.length()).toLowerCase();
                            }
                            if (tam.equals(".jpg") || tam.equals(".png") || tam.equals(".gif") || tam.equals("jpeg") || tam.equals("tiff") || tam.equals(".bmp")) {
                                item.setImageUrl(a);
                                break;
                            } else {
                                item.setImageUrl("");
                            }
                        }
                        if (!item.getImageUrl().equals("")) {
                            break;
                        }
                    }
                }
                item.setUrl(parser.getValue(element, "link"));
                switch (item.getUrl().substring(8, 17)) {
                    case "thanhnien": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[1]);
                        break;
                    }
                    case "thethao.t": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[1]);
                        break;
                    }
                    case "nld.com.v": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[2]);
                        break;
                    }
                    case "tienphong": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[3]);
                        break;
                    }
                    case "ictnews.v": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[4]);
                        break;
                    }
                    case "tuoitre.v": {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[5]);
                        break;
                    }
                    default: {
                        item.setNguon(com.example.newver3.MainActivity.img_nguon[0]);
                        break;
                    }
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH);
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                SimpleDateFormat formatter2 = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", java.util.Locale.ENGLISH);
                formatter2.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                formatter2.format(new Date().getTime());
                SimpleDateFormat formatter3 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.ENGLISH);
                formatter3.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                SimpleDateFormat formatter4 = new SimpleDateFormat("M/dd/yyyy HH:mm:ss", java.util.Locale.ENGLISH);
                formatter4.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                Date date = null;
                try {
                    date = formatter.parse(parser.getValue(element, "pubDate"));
                } catch (ParseException e) {
                    try {
                        date = formatter2.parse(parser.getValue(element, "pubDate"));
                    } catch (ParseException parseException) {
                        try {
                            String tam = parser.getValue(element, "pubDate");
                            date = formatter3.parse(tam.substring(0, tam.length() - 1) + "07:00");
                        } catch (ParseException exception) {
                            try {
                                date = formatter4.parse(parser.getValue(element, "pubDate"));
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                SimpleDateFormat dateVN = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                dateVN.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                item.setDate(dateVN.format(date));
                listItem.add(item);
            }
            Collections.sort(listItem, new Comparator<Item>() {
                public int compare(Item o1, Item o2) {
                    try {
                        return (new SimpleDateFormat("dd-M-yyyy HH:mm:ss")).parse(o2.getDate()).compareTo((new SimpleDateFormat("dd-M-yyyy HH:mm:ss")).parse(o1.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });

            customListViewAdapter.notifyDataSetChanged();
        }

        private String docNoiDung_Tu_URL(String theUrl) {
            StringBuilder content = new StringBuilder();
            try {
                // create a url object
                URL url = new URL(theUrl);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        }
    }

    public TheLoaiFragment(String[] theloai) {
        this.theloai = theloai;
    }

    public static TheLoaiFragment newInstance(String param1, String param2) {
        TheLoaiFragment fragment = new TheLoaiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    ////////////////////START///////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_the_loai, container, false);

        listView = (ListView) view.findViewById(R.id.theLoaiListView);// Ánh xạ listview
        //Khởi tạo CustomListViewAdapter //
        customListViewAdapter = new CustomListViewAdapter(getActivity(), R.layout.activity_listview, listItem, theloai[0]);
        listView.setAdapter(customListViewAdapter);
        for (int i = 1; i < theloai.length; i++) {
            if (com.example.newver3.MainActivity.theloai[i])
                new ReadRSS().execute(theloai[i]);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) customListViewAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ReaderActivity.class);
                intent.putExtra("linkBaiBao", item);
                startActivity(intent);
            }

        });

        return view;
    }
}