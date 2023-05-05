package com.example.mexpenses;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mexpenses.databinding.FragmentVerifyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyFragment extends Fragment {
    FragmentVerifyBinding binding;
    String mverificationId, phoneNumber;
    ProgressDialog progressDialog;
    PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    public VerifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVerifyBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        mverificationId = getArguments().getString("Id");
        phoneNumber =  getArguments().getString("phoneNumber");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading!");

        binding.buttonVerify.setOnClickListener(v -> this.VerifyOtp());
        binding.phoneNumber.setText(phoneNumber);
        binding.sendAgain.setOnClickListener(v -> this.sendAgain());
        return binding.getRoot();
    }

    private void sendAgain() {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mverificationId = verificationId;
                mforceResendingToken = forceResendingToken;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setForceResendingToken(mforceResendingToken)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void VerifyOtp() {
        if (binding.editOtp.getText().toString().equals(Constants.EMPTY)){
            Toast.makeText(getContext(), "You have to type the phone number!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverificationId, binding.editOtp.getText().toString());
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            Toast.makeText(getContext(), "Successful!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Navigation.findNavController(getView()).navigate(R.id.mainFragment);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}