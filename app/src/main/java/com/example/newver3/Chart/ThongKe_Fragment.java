package com.example.newver3.Chart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newver3.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongKe_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongKe_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayOutAdapter tabLayOutAdapter;
    public ThongKe_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThongKe_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThongKe_Fragment newInstance(String param1, String param2) {
        ThongKe_Fragment fragment = new ThongKe_Fragment();
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

        View view=inflater.inflate(R.layout.fragment_thong_ke_, container, false);

        tabLayout=view.findViewById(R.id.ThongKetabLayout);
        viewPager=view.findViewById(R.id.Thongke_viewpage);

        tabLayOutAdapter= new TabLayOutAdapter(getChildFragmentManager());
        tabLayOutAdapter.AddFragment(new LikeChartFragment(),"Người Like nhiều nhất");
        tabLayOutAdapter.AddFragment(new CommentChartFragment(),"Người Comment nhiều nhất");

        viewPager.setAdapter(tabLayOutAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private class TabLayOutAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragmentArrayList =new ArrayList<>();
        ArrayList<String> stringArrayList=new ArrayList<>();


        public void AddFragment(Fragment fragment,String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public TabLayOutAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }

    }

}