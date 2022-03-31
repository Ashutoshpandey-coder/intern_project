package com.example.intern_project.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.intern_project.R;
import com.example.intern_project.databinding.SingleCardBinding;
import com.example.intern_project.models.ApiModelClass;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    List<ApiModelClass> list;
    Context context;
    deleteItem deleteItem;

    public ListItemAdapter(List<ApiModelClass> list, Context context,deleteItem deleteItem) {
        this.list = list;
        this.context = context;
        this.deleteItem = deleteItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SingleCardBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ApiModelClass model = list.get(position);
        holder.binding.tvEmail.setText(model.getEmail());
        holder.binding.tvName.setText(model.getFirst_name() + " " + model.getLast_name());
        Glide.with(context).load(model.getAvatar()).centerCrop().placeholder(R.drawable.ic_user_place_holder).into(holder.binding.ivImage);
        holder.itemView.setOnLongClickListener(view -> {
            showConfirmationDialog(position);
            return true;
        });
    }
    private void showConfirmationDialog(int pos){
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteItem.onItemClick(pos))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public interface deleteItem{
        void onItemClick(int pos);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        SingleCardBinding binding;
        public ViewHolder(@NonNull SingleCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
