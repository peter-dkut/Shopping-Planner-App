package com.example.trial2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyviewHolder extends RecyclerView.ViewHolder {

    TextView date,note,type,amount;
    View view;
    public MyviewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        note =itemView.findViewById(R.id.note);
        type = itemView.findViewById(R.id.type);
        amount = itemView.findViewById(R.id.amount);
        view = itemView;
    }
}
