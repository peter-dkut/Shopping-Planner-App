package com.example.trial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import Models.Data;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import Models.Data;

public class Home extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mauth;
    private DatabaseReference mydatabase;
    private RecyclerView recyclerView;
    private  String Editedtype;
    private String Editednote;
    private  String pkey;
    private int EditedAmount;
    TextView totals;
    FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Shopping App");
        totals = findViewById(R.id.totals);
        mauth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance().getReference().child("Shopping list").child( mauth.getCurrentUser().getUid());
        add = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Data>options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(mydatabase,Data.class).build();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
        options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(mydatabase, Data.class).build();
        adapter = new FirebaseRecyclerAdapter<Data, MyviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyviewHolder holder, final int position, @NonNull final Data model) {
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editedtype = model.getType();
                        EditedAmount = model.getAmount();
                        Editednote = model.getNote();
                        pkey = getRef(position).getKey();
                        update();
                    }
                });
                holder.amount.setText(String.valueOf(model.getAmount()));
                holder.type.setText(model.getType());
                holder.note.setText(model.getNote());
                holder.date.setText(model.getDate());
            }

            @NonNull
            @Override
            public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsexample, parent, false);
                return new MyviewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private FirebaseRecyclerOptions<Data> options;
    private FirebaseRecyclerAdapter<Data,MyviewHolder> adapter;



    private void alertDialog() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater = LayoutInflater.from(Home.this);
        View myview = layoutInflater.inflate(R.layout.inputdialog, null);
        final AlertDialog dialog = mydialog.create();
        dialog.setView(myview);
        final EditText type, amount, note;
        Button save;
        type = myview.findViewById(R.id.edittype);
        amount = myview.findViewById(R.id.editamount);
        note = myview.findViewById(R.id.editnote);
        save = myview.findViewById(R.id.add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mtype = type.getText().toString();
                String mamount = amount.getText().toString();
                String mnote = note.getText().toString();
                String id = mydatabase.push().getKey();
                int amountn = Integer.parseInt(mamount);
                String date = DateFormat.getDateInstance().format(new Date());
                if (TextUtils.isEmpty(mtype)) {
                    type.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(mamount)) {
                    amount.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(mnote)) {
                    note.setError("Required Field");
                    return;
                }
                dialog.dismiss();
                Data data = new Data(mtype, amountn, mnote, date, id);
                mydatabase.child(id).setValue(data);
                Toast.makeText(getApplication(), "Sucess", Toast.LENGTH_SHORT).show();
                mydatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        {
                            int Totalamount = 0;
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Data data = snap.getValue(Data.class);
                                Totalamount += data.getAmount();
                                String stotal = String.valueOf(Totalamount);
                                totals.setText(stotal);
                            }

                        }
                        ;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        dialog.show();
    }

//view holder class

    public  void update()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        View mview = inflater.inflate(R.layout.update_input,null);
        final AlertDialog dialog = mydialog.create();
        dialog.setView(mview);
        final EditText mtype =  mview.findViewById(R.id.updatetype);
        final EditText mamount = mview.findViewById(R.id.updateamount);
        final EditText updatenote = mview.findViewById(R.id.updatenote);
        Button update = mview.findViewById(R.id.update);
        Button delete = mview.findViewById(R.id.delete);
        mtype.setText(Editedtype);
        mtype.setSelection(Editedtype.length());
        mamount.setText(String.valueOf(EditedAmount));
        mamount.setSelection(String.valueOf(EditedAmount).length());
        updatenote.setText(Editednote);
        updatenote.setSelection(Editednote.length());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editednote = updatenote.getText().toString();
                Editedtype = mtype.getText().toString();
                String amount = String.valueOf(EditedAmount);
                amount = mamount.getText().toString();
                int mount = Integer.parseInt(amount);
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(Editedtype,mount,Editednote,date,pkey);
                mydatabase.child(pkey).setValue(data);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydatabase.child(pkey).removeValue();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainone,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                mauth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
