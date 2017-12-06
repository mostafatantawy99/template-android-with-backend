package com.example.template.model.db;

import java.util.List;

/**
 * Created by Net22 on 11/15/2017.
 */

public interface SqliteCallBack<T> {

    void onDBDataListLoaded(List data, String methodName, String localDbOperation);

    void onDBDataObjectLoaded(T data, String methodName, String localDbOperation);
}
