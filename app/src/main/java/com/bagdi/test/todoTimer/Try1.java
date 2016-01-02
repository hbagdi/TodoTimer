package com.bagdi.test.todoTimer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.bagdi.test.todoTimer.db.TaskContract;
import com.bagdi.test.todoTimer.db.TaskDBHelper;
import com.bagdi.test.todoTimer.db.TaskDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Try1 extends ListActivity {
    private TaskDBHelper helper;
    private ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Try1.this, AddTask.class);
                startActivity(intent);
            }
        });

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "Onresume for try1 called");
        updateUI();
    }

    private void updateUI() {
        Log.d("MainActivity", "update UI Called");
        helper = new TaskDBHelper(Try1.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        TaskDatabase taskDatabase = new TaskDatabase(this);
        Cursor cursor2 = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK, TaskContract.Columns.DATETIME},
                null, null, null, null, "date(" + TaskContract.Columns.DATETIME + ")");

        Calendar c = Calendar.getInstance();
        cursor2.moveToFirst();
        Date d = new Date();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = null;
        while (cursor2.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor2.getString(
                            cursor2.getColumnIndexOrThrow(
                                    TaskContract.Columns.TASK)));
            timeString = cursor2.getString(
                    cursor2.getColumnIndexOrThrow(
                            TaskContract.Columns.DATETIME));
            if (timeString == null) {
                Log.d("MainActivity cursor", "Time String is  null");
            } else {
                Log.d("MainActivity cursor", timeString);
            }
            ;

        }
        Cursor cursor = null;
        Task[] tasks = null;
        try {
            taskDatabase.open();
            cursor = taskDatabase.getAll();

            tasks = taskDatabase.getAllTaskObjects();
            taskDatabase.close();
        } catch (ParseException p) {
            Log.d("Main", "Parse Exception");
        }
        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.listview_item,
                cursor,
                new String[]{TaskContract.Columns.TASK, TaskContract.Columns.DATETIME},
                new int[]{R.id.taskTextView, R.id.remainingTime},
                0
        );

//         this.setListAdapter(listAdapter);
        final CountdownAdapter countdownAdapter = new CountdownAdapter(getApplicationContext(), new LinkedList<Task>(Arrays.asList(tasks)),this);
        final ArrayList<Task> TaskList = new ArrayList<Task>(Arrays.asList(tasks));

        this.setListAdapter(countdownAdapter);
    }


//    public void onDoneButtonClick(View view) {
//        View v = (View) view.getParent();
//        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
//        String taskDescription = taskTextView.getText().toString();
//        TaskDatabase taskDatabase = new TaskDatabase(this);
//        taskDatabase.open();
//        Task task=null;
//        try {
//            task = taskDatabase.searchByString(taskDescription);
//        } catch (ParseException p) {
//            Log.d("Main", "Parse Exception");
//        }
//        taskDatabase.delete(taskDescription);
//        taskDatabase.close();
//        deleteNotification(task.getId());
//        updateUI();
//    }
//
//    private void deleteNotification(int id) {
//        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent.cancel();
//    }

}