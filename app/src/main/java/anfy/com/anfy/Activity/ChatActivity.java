package anfy.com.anfy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.CharacterIterator;
import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.UplodaImagesActivity;
import anfy.com.anfy.Adapter.ConsultChatAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultChatItem;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends UplodaImagesActivity implements GenericItemClickCallback<ConsultChatItem> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.txt_msg_ed)
    EditText msg_ed ;
    ConsultationItem consultationItem ;
    private ConsultChatAdapter adapter;

    @BindView(R.id.title)
    TextView title ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        ButterKnife.bind(this);
        getConsaltaionItem(savedInstanceState);
        initView();
        canSendMsg = true ;

    }

    private void getConsaltaionItem(Bundle savedInstanceState) {
        if (savedInstanceState !=null){
            title.setText(getString(R.string.consultation_number)+consultationItem.getId());
        }else if (getIntent().getExtras()!=null){
            consultationItem = (ConsultationItem) getIntent().getExtras().get("extra");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("extra" , consultationItem);
    }

    private void initView() {
        title.setText(getString(R.string.consultation_number)+consultationItem.getId());

        LinearLayoutManager layoutManager  =  new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , true) ;
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ConsultChatAdapter(null,layoutManager, this);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference("Chat").child(consultationItem.getId()+"")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("chatdb_onChildAdded" , dataSnapshot.getValue().toString());
                        ConsultChatItem item = dataSnapshot.getValue(ConsultChatItem.class);
                        if (item!=null)
                        adapter.push(item) ;
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onItemClicked(ConsultChatItem item) {

    }

    boolean canSendMsg;

    void clear(){
        canSendMsg = true ;
        msg_ed.setText("");
    }

    @OnClick(R.id.send_text_msg)
    void onSendMessageClick(){
        Log.e("onSendMessageClick" , "enter");
        if (canSendMsg){
            insertMessage(msg_ed.getText().toString() , ConsultChatItem.TYPE_TEXT);
        }
    }


    @OnClick(R.id.send_img)
    void onSendImageClick(){
        Log.e("onSendMessageClick" , "enter");
        if (canSendMsg){
            openGallery();
        }
    }

    private void openGallery() {
        pick_image_permission(1, new UplodaImagesActivity.onUploadResponse() {
            @Override
            public void onSuccess(ArrayList<String> imgs_urls) {
                if (imgs_urls !=null){
                    Log.e("file[]" , imgs_urls.toString());
                    onSendUploadImgSuccess(imgs_urls.get(0));

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }



    void onSendUploadImgSuccess(String image ){
      insertMessage(image , ConsultChatItem.TYPE_IMAGE);
    }


    void insertMessage(String msg , int tybe ){
        canSendMsg = false ;
        ConsultChatItem item = new ConsultChatItem(msg , AppController.getTimeStamp() , tybe , AppController.getUserId());
        FirebaseDatabase.getInstance().getReference("Chat").child(consultationItem.getId()+"").push()
                .setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clear();
                    }
                });

    }

    @OnClick(R.id.close)
    void back(){
        onBackPressed();
    }





}
