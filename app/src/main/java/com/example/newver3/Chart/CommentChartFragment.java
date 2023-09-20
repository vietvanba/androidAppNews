package com.example.newver3.Chart;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.newver3.Firebase.Comment;
import com.example.newver3.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PieChart pieChart;
    TextView dateFrom,dateTo;
    Button btn_thongKe;
    Date dateVNN = null;
    final Calendar c = Calendar.getInstance();
    SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("news/comment");

    ArrayList<PieEntry> entry = new ArrayList<>();

    public CommentChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentChartFragment newInstance(String param1, String param2) {
        CommentChartFragment fragment = new CommentChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_pie_char, container, false);

        pieChart=view.findViewById(R.id.pie_chart);
        dateFrom=view.findViewById(R.id.dateFrom);
        dateTo=view.findViewById(R.id.dateTo);
        btn_thongKe=view.findViewById(R.id.btn_thongKe);

        dateFrom.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + (c.get(Calendar.YEAR)-1));
        dateTo.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buttonSelectDate(dateFrom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buttonSelectDate(dateTo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_thongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateF,dateT;
                HashMap<String,Integer> thongkeComment = new HashMap<>();
                try {
                    SimpleDateFormat dateVN =new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                    dateF = formatter.parse(dateFrom.getText().toString());
                    dateT = formatter.parse(dateTo.getText().toString());
                    Query allComment = myRef;
                    allComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot news:snapshot.getChildren()) {


                                for(DataSnapshot listComment:news.child("listComment").getChildren())
                                {
                                    Comment comment= listComment.getValue(Comment.class);
                                    Date commentDate=null;
                                    SimpleDateFormat dateVN =new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                                    if(!thongkeComment.containsKey(comment.username))
                                    {
                                        thongkeComment.put(comment.username,0);
                                    }


                                    try {
                                        commentDate=dateVN.parse(comment.date);
                                        if(dateF.compareTo(commentDate)<=0&&(new Date(dateT.getTime()+(23*3599*1000))).compareTo(commentDate)>=0)
                                        {
                                            thongkeComment.put(comment.username,thongkeComment.get(comment.username)+1);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            Map<String,Integer> sort=thongkeComment.entrySet().stream()
                                    .sorted(Comparator.comparingInt(e -> -e.getValue()))
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (a, b) -> { throw new AssertionError(); },
                                            LinkedHashMap::new
                                    ));
                            entry.clear();
                            int tam=0;
                            for (Map.Entry<String,Integer> user: sort.entrySet()
                            ) {
                                if(tam<10)
                                {
                                    entry.add(new PieEntry(user.getValue(),user.getKey()));
                                    tam++;
                                }else
                                {
                                    break;
                                }


                            }
                            PieDataSet pieDataSet = new PieDataSet(entry,"User");
                            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            pieDataSet.setValueTextColor(Color.BLACK);
                            pieDataSet.setValueTextSize(16f);


                            PieData pieData= new PieData(pieDataSet);
                            pieDataSet.setValueFormatter(new DefaultValueFormatter(0));
                            pieChart.setData(pieData);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setCenterText("Người Comment nhiều nhất");
                            pieChart.animate();
                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });



        return view;
    }

    private void buttonSelectDate(TextView date) throws ParseException {



        dateVNN =formatter.parse(date.getText().toString());
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog ;

        datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, dateVNN.getYear()+1900, dateVNN.getMonth(), dateVNN.getDate());
        datePickerDialog.show();

    }

}