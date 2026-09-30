import React, { useEffect, useRef, useState, useCallback } from "react";

import {
  View,
  Text,
  FlatList,
  StyleSheet,
  ActivityIndicator,
} from "react-native";

import { API_BASE_URL, WS_URL } from "../config";

export default function LeadsScreen() {

  const [leads, setLeads] = useState([]);
  const [loading, setLoading] = useState(true);
  const [connected, setConnected] = useState(false);

  const wsRef = useRef(null);
  const reconnectTimer = useRef(null);



   useEffect(() => {
    fetch(`${API_BASE_URL}/api/leads`)
      .then((res) => res.json())
      .then((data) => setLeads(data))
      .catch((err) => console.warn("Initial fetch failed:", err.message))
      .finally(() => setLoading(false));

      }, []);




  const connect = useCallback(() => {
    const ws = new WebSocket(WS_URL);
    wsRef.current = ws;

    ws.onopen = () => setConnected(true);

    ws.onmessage = (event) => {

      try {
        const payload = JSON.parse(event.data);
        if (payload.type === "new_lead" && payload.lead) {
          setLeads((prev) => [payload.lead, ...prev]);
        }
      } catch (e) {
        console.warn("Bad WS payload:", e.message);
      }};



    ws.onerror = () => setConnected(false);
    ws.onclose = () => {
      setConnected(false);
      reconnectTimer.current = setTimeout(connect, 2000);
               };
           },
      []);




  useEffect(() => {
    connect();

    return () => {
      clearTimeout(reconnectTimer.current);
      wsRef.current?.close();};

    }, [connect]);




  const renderItem = ({ item }) => {
    const fields = item.fields || {};
    const name = fields.full_name || fields.first_name || "Unnamed lead";
    return (
      <View style={styles.card}>

        <Text style={styles.name}>{name}</Text>
        {Object.entries(fields).map(([key, value]) => (
          <Text key={key} style={styles.fieldLine}>
            <Text style={styles.fieldKey}>{key}: </Text>
            {value}
          </Text>))}
        <Text style={styles.meta}>{item.createdTime} · form {item.formId}</Text>
      </View>
    );};



  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Leads</Text>
        <View style={styles.statusRow}>
          <View
            style={[
              styles.dot,
              { backgroundColor: connected ? "#2ecc71" : "#e74c3c" },]} />
          <Text style={styles.statusText}>
            {connected ? "Live" : "Reconnecting…"}
          </Text>
        </View>
      </View>


      {loading ? (
        <ActivityIndicator style={{ marginTop: 40 }} />
      ) : (
        <FlatList
          data={leads}
          keyExtractor={(item, idx) => item.leadgenId ?? String(idx)}
          renderItem={renderItem}
          contentContainerStyle={styles.list}
          ListEmptyComponent={
            <Text style={styles.empty}> No leads yet. Submit one via the Meta Lead Ads Testing Tool. </Text>
          } />
      )}</View>
  ); }



const styles = StyleSheet.create({
  container: { flex: 1 },
  header: {
    marginTop:25,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    paddingTop: 25,
    paddingBottom: 12,
  },
  title: { fontSize: 28, fontWeight: "700" },
  statusRow: { flexDirection: "row", alignItems: "center" },
  dot: { width: 8, height: 8, borderRadius: 4, marginRight: 6 },
  statusText: { fontSize: 13, color: "#555" },
  list: { paddingHorizontal: 16, paddingBottom: 24 },
  card: {
    backgroundColor: "#fff",
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    shadowColor: "#000",
    shadowOpacity: 0.06,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 2,
  },
  name: {
    fontSize: 17,
    fontWeight: "600",
    marginBottom: 6
  },
  fieldLine:
      {
        fontSize: 14,
        color: "#333",
        marginBottom: 2
      },
  fieldKey:
      {
        fontWeight: "600",
        color: "#666"
      },
  meta: {
    fontSize: 12,
    color: "#999",
    marginTop: 8
  },
  empty: {
    textAlign: "center",
    marginTop: 60,
    color: "#888" },
});
