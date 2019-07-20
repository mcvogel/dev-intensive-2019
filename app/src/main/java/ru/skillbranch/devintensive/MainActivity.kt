package ru.skillbranch.devintensive

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {
	lateinit var benderImage : ImageView
	lateinit var textTxt : TextView
	lateinit var messageEt : EditText
	lateinit var sendBtn : ImageView

	lateinit var benderObj : Bender

	companion object {
		private val STATUS_TAG = "STATUS"
		private val QUESTION_TAG = "QUESTION"
	}

	/**
	 * Вызывается при первом создании или перезапуске Activity.
	 *
	 * задаётся внешний вид Activity (UI) через метод setContentView().
	 * инициализируются представления и модели
	 * представления связываются с необходимыми данными и ресурсами
	 * связываются данные со списками
	 *
	 * Этот метод так де предоставляет Bundle, содержащий ранее сохраненное
	 * состояние Activity, если оно было.
	 *
	 * Всегда сопровождается вызовом onStart()
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		// Способ по классике, с обратной совместимостью
		// benderImage = findViewById<ImageView>(R.id.iv_bender)

		benderImage = iv_bender
		textTxt = tv_text

		// Строка ввода сообщения
		messageEt = et_message
		messageEt.imeOptions = EditorInfo.IME_ACTION_DONE
		messageEt.setOnEditorActionListener { v, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				send()
				hideKeyboard()
				true
			} else false
		}

		sendBtn = iv_send
		sendBtn.setOnClickListener(this)

		// Сохраняли ли что-то в бандл?
		val status = savedInstanceState?.getString(STATUS_TAG) ?: Bender.Status.NORMAL.name
		val question = savedInstanceState?.getString(QUESTION_TAG) ?: Bender.Question.NAME.name

		// Создадим бендера
		benderObj = Bender(
			Bender.Status.valueOf(status),
			Bender.Question.valueOf(question)
		)

		// Зададим ему цвет
		val (r, g, b) = benderObj.status.color
		benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

		// Установить строку с вопросом
		textTxt.setText(benderObj.askQuestion())

		Log.d("M_MainActivity", "onCreate ST: ${benderObj.status.name} Q: ${benderObj.question.name}")
	}

	/**
	 * вызывается если Activity возвращается в приоритетный режим после
	 * вызова onStop(), т.е. вызывается после того, как Activity была остановлена и снова была
	 * запущена пользователем. Всегда сопровождается вызовом метода onStart().
	 * используется для специальных действий, которые должны выполняться только   при
	 * повторном запуске Activity
	 */
	override fun onRestart() {
		super.onRestart()
		Log.d("M_MainActivity", "onRestart")
	}

	override fun onStart() {
		super.onStart()
		Log.d("M_MainActivity", "onStart")
	}

	override fun onResume() {
		super.onResume()
		Log.d("M_MainActivity", "onResume")
	}

	override fun onPause() {
		super.onPause()
		Log.d("M_MainActivity", "onPause")
	}

	override fun onStop() {
		super.onStop()
		Log.d("M_MainActivity", "onStop")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d("M_MainActivity", "onDestroy")
	}

	// Немного всратый метод обработки нажатия на кнопку
	override fun onClick(v: View?) {
		// Если кликнули по кнопе отсылки
		if (v?.id == R.id.iv_send) {
			send()
		}
	}

	fun send() {
		// Обработать пользовательский ввод
		val(phrase, color) = benderObj.listenAnswer(messageEt.text.toString())

		// Сбросить пользовательский ввод
		messageEt.setText("")

		// Навесить на картинку бендера цветофильтр
		val (r, g, b) = color
		benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

		// Выведем ответ бендера
		textTxt.setText(phrase)
	}

	// Метод сохраняет состояние представления в bundle
	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		outState?.putString(STATUS_TAG, benderObj.status.name)
		outState?.putString(QUESTION_TAG, benderObj.question.name)
		
		Log.d("M_MainActivity", "onSaveInstanceState ST: ${benderObj.status.name} Q: ${benderObj.question.name}")
	}

}
