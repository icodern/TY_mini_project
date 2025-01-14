package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordVerifyOTP extends AppCompatActivity {
    String TAG = "ForgotPasswordVerifyOTP";
    String verificationId, phoneNo;
    Button btn_verifyOTP;
    EditText et_otp;
    ProgressBar progressBar;
    String checkUserType;
    LinearLayout btn_resendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_verify_o_t_p);
        progressBar = findViewById(R.id.progressbar);

        btn_resendOTP = findViewById(R.id.btn_resendOTP);
        btn_verifyOTP = findViewById(R.id.btn_verifyOTP);
        et_otp = findViewById(R.id.et_otp);


        if (getIntent() != null) {

            phoneNo = getIntent().getStringExtra("mobile");
            verificationId = getIntent().getStringExtra("verificationId");
            checkUserType = getIntent().getStringExtra("checkUserType");
        }

        Log.d(TAG, "inside onCreate: phoneNo : " + phoneNo);
        Log.d(TAG, "inside onCreate: verificationId : " + verificationId);
        Log.d(TAG, "inside onCreate : checkUserType---" + checkUserType);

        btn_verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOTP = et_otp.getText().toString();
                if (userOTP.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "enter OTP", Toast.LENGTH_LONG).show();
                    et_otp.setError("enter Valid Otp");
                } else {
                    if (verificationId != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        btn_verifyOTP.setVisibility(View.GONE);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);

                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        btn_verifyOTP.setVisibility(View.VISIBLE);
                                        if (task.isSuccessful()) {
                                            if (checkUserType.equals("customer")) {
                                                Toast.makeText(getApplicationContext(), "Verification completed", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), ForgotResetPassword.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.putExtra("mobno", phoneNo);
                                                intent.putExtra("customer", "customer");
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Verification completed", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), ForgotResetPassword.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.putExtra("mobno", phoneNo);
                                                //  intent.putExtra("messOwner", "messOwner");
                                                startActivity(intent);
                                                finish();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "enter Valid OTP", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                    }
                }
            }
        });


        btn_resendOTP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.e(TAG, "inside btn_resendOTP method : ");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNo,
                        60,
                        TimeUnit.SECONDS,
                        ForgotPasswordVerifyOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                btn_verifyOTP.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                               /* Log.e(TAG,"inside onVerificationFailed : " + e.getMessage());
                                Toast.makeText(GenerateOTP.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();
*/
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                btn_verifyOTP.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Log.e(TAG, "inside onVerificationFailed : " + e.getMessage());
                                Toast.makeText(ForgotPasswordVerifyOTP.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String NEWverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = NEWverificationId;
                                Toast.makeText(ForgotPasswordVerifyOTP.this, "OTP sent  ", Toast.LENGTH_LONG).show();

                            }
                        }
                );
            }
        });
    }


    public void onCancel(View view) {
        Intent intent = new Intent(ForgotPasswordVerifyOTP.this, ForgotPasswordGenerateOTP.class);
        startActivity(intent);
        finish();
    }
}