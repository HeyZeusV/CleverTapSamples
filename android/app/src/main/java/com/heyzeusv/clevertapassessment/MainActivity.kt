package com.heyzeusv.clevertapassessment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.clevertap.android.sdk.CTInboxListener
import com.clevertap.android.sdk.InAppNotificationButtonListener
import com.heyzeusv.clevertapassessment.ui.MainScreen
import com.heyzeusv.clevertapassessment.ui.MainViewModel
import com.heyzeusv.clevertapassessment.ui.eventform.EventFormScreen
import com.heyzeusv.clevertapassessment.ui.features.FeaturesScreen
import com.heyzeusv.clevertapassessment.ui.features.InAppScreen
import com.heyzeusv.clevertapassessment.ui.features.PillScreen
import com.heyzeusv.clevertapassessment.ui.push.PushTemplatesScreen
import com.heyzeusv.clevertapassessment.ui.theme.CleverTapAssessmentTheme
import com.heyzeusv.clevertapassessment.util.NotificationUtils
import com.heyzeusv.clevertapassessment.util.Pill.BLUE
import com.heyzeusv.clevertapassessment.util.Pill.RED
import com.heyzeusv.clevertapassessment.util.Screen
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import java.util.HashMap

class MainActivity : ComponentActivity(), InAppNotificationButtonListener, CTInboxListener {

	private val mainVM: MainViewModel by inject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mainVM.setUpCleverTap(this, this)
		enableEdgeToEdge()
		setContent {
			CleverTapAssessmentTheme {
				KoinAndroidContext {
					CleverTapAssessmentApp(
						mainVM = mainVM,
						navController = rememberNavController(),
					)
				}
			}
		}
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)

		// On Android 12, Raise notification clicked event when Activity is already running in activity backstack
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			mainVM.handleIntent(intent.extras)
			NotificationUtils.dismissNotification(intent, applicationContext)
		}
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()

		// Clear notification when intent contains extras.
		this.intent.extras?.let {
			mainVM.handleIntent(it)
			NotificationUtils.dismissNotification(this.intent, applicationContext)
		}
	}

	/**
	 * 	Handle In-App notifications with custom key-value.
	 */
	override fun onInAppButtonClick(p0: HashMap<String, String>?) {
		p0?.let {
			try {
				mainVM.handleCallToAction(p0.values.first())
			} catch (e: NoSuchElementException) {
				Log.d("CleverTap", "Error: ${e.message}")
				mainVM.handleCallToAction("")
			}
		}
	}

	/**
	 * 	Called if App Inbox was successfully initialized.
	 */
	override fun inboxDidInitialize() {
		Log.i("CleverTapAssessment", "inboxDidInitialize() called")
	}

	/**
	 * 	Not 100% sure, but I think this is mostly used for custom App Inbox
	 */
	override fun inboxMessagesDidUpdate() {
		Log.i("CleverTapAssessment", "inboxMessagesDidUpdate() called")
	}
}

@Composable
fun CleverTapAssessmentApp(
	mainVM: MainViewModel,
	navController: NavHostController,
) {
	val navigateTo by mainVM.navigateTo.collectAsState()

	LaunchedEffect(navigateTo) {
		if (navigateTo != null) {
			navController.navigate(navigateTo!!)
			mainVM.updateNavigateTo(null)
		}
	}
	LaunchedEffect(Unit) { mainVM.askPushNotificationPermission() }
	NavHost(
		navController = navController,
		startDestination = Screen.Main,
	) {
		composable<Screen.Main> {
			MainScreen(
				featuresOnClick = { navController.navigate(Screen.Features) },
				eventFormOnClick = { navController.navigate(Screen.EventForm) },
				pushTemplatesOnClick = { navController.navigate(Screen.PushTemplates) },
			)
		}
		composable<Screen.Features> { FeaturesScreen() }
		composable<Screen.Pill>(
			deepLinks = listOf(
				navDeepLink<Screen.Pill>(basePath = "https://www.clevertap-jesus.com/pill")
			),
		) { backStackEntry ->
			val pillSelected = backStackEntry.toRoute<Screen.Pill>().pill
			when (pillSelected) {
				"red-pill" -> PillScreen(RED)
				else -> PillScreen(BLUE)
			}
		}
		composable<Screen.InAppContent>(
			deepLinks = listOf(
				navDeepLink<Screen.InAppContent>(basePath = "https://www.clevertap-jesus.com/inapp")
			),
		) { backStackEntry ->
			val type = backStackEntry.toRoute<Screen.InAppContent>().type
			InAppScreen { mainVM.handleInAppContent(type) }
		}
		composable<Screen.EventForm> { EventFormScreen() }
		composable<Screen.PushTemplates> { PushTemplatesScreen() }
	}
}