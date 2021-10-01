package com.gnm.githubusersubmit2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gnm.githubusersubmit2.adapter.AdapterUser;
import com.gnm.githubusersubmit2.api.Api;
import com.gnm.githubusersubmit2.api.ApiInterface;
import com.gnm.githubusersubmit2.model.ModelSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtCari)
    EditText txtCari;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.loading)
    RelativeLayout loading;

    @BindView(R.id.kosong)
    RelativeLayout kosongData;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    AlertDialog.Builder alertDialogBuilder;
    AdapterUser adapterUser;
    List<ModelSearch.ItemsEntity> listData;
    LinearLayoutManager mLayoutManager;

    ApiInterface Service;
    retrofit2.Call<ModelSearch> Call;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        alertDialogBuilder = new AlertDialog.Builder(this);

        initView();
        getData("a");
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(txtCari.getText().toString());
                hideKeyboard(MainActivity.this);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void initView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterUser = new AdapterUser(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    void getData(String kata) {
        listData = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);

        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getSearch(kata, "2471d477fd5c6d0619548b344265c51ea11276e0 ");

        Call.enqueue(new Callback<ModelSearch>() {
            @Override
            public void onResponse(retrofit2.Call<ModelSearch> call, Response<ModelSearch> response) {
                listData.clear();
                try {
                    listData = response.body().getItems();

                    adapterUser.setDetail(listData);
                    recyclerView.setAdapter(adapterUser);
                    loading.setVisibility(View.GONE);
                    kosongData.setVisibility(View.GONE);

                    if (listData.size() == 0) {
                        kosongData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "" +"Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelSearch> call, Throwable t) {
                loading.setVisibility(View.GONE);
                kosongData.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                Log.e("cek Error", t.getMessage());
            }
        });
    }
}