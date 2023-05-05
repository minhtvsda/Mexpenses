package com.example.mexpenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mexpenses.data.TripEntity;
import com.example.mexpenses.databinding.ListTripBinding;
import java.util.List;


public class TripListAdapter extends
                            RecyclerView.Adapter<TripListAdapter.TripViewHolder>
                            {


    public interface ListTripListener{
        void onItemClick(int tripId);
        void showImage(String url);
    }


    public class TripViewHolder
            extends RecyclerView.ViewHolder{


        private final ListTripBinding tripViewBinding;

        public TripViewHolder (View tripView){
            super(tripView);
            tripViewBinding = ListTripBinding.bind(tripView);

        }

        public void bindData(TripEntity tData){
            tripViewBinding.tripName.setText("Name: "+tData.getName());
            tripViewBinding.tripDate.setText("Date: "+ tData.getDot());
            tripViewBinding.tripDestination.setText("Destination: "+ tData.getDestination());
            tripViewBinding.imageView.setOnClickListener(v -> listener.showImage(tData.getImageUrl()));
            Glide.with(context2).load(tData.getImageUrl())
                            .error(R.drawable.ic_outline_card_travel_24).into(tripViewBinding.imageView);
            tripViewBinding.getRoot() // lay toan bo list trip.
                    .setOnClickListener(v -> listener.onItemClick(tData.getId()));
//getRoot nghia la lay toan bo UI, tao ra event click, cu moil an kick vao thang nghe se thuc hien onItemClick vs Name tData dang co.
        }

    }


    private List<TripEntity> tripList;

    private ListTripListener listener;
    private Context context, context2;

    public TripListAdapter(List<TripEntity> tripList, ListTripListener listener, Context context){
        this.listener = listener;
        this.tripList = tripList;
        this.context = context;
    }

    public void filterList(List<TripEntity> filterList){
        this.tripList = filterList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context2 = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_trip,parent,false);
        //meaning get everything from app
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) { //get the data
        TripEntity tData = tripList.get(position);
        holder.bindData(tData);
        //gan data vao holder
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

}
