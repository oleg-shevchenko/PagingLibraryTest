package com.ozglob.paging_library_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private int initialPosition = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        PagedListModelAdapter adapter = initAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(initialPosition);
    }

    private PagedListModelAdapter initAdapter() {
        SimpleDataSource dataSource = new SimpleDataSource(124, 500);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(10)
                .setPrefetchDistance(5)     //optional (default = pageSize)
                .setInitialLoadSizeHint(20) //optional (default = pageSize*3)
                .build();

        PagedList<Model> pagedList = new PagedList.Builder<>(dataSource, config)
                .setNotifyExecutor(new MainThreadExecutor())
                .setFetchExecutor(Executors.newFixedThreadPool(2))
                .setBoundaryCallback(new MyBoundaryCallback())  //optional
                .setInitialKey(initialPosition)                 //optional
                .build();

        PagedListModelAdapter adapter = new PagedListModelAdapter();
        adapter.submitList(pagedList);
        return adapter;
    }

    private class MyBoundaryCallback extends PagedList.BoundaryCallback<Model> {
        @Override
        public void onZeroItemsLoaded() {
            Toast.makeText(MainActivity.this, "onZeroItemsLoaded()",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull Model itemAtFront) {
            Toast.makeText(MainActivity.this, "onItemAtFrontLoaded(): " + itemAtFront.getText(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemAtEndLoaded(@NonNull Model itemAtEnd) {
            Toast.makeText(MainActivity.this, "onItemAtEndLoaded(): " + itemAtEnd.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    //Main thread executor for notify loaded data
    private class MainThreadExecutor implements Executor {
        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }
}
