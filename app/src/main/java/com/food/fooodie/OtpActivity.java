package com.food.fooodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class OtpActivity extends AppCompatActivity {

    EditText otpEnteredByTheUser;
    TextView confirmingTheOtp;
    String gettingUserPhoneNumber, gettingFirebaseOtp, strFullOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpEnteredByTheUser = findViewById(R.id.userOTP);
        confirmingTheOtp = findViewById(R.id.confirmOtpBtn);
        gettingUserPhoneNumber = getIntent().getStringExtra("phoneNumberDigits");
        gettingFirebaseOtp = getIntent().getStringExtra("firebaseOtp");

        confirmingTheOtp.setOnClickListener(view -> {

            strFullOtp = otpEnteredByTheUser.getText().toString().trim();
            if (!TextUtils.isEmpty(strFullOtp)) {
                if (gettingFirebaseOtp != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(gettingFirebaseOtp, strFullOtp);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
                        Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                        Toast.makeText(OtpActivity.this, "Verification successful \n User Registered", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(OtpActivity.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(OtpActivity.this, "Error! Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                otpEnteredByTheUser.setError("Invalid OTP");
                otpEnteredByTheUser.requestFocus();
            }

        });

    }
}