package com.gnm.githubusersubmit2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gnm.githubusersubmit2.DetailActivity;
import com.gnm.githubusersubmit2.R;
import com.gnm.githubusersubmit2.model.ModelSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ItemDetailHolder> {

    private List<ModelSearch.ItemsEntity> detailList = new ArrayList<>();
    private Context context;

    public AdapterUser(Context context) {
        this.context = context;
    }

    @Override
    public ItemDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemDetailHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false)
        );
    }

    public void setDetail(List<ModelSearch.ItemsEntity> detail) {
        this.detailList = detail;
    }

    public List<ModelSearch.ItemsEntity> getList() {
        return detailList;
    }

    @Override
    public void onBindViewHolder(final ItemDetailHolder holder, int position) {
        holder.bindView(detailList.get(position));
    }

    @Override
    public int getItemCount() {
        int n;
        try {
            n = detailList.size();
        } catch (Exception e) {
            n = 0;
        }
        return n;
    }

    onItemClickListner onItemClickListner;

    public void setOnItemClickListner(AdapterUser.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner {
        void onClick(int logo, String namaBank);//pass your object types.
    }

    class ItemDetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama)
        TextView txtNama;

        @BindView(R.id.txtUsername)
        TextView txtUsername;

        @BindView(R.id.imgFoto)
        ImageView imgFoto;

        int logo;

        ItemDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final ModelSearch.ItemsEntity detail) {
        txtNama.setText(detail.getLogin());
            txtUsername.setText(detail.getHtmlUrl());
            Glide
                    .with(context)
                    .load(detail.getAvatarUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgFoto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("user", detail.getLogin());
                    context.startActivity(intent);
                }
            });
        }
    }
}
