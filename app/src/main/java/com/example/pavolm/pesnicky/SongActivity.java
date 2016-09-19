package com.example.pavolm.pesnicky;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {
    private static final String ALL_NOTES = "CDEFGABcd ";

    private static final @ColorInt int ALL_COLORS[] = {
            Color.parseColor("#ffc8003c"),
            Color.parseColor("#ffff8000"),
            Color.parseColor("#ffffff00"),
            Color.parseColor("#ff30c000"),
            Color.parseColor("#ff0000ff"),
            Color.parseColor("#ff0090a0"),
            Color.parseColor("#ffa0e0e0"),
            Color.parseColor("#ffffffff"),
            Color.parseColor("#ffff00ff"),
            Color.parseColor("#00000000"),
    };

    private Song mSong;
    private GridView mNotesList;
    private int mNoteWidth;
    private int mNotePadding;
    private int mNoteHeight;

    private Runnable mReleaseLocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mReleaseLocks = new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        mSong = getIntent().getParcelableExtra("EXTRA_SONG");
        setContentView(R.layout.activity_song);

        mNotesList = (GridView) findViewById(R.id.notesList);
        mNotesList.setAdapter(new NotesListAdapter(this));

        mNotesList.postDelayed(mReleaseLocks, 10 * 60 * 1000);

        final View view = this.findViewById(android.R.id.content);

        mNoteWidth = getResources().getDimensionPixelSize(R.dimen.note_width);
        mNoteHeight = getResources().getDimensionPixelSize(R.dimen.note_height);
        mNotePadding = getResources().getDimensionPixelSize(R.dimen.note_padding);

        setTitle(mSong.name);

        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = view.getWidth();
                        int numColumns = width / mNoteWidth / 5 * 5;
                        mNotesList.setNumColumns(numColumns);
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReleaseLocks != null) {
            this.mNotesList.removeCallbacks(mReleaseLocks);
            mReleaseLocks = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // hack to make android save state
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class NotesListAdapter extends ArrayAdapter<Character> {
        NotesListAdapter(Context context) {
            super(context, 0, asList(mSong.notes));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int note = ALL_NOTES.indexOf(getItem(position));
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.song, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.note_name);
            textView.setText(Character.toString(getItem(position)));

            int padding = (int)(note/8.0f/3.0f*mNoteHeight/2f)+mNotePadding;
            convertView.setPadding(mNotePadding, padding, mNotePadding, padding);

            View noteRect = convertView.findViewById(R.id.note_rect);

            if (getItem(position) != ' ') {
                Shape shape = new RoundRectShape(new float[]{
                        mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f, mNoteWidth / 2f}, null, null);
                ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
                shapeDrawable.setColorFilter(ALL_COLORS[note], PorterDuff.Mode.SRC_ATOP);
                noteRect.setBackground(shapeDrawable);
            } else {
                noteRect.setBackgroundColor(Color.argb(0, 0, 0, 0));
            }

            return convertView;
        }
    }

    public static List<Character> asList(String s) {
        int i = 0;
        List<Character> list = new ArrayList<>(s.length());
        for(char c : s.toCharArray()) {
            if (i == 4) {
                list.add(' ');
                i = 0;
            }
            list.add(c);
            ++i;
        }
        return list;
    }
}