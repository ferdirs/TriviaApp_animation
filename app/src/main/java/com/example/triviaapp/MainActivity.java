package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.data.AnswerListAsyncResponse;
import com.example.triviaapp.data.Repository;
import com.example.triviaapp.databinding.ActivityMainBinding;
import com.example.triviaapp.model.Question;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
     private int currentQuestionIndex = 0;
    List<Question> questionListlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main);

        questionListlist=  new Repository().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinish(ArrayList<Question> questionArrayList) {
                binding.tvQuestion.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                updateQuestionIndex((ArrayList<Question>) questionListlist);
            }
        });


        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex = (currentQuestionIndex +1) % questionListlist.size();
                updateQuestion();
            }
        });

        binding.btTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateQuestion();
            }
        });

        binding.btFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateQuestion();
            }
        });

    }


    @SuppressLint("ResourceType")
    private void checkAnswer(boolean userChooseCorrect){
        boolean answer = questionListlist.get(currentQuestionIndex).isAnswerTrue();
        int snackMsgId = 0;
        if (userChooseCorrect == answer){
            snackMsgId = R.string.correctAnswer;
            fadeAnimation();
        }else{
            snackMsgId = R.string.incorrect;
            shakeAnimation();
        }
        Snackbar.make(binding.card , snackMsgId , Snackbar.LENGTH_SHORT).show();

    }

    private void updateQuestion() {
        String question = questionListlist.get(currentQuestionIndex).getAnswer();
        binding.tvQuestion.setText(question);
        updateQuestionIndex((ArrayList<Question>) questionListlist);
    }

    private void updateQuestionIndex(ArrayList<Question> questionArrayList) {
        binding.txtQuestionNum.setText("Question: "+currentQuestionIndex + "/" + questionArrayList.size());
    }


    private void fadeAnimation(){

        AlphaAnimation fade = new AlphaAnimation(1.0F,0.0f);
        fade.setDuration(300);
        fade.setRepeatCount(1);
        fade.setRepeatMode(Animation.REVERSE);

        binding.card.setAnimation(fade);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.tvQuestion.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.tvQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void shakeAnimation(){

        Animation shake = AnimationUtils.loadAnimation(MainActivity.this , R.anim.shake_animation);
        binding.card.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.tvQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.tvQuestion.setTextColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}