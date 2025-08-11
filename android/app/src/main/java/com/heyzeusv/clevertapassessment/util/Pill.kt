package com.heyzeusv.clevertapassessment.util

import androidx.compose.ui.graphics.Color

enum class Pill(val title: String, val color: Color, val text: String, val deepLink: String) {
	RED(
		title = "Red Pill",
		color = Color.Red,
		text = "You take the red pill... you stay in Wonderland, and I show you how deep the rabbit hole goes.",
		deepLink = "red-pill",
	),
	BLUE(
		title = "Blue Pill",
		color = Color.Blue,
		text = "You take the blue pill... the story ends, you wake up in your bed and believe whatever you want to believe.",
		deepLink = "blue-pill",
	);
}