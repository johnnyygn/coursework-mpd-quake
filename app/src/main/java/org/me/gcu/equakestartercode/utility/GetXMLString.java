package org.me.gcu.equakestartercode.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.me.gcu.equakestartercode.interfaces.IGetXMLResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
//Get xml and turns it into big string.
public class GetXMLString extends AsyncTask<String , Void , String >
{
    //Object calling GetXMLSting
    private IGetXMLResponse listener;
    public GetXMLString(IGetXMLResponse listener)
    {
        this.listener = listener;
    }

    //Turns given url into bug string
    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String url = strings[0];
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        try {
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
            }
            in.close();
            return result;

        } catch (IOException ae) {
            Log.e("MyTag", "ioexception in run");
        }
        return null;
    }

    //Once completed runs returnXML
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.returnXML(s);
    }
}
