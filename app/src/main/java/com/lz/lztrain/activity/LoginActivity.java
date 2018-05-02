package com.lz.lztrain.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lz.lztrain.R;
import com.lz.lztrain.bean.StuffEntity;
import com.lz.lztrain.handler.RemoteDataHandler;
import com.lz.lztrain.utils.Constants;
import com.lz.lztrain.utils.GsonUtil;
import com.lz.lztrain.utils.PrefsUtil;
import com.lz.lztrain.view.PasswordEditText;
import com.lz.mylibrary.TrainToast;
import com.lz.mylibrary.BaseActivity;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

import static com.lz.lztrain.utils.Constants.REQUEST_STORAGE_STATE;


/**
 * Created by lenovo on 2017/6/29.
 */

public class LoginActivity extends BaseActivity {

    private Button login;
    private EditText username;
    private PasswordEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this, "395d7d994f63977bfddd7e0d54156d40");
//        BmobUpdateAgent.initAppVersion();

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_STATE);
        } else {
            BmobUpdateAgent.update(this);
        }
        login = (Button) findViewById(R.id.bt_login);
        username = (EditText) findViewById(R.id.username);
        password = (PasswordEditText) findViewById(R.id.password);
        mTitle.setText(getResources().getString(R.string.login));
        mToolbar.setNavigationIcon(null);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }
    private void login(){
//        startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("name","zhouzhou"));
//        PrefsUtil.getPrefs().setString(PrefsUtil.SP_NAME,"zhouzhou");
//        finish();
//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        if(TextUtils.isEmpty(username.getText().toString().trim())){
            TrainToast.makeText(LoginActivity.this, getResources().getString(R.string.error_empty_username), Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password.getText().toString().trim())){
            TrainToast.makeText(this, getResources().getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }
        SoapObject rpc = new SoapObject(Constants.NAME_SPACE, Constants.METHOD_LOGIN);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
//        SharedPreferences sharedPreferences = getSharedPreferences("ljq", Context.MODE_PRIVATE);
//        String name =  sharedPreferences.getString("name", "");
        rpc.addProperty("username", username.getText().toString().trim());
        rpc.addProperty("pass", password.getText().toString().trim());
        RemoteDataHandler.asyncSoap(LoginActivity.this, Constants.METHOD_LOGIN, rpc, new RemoteDataHandler.CallBackXml() {
            @Override
            public void dataLoaded(SoapObject soapObject) {
//                ResultUtil entity = GsonUtil.json2bean(result, ResultUtil.class);
//                PersonEntity person = GsonUtil.json2bean(GsonUtil.bean2json(entity.getResult(),PersonEntity.class),PersonEntity.class);
                if (soapObject != null && soapObject.getPropertyCount() > 0) {
                    String result = soapObject.getPropertyAsString(0);
                    if(result!=null){
                        StuffEntity stuffEntity = GsonUtil.json2bean(result, StuffEntity.class);
                        if(stuffEntity!=null){
                            int code = stuffEntity.getCode();
                            if(code==1){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("name",stuffEntity.getResult().getName());
                                intent.putExtra("number",stuffEntity.getResult().getNumber());
                                intent.putExtra("position",stuffEntity.getResult().getPosition());
                                intent.putExtra("department",stuffEntity.getResult().getPosition());
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("stuffEntity", stuffEntity);
//                                intent.putExtras(bundle);
                                startActivity(intent);
                                PrefsUtil.getPrefs().setString(PrefsUtil.SP_NAME,stuffEntity.getResult().getName());
                                PrefsUtil.getPrefs().setString(PrefsUtil.SP_NUM,stuffEntity.getResult().getNumber());
                                PrefsUtil.getPrefs().setString(PrefsUtil.SP_POSITION,stuffEntity.getResult().getPosition());
                                PrefsUtil.getPrefs().setString(PrefsUtil.SP_DEPARTMENT,stuffEntity.getResult().getDepartment());
                                finish();
                            }else{
                                TrainToast.makeText(LoginActivity.this,stuffEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            TrainToast.makeText(LoginActivity.this,getResources().getString(R.string.error_service), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        TrainToast.makeText(LoginActivity.this,getResources().getString(R.string.error_service), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TrainToast.makeText(LoginActivity.this, getResources().getString(R.string.error_net), Toast.LENGTH_SHORT).show();
                }
            }
        }, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BmobUpdateAgent.update(this);
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_permission_storage), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
