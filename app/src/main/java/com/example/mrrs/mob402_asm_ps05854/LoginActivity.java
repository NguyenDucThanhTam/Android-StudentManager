package com.example.mrrs.mob402_asm_ps05854;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class LoginActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private  static final String URL_ADDRESS = "http://10.200.200.85:3000";

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("[Data]", args[0].toString()+"");
            String data =  args[0].toString();
//
            if(data == "true"){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Log.d("CAN LOGIN", "can' login");

            }else{
//                Log.d("CAN NOT LOGIN", "can' login");
                hideDialog();
//                Toast.makeText(this, "Wrong password!!!", Toast.LENGTH_SHORT).show();
            }
//
//            hideDialog();
        }
    };


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(URL_ADDRESS);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSocket.on("client-login", onLogin);
        mSocket.connect();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });





    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        try{
            pDialog.setMessage("Logging in ...");
            showDialog();
            mSocket.emit("client-login", email, password);
        }
        catch (Exception err){
            Log.e("[error login]",err+"");
        }

    }


    private void showDialog() {
        try{

            if (!pDialog.isShowing())
                            pDialog.show();

        }
        catch (Exception err){
            Log.e("[error show dialog]",err+"");
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}