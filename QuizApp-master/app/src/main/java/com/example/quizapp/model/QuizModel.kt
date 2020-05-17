package com.example.quizapp.model

import androidx.recyclerview.widget.DiffUtil

data class QuizModel(
    var categoryId: Int,
    var category: String = "",
    var imgResUrl: String = ""
)

object QuizDiff : DiffUtil.ItemCallback<QuizModel>() {
    override fun areItemsTheSame(oldItem: QuizModel, newItem: QuizModel): Boolean {
        return (oldItem.category == newItem.category)

    }

    override fun areContentsTheSame(oldItem: QuizModel, newItem: QuizModel): Boolean {
        return oldItem == newItem
    }

}

val Quizes = listOf(

    QuizModel(9, "General Knowledge", "https://source.unsplash.com/random/800x600"),
    QuizModel(10, "Books", "https://source.unsplash.com/random/800x600"),
    QuizModel(11, "Film", "https://source.unsplash.com/random/800x600"),
    QuizModel(12, "Music", "https://source.unsplash.com/random/800x600"),
    QuizModel(13, "Musical and Theatres", "https://source.unsplash.com/random/800x600"),
    QuizModel(14, "Television", "https://source.unsplash.com/random/800x600"),
    QuizModel(15, "Video games", "https://source.unsplash.com/random/800x600"),
    QuizModel(16, "Board games", "https://source.unsplash.com/random/800x600"),
    QuizModel(17, "Science and Nature", "https://source.unsplash.com/random/800x600"),
    QuizModel(18, "Computers", "https://source.unsplash.com/random/800x600")

)

