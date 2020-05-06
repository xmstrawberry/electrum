package org.haobtc.wallet.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.haobtc.wallet.R;
import org.haobtc.wallet.activities.base.BaseActivity;
import org.haobtc.wallet.activities.service.CommunicationModeSelector;
import org.haobtc.wallet.activities.settings.recovery_set.RecoverySetActivity;
import org.haobtc.wallet.aop.SingleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HardwareDetailsActivity extends BaseActivity {

    public static final String TAG = "org.haobtc.wallet.activities.settings.HardwareDetailsActivity";
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tet_keyName)
    TextView tetKeyName;
    @BindView(R.id.lin_OnckOne)
    LinearLayout linOnckOne;
    @BindView(R.id.tet_code)
    TextView tetCode;
    @BindView(R.id.lin_OnckTwo)
    LinearLayout linOnckTwo;
    @BindView(R.id.change_pin)
    LinearLayout changePin;
    @BindView(R.id.tet_noPasspay)
    TextView tetNoPasspay;
    @BindView(R.id.lin_OnckFour)
    LinearLayout linOnckFour;
    @BindView(R.id.wipe_device)
    LinearLayout wipe_device;
    public static boolean dismiss;
    private String bleName;
    private String firmwareVersion;
    private String device_id;
    private String bleVerson;
    private String label;

    @Override
    public int getLayoutId() {
        return R.layout.activity_somemore;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        inits();
    }

    private void inits() {
        Intent intent = getIntent();
        bleName = intent.getStringExtra("bleName");
        firmwareVersion = intent.getStringExtra("firmwareVersion");
        bleVerson = intent.getStringExtra("bleVerson");
        device_id = intent.getStringExtra("device_id");
        label = intent.getStringExtra("label");
        if (!TextUtils.isEmpty(label)) {
            tetKeyName.setText(label);
        } else {
            tetKeyName.setText(String.format("%s", "BixinKEY"));
        }
        tetCode.setText(firmwareVersion);

    }

    @Override
    public void initData() {

    }

    @SingleClick
    @OnClick({R.id.img_back, R.id.lin_OnckOne, R.id.lin_OnckTwo, R.id.change_pin, R.id.lin_OnckFour, R.id.wipe_device, R.id.tetBluetoothSet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.lin_OnckOne:
                Intent intent = new Intent(HardwareDetailsActivity.this, BixinKeyMessageActivity.class);
                intent.putExtra("bleName", bleName);
                intent.putExtra("label", label);
                intent.putExtra("device_id", device_id);
                startActivity(intent);
                break;
            case R.id.lin_OnckTwo:
                Intent intentVersion = new Intent(HardwareDetailsActivity.this, VersionUpgradeActivity.class);
                intentVersion.putExtra("firmwareVersion", firmwareVersion);
                intentVersion.putExtra("bleVerson", bleVerson);
                startActivity(intentVersion);
                break;
            case R.id.change_pin:
                Intent intent1 = new Intent(this, CommunicationModeSelector.class);
                intent1.putExtra("tag", TAG);
                startActivity(intent1);
                break;
            case R.id.lin_OnckFour:
                mIntent(ConfidentialPaymentSettings.class);
                break;
            case R.id.wipe_device:
                mIntent(RecoverySetActivity.class);
                break;
            case R.id.tetBluetoothSet:
                mIntent(BixinKeyBluetoothSettingActivity.class);
                break;
        }
    }
}
