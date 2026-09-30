Meta Lead Ads to React Native Live Feed

A Proof of Concept: when someone submits a Meta Lead Ad form,
the lead shows up instantly in an already open React Native app screen.
No manual refresh or tap needed.

How it works?
Meta Lead Ad submitted
then
Meta sends a webhook to the backend
and then
Backend fetches the full lead details from the Graph API
and then
Backend pushes the lead to the app over a WebSocket
and then
App updates the list live

What's in this repo?
backend/- Spring Boot app. Receives the webhook, calls the Graph API, broadcasts new leads over WebSocket.
mobile/- React Native (Expo) app. Shows the leads list and updates live.


How am i testing it?
Using Meta's Lead Ads testing tool to submit a test lead.
It appears at the top of the app's list within a couple seconds,
with the app screen already open and untouched.

Some Notes that i thought could be informational:
1. Leads are stored in memory only, they reset when the backend restarts.
2. No authentication on the endpoints. Which is fine for a PoC, not for production.
3. Page access tokens expire in about an hour so i will have to regenerate before testing.