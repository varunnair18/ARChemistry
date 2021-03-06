package com.helloworldappclub.ARChemistry;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Justin on 9/24/2016.
 */
public class PubChemConnection {
    public interface PubChemDataListener{
        public void onSuccess(String message);
        public void onFailure(String message);
        public void onCancelled();

        // Run CODE: 0 - Get molecule conformers to build molecule.
        // Run CODE: 1 - Get molecule information to populate info popup.
    }

    public void loadCID(int CID, PubChemDataListener listener){
            ConnectionThreadRC0 t = new ConnectionThreadRC0();
            t.init(CID, listener, 0);
            t.start();
    }

    public void loadProperties(int CID, PubChemDataListener listener){
        ConnectionThreadRC0 t = new ConnectionThreadRC0();
        t.init(CID, listener, 1);
        t.start();
    }

    private class ConnectionThreadRC0 extends Thread{
        int CID;
        PubChemDataListener listener;
        int runCode;
        public void init(int CID, PubChemDataListener listener, int runCode){
            this.CID=CID;
            this.listener=listener;
            this.runCode = runCode;
        }
        public void run(){
            try {
                URL url = new URL("https://www.google.com/");
                if (runCode ==0) {
                        url = new URL("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/" + Integer.toString(CID) + "/record/JSON/?record_type=3d&response_type=display");
                }
                else if (runCode == 1){
                    url = new URL("https://pubchem.encbi.nlm.nih.gov/rest/pug/compound/cid/" + Integer.toString(CID) + "/property/MolecularFormula,MolecularWeight/JSON");
                }
                HttpsURLConnection uc=(HttpsURLConnection)url.openConnection();
                uc.setRequestMethod("GET");

                int code=-1;
                long start=System.currentTimeMillis();
                while(code==-1){
                    code=uc.getResponseCode();
                    if(System.currentTimeMillis()-start>10000){//server timed out, call appropriate listener method
                        listener.onCancelled();
                        return;
                    }
                }
                String body="";
                if (code == 200) {//action was successful
                    //parse message
                    InputStream is = new BufferedInputStream(uc.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        body += line;
                    }
                    listener.onSuccess(body);
                } else {//action failed
                    //parse error message
                    InputStream is = new BufferedInputStream(uc.getErrorStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        body += "" + line;
                    }
                    //call listener failure and pass error message
                    listener.onFailure(body);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class ConnectionThreadRC1 extends Thread{
        int CID;
        PubChemDataListener listener;
        int runCode;
        public void init(int CID, PubChemDataListener listener){
            this.CID=CID;
            this.listener=listener;
            this.runCode = runCode;
        }
        public void run(){
            try {
                URL url=new URL("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/"+Integer.toString(CID)+"/record/JSON/?record_type=3d&response_type=display");
                HttpsURLConnection uc=(HttpsURLConnection)url.openConnection();
                uc.setRequestMethod("GET");

                int code=-1;
                long start=System.currentTimeMillis();
                while(code==-1){
                    code=uc.getResponseCode();
                    if(System.currentTimeMillis()-start>10000){//server timed out, call appropriate listener method
                        listener.onCancelled();
                        return;
                    }
                }
                String body="";
                if (code == 200) {//action was successful
                    //parse message
                    InputStream is = new BufferedInputStream(uc.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        body += line;
                    }
                    listener.onSuccess(body);
                } else {//action failed
                    //parse error message
                    InputStream is = new BufferedInputStream(uc.getErrorStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        body += "" + line;
                    }
                    //call listener failure and pass error message
                    listener.onFailure(body);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
