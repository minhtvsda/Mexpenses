package com.example.mexpenses;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.databinding.FragmentEditorexBinding;
import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditorexFragment extends Fragment {

    private EditorexViewModel mViewModel;
    private FragmentEditorexBinding binding;

    public static EditorexFragment newInstance() {
        return new EditorexFragment();
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

        mViewModel = new ViewModelProvider(this).get(EditorexViewModel.class);

        mViewModel.setDatabase(db);

        binding = FragmentEditorexBinding.inflate(inflater, container, false);

        int expenseId = getArguments().getInt("expenseId");

        mViewModel.expense.observe(
                getViewLifecycleOwner(),
                ee -> {
                    binding.type.setText(ee.getType());
                    binding.time.setText(ee.getTime());
                    binding.comments.setText(ee.getComments());
                    binding.amount.setText(ee.getAmount());

                    requireActivity().invalidateOptionsMenu();// invalidate de refresh option menu
                    //no cung se goi den  onPrepareoptionmenu
                }
        );
        mViewModel.getExpenseById(expenseId);

        List<String> expenseType = Arrays.asList("travel","food","woman","medicine", "other");

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, expenseType);
        aa.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.Spinner.setAdapter(aa);
        binding.spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = binding.Spinner.getSelectedItem().toString();
                binding.type.setText(newName);
                if (newName.equals("other")){
                    binding.type.setText("");
                }
                Toast.makeText(getContext(), "You selected: " + newName, Toast.LENGTH_LONG).show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.time.setOnClickListener(v -> {
            EditText etDate = binding.time;
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                    month1 = month1 +1;
                    String date = day1 + "/" + month1 + "/"+ year1;
                    etDate.setText(date);
                    Toast.makeText(getContext(),"Select date successfully!", Toast.LENGTH_LONG).show();
                }

            },year, month, day);
                datePickerDialog.show();
        });

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        ExpenseEntity e = mViewModel.expense.getValue();
        if (e !=null
                && e.getId() == 0){
                menu.findItem(R.id.action_delete).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (this.validate())
                return savethenReturn();
                        else return false;
            case R.id.action_delete:
                return deletethenReturn();
            default:return super.onOptionsItemSelected(item);}
    }

    private boolean validate() {
        boolean Validated =  true;

        if (binding.type.getText().toString().equals(Constants.EMPTY)){
            binding.type.setError("You must select type!!!");
            Validated = false;
        }
        if (binding.amount.getText().toString().equals(Constants.EMPTY)){
            binding.amount.setError("You must type the amount!!!");
            Validated = false;
        }
        if (binding.time.getText().toString().equals(Constants.EMPTY)){
            binding.time.setError("You must select the time!!!");
            Validated = false;
        }
        return Validated;
    }

    private boolean deletethenReturn() {
        mViewModel.deleteExpense();
        Navigation.findNavController(getView()).navigateUp();
        Toast.makeText(getContext(), "Delete successfully!!!", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean savethenReturn() {
        Log.i(this.getClass().getName(), "save then return");

        int id = getArguments().getInt("expenseId");

        int tripId = getArguments().getInt("tripId");

        String type = binding.type.getText().toString();
        String amount = binding.amount.getText().toString();
        String time = binding.time.getText().toString();
        String comments = binding.comments.getText().toString();

        ExpenseEntity updateExpense =
                new ExpenseEntity(id,tripId, type, amount,time,comments, "");

        mViewModel.updateExpense(updateExpense);

        Navigation.findNavController(getView()).navigateUp();
        Toast.makeText(getContext(), "Save successfully!!!", Toast.LENGTH_LONG).show();
        return true;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}