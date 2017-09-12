package com.example.user.blockchainapplication.NetworkOperation;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * it's helps to connect your app to ethereum network
 * @version 1.10 5 Sep 2017
 * @author Fahad Aziz
 */

public class HttpHandler {
    /** Web3j variable 'web3' is used to implement all the functions, exist in Web3j Library*/
    public static Web3j web3;
    /** Credentials variable 'credentials' is used to implement all the functions, exist in Credentials Library*/
    public static Credentials credentials;
    /** Here singleten pattern is used because we need only one object of this class to call his class function*/

    private static HttpHandler httpHandler = new HttpHandler();

    private HttpHandler(){
    }

    public static HttpHandler getInstance() {

        return httpHandler;
    }

    /*This method connect you to some end client node,I used infura API for*/
    public Web3j makeHttpConnection() {

        web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/DsOEoyfFaCmFgYn1ouB5"));
        return web3;
    }

    /**
     * loadCredentialss function is used to load the UTC-JSON file from a particular path.
     * @param password  is used to access your UTC-JSON file.
     * @param path is used to give a path where your UTC-JSON file is located.
     * @throws IOException
     * @throws CipherException
     */
    public Credentials loadCredentialss(String password, String path) throws IOException, CipherException {

        credentials = WalletUtils.loadCredentials(password, path);
        return credentials;
    }

    /** This method give you your wallet address which is stored in UTC-JSON file */

    public String getWalletAddress() {
        return credentials.getAddress();

    }

    /**
     * getBalance function is used to get Balance of your account we passed some account address in this and it give the balance on this address in the form of BigInteger value.
     * @throws InterruptedException
     * @throws ExecutionException
     * @return
     */
    public BigInteger getBalances(String Address) throws ExecutionException, InterruptedException {

        EthGetBalance ethGetBalance = null;

            ethGetBalance = httpHandler.makeHttpConnection()
                    .ethGetBalance(Address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

        BigInteger accountBalance = ethGetBalance.getBalance();
        return accountBalance ;
    }
}
