package np.com.bimalkafle.quizonline

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import np.com.bimalkafle.quizonline.databinding.ActivityQuizBinding
import np.com.bimalkafle.quizonline.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0
    var isAnswerLocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text =
                    String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                // Finish the quiz
                finishQuiz()
            }
        }.start()
    }

    private fun loadQuestions() {
        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text =
                "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }

        isAnswerLocked = false // Enable answer selection for the current question
        resetButtonColors() // Reset button colors to default
    }

    override fun onClick(view: View?) {
        if (isAnswerLocked && view?.id != R.id.next_btn) return // Prevent changes if answer is locked

        val clickedBtn = view as Button
        when (clickedBtn.id) {
            R.id.next_btn -> {
                // Next button is clicked
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(applicationContext, "Please select an answer to continue", Toast.LENGTH_SHORT).show()
                    return
                }

                if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                    score++
                    Log.i("Score of quiz", score.toString())
                } else {
                    highlightCorrectAnswer()
                }
                isAnswerLocked = true // Lock the selection
                currentQuestionIndex++
                loadQuestions()
            }
            else -> {
                // Option button is clicked
                handleOptionClick(clickedBtn)
            }
        }
    }

    private fun handleOptionClick(selectedButton: Button) {
        if (isAnswerLocked) return // Do nothing if answer is locked

        selectedAnswer = selectedButton.text.toString()
        highlightSelectedAnswer(selectedButton)
        if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
            selectedButton.setBackgroundColor(getColor(R.color.green))
        } else {
            selectedButton.setBackgroundColor(getColor(R.color.red))
            highlightCorrectAnswer()
        }
        isAnswerLocked = true // Lock the selection after choosing an answer
    }

    private fun highlightSelectedAnswer(selectedButton: Button) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))

            selectedButton.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun highlightCorrectAnswer() {
        val correctAnswer = questionModelList[currentQuestionIndex].correct
        val correctButton = findViewById<Button>(
            when (correctAnswer) {
                binding.btn0.text.toString() -> R.id.btn0
                binding.btn1.text.toString() -> R.id.btn1
                binding.btn2.text.toString() -> R.id.btn2
                binding.btn3.text.toString() -> R.id.btn3
                else -> R.id.btn0
            }
        )
        correctButton.setBackgroundColor(getColor(R.color.green))

        if (selectedAnswer != correctAnswer) {
            val selectedButton = findViewById<Button>(
                when (selectedAnswer) {
                    binding.btn0.text.toString() -> R.id.btn0
                    binding.btn1.text.toString() -> R.id.btn1
                    binding.btn2.text.toString() -> R.id.btn2
                    binding.btn3.text.toString() -> R.id.btn3
                    else -> R.id.btn0
                }
            )
            selectedButton.setBackgroundColor(getColor(R.color.red))
        }
    }

    private fun resetButtonColors() {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
    }

    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage >= 50) {
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Oops! You have failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}
