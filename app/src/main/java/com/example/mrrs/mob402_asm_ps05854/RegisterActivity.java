package com.example.mrrs.mob402_asm_ps05854;

import android.app.Activity;
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

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String URL_ADDRESS = "http://10.200.200.85:3000";
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

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
        setContentView(R.layout.activity_register);

        mSocket.connect();

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mSocket.on("client-register", onRegister);

//        mSocket.on("client-register-result", onFalseResult);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String name, final String email,
                              final String password) {


        mSocket.emit("client-register", name, password, email);

        pDialog.setMessage("Registering ...");
        showDialog();
    }

//    private Emitter.Listener onFalseResult = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            JSONObject data = (JSONObject) args[0];
//            try {
//                String result = data.getString("result");
//                if(result == "true"){
//                    hideDialog();
//                    Toast.makeText(getApplicationContext(), "Register successful!", Toast.LENGTH_SHORT).show();
//                }else{
//                    hideDialog();
//                    Toast.makeText(getApplicationContext(), "Register failures!", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };

    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

            if(data == "true"){

                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Log.d("error", "cant register");
            }

            //   hideDialog();

        }
    };

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
}
