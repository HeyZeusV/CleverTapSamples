package com.heyzeusv.clevertapassessment.ui.features

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.variables.callbacks.VariablesChangedCallback
import com.heyzeusv.clevertapassessment.util.RemoteConfigValues
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.roundToInt
import kotlin.random.Random

private const val LOG_TAG = "CleverTapAssessment_MainViewModel"
class FeaturesViewModel(private val cleverTapAPI: CleverTapAPI) : ViewModel() {

	// Used to display switching between test users
	private val _cleverTapId = MutableStateFlow(cleverTapAPI.cleverTapID)
	val cleverTapId: StateFlow<String> get() = _cleverTapId.asStateFlow()

	// Retrieves most Remote Config value
	val remoteConfig: StateFlow<RemoteConfigValues> = callbackFlow {
		val variablesChanged = object : VariablesChangedCallback() {
			override fun variablesChanged() {
				Log.i(LOG_TAG, "variablesChanged called")
				val int = cleverTapAPI.getVariableValue("var_int") as Double
				val long = cleverTapAPI.getVariableValue("var_long") as Double
				val float = cleverTapAPI.getVariableValue("var_float") as Double
				val double = cleverTapAPI.getVariableValue("var_double") as Double
				val string = cleverTapAPI.getVariableValue("var_string") as String
				val boolean = cleverTapAPI.getVariableValue("var_boolean") as Boolean

				val values = RemoteConfigValues(
					int.toInt(), long.toLong(), float.toFloat(), double, string, boolean
				)
				Log.i(LOG_TAG, "variablesChanged - sending $values")
				trySend(values)
			}
		}

		cleverTapAPI.addVariablesChangedCallback(variablesChanged)

		awaitClose {
			Log.i(LOG_TAG, "Removing VariablesChangedCallbacks")
			cleverTapAPI.removeAllVariablesChangedCallbacks()
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(),
		initialValue = RemoteConfigValues()
	)

	/**
	 * 	Creates new account with hard coded values.
	 */
	fun createAccountWithIdentity(identity: String) {
		if (!cleverTapAPI.isPushPermissionGranted) {
			cleverTapAPI.promptForPushPermission(true)
		} else {
			viewModelScope.launch {
				val profileUpdate = mapOf(
					"Name" to identity,
					"Identity" to identity,
					"Email" to "$identity@gmail.com",
					"DOB" to Date(),
					"MyStuff" to listOf("Random", "Stuff"),
				)

				Log.i(LOG_TAG, "createAccount - create account with properties: $profileUpdate")

				// Used to check if user has changed by difference in CleverTap ids.
				val currentId = _cleverTapId.value
				cleverTapAPI.onUserLogin(profileUpdate)
				// Logging in is not instant, so add delay of 1 second between checks.
				while (currentId == cleverTapAPI.cleverTapID) {
					_cleverTapId.value = "Loading"
					delay(1000)
				}
				_cleverTapId.value = cleverTapAPI.cleverTapID
			}
		}
	}

	/**
	 * 	Log into user with given [identity] and update [_cleverTapId].
	 */
	fun logIntoAccountWithIdentity(identity: String) {
		viewModelScope.launch {
			Log.i(LOG_TAG, "logIntoAccountWithIdentity - log into account with identity: $identity")
			val profileUpdate = mapOf("Identity" to identity)
			// Used to check if user has changed by difference in CleverTap ids.
			val currentId = _cleverTapId.value

			cleverTapAPI.onUserLogin(profileUpdate)
			// Logging in is not instant, so add delay of 1 second between checks.
			while (currentId == cleverTapAPI.cleverTapID) {
				_cleverTapId.value = "Loading"
				delay(1000)
			}
			_cleverTapId.value = cleverTapAPI.cleverTapID
		}
	}

	/***
	 * 	Pushes 1-5 random strings to currently logged in user as "MyStuff" user property and pushes
	 * 	"Update MyStuff" event.
	 */
	fun updateMyStuff() {
		val stuff = mutableListOf<String>()
		repeat(Random.Default.nextInt(0, 5)) {
			stuff.add(randomString())
		}

		val profileUpdate = mapOf("MyStuff" to stuff)
		Log.i(LOG_TAG, "updateMyStuff - push to user: $profileUpdate")

		cleverTapAPI.pushProfile(profileUpdate)
		cleverTapAPI.pushEvent("Update MyStuff")
	}

	/**
	 * 	Pushes event "Product Viewed" with the following properties: [productId], [productName],
	 * 	and a random [Double] price. Afterwards, pushes email that includes [emailId] to currently
	 * 	logged in user.
	 *
	 * 	Tech Section Steps 4/5
	 */
	fun productViewedEvent(productId: String, productName: String, emailId: String) {
		val checkedId = if (productId.isBlank()) 1 else productId.toInt()
		val productPrice = (Random.Default.nextDouble(1.00, 100.00) * 100.0).roundToInt() / 100.0
		val productViewedAction = mapOf(
			"Product Id" to checkedId,
			"Product Name" to productName,
			"Product Price" to productPrice,
		)
		Log.i(LOG_TAG, "productViewedEvent - event properties: $productViewedAction")
		cleverTapAPI.pushEvent("Product Viewed", productViewedAction)

		val profileUpdate = mapOf("Email" to "clevertap+$emailId@gmail.com")
		Log.i(LOG_TAG, "productViewedEvent - push to user: $profileUpdate")
		cleverTapAPI.pushProfile(profileUpdate)
	}

	// Used for Push Notification with actions
	fun selectPillEvent() {
		cleverTapAPI.pushEvent("Select Pill")
	}

	// Used for In-App notifications
	fun inAppBasicEvent() {
		cleverTapAPI.pushEvent("In-App Basic")
	}

	// Used for In-App notifications that use deeplinks
	fun inAppDeepLinkEvent() {
		cleverTapAPI.pushEvent("In-App Deep Link")
	}

	// Used for In-App Journey with GIF notifications
	fun inAppGifPushJourney() {
		cleverTapAPI.pushEvent("In-App GIF Push Journey")
	}

	// Used for In-App Journey with video notifications
	fun inAppVideoPushJourney() {
		cleverTapAPI.pushEvent("In-App Video Push Journey")
	}

	// Used for In-App Journey with GIF notifications
	fun inAppGifInAppJourney() {
		cleverTapAPI.pushEvent("In-App GIF In-App Journey")
	}

	// Used for In-App Journey with video notifications
	fun inAppVideoInAppJourney() {
		cleverTapAPI.pushEvent("In-App Video In-App Journey")
	}

	// Resume receiving In-App notifications
	fun inAppResume() {
		cleverTapAPI.resumeInAppNotifications()
	}

	// Stop In-App notifications, but keep them in queue for when resume is called
	fun inAppSuspend() {
		cleverTapAPI.suspendInAppNotifications()
	}

	// Stop In-App notifications and discard any new ones that come in
	fun inAppDiscard() {
		cleverTapAPI.discardInAppNotifications()
	}

	fun showAppInbox() {
		cleverTapAPI.showAppInbox()
	}

	/**
	 * 	Define variables and sync them to Dashboard in order to be used as Remote Config. Must be
	 * 	called when logged in user is marked as test account.
	 */
	private fun defineAndSyncVariables() {
		cleverTapAPI.defineVariable("var_byte", 1)
		cleverTapAPI.defineVariable("var_short", 2)
		cleverTapAPI.defineVariable("var_int", 3)
		cleverTapAPI.defineVariable("var_long", 4L)
		cleverTapAPI.defineVariable("var_float", 5F)
		cleverTapAPI.defineVariable("var_double", 6.0)
		cleverTapAPI.defineVariable("var_string", "Remote Config")
		cleverTapAPI.defineVariable("var_boolean", true)

		cleverTapAPI.syncVariables()
	}

	/**
	 * 	Attempt to fetch Remote Config variables. This will cause Remote Config callbacks to be
	 * 	triggered.
	 */
	fun fetchVariables() {
		cleverTapAPI.fetchVariables { isSuccess ->
			Log.i(LOG_TAG, "fetchVariables - fetch is successful: $isSuccess")
		}
	}

	/**
	 * 	Produce a random 1-10 length string made up of letters and digits.
	 */
	private fun randomString(): String {
		val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

		return (1..10)
			.map { Random.Default.nextInt(0, charPool.size).let { charPool[it] } }
			.joinToString("")
	}

//	init {
//		defineAndSyncVariables()
//	}
}