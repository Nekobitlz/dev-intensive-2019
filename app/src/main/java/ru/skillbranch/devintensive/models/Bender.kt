package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = question.question

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${askQuestion()}" to status.color
        } else {
            if (status != Status.CRITICAL) {
                status = status.nextStatus()
                "Это не правильный ответ\n${askQuestion()}" to status.color
            } else {
                question = Question.NAME
                status = Status.NORMAL
                "Это неправильный ответ. Давай все по новой\n${askQuestion()}" to status.color
            }
        }
    }

    fun addValidation(question: Question): String {
        return when (question) {
            Question.NAME -> "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> "Материал не должен содержать цифр"
            Question.BDAY -> "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> "" //игнорировать валидацию
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status = if (this.ordinal < values().lastIndex) {
            values()[this.ordinal + 1]
        } else {
            values()[0]
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun validate(answer: String): Boolean = answer.trim().first().isUpperCase()
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validate(answer: String): Boolean = answer.trim().first().isLowerCase()
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("""^((?!\d).)*$"""))
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("""^\d*$"""))
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("""^\d{7}$"""))
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validate(answer: String): Boolean = true
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String):Boolean
    }
}