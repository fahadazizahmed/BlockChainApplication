package com.example.user.blockchainapplication.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.user.blockchainapplication.NetworkOperation.HttpHandler;
import com.example.user.blockchainapplication.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class SendEther extends AppCompatActivity {
    EditText accAddress;
    EditText accBalance;
    EditText receiverAddress;
    Button scanAddress;
    EditText sendAmount;
    EditText gasLimit;
    Button sendEther;
    Button transactionDetail;
    TransactionReceipt transactionReceipt;
    String fileName;
    String filePath;
    final Activity activity = this;
    String myAddress;
    Credentials credentials = null;
    String accountAddress;
    String accountBalance;
    HttpHandler httpHandler;
    String pass;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_send_ether);
                httpHandler = HttpHandler.getInstance();
                filePath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
                initializedVIew();
                getIntentData();
                accAddress.setText(accountAddress);
                accBalance.setText(accountBalance);

        /** We scan the receiver address using QR code here QRCode */

                scanAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                            intentIntegrator.setPrompt("Scan");
                            intentIntegrator.setCameraId(0);
                            intentIntegrator.setBeepEnabled(false);
                            intentIntegrator.setBarcodeImageEnabled(false);
                            intentIntegrator.initiateScan();

                        }
                });

                sendEther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                              if(receiverAddress.getText().toString().isEmpty() || sendAmount.getText().toString().isEmpty()){
                                    Toast.makeText(SendEther.this, "Please fill all the field", Toast.LENGTH_LONG).show();
                              }

                            else {
                                  new sendingTransaction().execute();
                              }
                    }
                });

                /** We get Detail of the transaction by using different method and send to the TransactionDetail Activity*/
                transactionDetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        BigInteger bigInteger = transactionReceipt.getGasUsed();
                        String gasUsed =  bigInteger.toString();
                        String from = accAddress.getText().toString();
                        String to = receiverAddress.getText().toString();
                        String value = sendAmount.getText().toString();
                        String blokHash = transactionReceipt.getBlockHash();
                        BigInteger blockNmb =  transactionReceipt.getBlockNumber();
                        String blockNumber = blockNmb.toString();
                        String tranHash = transactionReceipt.getTransactionHash();
                        Intent intent = new Intent(SendEther.this,TransactionDetail.class);

                        intent.putExtra("SENDER_ADDR",from );
                        intent.putExtra("RECEIVER_ADDR",to );
                        intent.putExtra("BLOCKHASH",blokHash );
                        intent.putExtra("TRANSACTIONHASH",tranHash );
                        intent.putExtra("GASUSED",gasUsed );
                        intent.putExtra("AMOUNT",value );
                        intent.putExtra("BLOCKNUMBER",blockNumber );
                        startActivity(intent);
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(intentResult !=null) {
                    if(intentResult.getContents()==null) {
                        Toast.makeText(SendEther.this,"you cancel Scanning",Toast.LENGTH_LONG).show();
                    }
                    else {
                            String CurrentString = intentResult.getContents();
                            String[] separated = CurrentString.split("ethereum:");
                            receiverAddress.setText(separated[1]);
                        }
            }
        else {
                super.onActivityResult(requestCode, resultCode, data);
            }
    }

    /** This method work in background basically and send your transaction from one your account to another account on rinkeby.
     * @param Void which we passed in when call this class we passed nothing that's why void is first .
     * @param Integer give your progress detail but in this we not give progress update.
     * @param Void it is the return we return nothing that's why Void.

     */
    public class sendingTransaction extends AsyncTask<Void,Integer,Void> {
        String receiverAddr = receiverAddress.getText().toString();
        double sendValue = Double.parseDouble(sendAmount.getText().toString());
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(SendEther.this,"Please wait...", "Your Transaction is in Progress ...", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)  {

            try {
                /**@param pass your wallet file password
                 * @param filePath your wallet file path
                 * 3rd @param   name of your wallet file
                 */
                credentials =    httpHandler.loadCredentialss(pass,filePath + "/" + "UTC--2017-09-01T08-23-25.280--5003a1b9f7dc462ecb7eae4ebe4d7d5591ddf8fc.json");
            } catch (IOException e) {
                e.getMessage();
            } catch (CipherException e) {
                e.getMessage();
            }
            /**@param httpHandler.makeHttpConnection() connect you to rinkeby network
             * @param credentials is the complete path of your file
             *  @param  BigDecimal.valueOf(sendValue) the amount you send
             *   @param  Convert.Unit.ETHER convert your amount ot ehter
             */

            try {
                transactionReceipt = Transfer.sendFunds(httpHandler.makeHttpConnection(), credentials,receiverAddr , BigDecimal.valueOf(sendValue), Convert.Unit.ETHER);
            } catch (InterruptedException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            } catch (TransactionTimeoutException e) {
                e.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Void result){
            try {
                accBalance.setText(Convert.toWei(httpHandler.getBalances(accountAddress)
                        + "", Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")).toString() );
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(SendEther.this, "Congratulation Your transaction is send", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            transactionDetail.setVisibility(View.VISIBLE);
        }
    }

    /** we get all our view in this method  */
    public void initializedVIew() {
        accAddress = (EditText) findViewById(R.id.et_Adress);
        accBalance = (EditText) findViewById(R.id.et_balance );
        receiverAddress = (EditText) findViewById(R.id. et_receiverAdd);
        scanAddress = (Button)findViewById(R.id.btn_scan);
        sendAmount = (EditText) findViewById(R.id.et_amount);
        gasLimit = (EditText) findViewById(R.id.et_glimit);
        sendEther= (Button)findViewById(R.id.btn_ethSend);
        transactionDetail= (Button)findViewById(R.id.btn_tranDetail);
    }

    /** Here we getData from WalletLogin activity*/
    public void getIntentData() {
        Intent intent = getIntent();
        pass = intent.getStringExtra("PASSWORD");
        accountBalance = intent.getStringExtra("ACC_BALANCE");
        accountAddress = intent.getStringExtra("ACC_ADDRESS");
    }
}































