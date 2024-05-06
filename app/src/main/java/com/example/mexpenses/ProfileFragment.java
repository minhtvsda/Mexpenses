package com.example.mexpenses;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mexpenses.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    Uri uri;
    FirebaseUser user;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container,false);

        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.profilePhone.setText("PhoneNumber: "+ user.getPhoneNumber());
        binding.profileName.setText(user.getDisplayName());
        binding.profileAvatar.setOnClickListener(v -> openGallery());
        binding.btnSaveProfile.setOnClickListener(v -> UpdateProfile());
        if (user.getPhotoUrl() != null){
            Glide.with(getContext())
                    .load(user.getPhotoUrl()).into(binding.profileAvatar);
        }
        binding.btnLogOut.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "Successful!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.signInFragment);
        });
        binding.backToMain.setOnClickListener(view -> Navigation.findNavController(getView()).navigateUp());

        return binding.getRoot();
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && data != null && data.getData() != null){
            uri = data.getData();
            binding.profileAvatar.setImageURI(uri);
        }
    }
    private void UpdateProfile() {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        if (uri != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.profileName.getText().toString())
                    .setPhotoUri(uri)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }else {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.profileName.getText().toString())
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}