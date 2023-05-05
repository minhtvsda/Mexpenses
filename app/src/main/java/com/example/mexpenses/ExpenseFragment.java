package com.example.mexpenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mexpenses.databinding.FragmentExpenseBinding;
import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.ListExpenseListener {

    private ExpenseViewModel mViewModel;
    private FragmentExpenseBinding binding;
    private ExpenseListAdapter adapter;

    public static ExpenseFragment newInstance() {
        return new ExpenseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity aca = (AppCompatActivity) getActivity();
        //Do mainfrag vs editor chung activity, do đó khi ta kích hoạt save ở editor thì ở frag cũng có.
        //Nên ta phải ẩn save đi ở mainF;
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        Database db = RoomH.createDatabase(getContext());
        mViewModel.setDatabase(db);

        binding = FragmentExpenseBinding.inflate(inflater, container, false);

        int tripId = getArguments().getInt("tripId");
        mViewModel.setView(binding.getRoot());
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext()).getOrientation())
        ));

        mViewModel.getData(tripId).observe(
                getViewLifecycleOwner(),
                expenseList -> {
                    adapter = new ExpenseListAdapter(expenseList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                });
        binding.fabaddExpense.setOnClickListener(v -> this.onItemClick(0));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        mViewModel.setController(navController);
    }

    @Override
    public void onItemClick(int expenseId) {
        mViewModel.onItemClick(expenseId, getArguments().getInt("tripId"));
    }
}