package com.example.pig_marco_ramos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pig_marco_ramos.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val defaultNames = arrayOf("Aitor Tilla", "Ana Conda", "Armando Broncas", "Aurora Boreal",
        "Bartolo Mesa", "Carmen Mente", "Dolores Delirio", "Elsa Pato", "Enrique Cido",
        "Esteban Dido", "Elba Lazo", "Fermin Tado", "Lola Mento", "Luz Cuesta", "Margarita Flores",
        "Paco Tilla", "Pere Gil", "Pío Nono", "Salvador Tumbado", "Zoila Vaca"
    )

    // Hashmap to know when all the recyclers view have been selected
    private val selectedItems = mutableMapOf<Int, Boolean>()
    private var totalRecyclers: Int = 0
    private var playerNumber = 0
    private var roundNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from the previous activity
        playerNumber = intent.getIntExtra("PLAYER_NUMBER", 2)
        roundNumber = intent.getStringExtra("ROUND_NUMBER")

        totalRecyclers = playerNumber

        // Array to store the data of the recyclers views and the players names
        val recyclers = arrayOf(
            PlayerSelector(binding.rvp1, binding.player1Player),
            PlayerSelector(binding.rvp2, binding.player2Player),
            PlayerSelector(binding.rvp3, binding.player3Player),
            PlayerSelector(binding.rvp4, binding.player4Player)
        )

        for (i in 0..<playerNumber) {
            val adapter = CustomAdapter(defaultNames, i)  { recyclerIndex, isSelected, name ->
                // Update the status of the names in the map
                selectedItems[recyclerIndex] = isSelected
                recyclers[i].title.text = name
                // Check if all the recyclers have been selected
                checkAllSelected(recyclers)
            }

            // Config all the recyclers
            recyclers[i].recyclerView.layoutManager = LinearLayoutManager(this)
            recyclers[i].recyclerView.adapter = adapter
            recyclers[i].recyclerView.visibility = View.VISIBLE
            recyclers[i].title.visibility = View.VISIBLE
        }
    }

    private fun checkAllSelected(recyclers: Array<PlayerSelector>) {
        // Check that all elements have one element selected
        if (selectedItems.size == totalRecyclers && selectedItems.values.all { it }) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("ROUND_NUMBER", roundNumber)
            intent.putExtra("ROUND_NUMBER", roundNumber)
            for (i in recyclers.indices) {
                intent.putExtra("PLAYER_$i", recyclers[i].title.text)
            }
            intent.putExtra("PLAYER_NUMBER", playerNumber)
            startActivity(intent)
        }
    }
}