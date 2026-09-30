

import { StatusBar } from "expo-status-bar";
import React from "react";

import { SafeAreaView, StyleSheet } from "react-native";
import LeadsScreen from "./screens/LeadsScreen";

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <LeadsScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {

    flex: 1,
      marginTop: 25,
    backgroundColor: "#ffffff",
  },
});
