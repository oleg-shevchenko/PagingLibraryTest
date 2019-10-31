package com.ozglob.paging_library_test;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModelVH extends RecyclerView.ViewHolder {
    public static final int VIEW_RES = android.R.layout.simple_list_item_1;
    private TextView textView;

    public ModelVH(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView;
    }

    public void bindModel(Model model) {
        // Null defines a placeholder item - PagedListAdapter automatically
        // invalidates this row when the actual object is loaded from the
        // database.
        if(model == null)
            textView.setText("Loading...");
        else
            textView.setText(model.getText());
    }
}
