/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)

    References used:
        1. http://apis.paralleldots.com/text_docs/index.html#v5-emotion
 */
package cpsc4150.projects.journal.util;

import android.os.AsyncTask;
import android.util.Log;


import cpsc4150.projects.journal.database.NoteRepository;
import cpsc4150.projects.journal.models.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ParallelDotsApi_v5 class handles the API call to ParallelDots that calculates emotions based off of
 * a given text.
 * Stores the calculated values into a note before the note is stored into the database
 *
 * Reference 1 used
 */
public class ParallelDotsApi_v5 {
    private final String api_key = "jFEcMS6z1DJ9Y2abzJO6tyQ3FarcVzp2LV5gAW9fz3o";

    private Note note;
    private NoteRepository noteRepo;

    /**
     * Empty constructor
     */
    public ParallelDotsApi_v5() {

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
                return client.newCall(request).execute();
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

                        double bored = emotion.getDouble("indifferent");
                        bored *= 100.00;

                        double angry = emotion.getDouble("angry");
                        angry *= 100.00;

                        double sad = emotion.getDouble("sad");
                        sad *= 100.00;

                        double fear = emotion.getDouble("fear");
                        fear *= 100.00;

                        double happy = emotion.getDouble("happy");
                        happy *= 100.00;

                        double excited = emotion.getDouble("excited");
                        excited *= 100.00;

                        HashMap map = new HashMap<String, Double>();
                        map.put("happy", happy);
                        map.put("sad", sad);
                        map.put("fear", fear);
                        map.put("bored", bored);
                        map.put("excited", excited);
                        map.put("angry", angry);

                        //https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
                        String prominentEmotion = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey().toString();

                        note.setAll(happy, sad, bored, excited, angry, fear);
                        note.setProminent_emotion(prominentEmotion);

                        Log.d("note", note.toString());

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

