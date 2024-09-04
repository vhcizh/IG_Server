// Import the scripts needed for Firebase Messaging
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');

// Firebase 설정
const firebaseConfig = {
      apiKey: "AIzaSyCiAfx7t19a2pF6llcR_2r02Jr23URKQBI",
      authDomain: "iguard-f7d74.firebaseapp.com",
      projectId: "iguard-f7d74",
      storageBucket: "iguard-f7d74.appspot.com",
      messagingSenderId: "434747743462",
      appId: "1:434747743462:web:91ed529af1cab5383c6f27",
      measurementId: "G-SC45BCKZ1Z"
};

// Firebase 초기화
firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// 백그라운드에서 메시지를 처리하기 위한 리스너 설정
messaging.onBackgroundMessage(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/firebase-logo.png' // 원하시는 아이콘으로 변경하세요
    };
    self.registration.showNotification(notificationTitle,
        notificationOptions);
});
