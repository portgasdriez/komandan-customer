package wrteam.multivendor.shop.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wrteam.multivendor.shop.R;
import wrteam.multivendor.shop.activity.MainActivity;
import wrteam.multivendor.shop.activity.MidtransActivity;
import wrteam.multivendor.shop.activity.PaymentActivity;
import wrteam.multivendor.shop.helper.ApiConfig;
import wrteam.multivendor.shop.helper.Constant;
import wrteam.multivendor.shop.helper.Session;

public class PopupPremiumNotifFragment extends DialogFragment {

    TextView next;
    ImageView close;
    View root;
    Activity activity;
    Session session;
    String from;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_popup_premium_notif, container, false);

        activity = getActivity();
        session = new Session(activity);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assert getArguments() != null;
        from = getArguments().getString(Constant.FROM);
        next = (TextView) root.findViewById(R.id.btNext);
        close = (ImageView) root.findViewById(R.id.close);
        MainActivity.popupPremiumNotifFragment.setCancelable(false);
//        MainActivity.popupPremiumNotifFragment.setCanceledOnTouchOutside(false);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.getData(Constant.IS_PREMIUM).equals("1")){
                    MainActivity.popupPremiumNotifFragment.dismiss();
                }else {
                    Toast.makeText(activity, "Masa trial anda habis, silahkan lakukan pembayaran", Toast.LENGTH_SHORT).show();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMidtransPayment();
            }
        });
        return root;
    }
    public void CreateMidtransPayment() {
        Map<String, String> params = new HashMap<>();
        String orderId = Constant.randomNumeric(3);
        params.put(Constant.ORDER_ID,orderId);
//        if (grossAmount.contains(",")) {
//            params.put(Constant.GROSS_AMOUNT, grossAmount.split(",")[0]);
//        } else if (grossAmount.contains(".")) {
//            params.put(Constant.GROSS_AMOUNT, grossAmount.split("\\.")[0]);
//        }
        params.put(Constant.GROSS_AMOUNT,"150000");
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        Intent intent = new Intent(activity, MidtransActivity.class);
                        intent.putExtra(Constant.URL, jsonObject.getJSONObject(Constant.DATA).getString(Constant.REDIRECT_URL));
                        intent.putExtra(Constant.ORDER_ID, orderId);
                        intent.putExtra(Constant.FROM, Constant.PAYMENT);
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (PaymentActivity.dialog_ != null) {
                PaymentActivity.dialog_.dismiss();
            }
        }, activity, Constant.MIDTRANS_PAYMENT_URL, params, true);
    }
}
