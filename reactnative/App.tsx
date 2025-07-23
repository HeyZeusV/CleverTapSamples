/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */
import React from 'react';
import { Pressable, StatusBar, StyleSheet, Text, TextInput, useColorScheme, View } from 'react-native';
import CleverTap from 'clevertap-react-native'

function App() {
  const isDarkMode = useColorScheme() === 'dark'
	const [identityText, onChangeIdentityText] = React.useState("")

  return (
    <View style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
			<Text></Text>
      <TextInput
				style={{
					height: 40,
					borderColor: 'gray',
					borderWidth: 1,
				}}
				onChangeText={onChangeIdentityText}
				value={identityText}
      	placeholder='Identity'
			/>
			<Pressable onPress={() =>{CreateLogin(identityText)}}>
				<View style={styles.buttonContainer}>
					<Text style={styles.buttonText}>Create/Login</Text>
				</View>
			</Pressable>
			<Pressable onPress={() => {ItemViewedEvent()}}>
				<View style={styles.buttonContainer}>
					<Text style={styles.buttonText}>Item Viewed Event</Text>
				</View>
			</Pressable>
			<Pressable onPress={() => {ItemPurchasedEvent()}}>
				<View style={styles.buttonContainer}>
					<Text style={styles.buttonText}>Item Purchased Event</Text>
				</View>
			</Pressable>
    </View>
  );
}

function CreateLogin(identity: String) {
	var props = {
		'Name': identity,
		'Identity': identity,
		'Favorite Color': 'Purple',
		'Dogs': 3
	}
	CleverTap.onUserLogin(props)
	CleverTap.promptForPushPermission(true)
}

function ItemViewedEvent() {
	let merchantId = GenerateRandomNumber(1, 10)
	let itemId = GenerateRandomNumber(1, 10)
	var props = {
		'Merchant': 'Merchant ' + merchantId,
		'Item': 'Item ' + itemId,
		'Price': GenerateRandomNumber(1, 100)
	}
	CleverTap.recordEvent('Product Viewed', props)
}

function ItemPurchasedEvent() {
	let numOfItems = GenerateRandomNumber(1, 10)
	let itemPrices = Array.from({length: numOfItems}, () => GenerateRandomNumber(1, 100))
	let itemPricesSum = itemPrices.reduce((a,b) => a+b)
	let items = Array()

	var props = {
		'Total': itemPricesSum,
		'Date': new Date()
	}

	for (let i = 1; i <= numOfItems; i++) {
		let merchantId = GenerateRandomNumber(1, 10)
		let itemId = GenerateRandomNumber(1, 10)

		let item = { 
			'Merchant': 'Merchant ' + merchantId,
			'Item': 'Item ' + itemId,
			'Price': itemPrices[i - 1]
		}
		items.push(item)
	}
	
	CleverTap.recordChargedEvent(props, items)
}

function GenerateRandomNumber(min: number, max: number) {
	const number = Math.floor(Math.random() * (max - min + 1)) + min
	return number
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
	buttonContainer: {
		backgroundColor: 'blue',
		minHeight: 50,
		flex: 1,
		alignItems: 'center',
		justifyContent: 'center',
		marginBottom: 70
	},
	 buttonText: {
		color: 'white',
		fontWeight: 700
	 }
});

export default App;
