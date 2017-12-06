package com.example.template.ui.TestSqliteDbflow.notes.presenter;

import com.example.template.model.bean.sqlite.Note;

import java.util.List;

import modules.basemvp.Base;

/**
 * Created by Net22 on 9/18/2017.
 */

public interface NoteContract {

    interface IView {

        void showNotes(List<Note> notes);

    }

    interface IPresenter extends Base.IPresenter {


    }

}
