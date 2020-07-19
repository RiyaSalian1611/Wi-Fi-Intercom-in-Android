package com.a1611.riya.wifidirectmessaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import com.a3331.hrishi.wifidirectmessaging.R;

public class WifiList extends AppCompatActivity {

    private DatabaseReference mRef;
    private RecyclerView.LayoutManager manager;
    private RecyclerView clientsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        clientsView = (RecyclerView)findViewById(R.id.clientlist);

        manager = new LinearLayoutManager(WifiList.this, LinearLayoutManager.VERTICAL, true);
        clientsView.setLayoutManager(manager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClientData, ClientViewHolder> adapter = new FirebaseRecyclerAdapter<ClientData, ClientViewHolder>(ClientData.class, R.layout.clienttab, ClientViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ClientViewHolder viewHolder, ClientData model, int position) {
                viewHolder.setmView(model.getName(), model.getIp());
                viewHolder.setListener(WifiList.this);
            }
        };

        clientsView.setAdapter(adapter);
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView name;
        private TextView ip;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = mView.findViewById(R.id.client_name);
            ip = mView.findViewById(R.id.client_ip);
        }

        public void setmView(String name, String ip){
            this.name.setText(name);
            this.ip.setText(ip);
        }

        public void setListener(final Context context){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Messaging.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("ip", ip.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}
