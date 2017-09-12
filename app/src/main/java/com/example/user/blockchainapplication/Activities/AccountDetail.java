package com.example.user.blockchainapplication.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.example.user.blockchainapplication.NetworkOperation.HttpHandler;
import com.example.user.blockchainapplication.R;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class AccountDetail extends AppCompatActivity {
    String filePath;
    String password;
    String fileName;
    HttpHandler httpHandler;
    EditText accAddress;
    String myAddres;
    EditText accBalance;
    Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        httpHandler = HttpHandler.getInstance();
        new loadAccountDetail().execute();
        filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        getIntentData();
        initializedView();
    }
    public void initializedView(){
        accAddress = (EditText) findViewById(R.id.et_Adress);
        accBalance = (EditText) findViewById(R.id.et_balance );
    }

    public class loadAccountDetail extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AccountDetail.this, "Please wait...", "Retreiving account detail ...", true);

        }

        @Override
        protected String doInBackground(String... String) {
            filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";

            try {
                credentials = httpHandler.loadCredentialss(password, filePath + "/" + fileName);
                myAddres = httpHandler.getWalletAddress();
                return myAddres;

            } catch (IOException e) {
                e.getMessage();
            } catch (CipherException e) {
                e.getMessage();
            }
            return null;

        }

        protected void onPostExecute(String result) {
            accAddress.setText(myAddres);
            try {
                accBalance.setText( Convert.toWei(httpHandler.getBalances(myAddres)
                        + "", Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")).toString());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
    /** we get data from GenerateWallet Activity*/
       public void getIntentData(){
            Intent intent = getIntent();
            fileName = intent.getStringExtra("FILENAME");
            password= intent.getStringExtra("PASSWORD");

    }
}