package ru.skillbranch.devintensive.models

import android.util.Log

class Bender (
	var status:Status = Status.NORMAL,
	var question: Question = Question.NAME
) {

	enum class Status (val color: Triple<Int, Int, Int>) {
		NORMAL(Triple(255, 255, 255)) ,
		WARNING(Triple(255, 120, 0)),
		DANGER(Triple(255, 60, 60)),
		CRITICAL(Triple(255, 0, 0));

		fun nextStatus() : Status {
			return if (this.ordinal < values().lastIndex) {
				values()[this.ordinal + 1]
			} else {
				values()[0]
			}
		}
	}

	enum class Question (val question: String, val answers : List<String>) {
		NAME("Как меня зовут?", listOf("бендер", "bender")) {
			override fun nextQuestion(): Question = PROFESSION
			override fun validation(answer: String?): Pair<Boolean, String> =
				(!answer.isNullOrBlank() && answer.first().isUpperCase()) to "Имя должно начинаться с заглавной буквы"
		},
		PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
			override fun nextQuestion(): Question = MATERIAL
			override fun validation(answer: String?): Pair<Boolean, String> =
				(!answer.isNullOrBlank() && answer.first().isLowerCase()) to "Профессия должна начинаться со строчной буквы"
		},
		MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
			override fun nextQuestion(): Question = BDAY
			override fun validation(answer: String?): Pair<Boolean, String> =
				(!answer.isNullOrBlank() && answer.all{ !it.isDigit() }) to "Материал не должен содержать цифр"
		},
		BDAY("Когда меня создали?", listOf("2993")) {
			override fun nextQuestion(): Question = SERIAL
			override fun validation(answer: String?): Pair<Boolean, String> =
				(answer?.all { it.isDigit() } ?: false) to "Год моего рождения должен содержать только цифры"
		},
		SERIAL("Мой серийный номер?", listOf("2716057")) {
			override fun nextQuestion(): Question = IDLE
			override fun validation(answer: String?): Pair<Boolean, String> =
				(answer?.length == 7 && answer.all { it.isDigit() }) to "Серийный номер содержит только цифры, и их 7"
		},
		IDLE("На этом все, вопросов больше нет", listOf()) {
			override fun nextQuestion(): Question = this
			override fun validation(answer: String?): Pair<Boolean, String> = true to ""
		};

		abstract fun nextQuestion() : Question
		abstract fun validation(answer: String?): Pair<Boolean, String>
	}

	fun askQuestion() : String = when (question) {
		Question.NAME -> Question.NAME.question
		Question.PROFESSION -> Question.PROFESSION.question
		Question.MATERIAL -> Question.MATERIAL.question
		Question.BDAY -> Question.BDAY.question
		Question.SERIAL -> Question.SERIAL.question
		Question.IDLE -> Question.IDLE.question
	}

	fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
		val (valid, reason) = question.validation(answer)
		when {
			question == Question.IDLE -> {
				return question.question to status.color
			}

			!valid -> {
				return "${question.validation(answer).second}\n${question.question}" to status.color
			}

			question.answers.contains(answer.toLowerCase().trim()) -> {
				question = question.nextQuestion()
				return "Отлично - ты справился\n${question.question}" to status.color
			}

			else -> {
				if (status == Status.CRITICAL) {
					status = Status.NORMAL
					question = Question.NAME
					return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
				} else {
					status = status.nextStatus()
					return "Это неправильный ответ\n${question.question}" to status.color
				}
			}
		}
	}
}