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
import org.web3j.crypto.WalletUtils;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class GenerateWallet extends AppCompatActivity {

    EditText password;
    EditText renterPassword;
    TextView checkPassowrd;
    TextView accountAdress;
    Button generateWallet;
    Button sendEhter;
    String myAddress;
    String passwords;
    String fileName;
    String filePath;
    String renter_Passowrds;
    Credentials credentials = null;
    Button checkAccDetail;
    HttpHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_wallet);
        initializedVIew();
        httpHandler = HttpHandler.getInstance();

        generateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();


            }
        });
        checkAccDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentIntent();

            }
        });
    }

    public void checkPassword() {
        passwords = password.getText().toString();
        renter_Passowrds = renterPassword.getText().toString();
        if (passwords.equals(renter_Passowrds)) {
            checkPassowrd.setVisibility(View.INVISIBLE);
            new GenerateWalletThread().execute();
        }
        else {
            checkPassowrd.setText("Password Not Matched");
            Toast.makeText(GenerateWallet.this, " Not matched", Toast.LENGTH_SHORT).show();
        }

    }
    /** we get all our view in this method  */
    public void initializedVIew() {
        password = (EditText) findViewById(R.id.et_password);
        renterPassword = (EditText) findViewById(R.id.et_renterPassword);
        checkPassowrd = (TextView) findViewById(R.id.tx_chkPassword);
        generateWallet = (Button) findViewById(R.id.btn_generateWallet);
        checkAccDetail= (Button) findViewById(R.id.btn_checkDetail);
    }

    public class GenerateWalletThread extends AsyncTask<String,Integer,String> {

        private ProgressDialog mDialog;
        protected  void onPreExecute(){
            filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            mDialog = ProgressDialog.show(GenerateWallet.this,"Please wait...", "Generate Wallet ...", true);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                /**@param passwords it will be your wallet password
                 * @param new File(filePath) is the destination directory where your UTC-JSON file save
                 */
                fileName = WalletUtils.generateNewWalletFile(passwords, new File(filePath), false);
                return fileName;
            } catch (CipherException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            } catch (InvalidAlgorithmParameterException e) {
                e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                e.getMessage();
            } catch (NoSuchProviderException e) {
                e.getMessage();
            }
            return null;
        }

        protected void onPostExecute(String result){
            mDialog.dismiss();
            checkAccDetail.setVisibility(View.VISIBLE);
            Toast.makeText(GenerateWallet.this, "Your wallet is created", Toast.LENGTH_SHORT).show();
        }
    }
    /** we send password and filename to AccountDetail activity */
    public void sentIntent(){
        Intent intent = new Intent(GenerateWallet.this,AccountDetail.class);
        intent.putExtra("FILENAME", fileName);
        intent.putExtra("PASSWORD",passwords);
        startActivity(intent);

    }
}












