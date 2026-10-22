package ru.chyrb.victorina1chyrbanova

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int
)