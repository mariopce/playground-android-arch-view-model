package pl.saramak.test.viewmodeltest.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import pl.saramak.test.viewmodeltest.R
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    val uiContext = UI
    val job = Job()
    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private lateinit var  teamScoreADisplay: TextView
    private lateinit var  teamScoreBDisplay: TextView

    private lateinit var addPointsForAButton: Button
    private lateinit var addPointsForBButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamScoreADisplay =  view.findViewById<TextView>(R.id.team_score1);
        teamScoreBDisplay =  view.findViewById<TextView>(R.id.team_score2);
        addPointsForAButton = view.findViewById<Button>(R.id.button);
        addPointsForBButton = view.findViewById<Button>(R.id.button2);
        addPointsForAButton.setOnClickListener { viewModel.scoreTeamA ++; displayScore();}
        addPointsForBButton.setOnClickListener { viewModel.scoreTeamB ++ ; displayScore();}


        launch (uiContext + exceptionHandler, parent = job) {
            val w1 = async {work()}
            val w2 = async {work()}
            Log.d("app", "Work result " + w1.await() + " "+ w2.await())
            addPointsForAButton.text = ""+ w1.await()
            addPointsForBButton.text = ""+ w2.await()
            Toast.makeText(addPointsForAButton.context, "Work result " + w1 + " " + w2, Toast.LENGTH_SHORT).show()
        }
        Log.d("app", "after launch")
    }

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        Log.d("app", throwable.message ?: "")
        addPointsForAButton.text = "error"
    }
    suspend fun work() : Long = withContext(CommonPool) {
        Log.d("app","Work start");
        delay(2, TimeUnit.SECONDS) // imitate long running operation
        val result = System.currentTimeMillis()
        Log.d("app","Work stop " + result)
        if (result % 2 == 0L){
            throw IllegalAccessError("err")
        }
        result
    }

    private fun displayScore() {
        teamScoreADisplay.text = viewModel.scoreTeamA.toString()
        teamScoreBDisplay.text = viewModel.scoreTeamB.toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScoreViewModel::class.java)
        displayScore()
    }

}
