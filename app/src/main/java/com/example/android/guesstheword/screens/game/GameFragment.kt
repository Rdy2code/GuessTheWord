/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played. Single activity.
 */
class GameFragment : Fragment() {

    //DataBinding Variable:
    // 1. Set dataBinding enabled block true in build.gradle.
    // 2. Add <layout> tag as root to xml layout file
    // 3. Create a binding object in the UI controller
    // 4. Replace setContentView() with DataBindingUtil.inflate() as below
    // 5. Use the binding object to replace all calls to findViewById()
    // 6. Kotlinize with binding.apply {... } if desired to set properties and calls on series of views
    private lateinit var binding: GameFragmentBinding

    //ViewModel Variable
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //When the word list is empty, the boolean in the ViewModel is set, and observer is notified
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished: Boolean ->
            if (hasFinished) {
                gameFinished()
            }
        })

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        //Assign the GameViewModel to the gameViewModel binding that is named in the game_fragment xml
        //layout. Data binding creates a listener and sets the listener on a view.
        binding.gameViewModel = viewModel
        //Set the fragment view as the lifecycle owner of the binding variable. This lets us remove
        //the observer for the word.
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        //Receiving fragment takes an integer argument called 'score' as part of the action
        //Set the value of this input to the data in the ViewModel
        //Use Elvis operator for default value if null
        action.score = viewModel.score.value?:0
        //Navigate to the ScoreFragment and pass the argument
        NavHostFragment.findNavController(this).navigate(action)
        //Add this call to reset the flag so that Toast message is only called once not every time
        //the Observer is activated on screen rotation and reconnected to the ViewModel
        viewModel.onGameFinishComplete()
    }
}
