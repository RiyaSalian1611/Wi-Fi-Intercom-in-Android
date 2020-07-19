package com.a1611.riya.wifidirectmessaging;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

//import com.a3331.hrishi.wifidirectmessaging.R;

public class Home extends AppCompatActivity {
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private TextView username, userip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = (TextView)findViewById(R.id.username);
        userip = (TextView)findViewById(R.id.userip);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        mRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        String  ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        userip.setText(ip);
        mRef.child("ip").setValue(ip);
        mRef.child("status").setValue("Active");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.child("status").setValue("Inactive");
        clearApplicationData();
        AppUtils.deleteCache(getApplicationContext());
    }

    public void connect(View view){
        Intent intent = new Intent(Home.this, WifiList.class);
        startActivity(intent);
    }

    public static class AppUtils {
        public static void deleteCache(Context context) {
            try {
                File dir = context.getCacheDir();
                deleteDir(dir);
            } catch (Exception e) {}
        }

        public static boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if(dir!= null && dir.isFile()) {
                return dir.delete();
            } else {
                return false;
            }
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("EEEEEERRRRRROOOOOOORRRR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }
}
