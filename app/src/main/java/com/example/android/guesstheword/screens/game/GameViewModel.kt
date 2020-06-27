package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.GameViewModelTag
import timber.log.Timber

/**
 * 1. Add dependencies to build.gradle
 * 2. Create ViewModel subclass of ViewModel()
 * 3. In Activity or Fragment, initialize the ViewModel subclass
 * 4. Move UI data variables and data logic into the ViewModel subclass
 */
class GameViewModel: ViewModel() {

    companion object {
        //Time when the game is over
        private const val DONE = 0L

        //Countdown time interval
        private const val ONE_SECOND = 1000L

        //Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() {
            return _currentTime
        }

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current word, encapsulated with Kotlin Backing Property
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() {
            return _word
        }

    // The current score
    //Use Kotlin Backing Property to separate edit and read versions
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() {
            return _score
        }

    //Boolean flag that notifies Observer when the word list is empty
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() {
            return _eventGameFinish
        }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    // The list of words - the front of the list is the next word to guess
    public lateinit var wordList: MutableList<String>

    // Member variable for timer
    private val timer: CountDownTimer

    init {
        _word.value = ""
        _score.value = 0
        Timber.i("GameViewModel created");
        resetList()
        nextWord()

        timer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ ONE_SECOND
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed");
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    //region Button presses
    public fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    public fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }
    //endregion
}