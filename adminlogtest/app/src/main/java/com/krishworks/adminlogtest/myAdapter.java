package com.krishworks.adminlogtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class myAdapter extends FirestoreRecyclerAdapter<Note, myAdapter.myHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public myAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myHolder holder, int position, @NonNull Note model) {

        //holder.id.setText(String.valueOf(model.getId()));
        holder.mac.setText(model.getMac());
        holder.imei.setText(model.getImei());
        holder.space.setText(model.getSpace());
        holder.timestamp.setText(model.getTimestamp());

    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new myAdapter.myHolder(v);

    }

    class myHolder extends RecyclerView.ViewHolder{


        TextView mac, imei, timestamp, space, id;
        public myHolder( View itemView) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.tv1);
            space = itemView.findViewById(R.id.tv3);
            //id = itemView.findViewById(R.id.tv2);
            mac = itemView.findViewById(R.id.tv4);
            imei = itemView.findViewById(R.id.tv5);

        }
    }
}
