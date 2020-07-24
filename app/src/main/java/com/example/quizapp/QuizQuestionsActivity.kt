package com.example.quizapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mQuestionsList = Constants.getQuestions()
        //Log.i("Questions Size", "${questionsList.size}")

        setQuestion()

        //click the options
        //this: needs to impl OnClickListener, so all the options has a joint click listener func onClick()
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)

        btn_submit.setOnClickListener(this)


    }

    private fun setQuestion() {

        //start from 0
        val question = mQuestionsList!![mCurrentPosition - 1]

        //when set a new question, default view
        defaultOptionsView()

        //in the end change submit text to finish
        if (mCurrentPosition == mQuestionsList!!.size) {
            btn_submit.text = "Finished"
        } else {
            btn_submit.text = "Submit"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "${mCurrentPosition}/${progressBar.max}"

        tv_question.text = question.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    //set each option as default view, e.g. not highlighted
    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.setTypeface(option.typeface, Typeface.NORMAL)
            option.setBackgroundResource(R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        //set options to default at begining
        defaultOptionsView()
        //assign the selected int
        mSelectedOptionPosition = selectedOptionNum

        //bold and outline
//        tv.typeface= Typeface.DEFAULT_BOLD
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.setTextColor(Color.parseColor("#363a43"))
//        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)
        tv.setBackgroundResource(R.drawable.selected_option_border_bg)

    }

    //submit button
    //drawableView is to set background blue or green
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.setBackgroundResource(drawableView)
            }
            2 -> {
                tv_option_two.setBackgroundResource(drawableView)
            }
            3 -> {
                tv_option_three.setBackgroundResource(drawableView)
            }
            4 -> {
                tv_option_four.setBackgroundResource(drawableView)
            }
        }
    }


    //impl the Onclicklistener interface, joint click func for all options
    override fun onClick(view: View?) {
        //when option has clicked
        when (view?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
            }

            R.id.btn_submit -> {
                //if no option selected after submit go to next question
                if (mSelectedOptionPosition == 0) {

                    //+question
                    mCurrentPosition++

                    //as long as <= list, set a new question
                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "You've successfully completed the quiz",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {


                    //if you set mposition = 0, then they will match
                    val question = mQuestionsList!![mCurrentPosition - 1]

                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)

                    }
                    //also draw the correct answer border as well
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)


                    //change submit to next question
                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btn_submit.text = "Finished"
                    } else {
                        btn_submit.text = "Next Question"
                    }

                    //need to set selected position to 0 to go to next question, in the if stat: if(mSelectedOptionPosition==0){
                    mSelectedOptionPosition = 0

                }


            }


        }
    }

}