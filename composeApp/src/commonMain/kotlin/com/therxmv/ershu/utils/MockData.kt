package com.therxmv.ershu.utils

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.Exam
import com.therxmv.ershu.data.models.ExamCalendarModel
import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.models.RatingItem
import com.therxmv.ershu.data.models.RatingModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.models.SpecialtyModel

object MockData {
    val allFacultiesModel = AllFacultiesModel(
        listOf(
            FacultyModel("ФМІ", "fmi_schedule"),
            FacultyModel("ППФ", "ppf_schedule"),
        )
    )

    val allSpecialtyModel = AllSpecialtiesModel(
        allYears = listOf(
            listOf(),
            listOf(),
            listOf(SpecialtyModel(specialtyName = "ІПЗ-31")),
            listOf(),
            listOf()
        )
    )

    val allCallsScheduleModel = AllCallsScheduleModel(
        first = CallScheduleModel(
            time = listOf("8:00 - 9:20", "9:35 - 10:55", "11:10 - 12:30", "12:45 - 14:05"),
        ),
        second = CallScheduleModel(
            time = listOf("12:45 - 14:05", "14:15 - 15:35", "15:45 - 17:05", "17:15 - 18:35")
        )
    )

    val scheduleModel = ScheduleModel(
        week =
        listOf(
            listOf(
                LessonModel(lessonName = null, lessonNumber = "1", link = null),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "2", link = "https://meet.google.com/aam-bmke-eon"),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "3", link = "https://meet.google.com/aam-bmke-eon"),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "4", link = "https://meet.google.com/aam-bmke-eon")
            ),
            listOf(
                LessonModel(lessonName = "Системний аналіз та теорія прийняття рішень доц. Викладач І.П.", lessonNumber = "1", link = "https://us04web.zoom.us/j/4546240462?pwd=QTFnOWlkY1hSbDk2c0FXQVBPL1ZiUT09"),
                LessonModel(lessonName = "Системний аналіз та теорія прийняття рішень доц. Викладач І.П.", lessonNumber = "2", link = "https://us04web.zoom.us/j/4546240462?pwd=QTFnOWlkY1hSbDk2c0FXQVBPL1ZiUT09"),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "3", link = "https://meet.google.com/aam-bmke-eon"),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "4", link = "https://meet.google.com/aam-bmke-eon")
            ),
            listOf(),
            listOf(
                LessonModel(lessonName = "Операційні системи ст.в. Викладач Т.Г.", lessonNumber = "1", link = "https://meet.google.com/kqk-dqbq-mjr"),
                LessonModel(lessonName = "Операційні системи ст.в. Викладач Т.Г.", lessonNumber = "2", link = "https://meet.google.com/kqk-dqbq-mjr"),
                LessonModel(lessonName = "Системний аналіз та теорія прийняття рішень доц. Викладач І.П.", lessonNumber = "3", link = "https://us04web.zoom.us/j/4546240462?pwd=QTFnOWlkY1hSbDk2c0FXQVBPL1ZiUT09"),
                LessonModel(lessonName = "Інтернет речей доц. Викладач Н.В.", lessonNumber = "4", link = "https://meet.google.com/aam-bmke-eon")
            ),
            listOf(
                LessonModel(lessonName = "Системне програмування ст.в. Викладач Т.Г.", lessonNumber = "1", link = "https://meet.google.com/kqk-dqbq-mjr"),
                LessonModel(lessonName = "Системне програмування ст.в. Викладач Т.Г.", lessonNumber = "2", link = "https://meet.google.com/kqk-dqbq-mjr"),
                LessonModel(lessonName = null, lessonNumber = "3", link = null),
                LessonModel(lessonName = "Адміністрування баз даних проф. Викладач Ю.В.", lessonNumber = "4", link = "https://meet.google.com/xyc-chmy-yhh"),
                LessonModel(lessonName = "Адміністрування баз даних проф. Викладач Ю.В.", lessonNumber = "5", link = "https://meet.google.com/xyc-chmy-yhh")
            ),
            listOf()
        )
    )

    val ratingModel = RatingModel(
        list = listOf(
            RatingItem(
                name = "Філософія",
                credits = 3,
            ),
            RatingItem(
                name = "Аналіз даних",
                credits = 4,
            ),
            RatingItem(
                name = "Моделювання та проєктування програмного забезпечення",
                credits = 5,
            ),
            RatingItem(
                name = "Інтерфейси користувача та системні інтерфейси",
                credits = 4,
            ),
        )
    )

    val examCalendar = ExamCalendarModel(
        exams = listOf(
            Exam(
                teacher = "доц. Пелех О.Б.",
                lesson = "Економіка і право в IT-галузі",
                date = "19.11",
            ),
            Exam(
                teacher = "проф.Турбал Ю.В.",
                lesson = "Якість і тестування програмного забезпечення",
                date = "25.11",
            ),
            Exam(
                teacher = "в.Копелюк В.О",
                lesson = "Конструювання програмного забезпечення",
                date = "29.11",
            ),
        ),
        zalik = listOf(
            Exam(
                teacher = null,
                lesson = "Курсова робота",
                date = null,
            ),
            Exam(
                teacher = null,
                lesson = "Технологічна практика",
                date = null,
            ),
            Exam(
                teacher = null,
                lesson = "Основи стандартизації та патентознавства",
                date = null,
            ),
            Exam(
                teacher = null,
                lesson = "Іноземна мова для науково-дослідної комунікації",
                date = null,
            ),
            Exam(
                teacher = null,
                lesson = "Хмарні обчислення та технології",
                date = null,
            ),
        )
    )
}