package com.gnm.githubusersubmit2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.gnm.githubusersubmit2.api.Api;
import com.gnm.githubusersubmit2.api.ApiInterface;
import com.gnm.githubusersubmit2.model.ModelSearch;
import com.gnm.githubusersubmit2.model.ModelUser;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imgFoto)
    CircleImageView imgFoto;

    @BindView(R.id.txtNama)
    TextView txtNama;

    @BindView(R.id.txtUsername)
    TextView txtUsername;

    @BindView(R.id.txtUrl)
    TextView txtUrl;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

//    @BindView(R.id.loading)
//    RelativeLayout loading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.scroll)
//    NestedScrollView scroll;

    private TabAdapter adapter;
    ApiInterface Service;
    retrofit2.Call<ModelUser> Call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FollowerFragment(), "Follower");
        adapter.addFragment(new FollowingFragment(), "Following");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        getData();
//        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (v.getChildAt(v.getChildCount() - 1) != null) {
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                            scrollY > oldScrollY) {
//
//                    }
//                }
//            }
//        });
//
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
//            CoordinatorLayout.LayoutParams lpm = new CoordinatorLayout.LayoutParams(
//                    CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
//            lpm.setMargins(0,0,0,0);
//            lpm.setBehavior(new AppBarLayout.ScrollingViewBehavior());
//            scroll.setLayoutParams(lpm);
//            scroll.requestLayout();
//        }
    }


    void getData() {
        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getUser(getIntent().getStringExtra("user"), "2471d477fd5c6d0619548b344265c51ea11276e0 ");

        Call.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(retrofit2.Call<ModelUser> call, Response<ModelUser> response) {
                try {
                    ModelUser user=new ModelUser();
                    user=response.body();

                    txtNama.setText(user.getName());
                    txtUsername.setText(user.getLogin());
                    txtUrl.setText(user.getHtmlUrl());
                    Glide
                            .with(DetailActivity.this)
                            .load(user.getAvatarUrl())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(imgFoto);
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "" +"Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelUser> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                Log.e("cek Error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
