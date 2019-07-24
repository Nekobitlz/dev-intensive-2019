package ru.skillbranch.devintensive.models

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = question.question

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> = when(question) {
        Question.IDLE -> question.question to status.color
        else -> "${writeAnswer(answer)}\n${question.question}" to status.color
    }

    fun addValidationMessage(question: Question): String = when(question) {
        Question.NAME -> "Имя должно начинаться с заглавной буквы"
        Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
        Question.MATERIAL -> "Материал не должен содержать цифр"
        Question.BDAY -> "Год моего рождения должен содержать только цифры"
        Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
        else -> "На этом все, вопросов больше нет"
    }

    private fun writeAnswer(answer: String): String = if (question.answer.contains(answer)) {
        question = question.nextQuestion()
        "Отлично - ты справился"
    } else {
        if (status == Status.CRITICAL) {
            question = Question.NAME
            status = Status.NORMAL

            "Это неправильный ответ. Давай все по новой"
        } else {
            status = status.nextStatus()
            "Это неправильный ответ"
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status = if (this.ordinal < values().lastIndex)
            values()[this.ordinal + 1]
        else
            values()[0]
    }

    enum class Question(val question: String, val answer: List<String>)  {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validate(answer: String): Boolean = answer.trim().firstOrNull()?.isUpperCase() ?: false
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender"))  {
            override fun nextQuestion(): Question = MATERIAL
            override fun validate(answer: String): Boolean = answer.trim().firstOrNull()?.isLowerCase() ?: false
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "iron", "wood", "metal")) {
            override fun nextQuestion(): Question = BDAY
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("\\d")).not()
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("^[0-9]*$"))
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("^[0-9]{7}$"))
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): Boolean = true
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String): Boolean
    }
}
