package anfy.com.anfy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
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
import anfy.com.anfy.Activity.Dialog.EditConsultActivity;
import anfy.com.anfy.Adapter.ConsultChatAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultChatItem;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ChatActivity extends UplodaImagesActivity implements GenericItemClickCallback<ConsultChatItem> {

    public static final int RESULT_DELETED = 404;
    public static final int RESULT_EDITED= 204;
    public static final int REQUEST_CODE = 33;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.txt_msg_ed)
    EditText msg_ed ;
    ConsultationItem consultationItem ;
    private ConsultChatAdapter adapter;

    @BindView(R.id.title)
    TextView title ;


    @BindView(R.id.details_txt)
    TextView details_txt ;

    @BindView(R.id.hide_details)
    View hide_details ;

    @BindView(R.id.details_container)
    View details_container ;

    @OnClick(R.id.hide_details)
    void hide_details() {
        details_container.setVisibility(View.GONE);
    }


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
        details_txt.setText(consultationItem.getDetails());
        LinearLayoutManager layoutManager  =  new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , true) ;
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ConsultChatAdapter(this , null,layoutManager, this);
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
        if (item.getType() == ConsultChatItem.TYPE_IMAGE && item !=null){
            Intent intent = new Intent(getApplicationContext() , BigImageActivity.class);
            intent.putExtra("url" , Utils.getImageUrl(item.getText()));
            startActivity(intent);
        }
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
            if (!msg_ed.getText().toString().isEmpty())
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
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Chat").child(consultationItem.getId()+"").push() ;
               item.setDbRefrence("Chat/"+consultationItem.getId()+"/"+reference.getKey());
                reference.setValue(item)
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



    @BindView(R.id.expanded_menu)
    View expanded_menu ;
    @OnClick(R.id.expanded_menu)
    void expanded_menu(){
        PopupMenu menu = new PopupMenu(this, expanded_menu);
        menu.getMenu().add(R.string.show_details) ;
        menu.getMenu().add(R.string.edit) ;
        menu.getMenu().add(R.string.delete) ;
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(getString(R.string.edit))){
                    editConsultation();
                }else if (item.getTitle().equals(getString(R.string.delete))){
                    deleteConsultaion();
                }else if (item.getTitle().equals(getString(R.string.show_details))){
                    details_container.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        menu.show();
    }

    private void editConsultation() {
        Intent intent = new Intent(getApplicationContext() , EditConsultActivity.class);
        intent.putExtra("model" , consultationItem) ;
        startActivityForResult(intent,0);
    }

    private void deleteConsultaion() {
        showLoading(true);
        Call<ResponseBody> call = Injector.Api().deleteConultaion(consultationItem.getId()) ;
        call.enqueue(new CallbackWithRetry<ResponseBody>(call, () -> showNoInternet(true, (v -> {
            showNoInternet(false, null);
            deleteConsultaion();
        }))) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                Intent data = new Intent() ;
                data.putExtra("model" , consultationItem) ;
                setResult(ChatActivity.RESULT_DELETED , data);
                finish();
            }
        });

    }

    Intent editResult = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_EDITED && data!=null){
            editResult = data ;
        }
    }

    @Override
    public void onBackPressed() {
        if (editResult!=null){
            setResult(RESULT_EDITED , editResult);
        }
        finish();
    }

    public static void deleteMessage(String dbRefrence) {
        FirebaseDatabase.getInstance().getReference(dbRefrence).setValue(null);
    }
}
