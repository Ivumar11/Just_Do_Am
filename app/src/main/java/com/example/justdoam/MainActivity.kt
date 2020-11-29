package com.example.justdoam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

// Host activity
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // sets view
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.myNavHostFragment)
        // Hooks up NavigationUI with ActionBar
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    // We override this method to specify what happens when the up button is pressed, else nothing happens
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
