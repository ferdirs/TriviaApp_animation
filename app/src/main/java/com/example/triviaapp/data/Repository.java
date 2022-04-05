package com.example.triviaapp.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionsArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion( final AnswerListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Question question = new Question(response.getJSONArray(i).getString(0) , response.getJSONArray(i).getBoolean(1));

                        //add question to arraylist/list
                        questionsArrayList.add(question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (null != callback) callback.processFinish(questionsArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionsArrayList;

    }
}
