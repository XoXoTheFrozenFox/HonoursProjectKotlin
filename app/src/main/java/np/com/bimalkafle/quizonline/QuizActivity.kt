package np.com.bimalkafle.quizonline

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import np.com.bimalkafle.quizonline.databinding.ActivityQuizBinding
import np.com.bimalkafle.quizonline.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    lateinit var binding: ActivityQuizBinding

    private var currentQuestionIndex = 0
    private var selectedAnswer = ""
    private var score = 0
    private var isAnswerLocked = false

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
                finishQuiz()
            }
        }.start()
    }

    private fun loadQuestions() {
        if (currentQuestionIndex >= questionModelList.size) {
            finishQuiz()
            return
        }

        val question = questionModelList[currentQuestionIndex]

        binding.apply {
            questionIndicatorTextview.text =
                "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = question.question
            btn0.text = question.options[0]
            btn1.text = question.options[1]
            btn2.text = question.options[2]
            btn3.text = question.options[3]
            feedbackTextview.text = "" // Clear feedback on new question
        }

        isAnswerLocked = false
        resetButtonColors()
    }

    override fun onClick(view: View?) {
        if (view !is Button) return

        if (isAnswerLocked && view.id != R.id.next_btn) return

        when (view.id) {
            R.id.next_btn -> handleNextButtonClick()
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3 -> handleOptionClick(view)
        }
    }

    private fun handleNextButtonClick() {
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

        isAnswerLocked = true
        currentQuestionIndex++
        loadQuestions()
    }

    private fun handleOptionClick(view: View) {
        if (isAnswerLocked) return

        val selectedButton = view as Button
        selectedAnswer = selectedButton.text.toString()
        highlightSelectedAnswer(selectedButton)

        if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
            selectedButton.setBackgroundColor(getColor(R.color.green))
            // Show feedback if the answer is correct
            binding.feedbackTextview.text = "Feedback: " + questionModelList[currentQuestionIndex].feedback
        } else {
            selectedButton.setBackgroundColor(getColor(R.color.red))
            highlightCorrectAnswer()
            // Show feedback if the answer is incorrect
            binding.feedbackTextview.text = "Feedback: " + questionModelList[currentQuestionIndex].feedback
        }
        isAnswerLocked = true
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
        val correctButtonId = when (correctAnswer) {
            binding.btn0.text.toString() -> R.id.btn0
            binding.btn1.text.toString() -> R.id.btn1
            binding.btn2.text.toString() -> R.id.btn2
            binding.btn3.text.toString() -> R.id.btn3
            else -> R.id.btn0
        }
        val correctButton = findViewById<Button>(correctButtonId)
        correctButton.setBackgroundColor(getColor(R.color.green))

        if (selectedAnswer != correctAnswer) {
            val selectedButtonId = when (selectedAnswer) {
                binding.btn0.text.toString() -> R.id.btn0
                binding.btn1.text.toString() -> R.id.btn1
                binding.btn2.text.toString() -> R.id.btn2
                binding.btn3.text.toString() -> R.id.btn3
                else -> R.id.btn0
            }
            val selectedButton = findViewById<Button>(selectedButtonId)
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
