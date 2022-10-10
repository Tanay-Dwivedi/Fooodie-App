package com.food.fooodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber;
    TextView OtpBtn;
    FirebaseAuth firebasemAuth;
    String strPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // This will remove the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        phoneNumber = findViewById(R.id.userPhoneNumber);
        OtpBtn = findViewById(R.id.sendOtpBtn);

        OtpBtn.setOnClickListener(view -> {

            strPhoneNumber = phoneNumber.getText().toString();

            if (TextUtils.isEmpty(strPhoneNumber)) {
                phoneNumber.setError("Invalid Phone Number");
                phoneNumber.requestFocus();
            } else {
                sendOtpVerificationCode(strPhoneNumber);
            }

        });

    }

    private void sendOtpVerificationCode(String strUserPhoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebasemAuth)
                        .setPhoneNumber("+91" + strUserPhoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Toast.makeText(LoginActivity.this, "Verification Code send", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, "Code not sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String otpFromFirebase, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            Toast.makeText(LoginActivity.this, "Verifying Account", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            intent.putExtra("mobileNo", strPhoneNumber);
            intent.putExtra("firebaseOtp", otpFromFirebase);
            startActivity(intent);
        }
    };

}