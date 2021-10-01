package com.gnm.githubusersubmit2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnm.githubusersubmit2.adapter.AdapterFollow;
import com.gnm.githubusersubmit2.api.Api;
import com.gnm.githubusersubmit2.api.ApiInterface;
import com.gnm.githubusersubmit2.model.ModelFollower;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.kosong)
    RelativeLayout kosong;

    AlertDialog.Builder alertDialogBuilder;
    AdapterFollow adapterFollow;
    List<ModelFollower> listData;
    LinearLayoutManager mLayoutManager;

    ApiInterface Service;
    retrofit2.Call<List<ModelFollower>> Call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follower, container, false);

        ButterKnife.bind(this, view);
        initView();
        getData();
        return view;
    }

    void initView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterFollow = new AdapterFollow(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    void getData() {
        listData = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);

        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getFollowing(getActivity().getIntent().getStringExtra("user"), "2471d477fd5c6d0619548b344265c51ea11276e0 ");

        Call.enqueue(new Callback<List<ModelFollower>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ModelFollower>> call, Response<List<ModelFollower>> response) {
                listData.clear();
                try {
                    listData = response.body();
                    adapterFollow.setDetail(listData);
                    recyclerView.setAdapter(adapterFollow);
                    loading.setVisibility(View.GONE);
                    kosong.setVisibility(View.GONE);

                    if (listData.size() == 0) {
                        kosong.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ModelFollower>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                kosong.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                Log.e("cek Error", t.getMessage());
            }
        });
    }
}