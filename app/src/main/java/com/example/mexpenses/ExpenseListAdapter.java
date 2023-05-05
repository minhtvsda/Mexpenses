package com.example.mexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpenses.data.ExpenseEntity;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.databinding.ListExpenseBinding;

import java.util.List;

public class ExpenseListAdapter extends
                RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    public interface ListExpenseListener{
        void onItemClick(int expenseId);
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{

            private final ListExpenseBinding expenseViewBinding;

            public ExpenseViewHolder(View itemView) {
                super(itemView);
                expenseViewBinding = ListExpenseBinding.bind(itemView);
            }

            public void bindData(ExpenseEntity eData){
                expenseViewBinding.expenseType.setText(eData.getType());
                expenseViewBinding.getRoot()
                        .setOnClickListener(v -> listener.onItemClick(eData.getId()));
            }
        }


    private List<ExpenseEntity> expenseList;
    private ListExpenseListener listener;

    public ExpenseListAdapter(List<ExpenseEntity> expenseList, ListExpenseListener listener) {
        this.listener = listener;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_expense,parent,false);

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseEntity eData = expenseList.get(position);
        holder.bindData(eData);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
