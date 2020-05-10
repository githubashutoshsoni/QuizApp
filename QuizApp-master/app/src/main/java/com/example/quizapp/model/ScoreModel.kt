package com.example.quizapp.model

import io.realm.RealmObject

open class ScoreModel : RealmObject() {
    var score: Int = 0;
}