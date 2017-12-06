package com.example.template.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.template.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.example.template.model.db.Constants.DatabaseName;

/**
 * Created by Net22 on 11/28/2017.
 */

public class FileUtils {

    public static void importNewSqliteFile(Context context, String downloadedSqlitePath) {
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {

                downloadedSqlitePath = context.getString(R.string.sqlite_folder_app_name_english)
                        + "/" +
                        DatabaseName
                        + ".db";

                String currentDBPath = downloadedSqlitePath; //SOURCE
                String backupDBPath = "data/" + context.getApplicationContext().getPackageName() + "/databases/"
                        + DatabaseName + ".db";  //Destination

                File backupDB = new File(sd, currentDBPath);  //SOURCE
                File currentDB = new File(data, backupDBPath);  //Destination

                FileChannel src = new FileInputStream(backupDB).getChannel();  //SOURCE
                FileChannel dst = new FileOutputStream(currentDB).getChannel(); //Destination
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

//                FlowManager.init(FlowConfig.builder(context.getApplicationContext())
//                        .openDatabasesOnInit(true)
//                        .build());
                Log.e("importNewSqltestsuccess", "importNewSqltestsuccess");

            }
        } catch (Exception e) {
            Log.e("importNewSqtesterror", e.toString());
        }
    }


//    public boolean importDatabase(Context context , String dbPath) throws IOException {
//
//        // Close the SQLiteOpenHelper so it will commit the created empty
//        // database to internal storage.
//        close();
//        File newDb = new File(dbPath);
//        File oldDb = new File(DB_FILEPATH);
//        if (newDb.exists()) {
//            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
//            // Access the copied database so SQLiteHelper will cache it and mark
//            // it as created.
//            getWritableDatabase().close();
//            return true;
//        }
//        return false;
//    }

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile - FileInputStream for the file to copy from.
     * @param toFile   - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}


