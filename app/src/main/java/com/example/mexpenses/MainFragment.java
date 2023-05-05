package com.example.mexpenses;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.databinding.FragmentMainBinding;
import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainFragment extends Fragment
                    implements TripListAdapter.ListTripListener {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;
    private TripListAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // an action bar
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        //Do mainfrag vs editor chung activity, do đó khi ta kích hoạt save ở editor thì ở frag cũng có.
        //Nên ta phải ẩn save đi ở mainF;
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Database db = RoomH.createDatabase(getContext());

        mViewModel.setDatabase(db);

        binding = FragmentMainBinding.inflate(inflater,container,false);
//        return inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext()).getOrientation())
        ));

        mViewModel.getData().observe( //watch for the change
                getViewLifecycleOwner(),//app
                tripList -> {
                    adapter = new TripListAdapter(tripList, this, getContext());
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                });

        binding.fabaddTrip.setOnClickListener(v -> this.onItemClick(0));
        binding.Delete.setOnClickListener(v ->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Delete all database");
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
                    deleteDB(db);
                }
            });
            dialog.show();
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button even
                Toast.makeText(getContext(), "You are in the main screen!", Toast.LENGTH_SHORT).show();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return binding.getRoot();

    }
    private void deleteDB(Database db){
        db.createTripDao().deleteAllExpense();
        db.createTripDao().deleteAllTrip();
        Toast.makeText(getContext(), "Successful!", Toast.LENGTH_SHORT).show();
        mViewModel.getData();
    }

    @Override
    public void onItemClick(int tripId) {
            Bundle bundle = new Bundle();
            bundle.putInt("tripId", tripId);
            Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
        }

    @Override
    public void showImage(String url) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.show_image);
        Glide.with(getContext())
                .load(url).error(R.drawable.ic_outline_card_travel_24).into((ImageView) dialog.findViewById(R.id.showImage));
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile){
            Navigation.findNavController(getView()).navigate(R.id.profileFragment);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search); // get our menu item.
        //Interface for direct access to a previously created menu item.
        SearchView sv= (SearchView) MenuItemCompat.getActionView(menuItem);// getting search view of our item.
        sv.setMaxWidth(Integer.MAX_VALUE);
        // below line is to call set on query text listener method.
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }
    public void filter(String text){
        List<TripEntity> filtered = new ArrayList<TripEntity>();
        for (TripEntity te : mViewModel.tripList.getValue()){
            if (te.getName().toUpperCase().contains(text.toUpperCase())
             ||te.getDestination().toUpperCase().contains(text.toUpperCase())
             ||te.getDot().toUpperCase().contains(text.toUpperCase())){
                filtered.add(te);
            }
        }
        adapter.filterList(filtered);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getData();
    }
}