package com.example.gamebacklog.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gamebacklog.R;
import com.example.gamebacklog.database.GameRoomDatabase;
import com.example.gamebacklog.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<Game> mGames;
    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainViewModel mainViewModel;
    private GestureDetector mGestureDetector;
    private GameRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int mModifyPosition;

    public static final int REQUESTCODEADD = 1234;
    public static final int REQUESTCODEEDIT = 4321;
    public static final String EDIT = "editoradd";
    public static final String EXTRA_GAME = "extragame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = GameRoomDatabase.getDatabase(this);
        mGames = new ArrayList<>();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGames = games;
                updateUI();
            }
        });

        initToolbar();
        initFloatingButton();
        initRecyclerView();

        updateUI();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, editGameActivity.class);
                intent.putExtra(EDIT, false);
                startActivityForResult(intent, REQUESTCODEADD);
            }
        });
    }
    private void initRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                /*Snackbar.make(findViewById(R.id.parent), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(this);

    }

    public void updateUI() {
        if( mAdapter == null ) {
            mAdapter = new GameAdapter(mGames);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGames);
        }
    }

    private void deleteAllGames(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mainViewModel.deleteAll(games);
                //getAllGames();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete_item) {
            deleteAllGames(mGames);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null) {
            int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
            if(mGestureDetector.onTouchEvent(motionEvent)){
                Intent intent = new Intent(MainActivity.this, editGameActivity.class);
                intent.putExtra(EDIT, true);
                mModifyPosition = mAdapterPosition;
                intent.putExtra(EXTRA_GAME, mGames.get(mAdapterPosition));
                Log.d("activityStart", mGames.get(mAdapterPosition).getGameName());
                startActivityForResult(intent, REQUESTCODEEDIT);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
