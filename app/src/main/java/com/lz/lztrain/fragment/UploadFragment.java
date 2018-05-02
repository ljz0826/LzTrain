package com.lz.lztrain.fragment;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.lztrain.R;
import com.lz.lztrain.activity.CaptureActivity;
import com.lz.lztrain.activity.ImageViewActivity;
import com.lz.lztrain.activity.MainActivity;
import com.lz.lztrain.bean.ResultUtil;
import com.lz.lztrain.handler.RemoteDataHandler;
import com.lz.lztrain.utils.Constants;
import com.lz.lztrain.utils.FlashLightManager;
import com.lz.lztrain.utils.GsonUtil;
import com.lz.mylibrary.TrainToast;
import com.theartofdev.edmodo.cropper.CropImage;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.theartofdev.edmodo.cropper.CropImage.isExplicitCameraPermissionRequired;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.getCameraIntents;
import static com.theartofdev.edmodo.cropper.CropImage.getGalleryIntents;

/**
 * Created by lenovo on 2017/8/10.
 */

public class UploadFragment extends BaseFragment {

    private final static int ON_UPLOAD_ZXING = 202;
    private final static int MATERIAL_LENGTH = 12;

    private Button bt_upload;
    private ImageView iv_pic, iv_del, manager, upload_zxing;
    private LinearLayout ll_pic;
    private Uri uri;
    private EditText code;
    private final static int REQUEST_CODE_CAMERA_IMG = 101;
    private final static int REQUEST_CODE_CAMERA_ZXING = 102;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListener();
    }

    public static UploadFragment getInstance() {
        return new UploadFragment();
    }

    private void initView(View rootView) {
        bt_upload = (Button) rootView.findViewById(R.id.bt_upload);
        iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
        ll_pic = (LinearLayout) rootView.findViewById(R.id.ll_pic);
        iv_del = (ImageView) rootView.findViewById(R.id.iv_del);
        code = (EditText) rootView.findViewById(R.id.code);
        manager = (ImageView) rootView.findViewById(R.id.manager);
        upload_zxing = (ImageView) rootView.findViewById(R.id.upload_zxing);
    }

    private void addListener() {
        ll_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_IMG);
                } else {
                    if (uri != null) {
                        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                        intent.putExtra("uri", uri.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv_pic, "iv_pic");
//                        intent.putExtra("showTransitionAnimation",true);
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivityForResult(getPickImageChooserIntent(getActivity(), "", false, true), PICK_IMAGE_CHOOSER_REQUEST_CODE);
                    }
                }
            }
        });
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.error_empty_uri), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code.getText().toString().trim())) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.error_empty_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadPic();
            }
        });
        iv_del.setVisibility(View.GONE);
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = null;
                iv_del.setVisibility(View.GONE);
                iv_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_pic));
            }
        });
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code.setText("");
                upload_zxing.setVisibility(View.VISIBLE);
            }
        });
        upload_zxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlashLightManager flashLightManager = new FlashLightManager(getActivity());
                flashLightManager.init();
                if(flashLightManager.isTurnOnFlash()){
                    flashLightManager.turnOff();
                }else{
                    flashLightManager.turnOn();
                }
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_ZXING);
//                } else {
//                    startActivityForResult(new Intent(getActivity(), CaptureActivity.class), ON_UPLOAD_ZXING);
//                }
            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    upload_zxing.setVisibility(View.VISIBLE);
                    manager.setVisibility(View.GONE);
                    upload_zxing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(getActivity(), CaptureActivity.class), ON_UPLOAD_ZXING);
                        }
                    });
                } else {
                    manager.setVisibility(View.VISIBLE);
                    upload_zxing.setVisibility(View.GONE);
                    manager.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            code.setText("");
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!TextUtils.isEmpty(code.getText())) {
            code.setSelection(code.getText().length());
        }
    }

    public static Intent getPickImageChooserIntent(@NonNull Context context, CharSequence title, boolean includeDocuments, boolean includeCamera) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents if Camera permission is available
        if (!isExplicitCameraPermissionRequired(context) && includeCamera) {
            allIntents.addAll(getCameraIntents(context, packageManager));
        }

        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        if (galleryIntents.size() > 1) {
            int i = galleryIntents.size() - 1;
            for (; i > 0; i--) {
                galleryIntents.remove(i);
            }
        }
        allIntents.addAll(galleryIntents);

        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private void uploadPic() {
        SoapObject rpc = new SoapObject(Constants.NAME_SPACE, Constants.METHOD_UPLOADFILE);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        Bitmap imageBitmap = BitmapFactory.decodeFile(uri.getPath());
        String uploadPhoto = convertIconToString(imageBitmap);
        rpc.addProperty("fs", uploadPhoto);
        rpc.addProperty("FileName", code.getText().toString().trim());
        RemoteDataHandler.asyncSoapfromfragment(UploadFragment.this, Constants.METHOD_UPLOADFILE, rpc, new RemoteDataHandler.CallBackXml() {

            @Override
            public void dataLoaded(SoapObject soapObject) {
                if (soapObject != null && soapObject.getPropertyCount() > 0) {
                    String result = soapObject.getPropertyAsString(0);
                    if (result != null) {
                        ResultUtil resultUtil = GsonUtil.json2bean(result, ResultUtil.class);
                        if (resultUtil != null) {
                            int code = resultUtil.getCode();
                            if (code == 1) {
                                TrainToast.makeText(getActivity(), resultUtil.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("zhou", resultUtil.getMsg());
                                TrainToast.makeText(getActivity(), resultUtil.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            TrainToast.makeText(getActivity(), getResources().getString(R.string.error_service), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        TrainToast.makeText(getActivity(), getResources().getString(R.string.error_service), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.error_net), Toast.LENGTH_SHORT).show();
                }
            }
        }, true);
    }

    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_IMG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (uri != null) {
                        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                        intent.putExtra("uri", uri.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv_pic, "iv_pic");
//                        intent.putExtra("showTransitionAnimation",true);
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivityForResult(getPickImageChooserIntent(getActivity(), "", false, true), PICK_IMAGE_CHOOSER_REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_permission_camera), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_CODE_CAMERA_ZXING:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(getActivity(), CaptureActivity.class), ON_UPLOAD_ZXING);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_permission_camera), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
                CropImage.activity(imageUri)
                        .setActivityMenuIconColor(Color.WHITE)
                        .setAllowRotation(true)
                        .setAspectRatio(150, 150)
                        .setMinCropResultSize(500, 500)
                        .setRequestedSize(500, 500)
                        .start(getContext(), UploadFragment.this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    uri = result.getUri();
                    iv_pic.setImageURI(result.getUri());
                    iv_pic.setTag(result.getUri());
                    iv_del.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == ON_UPLOAD_ZXING) {
                Bundle bundle = data.getExtras();
                String material = bundle.getString("result");
                if (material == null) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.no_zxing), Toast.LENGTH_SHORT).show();
                }
                if (material.length() != MATERIAL_LENGTH) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.no_material), Toast.LENGTH_SHORT).show();
                    return;
                }
                code.setText(material);
                manager.setVisibility(View.VISIBLE);
                upload_zxing.setVisibility(View.GONE);
            }
        }
    }


}
