package com.ozglob.paging_library_test;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;

public class SimpleDataSource extends PositionalDataSource<Model> {
    private List<Model> itemsStorage;    //this should be database or remote
    private long loadingDelay;

    public SimpleDataSource(int itemsSize, long loadingDelay) {
        this.loadingDelay = loadingDelay;
        itemsStorage = new ArrayList<>(itemsSize);
        for(int i = 0; i < itemsSize; i++)
            itemsStorage.add(new Model(i, "Item: " + i));
    }

    //Будет вызвано в том потоке, в котором вызван метод build() для PagedList.Builder
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Model> callback) {
        Log.d("SimpleDataSource", "loadInitial():"
                + "  startPosition=" + params.requestedStartPosition
                + ", loadSize=" + params.requestedLoadSize
                + ", pageSize=" + params.pageSize
                + ", placeholders=" + params.placeholdersEnabled);
        try {
            Thread.sleep(loadingDelay);
        } catch (InterruptedException e) {/**/}

        int startIndex = params.requestedStartPosition;
        int endIndex = params.requestedStartPosition + params.requestedLoadSize;
        int maxIndex = Math.min(endIndex, itemsStorage.size());

        if(startIndex > maxIndex) {
            startIndex = itemsStorage.size() - params.requestedLoadSize;
            if(startIndex < 0)
                startIndex = 0;
        }
        //This should be database or api request
        List<Model> result = itemsStorage.subList(startIndex, maxIndex);
        if(params.placeholdersEnabled)
            callback.onResult(result, startIndex, itemsStorage.size());
        else
            callback.onResult(result, startIndex);
    }

    //Будет вызываться в том потоке, который указан в setFetchExecutor() для PagedList.Builder
    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Model> callback) {
        Log.d("SimpleDataSource", "loadRange():"
                + "  start=" + params.startPosition
                + ", loadSize=" + params.loadSize);
        try {
            Thread.sleep(loadingDelay);
        } catch (InterruptedException e) {/**/}

        int startIndex = params.startPosition;
        int endIndex = startIndex + params.loadSize;
        int maxIndex = Math.min(endIndex, itemsStorage.size());
        //This should be database or api request
        List<Model> result = itemsStorage.subList(startIndex, maxIndex);
        callback.onResult(result);
    }
}
