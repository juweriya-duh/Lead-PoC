

const IS_NGROK = true;
const HOST = "ion-reveal-bulldog.ngrok-free.dev";

export const API_BASE_URL = IS_NGROK ? `https://${HOST}` : `http://${HOST}`;

export const WS_URL = IS_NGROK ? `wss://${HOST}/ws/leads` : `ws://${HOST}/ws/leads`;