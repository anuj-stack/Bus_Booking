package com.whatsapp.lnmbusservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bus> list;

    public MyAdapter(Context context, ArrayList<Bus> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busentry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {

        Bus bus = list.get(position);

        holder.Id.setText(bus.getId());
        holder.arrival.setText(bus.getArrival());
        holder.departure.setText(bus.getDestination());
        holder.time.setText(bus.getTime());
        holder.seat.setText(bus.getSeats());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.arrival.getContext());
                builder.setTitle("Are You Sure ?");
                builder.setMessage("Deleted Data can't be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


//                        String id = bus.getId().toString();
//
                        //buss
                        FirebaseDatabase.getInstance().getReference("Bus_Details")
                                .child("Bus " + bus.getId())
                                .removeValue();
                        list.clear();
                        notifyDataSetChanged();

                        Toast.makeText(holder.arrival.getContext(), "Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.arrival.getContext(), "Cancelled Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Id, arrival, departure, time, seat;

        Button button;

        public MyViewHolder(View itemView) {
            super(itemView);

            Id = itemView.findViewById(R.id.busId);
            arrival = itemView.findViewById(R.id.busArrival);
            departure = itemView.findViewById(R.id.busDeparture);
            time = itemView.findViewById(R.id.busTime);
            seat = itemView.findViewById(R.id.busSeats);

            button =itemView.findViewById(R.id.btn_delete);
        }
    }
}
