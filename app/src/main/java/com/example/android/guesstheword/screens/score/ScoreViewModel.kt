package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

/**
 * Contains parameterized constructor to store the score from the GameFragment. This class is
 * instantiated with a factory method
 */
class ScoreViewModel (finalScore: Int): ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() {
            return _score
        }

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() {
            return _eventPlayAgain
        }

    init {
        Timber.i("Final score is $finalScore")
        _score.value = finalScore
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}