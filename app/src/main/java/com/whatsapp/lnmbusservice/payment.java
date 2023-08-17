package com.whatsapp.lnmbusservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class payment extends AppCompatActivity implements PaymentResultListener {

    TextView arrival, desti, time;
    Button button, upi_btn;

    int st;

    String Arrival,Dept,Times,ID;

    final int UPI_PAYMENT = 0;

    DatabaseReference databaseReference;

    FirebaseDatabase firebaseDatabase;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        databaseReference = FirebaseDatabase.getInstance().getReference("Bus_Details").child("Bus "+ID);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){

                    Bus bs =dataSnapshot.getValue(Bus.class);
                    String ans = bs.getSeats();
                    st = Integer.valueOf(ans);
                    st = st+1;
                    String seams = String.valueOf(st);

                    databaseReference
                            .child("seats")
                            .setValue(seams);

                }
            }
        });

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        button = findViewById(R.id.pay_razor);
        upi_btn = findViewById(R.id.pay_upi);

        arrival = findViewById(R.id.pay_arrival);
        desti = findViewById(R.id.pay_dept);
        time = findViewById(R.id.pay_time);

        Arrival = getIntent().getStringExtra("Arrival_next");
        Dept = getIntent().getStringExtra("Dept_next");
        Times = getIntent().getStringExtra("Time_next");
        ID = getIntent().getStringExtra("ID_next");

        arrival.setText(Arrival);
        desti.setText(Dept);
        time.setText(Times);

//        final String[] seat = new String[1];
        databaseReference = FirebaseDatabase.getInstance().getReference("Bus_Details").child("Bus "+ID);
       databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
           @Override
           public void onSuccess(DataSnapshot dataSnapshot) {
               if(dataSnapshot!=null){

                   Bus bs =dataSnapshot.getValue(Bus.class);
                   String ans = bs.getSeats();
                   st = Integer.valueOf(ans);
                   st = st-1;
                   String seams = String.valueOf(st);

                   databaseReference
                           .child("seats")
                           .setValue(seams);

               }
           }
       });


        Checkout.preload(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        upi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingUpi("Ayush Jain", "7891220133@ybl", "Bus Ticket", "25");
            }
        });
    }

    private void payUsingUpi(String name,String upiId, String note, String amount) {

        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(payment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(payment.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void startPayment(){


        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_OAm1ZYYNzxYxbb");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "LNMIIT");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "2500");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","7891220133");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {



        Log.d("onSucess", "onPaymentSuccess: "+ s);
        Toast.makeText(payment.this, "Payment Successfull", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(payment.this, confirmation.class);
        intent.putExtra("Arrival_conf", Arrival);
        intent.putExtra("Dept_conf", Dept);
        intent.putExtra("Time_conf", Times);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(payment.this, "Payment Failed", Toast.LENGTH_SHORT).show();

        Log.d("onError", "onPaymentError: "+ s);

        databaseReference = FirebaseDatabase.getInstance().getReference("Bus_Details").child("Bus "+ID);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){

                    Bus bs =dataSnapshot.getValue(Bus.class);
                    String ans = bs.getSeats();
                    st = Integer.valueOf(ans);
                    st = st+1;
                    String seams = String.valueOf(st);

                    databaseReference
                            .child("seats")
                            .setValue(seams);

                }
            }
        });

        Intent intent = new Intent(payment.this, bookBus.class);
        startActivity(intent);

    }
}