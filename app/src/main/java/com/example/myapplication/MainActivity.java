package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<notification> extends AppCompatActivity {

    ListView listView;
    String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        runtimePermission();

        //код уведомления
        /*listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "[thm";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        MainActivity.this
                )
                        .setSmallIcon(R.drawable.ic_music)
                        .setContentTitle("Music Player")
                        .setContentText(message)
                        .setAutoCancel(true);


                Intent intent = new Intent(MainActivity.this,
                        PlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

                notificationManager.notify(0,builder.build());

            }
        });*/
    }

     /*.withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
        @Override public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            displaySong();
        }
        @Override public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}
        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
            permissionToken.continuePermissionRequest();
        }
    }).check();*/

    public void runtimePermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
               }

                public ArrayList<File> findSong(File file)
                {
                    ArrayList<File> arrayList = new ArrayList<>();
                    File[] files=file.listFiles();

                    assert files != null;
                    for(File singleFile: files)
                        if (singleFile.isDirectory() && !singleFile.isHidden()) {
                            arrayList.addAll(findSong(singleFile));
                        } else {
                            if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                                arrayList.add(singleFile);
                            }
                        }
                    return arrayList;
                }

                public void displaySong()
                {
                    final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
                    items = new String[mySongs.size()];
                    for (int i=0;i<mySongs.size();i++)
                    {
                        items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
                    }

                    customAdapter customAdapter = new customAdapter();
                    listView.setAdapter(customAdapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String songName = (String) listView.getItemAtPosition(position);

                            startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                            .putExtra("songs", mySongs)
                                    .putExtra("songname", songName)
                                    .putExtra("pos", position)
                            );
                        }
                    });
                }

                class customAdapter extends BaseAdapter
                {

                    @Override
                    public int getCount() {
                        return items.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        @SuppressLint({"InflateParams", "ViewHolder"}) View view = getLayoutInflater().inflate(R.layout.list_item, null);
                        TextView txtSong = view.findViewById(R.id.txtSong);
                        txtSong.setSelected(true);
                        txtSong.setText(items[position]);
                        return view;
                    }
                }

}
