package org.haobtc.wallet.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.haobtc.wallet.R;
import org.haobtc.wallet.activities.base.BaseActivity;
import org.haobtc.wallet.activities.transaction.DeatilMoreAddressActivity;
import org.haobtc.wallet.adapter.SinatrayPersonAdapetr;
import org.haobtc.wallet.bean.GetnewcreatTrsactionListBean;
import org.haobtc.wallet.bean.InputOutputAddressEvent;
import org.haobtc.wallet.bean.ScanCheckDetailBean;
import org.haobtc.wallet.utils.Daemon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionDetailsActivity extends BaseActivity {

    @BindView(R.id.img_progressone)
    ImageView imgProgressone;
    @BindView(R.id.img_progresstwo)
    ImageView imgProgresstwo;
    @BindView(R.id.img_progressthree)
    ImageView imgProgressthree;
    @BindView(R.id.img_progressfour)
    ImageView imgProgressfour;
    @BindView(R.id.tet_Trone)
    TextView tetTrone;
    @BindView(R.id.tet_Trtwo)
    TextView tetTrtwo;
    @BindView(R.id.tet_Trthree)
    TextView tetTrthree;
    @BindView(R.id.tet_Trfore)
    TextView tetTrfore;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_in_tb2)
    TextView tvInTb2;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.tb2)
    RelativeLayout tb2;

    @BindView(R.id.card_Trantime)
    CardView cardTrantime;
    @BindView(R.id.tet_getMoneyaddress)
    TextView tetGetMoneyaddress;
    @BindView(R.id.tet_payAddress)
    TextView tetPayAddress;
    @BindView(R.id.cardView4)
    CardView cardView4;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.textView14)
    TextView textView14;
    @BindView(R.id.textView13)
    TextView textView13;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.textView16)
    TextView textView16;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.textView19)
    TextView textView19;
    @BindView(R.id.textView20)
    TextView textView20;
    @BindView(R.id.cardView3)
    CardView cardView3;
    @BindView(R.id.sig_trans)
    Button sigTrans;
    public static final String TAG = "com.bixin.wallet.activities.TransactionDetailsActivity";
    @BindView(R.id.recy_Signatory)
    RecyclerView recySignatory;
    @BindView(R.id.tet_content)
    TextView tetContent;
    @BindView(R.id.tet_state)
    TextView tetState;
    @BindView(R.id.tet_trsactionHash)
    TextView tetTrsactionHash;
    @BindView(R.id.tet_trsactionTime)
    TextView tetTrsactionTime;
    @BindView(R.id.lin_getMoreaddress)
    LinearLayout linGetMoreaddress;
    @BindView(R.id.tet_addressNum)
    TextView tetAddressNum;
    @BindView(R.id.tet_confirm)
    TextView tetConfirm;
    private String keyValue;
    private String tx_hash;
    private ArrayList<InputOutputAddressEvent> strSinalist;
    private String listType;
    private String rowTrsation;
    private String jsondef_get;
    private GetnewcreatTrsactionListBean getnewcreatTrsactionListBean;
    private String rowtx;
    private String strParse;
    private String language;


    @Override
    public int getLayoutId() {
        return R.layout.trans_details;
    }

    @Override
    public void initView() {
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        language = preferences.getString("language", "");
        Intent intent = getIntent();
        rowTrsation = intent.getStringExtra("txCreatTrsaction");
        keyValue = intent.getStringExtra("keyValue");//Judge which interface to jump in from
        tx_hash = intent.getStringExtra("tx_hash");
        listType = intent.getStringExtra("listType");
        strParse = intent.getStringExtra("strParse");


        Log.i("listType", "listType--: " + listType + "   tx_hash--: " + tx_hash + "   rowTrsation -- : " + rowTrsation);
    }

    //029a5002de1703279f256bb09c09c6d8fdf8f784b762c26fa6d5f7f9b5de7d6a
    //d186e6b87e3c8779172b2fdec31602f8811b279674e36f688132f1ca1d36ec1c

    @Override
    public void initData() {
        strSinalist = new ArrayList<>();
        if (!TextUtils.isEmpty(keyValue)) {
            if (keyValue.equals("A")) {
                //creat succses
                mCreataSuccsesCheck();
            } else if (keyValue.equals("B")) {
                if (listType.equals("history")) {
                    //histry trsaction detail
                    trsactionDetail();

                } else if (listType.equals("scan")) {
                    scanDataDetailMessage();

                } else {
                    //creat succses
                    mCreataSuccsesCheck();

                }

            }
        }
    }

    //trsaction detail
    private void trsactionDetail() {
        if (!TextUtils.isEmpty(tx_hash)) {
            PyObject get_tx_info = null;
            try {
                get_tx_info = Daemon.commands.callAttr("get_tx_info", tx_hash);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("printStackTrace", "tr----- " + e.getMessage());
                return;
            }
            if (get_tx_info != null) {
                jsondef_get = get_tx_info.toString();
                jsonDetailData(jsondef_get);
            }

        }
    }

    //creat succses check
    private void mCreataSuccsesCheck() {
        //get trsaction list content
        if (!TextUtils.isEmpty(rowTrsation)) {
            PyObject def_get_tx_info_from_raw = null;
            try {
                def_get_tx_info_from_raw = Daemon.commands.callAttr("get_tx_info_from_raw", rowTrsation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (def_get_tx_info_from_raw != null) {
                jsondef_get = def_get_tx_info_from_raw.toString();
                jsonDetailData(jsondef_get);
            }

        }

    }

    @SuppressLint("DefaultLocale")
    private void jsonDetailData(String jsondef_get) {
        Log.i("jsonDetailData", "jsonDetail==== " + jsondef_get);
        try {
            Gson gson = new Gson();
            getnewcreatTrsactionListBean = gson.fromJson(jsondef_get, GetnewcreatTrsactionListBean.class);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }

        String amount = getnewcreatTrsactionListBean.getAmount();
        String fee = getnewcreatTrsactionListBean.getFee();
        String description = getnewcreatTrsactionListBean.getDescription();
        String tx_status = getnewcreatTrsactionListBean.getTxStatus();
        List<GetnewcreatTrsactionListBean.OutputAddrBean> output_addr = getnewcreatTrsactionListBean.getOutputAddr();
//        List<String> input_addr = getnewcreatTrsactionListBean.getInputAddr();
        List<String> cosigner = getnewcreatTrsactionListBean.getCosigner();
        List<Integer> signStatus = getnewcreatTrsactionListBean.getSignStatus();
        String txid = getnewcreatTrsactionListBean.getTxid();
        rowtx = getnewcreatTrsactionListBean.getTx();
        //trsaction hash
        tetTrsactionHash.setText(txid);
        //input address num
        int size = output_addr.size();
        if (language.equals("Chinese")) {
            tetAddressNum.setText(String.format("%s%d%s", getResources().getString(R.string.wait), size, getResources().getString(R.string.ge)));
        } else if (language.equals("English")) {
            tetAddressNum.setText(String.format("%s%d", getResources().getString(R.string.wait), size));
        }
        //output_address
        String addr = output_addr.get(0).getAddr();
        tetGetMoneyaddress.setText(addr);

//        if (input_addr != null) {
//            //input_address
//            String strInputAddr = input_addr.get(0);
//            tetPayAddress.setText(strInputAddr);
//        }
        if (signStatus != null) {
            Integer integer = signStatus.get(0);
            Integer integer1 = signStatus.get(1);
            String strNum = integer + "/" + integer1;
            textView20.setText(strNum);
        }
        //Transfer accounts num
        if (!TextUtils.isEmpty(amount)) {
            textView14.setText(amount);
        }
        //Miner's fee
        if (!TextUtils.isEmpty(fee)) {
            textView15.setText(fee);
        }
        //Remarks
        tetContent.setText(description);
        //cosigner
        for (int i = 0; i < cosigner.size(); i++) {
            InputOutputAddressEvent inputOutputAddressEvent = new InputOutputAddressEvent();
            inputOutputAddressEvent.setNum(String.valueOf(i + 1));
            inputOutputAddressEvent.setAddress(cosigner.get(i));
            strSinalist.add(inputOutputAddressEvent);
        }
        SinatrayPersonAdapetr sinatrayPersonAdapetr = new SinatrayPersonAdapetr(strSinalist);
        recySignatory.setAdapter(sinatrayPersonAdapetr);
        if (!TextUtils.isEmpty(tx_status)){
            //judge state
            judgeState(tx_status);
        }

    }

    @SuppressLint("DefaultLocale")
    private void scanDataDetailMessage() {
        Gson gson = new Gson();
        ScanCheckDetailBean scanCheckDetailBean = gson.fromJson(strParse, ScanCheckDetailBean.class);
        ScanCheckDetailBean.DataBean scanListdata = scanCheckDetailBean.getData();
        String amount = scanListdata.getAmount();
        String fee = scanListdata.getFee();
        String description = scanListdata.getDescription();
        String tx_status = scanListdata.getTxStatus();
        List<ScanCheckDetailBean.DataBean.OutputAddrBean> outputAddr = scanListdata.getOutputAddr();
//        List<String> input_addr = getnewcreatTrsactionListBean.getInputAddr();
        List<String> cosigner = scanListdata.getCosigner();
        List<Integer> signStatusMes = scanListdata.getSignStatus();
        String txid = scanListdata.getTxid();
        rowtx = scanListdata.getTx();
        //trsaction hash
        tetTrsactionHash.setText(txid);
        //input address num
        int size = outputAddr.size();
        if (language.equals("English")) {
            tetAddressNum.setText(String.format("%s%d", getResources().getString(R.string.wait), size));
        } else {
            tetAddressNum.setText(String.format("%s%d%s", getResources().getString(R.string.wait), size, getResources().getString(R.string.ge)));
        }
        //output_address
        String addr = outputAddr.get(0).getAddr();
        tetGetMoneyaddress.setText(addr);

//        if (input_addr != null) {
//            //input_address
//            String strInputAddr = input_addr.get(0);
//            tetPayAddress.setText(strInputAddr);
//        }
        if (signStatusMes != null) {
            Integer integer = signStatusMes.get(0);
            Integer integer1 = signStatusMes.get(1);
            String strNum = integer + "/" + integer1;
            textView20.setText(strNum);
        }
        //Transfer accounts num
        if (!TextUtils.isEmpty(amount)) {
            textView14.setText(amount);
        }
        //Miner's fee
        if (!TextUtils.isEmpty(fee)) {
            textView15.setText(fee);
        }
        //Remarks
        tetContent.setText(description);
        //cosigner
        for (int i = 0; i < cosigner.size(); i++) {
            InputOutputAddressEvent inputOutputAddressEvent = new InputOutputAddressEvent();
            inputOutputAddressEvent.setNum(String.valueOf(i + 1));
            inputOutputAddressEvent.setAddress(cosigner.get(i));
            strSinalist.add(inputOutputAddressEvent);
        }
        SinatrayPersonAdapetr sinatrayPersonAdapetr = new SinatrayPersonAdapetr(strSinalist);
        recySignatory.setAdapter(sinatrayPersonAdapetr);
        if (!TextUtils.isEmpty(tx_status)){
            //judge state
            judgeState(tx_status);
        }


    }

    //judge state
    private void judgeState(String tx_status) {
        //trsaction state
        if (tx_status.equals("Unconfirmed")) {//Unconfirmed
            tetState.setText(R.string.waitchoose);
            sigTrans.setText(R.string.check_trsaction);
            imgProgressone.setVisibility(View.GONE);
            imgProgresstwo.setVisibility(View.GONE);
            imgProgressthree.setVisibility(View.GONE);
            imgProgressfour.setVisibility(View.VISIBLE);
            //text color
            tetTrtwo.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            tetTrthree.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            tetTrfore.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            //trsaction hash and time
            cardTrantime.setVisibility(View.VISIBLE);
        } else if (tx_status.contains("confirmations")) {//Confirmed
            tetState.setText(R.string.completed);
            sigTrans.setText(R.string.check_trsaction);
            imgProgressone.setVisibility(View.GONE);
            imgProgresstwo.setVisibility(View.GONE);
            imgProgressthree.setVisibility(View.GONE);
            imgProgressfour.setVisibility(View.VISIBLE);
            //Number of judgment confirmation
            String strConfirl = tx_status.replaceAll(" confirmations","");
            BigDecimal bignum1 = new BigDecimal(strConfirl);
            BigDecimal bigDecimal = new BigDecimal(100);
            int mathMax = bignum1.compareTo(bigDecimal);
            if (mathMax == 1) {
                tetConfirm.setText(String.format("%s%s", getResources().getString(R.string.confirmnum), ">100"));
            }else{
                tetConfirm.setText(String.format("%s%s", getResources().getString(R.string.confirmnum), strConfirl));
            }

            //text color
            tetTrtwo.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            tetTrthree.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            tetTrfore.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            ////trsaction hash and time
            cardTrantime.setVisibility(View.VISIBLE);
        } else if (tx_status.contains("Unsigned")) {//unsigned
            tetState.setText(R.string.unsigned);
            sigTrans.setText(R.string.signature_trans);
        } else if (tx_status.contains("Signed")) {//signed
            tetState.setText(R.string.wait_broadcast);
            sigTrans.setText(R.string.broadcast);
            //progress
            imgProgressone.setVisibility(View.GONE);
            imgProgresstwo.setVisibility(View.GONE);
            imgProgressthree.setVisibility(View.VISIBLE);
            imgProgressfour.setVisibility(View.GONE);
            //text color
            tetTrtwo.setTextColor(getResources().getColor(R.color.button_bk_disableok));
            tetTrthree.setTextColor(getResources().getColor(R.color.button_bk_disableok));

        } else if (tx_status.contains("Partially signed")) {//you are signer
            tetState.setText(R.string.you_are_signed);
            sigTrans.setText(R.string.forWord_orther);
            //progress
            imgProgressone.setVisibility(View.GONE);
            imgProgresstwo.setVisibility(View.VISIBLE);
            imgProgressthree.setVisibility(View.GONE);
            imgProgressfour.setVisibility(View.GONE);
            //text color
            tetTrtwo.setTextColor(getResources().getColor(R.color.button_bk_disableok));
        }

    }


    @OnClick({R.id.img_back, R.id.img_share, R.id.sig_trans, R.id.lin_getMoreaddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_share:
                Intent intent = new Intent(TransactionDetailsActivity.this, ShareOtherActivity.class);
                intent.putExtra("rowTrsaction", tx_hash);
                intent.putExtra("rowTx", rowtx);
                startActivity(intent);
                break;
            case R.id.sig_trans:
                String strBtncontent = sigTrans.getText().toString();
                if (strBtncontent.equals(getResources().getString(R.string.signature_trans))) {
                    Intent intentCon = new Intent(TransactionDetailsActivity.this, ConfirmOnHardware.class);

                    startActivity(intentCon);

                } else if (strBtncontent.equals(getResources().getString(R.string.forWord_orther))) {
                    Intent intent1 = new Intent(TransactionDetailsActivity.this, ShareOtherActivity.class);
                    intent1.putExtra("rowTrsaction", tx_hash);
                    intent1.putExtra("rowTx", rowtx);
                    startActivity(intent1);

                } else if (strBtncontent.equals(getResources().getString(R.string.broadcast))) {

                } else if (strBtncontent.equals(getResources().getString(R.string.check_trsaction))) {

                }

                break;
            case R.id.lin_getMoreaddress:
                Intent intent1 = new Intent(TransactionDetailsActivity.this, DeatilMoreAddressActivity.class);
                intent1.putExtra("jsondef_get", jsondef_get);
                startActivity(intent1);
                break;
        }
    }

}

