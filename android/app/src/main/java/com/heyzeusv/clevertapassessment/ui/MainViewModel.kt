package com.heyzeusv.clevertapassessment.ui

import androidx.core.bundle.Bundle
import androidx.lifecycle.ViewModel
import com.clevertap.android.sdk.CTInboxListener
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.InAppNotificationButtonListener
import com.clevertap.android.sdk.isNotNullAndBlank
import com.heyzeusv.clevertapassessment.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel(private val cleverTapAPI: CleverTapAPI) : ViewModel() {

	// used to determine which screen to navigate to
	private val _navigateTo = MutableStateFlow<Screen?>(null)
	val navigateTo: StateFlow<Screen?> get() = _navigateTo.asStateFlow()
	fun updateNavigateTo(newValue: Screen?) { _navigateTo.update { newValue } }

	fun setUpCleverTap(
		inAppListener: InAppNotificationButtonListener,
		inboxListener: CTInboxListener,
	) {
		cleverTapAPI.apply {
			setInAppNotificationButtonListener(inAppListener)
			cleverTapAPI.ctNotificationInboxListener = inboxListener
			cleverTapAPI.initializeInbox()
		}
	}

	// invokes Android permission dialog
	fun askPushNotificationPermission() {
		if (!cleverTapAPI.isPushPermissionGranted) {
			cleverTapAPI.promptForPushPermission(true)
		}
	}

	/**
	 *	Due to Android 12 update, have to manually handle push notification actions when Activity is
	 *	in Activity stack. Afterwards, pass call to action value (if any) for further processing.
	 */
	fun handleIntent(extras: Bundle?) {
		cleverTapAPI.pushNotificationClickedEvent(extras)

		extras?.let {
			val c2a = it.getString("wzrk_c2a")
			if (c2a.isNotNullAndBlank()) {
				handleCallToAction(c2a)
			} else {
				it.getString("wzrk_dl")?.let { value ->
					handleDeepLink(value)
				}
			}
		}
	}

	/**
	 * 	Update currently shown screen depending on passed [value].
	 */
	fun handleCallToAction(value: String) {
		when (value) {
			"Red Pill" -> _navigateTo.value = Screen.RedPill
			"Blue Pill" -> _navigateTo.value = Screen.BluePill
			else -> _navigateTo.value = null
		}
	}

	/**
	 * 	Manually handle Deep Links due to Android 12
	 */
	private fun handleDeepLink(value: String) {
		when {
			value.contains("red") -> _navigateTo.value = Screen.RedPill
			value.contains("blue") -> _navigateTo.value = Screen.BluePill
			else -> _navigateTo.value = null
		}
	}
}