package com.example.template.ui.TestSqliteDbflow.notes.presenter;

import android.content.Context;
import android.util.Log;

import com.example.template.model.DataManager;
import com.example.template.model.bean.sqlite.Note;
import com.example.template.model.bean.sqlite.NoteType;
import com.example.template.model.db.SqliteCallBack;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Net22 on 9/18/2017.
 */

public class NotePresenter
        implements NoteContract.IPresenter, SqliteCallBack {

    NoteContract.IView mView;
    Context mContext;
    private final DataManager mDataManager;

    public NotePresenter(Context context, NoteContract.IView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext
        );
        mDataManager.setPresenterSqliteCallBack(this);

    }

    public void getNotes() {
        mDataManager.getDataHelper().getAll(new Note(), mContext, this);
    }

    public void deleteNote(Note note) {
        mDataManager.getDataHelper().deleteData(note, mContext, this);
    }

    public void addNote(String noteText) {

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setType(NoteType.TEXT);
        note.insertData(note, mContext, this);
        //or
         //mDataManager.addLocalNote(noteText);
        Log.e("dbflowExample Note", "Inserted new note, ID: " + note.getId());

      }




    @Override
    public void createView() {

    }

    @Override
    public void destroyView() {

    }


    @Override
    public void onDBDataListLoaded(List data, String methodName, String localDbOperation) {
        mView.showNotes(data);
    }

    @Override
    public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {

    }
}

