package com.heyzeusv.clevertapassessment.ui.features

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppScreen(ctEvent: () -> Unit) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(text = "In-App")
				},
			)
		},
		modifier = Modifier.fillMaxSize(),
	) {
		LaunchedEffect(key1 = Unit) {
			Log.d("CleverTap", "Launching CT Event In-App Content")
			ctEvent()
		}
	}
}