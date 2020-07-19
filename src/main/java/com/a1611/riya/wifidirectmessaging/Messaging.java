package com.a1611.riya.wifidirectmessaging;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//import com.a3331.hrishi.wifidirectmessaging.R;

public class Messaging extends AppCompatActivity {

    private String name;
    private String ip;
    private TextView nameView;
    public TextView entermsg,mysent,recmsg;
    public String myip1,entermsg1,recmsg1,enterip1,mysent1;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Socket sentclient;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        ip = intent.getStringExtra("ip");

        nameView = (TextView)findViewById(R.id.m_name);
        nameView.setText(name);

        entermsg = (EditText)findViewById(R.id.new_message);
        mysent = (TextView)findViewById(R.id.sent_message);

        recmsg = (TextView)findViewById(R.id.received_message);

        WifiManager manager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        myip1 = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());

        new Thread(new Runnable() {
            @Override
            public void run() { // new thread Runnable Interface
                try {
                    // for a client to connect to a  server we need a socket
                    serverSocket = new ServerSocket(4444);      // any port from 1024-65535 can be used
                    while (true) {
                        clientSocket = serverSocket.accept();       // establish connection between server and client
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        recmsg1 = inputStream.readUTF(); // reading the input message
                        setrecdata();

                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        }).start();


    }

    public void sendMessage(View view){
        entermsg1 = entermsg.getText().toString();  // convert message to string
        if (!ip.equals("")) {  // if ip is entered
            if (!entermsg1.equals("")) { // and if message is entered
                new Thread(new Runnable() {
                    @Override
                    public void run() { // execute Runnable interface in the thread
                        try {
                            sentclient = new Socket(ip, 4444);
                            DataOutputStream outputStream = new DataOutputStream(sentclient.getOutputStream());// send message
                            outputStream.writeUTF(entermsg1); // display the sent message
                            outputStream.flush();   // flush the garbage data in the stream
                            setsentmsg();
                            outputStream.close();
                            sentclient.close();
                        } catch (Exception e) {
                            msgnotsent();
                        }
                    }
                }).start();
            } else { // If message isn't entered
                Toast.makeText(getApplicationContext(), "Type Your Msg", Toast.LENGTH_SHORT).show();
            }
        } else { // If IP address isn't entered
            Toast.makeText(getApplicationContext(), "Please Enter Friend IP", Toast.LENGTH_SHORT).show();
        }
        entermsg.setText("");
    }

    public void finishActivity(View view){
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setrecdata()
    {
        Messaging.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recmsg.append("Other :"+recmsg1+"\n"); // DISPLAYS RECEIVED MESSAGE
                Toast.makeText(getApplicationContext(),"MSG RECEIVED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setsentmsg()
    {
        Messaging.this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                mysent.append("MY :"+entermsg1+"\n");   // DISPLAYS THE MESSAGE YOU HAVE SENT
                Toast.makeText(getApplicationContext(),"MSG SENT SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void msgnotsent()
    {
        Messaging.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"MSG NOT SENT TRY AGAIN",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void callUser(View view) {
        Intent intent=new Intent(Messaging.this,SipAudioCall.class);
        startActivity(intent);
    }

}
