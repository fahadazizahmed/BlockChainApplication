package com.example.user.blockchainapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.user.blockchainapplication.R;

public class TransactionDetail extends AppCompatActivity {

    TextView receiverAddress;
    TextView senderAddress;
    TextView value;
    TextView blockHash;
    TextView TransactionHash;
    TextView useGas;
    TextView blockNmb;
    String fromAddress;
    String toAddress;
    String sendAmount;
    String blkHash;
    String TransHash;
    String gasUsed;
    String  blockNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        getIntentData();
        initializedView();
        receiverAddress.setText(toAddress);
        senderAddress.setText(fromAddress);
        blockHash.setText(blkHash);
        blockNmb.setText(blockNumber);
        useGas.setText(gasUsed);
        value.setText(sendAmount+ " "+"Ether");
        TransactionHash.setText(TransHash);
    }

    /** Here we getData from SendEther activity*/
    public void getIntentData() {
        Intent intent = getIntent();
        fromAddress = intent.getStringExtra("SENDER_ADDR");
        toAddress  = intent.getStringExtra("RECEIVER_ADDR");
        blkHash  = intent.getStringExtra("BLOCKHASH");
        TransHash  = intent.getStringExtra("TRANSACTIONHASH");
        blockNumber = intent.getStringExtra("BLOCKNUMBER");
        gasUsed = intent.getStringExtra("GASUSED");
        gasUsed = intent.getStringExtra("GASUSED");
        sendAmount = intent.getStringExtra("AMOUNT" );
    }

    /** we get all our view in this method */
    public void initializedView(){
        receiverAddress = (TextView)findViewById(R.id.tx_receiverAdd);
        senderAddress = (TextView)findViewById(R.id.tx_senderAdd);
        value = (TextView)findViewById(R.id.tx_value);
        blockHash = (TextView)findViewById(R.id.tx_blocoHash);
        TransactionHash = (TextView)findViewById(R.id.tx_transHash);
        useGas = (TextView)findViewById(R.id.tx_gasUsed);
        blockNmb = (TextView)findViewById(R.id.tx_blockNumber);
    }
}
