package com.example.pavolm.pesnicky;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    List<Song> mSongs;
    ListView mSongListView;
    BaseAdapter mSongListAdapter;

    RequestQueue mRequestQueue;

    public SongListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRequestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        mSongs = new ArrayList<>();
        mSongs.add(new Song("Kohútik", R.drawable.kohutik, "CDEFFFFEDEEEEDCDDDDEDCCC"));
        mSongs.add(new Song("Čierne oči", R.drawable.cierne_oci, "GDGABAGBABcdcBAGABcBAGDGABAG"));
        mSongs.add(new Song("Rudolf", R.drawable.rudolf, "GAGEcAGGAGAGcBFGFDBAGGAGAGdc"));
        mSongs.add(new Song("Ide ide vláčik", R.drawable.ide_ide_vlacik, "GGGAGEGFEDCGGGAGEGFEDCFDDDECCCFDDDECCCGGGAGEGFEDC"));
        mSongs.add(new Song("Ide vláčik ši-ši-ši", R.drawable.ide_vlacik_si_si_si, "GGGAGGEGGGAGGEGGGEGGGGEGGEEE"));
        mSongs.add(new Song("Itsy bitsy spider", R.drawable.itsy_bitsy_spider, "GGGABBBAGABGBBcddcBcdBGGABBAGABGDDGGGABBBAGABG"));

        setContentView(R.layout.activity_song_list);

        mSongListAdapter = new SongListAdapter(this);
        mSongListView = (ListView)this.findViewById(R.id.songListView);
        mSongListView.setAdapter(mSongListAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            startRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRefresh() {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://pesnicky2.appspot.com/json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Song> result = new ArrayList<>();
                        try {
                            JSONArray songs = response.getJSONArray("songs");
                            for (int i = 0; i < songs.length(); ++i) {
                                JSONObject song = songs.getJSONObject(i);
                                String name = song.getString("name");
                                String encodedImage = song.getString("image");
                                String notes = song.getString("notes");
                                byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap icon = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                result.add(new Song(name, icon, notes));
                            }
                        } catch (JSONException e) {
                            // nom nom
                        }

                        mSongs.clear();
                        mSongs.addAll(result);
                        mSongListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SongListActivity.this, "Failed to download songs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        mRequestQueue.add(request);
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
            if (song.bitmap != null) {
                iconView.setImageBitmap(song.bitmap);
            } else {
                iconView.setImageResource(song.icon);
            }

            return convertView;
        }
    }
}
