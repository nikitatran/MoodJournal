/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */
package cpsc4150.projects.journal.util;

import android.os.AsyncTask;


import cpsc4150.projects.journal.database.NoteRepository;
import cpsc4150.projects.journal.models.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ParallelDotsApi class handles the API call to ParallelDots that calculates emotions based off of
 * a given text.
 * Stores the calculated values into a note before the note is stored into the database
 *
 * Reference: http://apis.paralleldots.com/text_docs/index.html#v5-emotion
 */
public class ParallelDotsApi {
    String api_key = "jFEcMS6z1DJ9Y2abzJO6tyQ3FarcVzp2LV5gAW9fz3o";
    private double dBored;
    private double dAngry;
    private double dSad;
    private double dFear;
    private double dHappy;
    private double dExcited;

    private Note note;
    private NoteRepository noteRepo;

    /**
     * Empty constructor
     */
    public ParallelDotsApi() {

    }

    /**
     * SetNote function sets up the Note and NoteRepository values
     * Pre-Condition: Requires the following variables of
     * @param n An instance of a Note
     * @param repo An instance of a NoteRepository
     * Post-Condition: Sets the values for the Note and NoteRepository instances
     */
    public void setNote(Note n, NoteRepository repo) {
        note = n;
        noteRepo = repo;
    }

    /**
     * apicall functions handles the async API called for ParallelDots emotion analysis
     * Pre-Condition: Requires a user input string to be analysed
     * @param stringToAnalyze represents a user input string from a note
     * Post-Condition: Handles the api call to ParallelDots
     */
    public void apiCall(String stringToAnalyze) {
        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute(stringToAnalyze);
    }

    private class AsyncTaskExample extends AsyncTask<String, Response, Response> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(String... sentenceToAnalyze) {
            String text = sentenceToAnalyze[0];
            String url = "https://apis.paralleldots.com/v5/emotion";

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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response emotionResponse) {
            super.onPostExecute(emotionResponse);
            try {
                try {
                    if (emotionResponse != null) {
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

                        noteRepo.updateNoteTask(note);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

