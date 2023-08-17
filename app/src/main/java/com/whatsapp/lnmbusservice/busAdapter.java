package com.whatsapp.lnmbusservice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class busAdapter extends RecyclerView.Adapter<busAdapter.Holder> {

    Context context;
    ArrayList<Bus> list;

    public busAdapter(Context context, ArrayList<Bus> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showbus, parent, false);
        return new busAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Bus bus = list.get(position);

        holder.arrival.setText(bus.getArrival());
        holder.departure.setText(bus.getDestination());
        holder.time.setText(bus.getTime());
        holder.seat.setText(bus.getSeats());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bus.getSeats().equals("0")){
                    Toast.makeText(holder.arrival.getContext(), "Seats not Available", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(context, payment.class);
                    intent.putExtra("Arrival_next", bus.getArrival());
                    intent.putExtra("Dept_next", bus.getDestination());
                    intent.putExtra("Time_next", bus.getTime());
                    intent.putExtra("ID_next", bus.getId());
                    context.startActivity(intent);

                    list.clear();
                    notifyDataSetChanged();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView arrival, departure, time, seat;

        Button button;

        public Holder(View itemView) {
            super(itemView);

            arrival = itemView.findViewById(R.id.Arrival_bus);
            departure = itemView.findViewById(R.id.Depart_bus);
            time = itemView.findViewById(R.id.Time_bus);
            seat = itemView.findViewById(R.id.Seats_bus);

            button =itemView.findViewById(R.id.book_seat);
        }
    }
}
