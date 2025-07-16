/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { NewAppScreen } from '@react-native/new-app-screen';
import { Button, StatusBar, StyleSheet, Text, TextInput, useColorScheme, View } from 'react-native';

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <View style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
			<Text></Text>
      <Text>Current AccountId:{"\n"}???</Text>
      <TextInput
				style={{
					height: 40,
					borderColor: 'gray',
					borderWidth: 1,
				}}
      	placeholder='Identity'
			/>
			<View style={styles.row}>
				<View style={{flex: 1}}>
					<Button
						onPress={() =>
						{
						}}
						title='Create User'
					/>
				</View>
				<View style={{flex: .2}}/>
				<View style={{flex: 1}}>
					<Button
						onPress={() =>
						{
						}}
						title='Login'
					/>
				</View>
			</View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
	row: {
		flexDirection: "row",
	}
});

export default App;
