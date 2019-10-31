package com.ozglob.paging_library_test;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;

public class MyDataSource extends PositionalDataSource<Model> {
    private List<Model> sourceItems;    //this should be database or remote
    private long loadingDelay;
    public MyDataSource(int itemsSize, long loadingDelay) {
        this.loadingDelay = loadingDelay;
        sourceItems = new ArrayList<>(itemsSize);
        for(int i = 0; i < itemsSize; i++)
            sourceItems.add(new Model(i, "Item: " + i));
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Model> callback) {
        Log.d("MyDataSource", "loadInitial():"
                + "  startPosition=" + params.requestedStartPosition
                + ", loadSize=" + params.requestedLoadSize
                + ", pageSize=" + params.pageSize
                + ", placeholders=" + params.placeholdersEnabled);
        try {
            Thread.sleep(loadingDelay);
        } catch (InterruptedException e) {/**/}

        int startIndex = params.requestedStartPosition;
        int endIndex = params.requestedStartPosition + params.requestedLoadSize;
        int maxIndex = Math.min(endIndex, sourceItems.size());
        //This should be database or api request
        List<Model> result = sourceItems.subList(startIndex, maxIndex);
        callback.onResult(result, startIndex, sourceItems.size());
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Model> callback) {
        Log.d("MyDataSource", "loadRange():"
                + "  start=" + params.startPosition
                + ", loadSize=" + params.loadSize);
        try {
            Thread.sleep(loadingDelay);
        } catch (InterruptedException e) {/**/}

        int startIndex = params.startPosition;
        int endIndex = startIndex + params.loadSize;
        int maxIndex = Math.min(endIndex, sourceItems.size());
        //This should be database or api request
        List<Model> result = sourceItems.subList(startIndex, maxIndex);
        callback.onResult(result);
    }
}
