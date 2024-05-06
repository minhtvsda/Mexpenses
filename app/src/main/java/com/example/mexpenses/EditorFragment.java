package com.example.mexpenses;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.databinding.FragmentEditorBinding;
import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditorFragment extends Fragment {

    private EditorViewModel mViewModel;
    private FragmentEditorBinding binding;
    Bitmap bitmap;




    public static EditorFragment newInstance() {
        return new EditorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        //get activity, app chinh la ca activity chua fragment  nay
        ActionBar ab = app.getSupportActionBar(); //lay phan giai mau tim

        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_baseline_save_alt_24);
        setHasOptionsMenu(true);

        Database db = RoomH.createDatabase(getContext());
        mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        mViewModel.setDatabase(db);

        binding = FragmentEditorBinding.inflate(inflater, container, false);



//        binding.name.setText(tripId);

        mViewModel.trip.observe(
                getViewLifecycleOwner(),
                te -> {
                    binding.name.setText(te.getName());
                    binding.description.setText(te.getDescription());
                    binding.dot.setText(te.getDot());
                    binding.rriskasses.setText(te.getRrisk());
                    binding.destination.setText(te.getDestination());
                    if (!te.getImageUrl().equals(Constants.EMPTY)){
                        Glide.with(getContext()).load(te.getImageUrl())
                                .error(R.drawable.ic_launcher_background).into(binding.imageView);
                    }

                    requireActivity().invalidateOptionsMenu(); // invalidate de refresh option menu
                    //no cung se goi den  onPrepareoptionmenu
                }
        );
        int tripId = getArguments().getInt("tripId");
        mViewModel.getTripByID(tripId);
        //Sau đó ta lấy dữ liệu thay đổi.

        binding.backButton.setOnClickListener(view -> Navigation.findNavController(getView()).navigateUp());

        List<String> tripName = Arrays.asList("Select trip","Client Meeting", "Android Conference", "Food", "Woman", "Other");

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, tripName);
        // create view for each of elements and display value
        aa.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); // create specify view for elements
        binding.Spinner.setAdapter(aa);
        binding.spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = binding.Spinner.getSelectedItem().toString();
                binding.name.setText(newName);
                Toast.makeText(getContext(), "You selected: " + newName, Toast.LENGTH_LONG).show();
                if (newName.equals("Other") || newName.equals("Select trip")){
                    binding.name.setText("");
                }
            }
        });

        List<String> tripRisk = Arrays.asList("Yes", "No");

        ArrayAdapter aa2 = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, tripRisk);
        aa2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        binding.Spinner2.setAdapter(aa2);
        binding.spBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newRisk = binding.Spinner2.getSelectedItem().toString();
                binding.rriskasses.setText(newRisk);
                Toast.makeText(getContext(), "You selected: " + newRisk, Toast.LENGTH_LONG).show();
            }
        });

        Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.dot.setOnClickListener(v -> {
            EditText etDate = binding.dot;
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                    month1 = month1 +1;
                    String date = day1 + "/" + month1 + "/"+ year1;
                    etDate.setText(date);
                    Toast.makeText(getContext(),"Select date successfully!", Toast.LENGTH_LONG).show();
                }
            }
                    ,year, month, day);
            datePickerDialog.show();
        });

        binding.showExpense.setOnClickListener(v -> this.showExpense(tripId));
        if (tripId == 0){
            binding.showExpense.setVisibility(View.INVISIBLE);
        }
        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });
        return binding.getRoot();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100  && data!= null && data.getExtras() != null){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            binding.imageView.setImageBitmap(bitmap);
        }
    }

    private void showExpense(int tripId) {
        Bundle bundle = new Bundle();
        bundle.putInt("tripId", tripId);
        Navigation.findNavController(getView()).navigate(R.id.expenseFragment, bundle);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) { //check or update
        super.onPrepareOptionsMenu(menu);
        TripEntity t = mViewModel.trip.getValue();
        if ( t != null
                && t.getId() == 0){     //neu id = 0
            menu.findItem(R.id.action_delete).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //kiem tra xem item nao dc an
        switch (item.getItemId()){
            case android.R.id.home:
                if (this.validate()) return showDialogSave();
                else return false;
            case R.id.action_delete:
                return deletethenReturn();

            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean validate() {
        EditText name = binding.name;
        EditText desti = binding.destination;
        EditText dot = binding.dot;
        EditText risk = binding.rriskasses;
        boolean Validated =  true;
        if (name.getText().toString().equals("other")){}

        if (name.getText().toString().equals(Constants.EMPTY)){
            name.setError("You must choose or type the name!!!");
            Validated = false;
        }

        if (desti.getText().toString().equals(Constants.EMPTY)){
            desti.setError("You must type the destination!!!");
            Validated = false;
        }
        if (dot.getText().toString().equals(Constants.EMPTY)){
            dot.setError("You must choose the date!!!");
            Validated = false;
        }

        if(!risk.getText().toString().trim().equals("Yes")
                && !risk.getText().toString().trim().equals("No")) {
            risk.setError("You must choose Yes or No for Risk!!!");
            Validated = false;
        }


        return Validated;

    }

    private boolean deletethenReturn() {
        Log.i(this.getClass().getName(), "delete then return");
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete trip");
        dialog.setMessage("Are you sure?");
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mViewModel.deleteTrip();
                Navigation.findNavController(getView()).navigateUp();
                Toast.makeText(getContext(), "Delete successfully!!!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
        return true;
    }
    boolean showDialogSave() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Save trip");
        dialog.setMessage("Are you sure?");
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savethenReturn();
            }
        });
        dialog.show();
        return true;
    }

    private boolean savethenReturn() {
        Log.i(this.getClass().getName(), "save then return");

//log la qua trinh ghe thong chay
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        int id = getArguments().getInt("tripId");
        //lay tu trip do id ko doi
        String name = binding.name.getText().toString(); //con lai lay tu UI

        String destination = binding.destination.getText().toString();

        String dot = binding.dot.getText().toString();
        String rriskasses = binding.rriskasses.getText().toString();
        String description = binding.description.getText().toString();

        if (bitmap != null){
            StorageReference imageRef = FirebaseStorage.getInstance().getReference("image/"+ new Date().getTime()+".png");

            binding.imageView.setDrawingCacheEnabled(true);
            binding.imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "Upload iamge failed!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photolink =uri.toString();
                            TripEntity updateTrip
                                    = new TripEntity(id,//neu id la null thi ta se lay const
                                    name, destination, dot, rriskasses, description, photolink);      // sau nay co the dung add book

                            mViewModel.updateTrip(updateTrip);
                            Toast.makeText(getContext(), "Save successfully!!!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Navigation.findNavController(getView()).navigateUp(); // 1 trong 3 cach de nhay giua cac fragment

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
            return true;
        }else {
            TripEntity updateTrip
                    = new TripEntity(id,//neu id la null thi ta se lay const
                    name, destination, dot, rriskasses, description, mViewModel.trip.getValue().getImageUrl());      // sau nay co the dung add book

            mViewModel.updateTrip(updateTrip);
            Toast.makeText(getContext(), "Save successfully!!!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            Navigation.findNavController(getView()).navigateUp(); // 1 trong 3 cach de nhay giua cac fragment

            return true;
        }

    }
}