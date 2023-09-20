package com.example.newver3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newver3.Activity.TheLoaiFragment;
import com.example.newver3.Chart.ThongKe_Fragment;
import com.example.newver3.Downloads.DownloadsFragment;
import com.example.newver3.Firebase.Users;
import com.example.newver3.Others.ConnectTheInternet;
import com.example.newver3.UsersActivity.InfoUserActivity;
import com.example.newver3.UsersActivity.LoginActivity;
import com.example.newver3.UsersActivity.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String[] moi = {"Mới","https://thanhnien.vn/rss/home.rss","https://nld.com.vn/tin-moi-nhat.rss","https://www.tienphong.vn/rss/home.rss","https://ictnews.vietnamnet.vn/rss/tin-nong-ict.rss","https://tuoitre.vn/rss/tin-moi-nhat.rss"};
    String[] thoisu = {"Thời sự","https://thanhnien.vn/rss/thoi-su.rss","https://nld.com.vn/thoi-su.rss","https://tienphong.vn/rss/xa-hoi-tin-tuc-104.rss","https://ictnews.vietnamnet.vn/rss/tin-nong-ict.rss","https://tuoitre.vn/rss/thoi-su.rss"};
    String[] theThao = {"Thể thao","https://thethao.thanhnien.vn/rss/home.rss","https://nld.com.vn/the-thao.rss","https://tienphong.vn/rss/the-thao-11.rss","https://ictnews.vietnamnet.vn/rss/game.rss","https://tuoitre.vn/rss/the-thao.rss"};
    String[] kinhTe = {"Kinh tế","https://thanhnien.vn/rss/tai-chinh-kinh-doanh.rss","https://nld.com.vn/kinh-te.rss","https://tienphong.vn/rss/kinh-te-3.rss","https://ictnews.vietnamnet.vn/rss/khoi-nghiep.rss","https://tuoitre.vn/rss/kinh-doanh.rss"};
    String[] docla = {"Độc & lạ","https://thanhnien.vn/rss/du-lich/kham-pha.rss","https://nld.com.vn/tin-doc-quyen.rss","https://tienphong.vn/rss/chuyen-la-32.rss","https://ictnews.vietnamnet.vn/rss/kham-pha.rss","https://tuoitre.vn/rss/du-lich.rss"};
    String[] giaitri = {"Giải trí","https://thanhnien.vn/rss/giai-tri.rss","https://nld.com.vn/giai-tri.rss","https://tienphong.vn/rss/giai-tri-36.rss","https://ictnews.vietnamnet.vn/rss/multimedia.rss","https://tuoitre.vn/rss/giai-tri.rss"};
    String[] cuocSong = {"Cuộc sống","https://thanhnien.vn/rss/ttt.rss","https://nld.com.vn/ly-tuong-song.rss","https://tienphong.vn/rss/gioi-tre-nhip-song-27.rss","https://ictnews.vietnamnet.vn/rss/cuoc-song-so.rss","https://tuoitre.vn/rss/nhip-song-tre.rss"};
    public static int[] img_nguon={R.drawable.others,R.drawable.thanhnien,R.drawable.laodong,R.drawable.tienphong,R.drawable.ictnews,R.drawable.tuoitre};
    public static boolean[] theloai ={true,true,true,true,true,true};
    public static Users user=null;
    public static ImageView Im_user;
    public static RoundedImageView avatarHeader;
    public static TextView textlogin;
    Toolbar toolbar;
    View headerView;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        Im_user=findViewById(R.id.Im_user);
        Im_user.setColorFilter(getResources().getColor(R.color.white));
        navigationView =findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        textlogin = (TextView) headerView.findViewById(R.id.textLogin);
        avatarHeader=headerView.findViewById(R.id.imageProfile);

        toolbar.setTitle("");
        toolbar.setBackground(getResources().getDrawable(R.drawable.grad));

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null)
                {
                    textlogin.setText(user.name);
                    Picasso.with(getApplicationContext()).load(Uri.parse(user.avatar)).into(avatarHeader);
                }else
                {
                    textlogin.setText("Đăng nhập");
                }
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if(savedInstanceState==null&&isOnline())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(moi)).commit();
            navigationView.setCheckedItem(R.id.nav_Moi);
        }else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ConnectTheInternet()).commit();
            navigationView.setCheckedItem(R.id.nav_Moi);
        }
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user==null)
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Intent intentInfo = new Intent(MainActivity.this, InfoUserActivity.class);
                    startActivity(intentInfo);
                }
            }
        });
        Im_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user==null)
                {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);



                }
                else
                {
                    Intent intentInfo = new Intent(MainActivity.this,InfoUserActivity.class);
                    startActivity(intentInfo);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Nhấn thêm lần nữa để thoát", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(isOnline())
        {


        switch (item.getItemId())
        {
            case R.id.nav_Moi:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(moi)).commit();
                navigationView.setCheckedItem(R.id.nav_Moi);

                break;
            }
            case R.id.nav_ThoiSu:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(thoisu)).commit();
                navigationView.setCheckedItem(R.id.nav_ThoiSu);
                break;
            }
            case R.id.nav_TheThao:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(theThao)).commit();
                navigationView.setCheckedItem(R.id.nav_TheThao);
                break;
            }
            case R.id.nav_KinhTe:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(kinhTe)).commit();
                navigationView.setCheckedItem(R.id.nav_KinhTe);
                break;
            }
            case R.id.nav_DocLa:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(docla)).commit();
                navigationView.setCheckedItem(R.id.nav_DocLa);
                break;
            }
            case R.id.nav_GiaiTri:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(giaitri)).commit();
                navigationView.setCheckedItem(R.id.nav_GiaiTri);
                break;
            }
            case R.id.nav_CuocSong:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TheLoaiFragment(cuocSong)).commit();
                navigationView.setCheckedItem(R.id.nav_CuocSong);
                break;
            }
            case R.id.nav_info:
            {
                navigationView.setCheckedItem(R.id.nav_info);
                Toast.makeText(getApplicationContext(),"Nhóm 16",Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.nav_setting:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_setting);
                break;
            }
            case R.id.nav_downloads:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DownloadsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_downloads);
                break;
            }
            case R.id.nav_thongKe:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ThongKe_Fragment()).commit();
                navigationView.setCheckedItem(R.id.nav_thongKe);
                break;
            }
        }
        }else
        {
            if(item.getItemId()==R.id.nav_downloads)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DownloadsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_downloads);

            }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ConnectTheInternet()).commit();
                navigationView.setCheckedItem(R.id.nav_downloads);
            }

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean isOnline() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
