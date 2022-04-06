package wrteam.multivendor.shop.fragment;

import static wrteam.multivendor.shop.helper.Constant.PEMBAYARAN_BULAN;
import static wrteam.multivendor.shop.helper.Constant.POST_SIMPANAN_WAJIB;
import static wrteam.multivendor.shop.helper.Constant.TAHUN;
import static wrteam.multivendor.shop.helper.Constant.TANGGAL_JATUH_TEMPO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wrteam.multivendor.shop.R;
import wrteam.multivendor.shop.helper.ApiConfig;
import wrteam.multivendor.shop.helper.Constant;
import wrteam.multivendor.shop.helper.Session;
import wrteam.multivendor.shop.model.Seller;
import wrteam.multivendor.shop.model.SimpananWajib;

public class RiwayatSimWajibFragment extends Fragment {
    Activity activity;
    Session session;
    View root;
    TextView pembayaranBulan,biaya,tglJatuhTempo,tglBayar,jmlBayar,statusPembayaran;
    Button btnBayar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_riwayat_sim_wajib, container, false);
        activity = getActivity();
        session = new Session(activity);
        setHasOptionsMenu(true);
        pembayaranBulan = root.findViewById(R.id.pembayaranBulan);
        biaya = root.findViewById(R.id.biaya);
        tglJatuhTempo = root.findViewById(R.id.tglJatuhTempo);
        tglBayar = root.findViewById(R.id.tglBayar);
        jmlBayar = root.findViewById(R.id.jmlBayar);
        statusPembayaran = root.findViewById(R.id.statusPembayaran);
        btnBayar = root.findViewById(R.id.btnBayar);

        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_SIMPANAN_WAJIB, "1");
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        SimpananWajib simpananWajib = new Gson().fromJson(jsonObject.getJSONArray(Constant.DATA).getJSONObject(0).toString(), SimpananWajib.class);
                        pembayaranBulan.setText(simpananWajib.getPeriod());
                        pembayaranBulan.setText(simpananWajib.getPeriod());
                        biaya.setText(simpananWajib.getAmount());
                        tglJatuhTempo.setText(simpananWajib.getBilling_date());
                        tglBayar.setText(simpananWajib.getPayment_date());
                        jmlBayar.setText(simpananWajib.getAmount());
                        String statusPayment = simpananWajib.getIs_status_payment();
                        if(statusPayment.equals("0")){
                            statusPembayaran.setText("Sukses");
                            btnBayar.setVisibility(View.GONE);
                            statusPembayaran.setTextColor(getResources().getColor(R.color.light_green));
                        }else if(statusPayment.equals("1")){
                            statusPembayaran.setText("Pending");
                            btnBayar.setVisibility(View.GONE);
                            statusPembayaran.setTextColor(getResources().getColor(R.color.active_yellow));
                        }else {
                            statusPembayaran.setText("Belum Bayar");
                            statusPembayaran.setTextColor(getResources().getColor(R.color.red));
                        }

                    }
//                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        },activity, Constant.GET_SIMPANAN_WAJIB_URL, params, true);
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
            return root;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Constant.TOOLBAR_TITLE = "Simpanan Wajib";
        activity.invalidateOptionsMenu();
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.toolbar_cart).setVisible(false);
        menu.findItem(R.id.toolbar_layout).setVisible(false);
        menu.findItem(R.id.toolbar_sort).setVisible(false);
        menu.findItem(R.id.toolbar_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void next(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        // Setting Dialog Message
        alertDialog.setTitle("Pembayaran Premium");
        alertDialog.setMessage("Mohon lakukan transaksi segera jika sudah menclick IYA");
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();

        // Setting OK Button
        alertDialog.setPositiveButton(R.string.yes, (dialog, which) -> {
            Map<String, String> params = new HashMap<>();
            params.put(Constant.TYPE, POST_SIMPANAN_WAJIB);
            params.put(Constant.USER_ID, session.getData(Constant.ID));

            Date c = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat period = new SimpleDateFormat("MMMM");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat years = new SimpleDateFormat("yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat clickDate = new SimpleDateFormat("yyyy-MM-dd");

            params.put(Constant.PEMBAYARAN_BULAN, period.format(c));
            params.put(Constant.TAHUN, years.format(c));
            params.put(Constant.TANGGAL_JATUH_TEMPO, clickDate.format(c));
            params.put(Constant.STATUS_PEMBAYARAN,"1");
            params.put(Constant.BIAYA, "100000");


            ApiConfig.RequestToVolley((result, response) -> {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(Constant.ERROR)) {
//                            SimpananWajib simpananWajib = new Gson().fromJson(jsonObject.getJSONArray(Constant.DATA).getJSONObject(0).toString(), SimpananWajib.class);

                            SimpananWajib simpananWajib = new Gson().fromJson(jsonObject.toString(), SimpananWajib.class);

                            pembayaranBulan.setText(simpananWajib.getPeriod());
                            pembayaranBulan.setText(simpananWajib.getPeriod());
                            biaya.setText(simpananWajib.getAmount());
                            tglJatuhTempo.setText(simpananWajib.getBilling_date());
                            tglBayar.setText(simpananWajib.getPayment_date());
                            jmlBayar.setText(simpananWajib.getAmount());
                            String statusPayment = simpananWajib.getIs_status_payment();
                            if(statusPayment.equals("0")){
                                statusPembayaran.setText("Sukses");
                                btnBayar.setVisibility(View.GONE);
                                statusPembayaran.setTextColor(getResources().getColor(R.color.light_green));
                            }else if(statusPayment.equals("1")){
                                statusPembayaran.setText("Pending");
                                btnBayar.setVisibility(View.GONE);
                                statusPembayaran.setTextColor(getResources().getColor(R.color.active_yellow));
                            }else {
                                statusPembayaran.setText("Belum Bayar");
                                statusPembayaran.setTextColor(getResources().getColor(R.color.red));
                            }

                        }
//                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            },activity, Constant.POST_SIMPANAN_WAJIB_URL, params, true);

        });
        alertDialog.setNegativeButton(R.string.no, (dialog, which) -> alertDialog1.dismiss());
        // Showing Alert Message
        alertDialog.show();
    }
}
