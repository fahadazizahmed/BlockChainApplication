package com.example.user.blockchainapplication.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.blockchainapplication.NetworkOperation.HttpHandler;
import com.example.user.blockchainapplication.R;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class WalletLogin extends AppCompatActivity {
    EditText passwords;
    EditText renterPassword;
    TextView checkPassowrd;
    Button login;
    String password;
    String renter_Passowrds;
    String myAddress;
    String filePath;
    String accBalance;
    HttpHandler httpHandler;
    Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_login);
        initializedVIew();
        httpHandler = HttpHandler.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });
    }

    /** This method check your wallet password wheter you enter the right wallet password  */
    public void checkPassword() {
        password = passwords.getText().toString();
        renter_Passowrds = renterPassword.getText().toString();
        if (password.equals("fahad")&&renter_Passowrds.equals("fahad")) {
            checkPassowrd.setVisibility(View.INVISIBLE);
            new loadAccountDetail().execute();
        }
        else {
            checkPassowrd.setText("Password Not Matched");
            Toast.makeText(WalletLogin.this, " Not matched", Toast.LENGTH_SHORT).show();
        }
    }

    /** we get all our view in this method  */
    public void initializedVIew() {
        passwords = (EditText) findViewById(R.id.et_password);
        renterPassword = (EditText) findViewById(R.id.et_renterPassword);
        checkPassowrd = (TextView) findViewById(R.id.tx_chkPassword);
        login = (Button) findViewById(R.id.btn_login);
    }

    /** This method work in background basically give you the detail of your account against your password it give account address and balance in this account
     * @param Void which we passed in when call this class we passed nothing that's why void is first
     * @param Integer give your progress detail but in this we not give progress update
     * @param Void it is the return we return nothing that's why Void

     */

    public class loadAccountDetail extends AsyncTask<Void,Integer,Void> {
        ProgressDialog progressDialog;
        protected  void onPreExecute() {
            progressDialog = ProgressDialog.show(WalletLogin.this,"Please wait...", "Retreiving account detail ...", true);
        }

        @Override
        protected Void doInBackground(Void... Voids) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            try {
                /**@param pass your wallet file password
                 * @param filePath your wallet file path
                 * 3rd @param   name of your wallet file
                 */
                credentials =    httpHandler.loadCredentialss(password,filePath + "/" + "UTC--2017-09-01T08-23-25.280--5003a1b9f7dc462ecb7eae4ebe4d7d5591ddf8fc.json");
                myAddress  =  httpHandler.getWalletAddress();
            }
            catch (IOException e) {
                e.getMessage();
            } catch (CipherException e) {
                e.getMessage();
            }
            return null;
        }

        protected void onPostExecute(String result) throws ExecutionException, InterruptedException {
            accBalance = Convert.toWei(httpHandler.getBalances(myAddress)
                    + "", Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")).toString();
            progressDialog.dismiss();
            sentIntent();
        }
        /** In this method we sent account balance password and address that we get in this activity passeed to the next activity*/
        public void sentIntent() {
            Intent intent = new Intent (WalletLogin.this, SendEther.class);
            intent.putExtra("ACC_ADDRESS",  myAddress);
            intent.putExtra("ACC_BALANCE",accBalance);
            intent.putExtra("PASSWORD",password);
            startActivity(intent);

        }
    }

}




















