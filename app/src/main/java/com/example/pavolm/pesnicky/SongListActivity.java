package com.example.pavolm.pesnicky;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    List<Song> mSongs;
    ListView mSongListView;

    public SongListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongs = new ArrayList<>();
        mSongs.add(new Song("Kohútik", R.drawable.kohutik, "CDEFFFFEDEEEEDCDDDDEDCCC"));
        mSongs.add(new Song("Čierne oči", R.drawable.cierne_oci, "GDGABAGBABcdcBAGABcBAGDGABAG"));
        mSongs.add(new Song("Rudolf", R.drawable.rudolf, "GAGEcAGGAGAGcBFGFDBAGGAGAGdc"));
        mSongs.add(new Song("Ide ide vláčik", R.drawable.ide_ide_vlacik, "GGGAGEGFEDCGGGAGEGFEDCFDDDECCCFDDDECCCGGGAGEGFEDC"));
        mSongs.add(new Song("Ide vláčik ši-ši-ši", R.drawable.ide_vlacik_si_si_si, "GGGAGGEGGGAGGEGGGEGGGGEGGEEE"));
        mSongs.add(new Song("Itsy bitsy spider", R.drawable.itsy_bitsy_spider, "GGGABBBAGABGBBcddcBcdBGGABBAGABADDGGGABBBAGABG"));

        setContentView(R.layout.activity_song_list);

        mSongListView = (ListView)this.findViewById(R.id.songListView);
        mSongListView.setAdapter(new SongListAdapter(this));

        mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Song song = (Song)adapterView.getItemAtPosition(position);
                Intent intent = new Intent(SongListActivity.this, SongActivity.class);
                intent.putExtra("EXTRA_SONG", song);
                startActivity(intent);
            }
        });
    }

    public class SongListAdapter extends ArrayAdapter<Song> {
        public SongListAdapter(Context context) {
            super(context, 0, mSongs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Song song = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_preview, parent, false);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            titleView.setText(song.name);

            ImageView iconView = (ImageView) convertView.findViewById(R.id.icon);
            iconView.setImageResource(song.icon);

            return convertView;
        }
    }
}
