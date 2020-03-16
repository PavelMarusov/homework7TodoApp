package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

//    TODO Протестить все
    EditText phoneEdit, editCode;
    Button phoneBtn;
    PhoneAuthProvider.ForceResendingToken token;
    String sentCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("pop","PhoneActivity");
        setContentView(R.layout.activity_phone);
        phoneEdit = findViewById(R.id.phone_edit);
        phoneBtn = findViewById(R.id.phone_btn);
        editCode = findViewById(R.id.codeEdit);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                Log.d("pop","onVerificationCompleted");
                    editCode.setText(code);
                   signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d("pop","onCodeSent");
                sentCode = s;
                token = forceResendingToken;
            }
        };
    }



    private void verifySignInCode() {
        Log.d("pop","verifySignInCode");
        String code = editCode.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentCode
                , code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("pop","signInWithPhoneAuthCredential");
                            Toast.makeText(PhoneActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void onClickContinue(View view) {
        Log.d("pop","onClickContinue");
        String phone = phoneEdit.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);
    }

    public void onConfirm(View view) {
        verifySignInCode();
    }
}
