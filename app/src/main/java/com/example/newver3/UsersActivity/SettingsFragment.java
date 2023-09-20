package com.example.newver3.UsersActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.newver3.Activity.TheLoaiFragment;
import com.example.newver3.MainActivity;
import com.example.newver3.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Switch SwThanhNien,SwTuoiTre,SwLaoDong,SwICTNews,SwTienPhong;
    Button btn_save,btncancel;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    boolean[] theloai;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view =inflater.inflate(R.layout.fragment_settings, container, false);
        SwICTNews=view.findViewById(R.id.switchictnews);
        SwLaoDong=view.findViewById(R.id.switchlaodong);
        SwThanhNien=view.findViewById(R.id.switchthanhnien);
        SwTienPhong=view.findViewById(R.id.switchtienphong);
        SwTuoiTre=view.findViewById(R.id.switchtuoitre);
        btn_save=view.findViewById(R.id.btn_saveSettings);
        
        reloadTheLoai();
        SwThanhNien.setOnCheckedChangeListener((buttonView, isChecked) -> theloai[1]=isChecked);
        SwLaoDong.setOnCheckedChangeListener((buttonView, isChecked) -> theloai[2]=isChecked);
        SwTienPhong.setOnCheckedChangeListener((buttonView, isChecked) -> theloai[3]=isChecked);
        SwICTNews.setOnCheckedChangeListener((buttonView, isChecked) -> theloai[4]=isChecked);
        SwTuoiTre.setOnCheckedChangeListener((buttonView, isChecked) -> theloai[5]=isChecked);

        btn_save.setOnClickListener(v -> {
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.lefttoright);
                btn_save.startAnimation(animation);
                for (int i = 0; i <6 ; i++) {
                    MainActivity.theloai[i]=theloai[i];
                }
                List<Boolean> tam= new ArrayList<>();
                for(boolean theloai:MainActivity.theloai)
                {
                    tam.add(theloai);
                }

                if(MainActivity.user!=null) {
                    MainActivity.user.theloai=tam;
                    myRef.child(MainActivity.user.username).setValue(MainActivity.user);
                }
                Toast.makeText(getActivity().getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();


        });

        return view;
    }
    public void reloadTheLoai()
    {
        theloai= new boolean[6];
        for (int i = 0; i <6 ; i++) {
            theloai[i]=MainActivity.theloai[i];
        }

        for(int i = 1; i< theloai.length; i++)
        {
            switch (i)
            {
                case 1:
                {
                    SwThanhNien.setChecked(theloai[i]);
                    break;
                }
                case 2:
                {
                    SwLaoDong.setChecked(theloai[i]);
                    break;
                }
                case 3:
                {
                    SwTienPhong.setChecked(theloai[i]);
                    break;
                }
                case 4:
                {
                    SwICTNews.setChecked(theloai[i]);
                    break;
                }
                case 5:
                {
                    SwTuoiTre.setChecked(theloai[i]);
                    break;
                }
            }
        }
    }
}