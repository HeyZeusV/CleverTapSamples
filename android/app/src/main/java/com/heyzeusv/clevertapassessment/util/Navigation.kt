package com.heyzeusv.clevertapassessment.util

import kotlinx.serialization.Serializable

sealed class Screen {
	@Serializable
	data object Main : Screen()
	@Serializable
	data object Features : Screen()
	@Serializable
	data class Pill(val pill: String) : Screen()
	@Serializable
	data class InAppContent(val type: String) : Screen()
	@Serializable
	data object EventForm : Screen()
	@Serializable
	data object PushTemplates : Screen()
}