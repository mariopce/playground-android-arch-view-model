package pl.saramak.test.viewmodeltest.ui.main

import android.arch.lifecycle.ViewModel

data class ScoreViewModel(var scoreTeamA: Int = 0, var scoreTeamB : Int = 0) : ViewModel() {
}
