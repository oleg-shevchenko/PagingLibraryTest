package com.ozglob.paging_library_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class PagedListModelAdapter extends PagedListAdapter<Model, ModelVH> {
    private static final DiffUtil.ItemCallback<Model> DIFF_CALLBACK = new DiffUtil.ItemCallback<Model>() {
        //Text may have changed if reloaded from database/api, but ID is fixed.
        @Override
        public boolean areItemsTheSame(@NonNull Model oldItem, @NonNull Model newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Model oldItem, @NonNull Model newItem) {
            return oldItem.getText().equals(newItem.getText()); //oldItem.equals(newItem);
        }
    };

    public PagedListModelAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ModelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ModelVH.VIEW_RES, parent, false);
        return new ModelVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelVH holder, int position) {
        holder.bindModel(getItem(position));
    }
}
