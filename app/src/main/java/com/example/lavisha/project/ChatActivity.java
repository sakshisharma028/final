package com.example.lavisha.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
   // private static  final int GALLERY_PICK=1;
    ArrayList<Chat> arrayList;
    DatabaseReference databaseReference;
    TextView tvChatUserName;
    String userId;
    CircleImageView imgChatUserImage;
    FirebaseUser dbuser;
    EditText etMessage;
    MessageAdapter messageAdapter;
    ValueEventListener listener;
    NotificationManager notificationManager;
    ImageView btnSend;
    ImageView btnCamera;
    StorageReference storageReference;
    RecyclerView recyclerView;
    ImageView btnAttachment;
    String message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_activity);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarchatacti);
        getSupportActionBar().setTitle("");
        storageReference = FirebaseStorage.getInstance().getReference();
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.imgbtnSendMessage);
        btnCamera = findViewById(R.id.imgbtnCamera);
        btnAttachment = findViewById(R.id.tmgbtnAttachment);
        recyclerView = findViewById(R.id.recyclerViewForChat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userId = getIntent().getStringExtra("UserId");
        dbuser = FirebaseAuth.getInstance().getCurrentUser();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etMessage.getText().toString();
                if (!message.equals("")) {
                    Log.e("TAG", "Chat act " + dbuser.getUid() + " " + userId);
                    //user.getUid sender & userId receiver
                    sendMessage(dbuser.getUid(), userId, message);

                    Log.e("TAG", arrayList.size() + " Size of arraylist");
                } else {
                    Toast.makeText(ChatActivity.this, "Cannot send empty messages", Toast.LENGTH_SHORT).show();
                }
                etMessage.setText("");
            }
        });

       /* btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvChatUserName=findViewById(R.id.tvUserNameChat);
        imgChatUserImage=findViewById(R.id.circleImageMessageUser);
        final TextView statusOfUser=findViewById(R.id.tvStatus);

        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Log.e("TAG","User is: "+user);
                        tvChatUserName.setText(user.getUsername());
                Log.e("TAGCHAT1","Status is: "+user.getStatus());
                Log.e("TAGSTATUS",user.getStatus()+" Status is");
                statusOfUser.setText(user.getStatus());
                Log.e("TAG",user.getUsername());
                if(user.getDp()=="default")
                {
                    imgChatUserImage.setImageResource(R.drawable.ic_default_user_dp);
                }
                else{
                    Picasso.get().load(user.getDp()).into(imgChatUserImage);
                }
                readMessages(dbuser.getUid(),userId);
                Log.e("TAG",arrayList.size()+" Size of arraylist");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*protected   void onActivityResult(int requestCode,int resultCode,Intent data)
{
    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
    {
        Uri image=data.getData();
        final String current_user_ref="messages/"+ mCurrentUserId + "/" + mChatUser;
        final String chat_user_ref
    }
}*/

    void sendMessage(final String sender, final String receiver, String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("Sender",sender);
        hashMap.put("Receiver",receiver);
        hashMap.put("Message",message);
       hashMap.put("isSeen",false);
        reference.child("Chats").push().setValue(hashMap);

        seen(userId);
        /*databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final User user=dataSnapshot.getValue(User.class);
                final String username=user.getUsername();
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel=new NotificationChannel("123","Default channel",NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Chats");
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                      for(DataSnapshot dataSnapshot1:dataSnapshot2.getChildren()){
                          Chat chat=dataSnapshot1.getValue(Chat.class);
                          String message1=chat.getMessage();
                          Notification notification=new NotificationCompat.Builder(ChatActivity.this,"123")
                                  .setContentTitle(username)
                                  .setContentText(message1)
                                  .setSmallIcon(R.mipmap.ic_launcher)
                                  .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                                  .build();


                          Log.e("TAGCHAT","chat.getSender"+chat.getSender());
                          Log.e("TAGCHAT","chat.getReceiver"+chat.getReceiver());
                          Log.e("TAGCHAT","userId"+userId);
                          if(chat.getSender()!=null) {
                              if (chat.getSender().equals(userId) && user.getStatus().equals("Offline") && !chat.getisSeen())
                                  notificationManager.notify(23, notification);
                          }
                      }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
    }


    private void readMessages(final String myId, final String userId)
    {
        arrayList=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                Log.e("TAG","Children: "+dataSnapshot.getChildren());
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Chat chat=dataSnapshot1.getValue(Chat.class);

                    Log.d("TAGLAVISHA",chat+"");
                    Log.e("TAGN","myId is:"+ myId+"& userId is: "+userId);
                    Log.e("TAGN","Message is: "+chat.getMessage());
                    Log.e("TAGN","chat.getReceiver()"+chat.getReceiver()+"chat.getSender()"+chat.getSender());
                    if(chat.getReceiver()!=null&&chat.getSender()!=null) {
                        if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                            arrayList.add(chat);
                            Log.e("TAGL", "Size of arraylist is: " + arrayList.size());
                        }
                    }

                  messageAdapter=new MessageAdapter(ChatActivity.this,arrayList);
                  recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void seen(final String userId)
    {
        final FirebaseUser dbuser=FirebaseAuth.getInstance().getCurrentUser();
        Log.e("TAGS","id  "+dbuser.getUid());
        final DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Chats");
        listener=dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    final Chat chat=dataSnapshot1.getValue(Chat.class);
                    Log.e("TAGS", "Sender:" + chat.getSender());
                   final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
                   databaseReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           for(DataSnapshot snapshot:dataSnapshot.getChildren())
                           {
                               User user=snapshot.getValue(User.class);
                               if(user.getId()==userId)
                               {
                                   if(user.getStatus()=="Online"&&chat.getReceiver()!=null)
                                   {
                                       HashMap<String ,Object>hashMap=new HashMap<>();
                                       hashMap.put("isSeen",true);
                                       dataSnapshot1.getRef().updateChildren(hashMap);
                                   }
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


   /* public void seen()
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDataba
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Log.e("TAG3","onResume chat");
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Users");
        //Log.e("TAGABC","Id is:"+id);
        dbref.child(firebaseUser.getUid()).child("status").setValue("Online");
    }

    @Override
    protected void onPause() {

        Log.d("TAGFinal","in OnStop");
        super.onPause();
        Log.e("TAG3","onPause chat");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

           DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
//        Log.e("TAGFinal","uid"+ firebaseUser.getUid());

         //   dbref.removeEventListener(listener);

            dbref.child(firebaseUser.getUid()).child("status").setValue("Offline");
        }
        finish();
    }

}
