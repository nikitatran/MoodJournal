package com.codingwithmitch.journal.util;

import android.os.AsyncTask;
import android.util.Log;


import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.models.Note;
import com.paralleldots.paralleldots.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ParallelDotsApi {
    String api_key = "1yajIxksKMQYuy9Wy13XRnilpBVtHtVwzwv07QWcm8w";
    private double dBored;
    private double dAngry;
    private double dSad;
    private double dFear;
    private double dHappy;
    private double dExcited;


    private Note note;
    private NoteRepository noteRepo;

    public ParallelDotsApi(){

    }

    public void setNote(Note n, NoteRepository repo){
        note = n;
        noteRepo = repo;
    }

    public double getdBored(){
        return dBored;
    }
    public double getdSad(){
        return dSad;
    }
    public double getdHappy(){
        return dHappy;
    }
    public double getdAngry(){
        return dAngry;
    }
    public double getdFear(){
        return dFear;
    }
    public double getdExcited(){
        return dExcited;
    }

    public void apiCall(String stringToAnalyze){
        AsyncTaskExample asyncTask=new AsyncTaskExample();
        asyncTask.execute(stringToAnalyze);
    }

    private class AsyncTaskExample extends AsyncTask<String, Response, Response> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            p = new ProgressDialog(MainActivity.this);
//            p.setMessage("Please wait...It is downloading");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
        }

        protected Response doInBackground(String... sentenceToAnalyze) {
            //App pd = new App ("1yajIxksKMQYuy9Wy13XRnilpBVtHtVwzwv07QWcm8w");

            String text = sentenceToAnalyze[0];
            String url = "https://apis.paralleldots.com/v5/emotion";



            Response Theresponse = null;
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("api_key", api_key)
                    .addFormDataPart("text", text)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("cache-control", "no-cache")
                    .build();
            OkHttpClient client = new OkHttpClient();

            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace ();
            }

            return Theresponse;
        }
        @Override
        protected void onPostExecute(Response emotionResponse) {
            super.onPostExecute(emotionResponse);

            try {

                try {
                    // TODO: Delete Decimal Formatter. This is used here for testing purposes
                    DecimalFormat df2 = new DecimalFormat ("#");
                    df2.setRoundingMode(RoundingMode.UP);
                    //Decimal Formatter

                    //Change each string into a float of two decimal places
                    String jsonResponseString = emotionResponse.body ().string ();
                    JSONObject myObject = new JSONObject (jsonResponseString);
                    JSONObject emotion = myObject.getJSONObject("emotion");
                    dBored = emotion.getDouble ("indifferent" );
                    dBored *= 100.00;
                    Log.d ("dBoard", String.valueOf (dBored));
                    dAngry = emotion.getDouble ("angry");
                    dAngry *= 100.00;
                    Log.d ("dAngry", String.valueOf (dAngry));
                    dSad = emotion.getDouble ("sad");
                    dSad *= 100.00;
                    Log.d ("dSad", String.valueOf (dSad));
                    dFear = emotion.getDouble ("fear");
                    dFear *= 100.00;
                    Log.d ("dfear", String.valueOf (dFear));
                    dHappy = emotion.getDouble ("happy");
                    dHappy *= 100.00;
                    Log.d ("dHappy", String.valueOf (dHappy));
                    dExcited = emotion.getDouble ("excited");
                    dExcited *= 100.00;
                    Log.d ("dExcited", String.valueOf (dExcited));

                    note.setAngry(dAngry);
                    note.setBored(dBored);
                    note.setSad(dSad);
                    note.setFear(dFear);
                    note.setHappy(dHappy);
                    note.setExcited(dExcited);

                    //update note's emotion columns
                    noteRepo.updateNoteTask(note);
                    Log.d("Emotion API: ", note.toString());

                } catch (IOException e) {
                    e.printStackTrace ();
                }





            } catch (JSONException e) {
                e.printStackTrace ();
            }

        }
    }
}

