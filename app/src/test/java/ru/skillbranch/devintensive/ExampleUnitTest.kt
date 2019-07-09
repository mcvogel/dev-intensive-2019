package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.Chat
import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
	@Test
	fun addition_isCorrect() {
		assertEquals(4, 2 + 2)
	}

	@Test
	fun test_instance() {
		val user = User("2", "John", "Cena")
		println(user)
	}

	@Test
	fun test_data_mapping() {
		val user = User.makeUser("Павел Стрелец")
		val newUser = user.copy(lastVisit = Date().add(-7, TimeUnits.DAY))
		println(newUser)

		val userView = newUser.toUserView()
		userView.printMe()
	}

	@Test
	fun test_copy() {
		val user = User.makeUser("John Wick")
		val user2 = user.copy(lastVisit = Date())
		val user3 = user.copy(lastVisit = Date().add(-2, TimeUnits.SECOND))
		val user4 = user.copy(lastName = "Cena", lastVisit = Date().add(2, TimeUnits.HOUR))
		println(
			"""
            ${user.lastVisit?.format()}
            ${user2.lastVisit?.format()}
            ${user3.lastVisit?.format()}
            ${user4.lastVisit?.format()}
        """.trimIndent()
		)
	}

	@Test
	fun test_abstract_factory() {
		val user = User.makeUser("Павел Стрелец")

		val txtMessage = BaseMessage.makeMessage(user, Chat("0"), payload = "any text message", type = "text")
		val imgMessage = BaseMessage.makeMessage(user, Chat("1"), payload = "any image url", type = "image")

		println(txtMessage.formatMessage())
		println(imgMessage.formatMessage())

		val txtMessageRecv = BaseMessage.makeMessage(
			user,
			Chat("0"),
			payload = "any text message",
			type = "text",
			isIncoming = true,
			date = Date().add(-12, TimeUnits.DAY)
		)

		val imgMessageRecv = BaseMessage.makeMessage(
			user,
			Chat("1"),
			payload = "any image url",
			type = "image",
			isIncoming = true,
			date = Date().add(-4, TimeUnits.HOUR)
		)

		println(txtMessageRecv.formatMessage())
		println(imgMessageRecv.formatMessage())
	}

	@Test
	fun test_parse_full_name() {
		assertEquals(Pair(null, null), Utils.parseFullName(null))
		assertEquals(Pair(null, null), Utils.parseFullName(""))
		assertEquals(Pair(null, null), Utils.parseFullName(" "))
		assertEquals(Pair("John", null), Utils.parseFullName("John"))
	}

	@Test
	fun test_date_format() {
		println(Date().format())
		println(Date().format("HH:mm"))
	}

	//Задание 2.5
	@Test
	fun test_date_add() {
		println(Date().add(2, TimeUnits.SECOND))
		println(Date().add(-4, TimeUnits.DAY))
	}

	//Задание 2.6
	@Test
	fun test_initials() {
		assertEquals("НБ", Utils.toInitials("Николай", "Басков"))
		assertEquals("НБ", Utils.toInitials("николай", "басков"))
		assertEquals("Б", Utils.toInitials(null, "Басков"))
		assertEquals("Б", Utils.toInitials("", "Басков"))
		assertEquals("Б", Utils.toInitials(null, "Басков"))
		assertEquals("Б", Utils.toInitials("", "басков"))
		assertEquals("Н", Utils.toInitials("Николай", null))
		assertEquals("Н", Utils.toInitials("Николай", ""))
		assertEquals("Н", Utils.toInitials("николай", null))
		assertEquals("Н", Utils.toInitials("николай", ""))
		assertEquals(null, Utils.toInitials(null, null))
		assertEquals(null, Utils.toInitials("", null))
		assertEquals(null, Utils.toInitials(null, ""))
		assertEquals(null, Utils.toInitials("", ""))
		assertEquals(null, Utils.toInitials(" ", ""))
		assertEquals(null, Utils.toInitials("", " "))
		assertEquals(null, Utils.toInitials(" ", " "))
	}

	//Задание 2.7
	@Test
	fun test_transliteration() {
		assertEquals("Zhenya Stereotipov", Utils.transliteration("Женя Стереотипов", " "))
		assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
		assertEquals("Zhirinovskii", Utils.transliteration("Жириновский", " "))
		assertEquals("zhirinovskiI", Utils.transliteration("жириновскиЙ", " "))
		assertEquals("Sh'erbakov", Utils.transliteration("Щербаков", " "))
	}

	//Задание 2.8 ty bgv26
	@Test
	fun test_humanizeDiff() {
		assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
		assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
		assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
		assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
		assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
		assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
	}


	//Задание 2.9
	@Test
	fun test_builder() {
		val user =
			User.Builder()
				.firstName("Владимир")
				.lastName("Жириновский")
				.rating(5)
				.isOnline(true)
				.lastVisit(Date().add(-2, TimeUnits.DAY))
				.build()

		print(user)
	}

}
