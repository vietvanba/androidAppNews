package com.example.newver3.Downloads;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newver3.Adapter.CustomDownloadListViewAdapter;
import com.example.newver3.Database.AppDatabase;
import com.example.newver3.Database.BaiBao;
import com.example.newver3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppDatabase db;
    ListView listView;
    CustomDownloadListViewAdapter customDownloadListViewAdapter;
    public DownloadsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadsFragment newInstance(String param1, String param2) {
        DownloadsFragment fragment = new DownloadsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_downloads, container, false);
        db= AppDatabase.getInMemoryDatabase(getActivity().getApplicationContext());
        listView = (ListView) view.findViewById(R.id.downloadListView);
        customDownloadListViewAdapter= new CustomDownloadListViewAdapter(getActivity(),R.layout.activity_downloadlistview,db.baiBaoDao().getAllBaiBao());
        listView.setAdapter(customDownloadListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaiBao baiBao = (BaiBao) customDownloadListViewAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ReaderActivityOffline.class);
                intent.putExtra("BaiBao", baiBao);
                startActivity(intent);

            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(customDownloadListViewAdapter.getCount()>db.baiBaoDao().getAllBaiBao().size())
        {
            customDownloadListViewAdapter= new CustomDownloadListViewAdapter(getActivity(),R.layout.activity_downloadlistview,db.baiBaoDao().getAllBaiBao());
            listView.setAdapter(customDownloadListViewAdapter);
        }
    }
}