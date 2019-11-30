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

//TODO list dialog box when response is null
public class ParallelDotsApi {
    String api_key = "jFEcMS6z1DJ9Y2abzJO6tyQ3FarcVzp2LV5gAW9fz3o";//"1yajIxksKMQYuy9Wy13XRnilpBVtHtVwzwv07QWcm8w";
    private double dBored;
    private double dAngry;
    private double dSad;
    private double dFear;
    private double dHappy;
    private double dExcited;

    private Note note;
    private NoteRepository noteRepo;
    private boolean isNewNote;

    public static boolean apiCallFail = false;

    public ParallelDotsApi(){

    }

    public void setNote(Note n, NoteRepository repo, boolean b){
        note = n;
        noteRepo = repo;
        isNewNote = b;
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

        @Override
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
                    if(emotionResponse != null) {
                        //Change each string into a float of two decimal places
                        String jsonResponseString = emotionResponse.body().string();
                        JSONObject myObject = new JSONObject(jsonResponseString);
                        JSONObject emotion = myObject.getJSONObject("emotion");
                        dBored = emotion.getDouble("indifferent");
                        dBored *= 100.00;

                        dAngry = emotion.getDouble("angry");
                        dAngry *= 100.00;

                        dSad = emotion.getDouble("sad");
                        dSad *= 100.00;

                        dFear = emotion.getDouble("fear");
                        dFear *= 100.00;

                        dHappy = emotion.getDouble("happy");
                        dHappy *= 100.00;

                        dExcited = emotion.getDouble("excited");
                        dExcited *= 100.00;

                        note.setAngry(dAngry);
                        note.setBored(dBored);
                        note.setSad(dSad);
                        note.setFear(dFear);
                        note.setHappy(dHappy);
                        note.setExcited(dExcited);

                        //update note's emotion columns
                        if (isNewNote) {
                            noteRepo.updateNoteTask(note);
                        } else
                            noteRepo.updateNoteTask(note);

                        Log.d("Emotion API: ", note.toString());
                        apiCallFail = false;
                    }

                    else apiCallFail = true;
                    Log.d("apiCallFail", ""+apiCallFail);
                } catch (IOException e) {
                    e.printStackTrace ();
                }





            } catch (JSONException e) {
                e.printStackTrace ();
            }

        }
    }
}

