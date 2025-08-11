package com.heyzeusv.clevertapassessment.ui.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heyzeusv.clevertapassessment.ui.CreateLoginForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeaturesScreen(featuresVm: FeaturesViewModel = koinViewModel()) {
	val cleverTapId by featuresVm.cleverTapId.collectAsState()
	val remoteConfig by featuresVm.remoteConfig.collectAsState()

	var productId by remember { mutableStateOf("1") }
	var productName by remember { mutableStateOf("CleverTap") }
	var emailId by remember { mutableStateOf("jesus") }

	Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(horizontal = 8.dp)
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.spacedBy(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			CreateLoginForm(
				cleverTapId = cleverTapId,
				createOnClick = featuresVm::createAccountWithIdentity,
				loginOnClick = featuresVm::logIntoAccountWithIdentity,
			)
			Button(
				onClick = { featuresVm.updateMyStuff() },
				enabled = cleverTapId != "Loading",
			) {
				Text("Update dummy stuff")
			}
			Card {
				Column(
					modifier = Modifier.padding(all = 8.dp),
					verticalArrangement = Arrangement.spacedBy(4.dp)
				) {
					Text(
						text = "Push Notifications",
						modifier = Modifier.fillMaxWidth(),
						style = MaterialTheme.typography.titleMedium.copy(
							textAlign = TextAlign.Center,
						),
					)
					OutlinedTextField(
						value = productId,
						onValueChange = { productId = it },
						modifier = Modifier.fillMaxWidth(),
						label = { Text("Product Id") },
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					)
					OutlinedTextField(
						value = productName,
						onValueChange = { productName = it },
						modifier = Modifier.fillMaxWidth(),
						label = { Text("Product Name") },
					)
					OutlinedTextField(
						value = emailId,
						onValueChange = { emailId = it },
						modifier = Modifier.fillMaxWidth(),
						label = { Text("Email Id") },
					)
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = { featuresVm.productViewedEvent(productId, productName, emailId) },
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Product Viewed Event")
						}
						Button(
							onClick = featuresVm::selectPillEvent,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Select Pill Event")
						}
					}
				}
			}
			Card {
				Column(modifier = Modifier.padding(all = 8.dp)) {
					Text(
						text = "In-App",
						modifier = Modifier.fillMaxWidth(),
						style = MaterialTheme.typography.titleMedium.copy(
							textAlign = TextAlign.Center,
						),
					)
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = featuresVm::inAppBasicEvent,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Basic")
						}
						Button(
							onClick = featuresVm::inAppDeepLinkEvent,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Deep Link")
						}
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = featuresVm::inAppGifPushJourney,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("GIF Push")
						}
						Button(
							onClick = featuresVm::inAppGifInAppJourney,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("GIF In-App")
						}
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = featuresVm::inAppVideoPushJourney,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Video Push")
						}
						Button(
							onClick = featuresVm::inAppVideoInAppJourney,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Video In-App")
						}
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = featuresVm::inAppResume,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Resume")
						}
						Button(
							onClick = featuresVm::inAppSuspend,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Suspend")
						}
						Button(
							onClick = featuresVm::inAppDiscard,
							modifier = Modifier.weight(1f),
							enabled = cleverTapId != "Loading",
						) {
							Text("Discard")
						}
					}
				}
			}
			Button(
				onClick = { featuresVm.showAppInbox() }
			) {
				Text("Show App Inbox")
			}
			Card {
				Column(modifier = Modifier.padding(all = 8.dp)) {
					Text(
						text = "Remote Config",
						modifier = Modifier.fillMaxWidth(),
						style = MaterialTheme.typography.titleMedium.copy(
							textAlign = TextAlign.Center,
						),
					)
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Button(
							onClick = featuresVm::fetchVariables,
							modifier = Modifier.weight(1f),
						) {
							Text("Fetch Variables")
						}
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = "Int: ${remoteConfig.int}\nLong: ${remoteConfig.long}\nFloat: ${remoteConfig.float}",
							modifier = Modifier.weight(1f),
						)
						Text(
							text = "Double: ${remoteConfig.double}\nString: ${remoteConfig.string}\n${remoteConfig.boolean}",
							modifier = Modifier.weight(1f),
						)
					}
				}
			}
		}
	}
}