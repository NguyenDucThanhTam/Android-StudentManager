package com.example.mrrs.mob402_asm_ps05854;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {
    private static final String URL_ADDRESS ="http://10.200.200.85:3000";

            ListView lvCustomer;
    Button btnAdd , btnDel;
    Dialog dialog, dialog_del;
    ArrayList<Customer> arrayCustomer;
    ListAdapter adapter;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(URL_ADDRESS);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private Emitter.Listener remove = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("[Data]", args[0].toString()+"");
            String data =  args[0].toString();
//
            if(data == "true"){
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();

            }else{
//                Log.d("CAN NOT LOGIN", "can' login");
                Toast.makeText(getApplicationContext(), "Can't Delete", Toast.LENGTH_SHORT).show();
            }
//
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickAdd();
            }
        });

        arrayCustomer = new ArrayList<Customer>();
        updateList();


        lvCustomer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = arrayCustomer.get(i).Code;
                            arrayCustomer.remove(i);
                            adapter.notifyDataSetChanged();
                            removeStudent(id);
                return false;
            }
        });



    }

    private void clickAdd() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("ADD");
        final EditText edName, edEmail, edSdt, edGender, edCode;
        edName = (EditText)dialog.findViewById(R.id.edtName);
        edEmail = (EditText)dialog.findViewById(R.id.edtEmail);
        edSdt = (EditText)dialog.findViewById(R.id.edtSdt);
        edGender = (EditText)dialog.findViewById(R.id.edtGender);
        edCode = (EditText)dialog.findViewById(R.id.edtCode);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name, email, sdt, gender,code;
                name = edName.getText().toString();
                email = edEmail.getText().toString();
                sdt = edSdt.getText().toString();
                gender = edGender.getText().toString();
                code = edCode.getText().toString();

                final Customer customer = new Customer(name , code, email, sdt, gender);

                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Name: "+name +"\n" +"Code: "+code+"\n"+ "Email: "+email + "\n" + "Phone: "+sdt + "\n" + "Gender: "+gender, Toast.LENGTH_SHORT).show();
                registerUser(name , code, email, sdt, gender);
                arrayCustomer.add(customer);
                updateList();
            }
        });
        dialog.show();
    }

    private void registerUser(String name,String code,  String email, String sdt, String gender) {
        mSocket.emit("client-addCust", name,code, email, sdt, gender);
    }

    private void removeStudent(String position){
        mSocket.emit("remove-students", position);
    }
    private void init(){
        lvCustomer = (ListView) findViewById(R.id.listViewCustomer);
        btnAdd = (Button) findViewById(R.id.btnAdd);
    }
    public void updateList(){
        adapter = new ListAdapter(MainActivity.this, R.layout.layout_custom, arrayCustomer);
        lvCustomer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
