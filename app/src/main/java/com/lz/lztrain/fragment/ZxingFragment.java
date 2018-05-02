package com.lz.lztrain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lz.lztrain.R;
import com.lz.lztrain.activity.CaptureActivity;
import com.lz.lztrain.bean.MaterialEntity;
import com.lz.lztrain.handler.RemoteDataHandler;
import com.lz.lztrain.utils.Constants;
import com.lz.lztrain.utils.GsonUtil;
import com.lz.lztrain.utils.PrefsUtil;
import com.lz.mylibrary.TrainToast;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by lenovo on 2017/8/10.
 */

public class ZxingFragment extends BaseFragment {

    private final static int ON_ACTIVITY_RESULT_ZXING = 201;
    private final static int MATERIAL_LENGTH = 12;

    //data
    private EditText material_code;
    private EditText material_name;
    private EditText standard;
    private EditText unit;
    private EditText price;
    private EditText total_num;
    private EditText apply_num;
    private EditText true_num;

    //submit

    private Button bt_claiming;

    //material
    private String material;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zxing, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt_claiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = material_code.getText().toString().trim();
                String num = true_num.getText().toString().trim();
                submitMaterial(code, num);
            }
        });
    }

    public static ZxingFragment getInstance() {
        return new ZxingFragment();
    }

    private void initView(View rootView) {
        material_code = (EditText) rootView.findViewById(R.id.material_code);
        material_name = (EditText) rootView.findViewById(R.id.material_name);
        standard = (EditText) rootView.findViewById(R.id.standard);
        unit = (EditText) rootView.findViewById(R.id.unit);
        price = (EditText) rootView.findViewById(R.id.price);
        total_num = (EditText) rootView.findViewById(R.id.total_num);
        apply_num = (EditText) rootView.findViewById(R.id.apply_num);
        true_num = (EditText) rootView.findViewById(R.id.true_num);

        bt_claiming = (Button) rootView.findViewById(R.id.bt_claiming);
    }

    public void startZxing() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), ON_ACTIVITY_RESULT_ZXING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == ON_ACTIVITY_RESULT_ZXING) {
                Bundle bundle = data.getExtras();
                material = bundle.getString("result");
                if (material == null) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.no_zxing), Toast.LENGTH_SHORT).show();
                }
                if (material.length() != MATERIAL_LENGTH) {
                    TrainToast.makeText(getActivity(), getResources().getString(R.string.no_material), Toast.LENGTH_SHORT).show();
                    return;
                }
                getMaterialMessage(material);
            }
        }
    }

    public void getMaterialMessage(String material) {
        this.material = material;
        SoapObject rpc = new SoapObject(Constants.NAME_SPACE, Constants.METHOD_WEBFL);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        rpc.addProperty("wzbm", material);
        RemoteDataHandler.asyncSoapfromfragment(ZxingFragment.this, Constants.METHOD_WEBFL, rpc, new RemoteDataHandler.CallBackXml() {
            @Override
            public void dataLoaded(SoapObject soapObject) {
                if (soapObject != null && soapObject.getPropertyCount() > 0) {
                    String result = soapObject.getPropertyAsString(0);
                    if (result != null) {
                        MaterialEntity materialEntity = GsonUtil.json2bean(result, MaterialEntity.class);
                        if (materialEntity != null) {
                            int code = materialEntity.getCode();
                            if (code == 1) {
                                material_code.setText(materialEntity.getResult().getWzbm());
                                material_name.setText(materialEntity.getResult().getWzmc());
                                standard.setText(materialEntity.getResult().getGgth());
                                unit.setText(materialEntity.getResult().getJldw());
                                price.setText(materialEntity.getResult().getKcdj());
                                total_num.setText(materialEntity.getResult().getKcsl());
                                apply_num.setText(materialEntity.getResult().getQlsl());
                            } else {
                                TrainToast.makeText(getActivity(), materialEntity.getMsg(), Toast.LENGTH_SHORT).show();
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

    /**
     * @param code 物质编码
     * @param num  实发数量
     */
    private void submitMaterial(String code, String num) {
        String name = PrefsUtil.getPrefs().getString(PrefsUtil.SP_NAME, "");
        SoapObject rpc = new SoapObject(Constants.NAME_SPACE, Constants.METHOD_UPKCB);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        if (code == null || TextUtils.isEmpty(material_code.getText())) {
            TrainToast.makeText(getActivity(), getResources().getString(R.string.error_matrial), Toast.LENGTH_SHORT).show();
            return;
        }
        if (num == null || TextUtils.isEmpty(true_num.getText())) {
            TrainToast.makeText(getActivity(), getResources().getString(R.string.error_true), Toast.LENGTH_SHORT).show();
            return;
        }
        if (name == null || name.equals("")) {
            TrainToast.makeText(getActivity(), getResources().getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(true_num.getText().toString()) > Integer.parseInt(total_num.getText().toString())) {
            TrainToast.makeText(getActivity(), getResources().getString(R.string.error_num), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("zhou", "name" + name);
        rpc.addProperty("wzbm", code);
        rpc.addProperty("sfsl", num);
        rpc.addProperty("name", name);
        RemoteDataHandler.asyncSoapfromfragment(ZxingFragment.this, Constants.METHOD_UPKCB, rpc, new RemoteDataHandler.CallBackXml() {
            @Override
            public void dataLoaded(SoapObject soapObject) {
                if (soapObject != null && soapObject.getPropertyCount() > 0) {
                    String result = soapObject.getPropertyAsString(0);
                    if (result != null) {
                        MaterialEntity materialEntity = GsonUtil.json2bean(result, MaterialEntity.class);
                        if (materialEntity != null) {
                            int code = materialEntity.getCode();
                            if (code == 1) {
                                TrainToast.makeText(getActivity(), materialEntity.getMsg(), Toast.LENGTH_SHORT).show();
                                true_num.setText("");
                                getMaterialMessage(material);
                            } else {
                                TrainToast.makeText(getActivity(), materialEntity.getMsg(), Toast.LENGTH_SHORT).show();
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


}
