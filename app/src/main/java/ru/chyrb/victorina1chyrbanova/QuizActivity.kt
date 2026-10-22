package ru.chyrb.victorina1chyrbanova

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestionCount: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnOptions: List<Button>

    private var currentQuestionIndex = 0
    private var score = 0
    private val questionList = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // 1. Находим визуальные элементы
        tvQuestionCount = findViewById(R.id.tvQuestionProgress)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnOptions = listOf(
            findViewById(R.id.btnAnswer1),
            findViewById(R.id.btnAnswer2),
            findViewById(R.id.btnAnswer3),
            findViewById(R.id.btnAnswer4)
        )

        // 2. Загружаем вопросы
        loadQuestionsFromAssets()

        // 3. Показываем первый вопрос
        showNextQuestion()

        // 4. Вешаем листенеров на кнопки
        for (i in btnOptions.indices) {
            btnOptions[i].setOnClickListener {
                checkAnswer(i)
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex >= questionList.size) {
            // Завершаем викторину и идем на экран результатов
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("SCORE", score)
            intent.putExtra("TOTAL", questionList.size)
            startActivity(intent)
            finish()
            return
        }

        val currentQuestion = questionList[currentQuestionIndex]

        tvQuestionCount.text = getString(R.string.question_progress_format, currentQuestionIndex + 1, questionList.size)
        tvQuestionText.text = currentQuestion.questionText

        for (i in btnOptions.indices) {
            btnOptions[i].text = currentQuestion.options[i]
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questionList[currentQuestionIndex]

        if (selectedIndex == currentQuestion.correctIndex) {
            score++
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Неправильно!", Toast.LENGTH_SHORT).show()
        }

        currentQuestionIndex++
        showNextQuestion()
    }

    private fun loadQuestionsFromAssets() {
        try {
            val reader = BufferedReader(InputStreamReader(assets.open("questions.txt")))
            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split("|")
                if (parts.size == 6) {
                    val questionText = parts[0]
                    val options = listOf(parts[1], parts[2], parts[3], parts[4])
                    val correctIndex = parts[5].toIntOrNull() ?: 0
                    questionList.add(Question(questionText, options, correctIndex))
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}