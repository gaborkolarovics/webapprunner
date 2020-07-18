const functions = require('firebase-functions');

var admin = require("firebase-admin");

var serviceAccount = require("./webapprunner-1093-firebase-adminsdk-ftbvi-db57a6fba5.json");

var message = {
  notification:{
    title:"FCM Message",
    body:"This is an FCM Message"
  },
  token: 'eCq-YabAQVSt-YKdxWLiJG:APA91bFPjaGoal3Pj4MZRTRYQnAM8UrZjPx5QHToH65idO8K-IzaVj1DiKNFhD7eJPcfynBOYrHBDUv5FtrF7kMTtXMF8JcjOAsYQrX7AdofZ4J0A5tnywTmGux6HsrrKfsvIaQGUFe8'
};

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://webapprunner-1093.firebaseio.com"
});


// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
    console.log("Test body: " + request.body.key1);
    // Send a message to the device corresponding to the provided
    // registration token.
    var fcmRespons = admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
      return "ok";
    })
    .catch((error) => {
      console.log('Error sending message:', error);
      return 'error';
    });
    
    response.send('Hello from Firebase! req: ' + request.body.key1 + ' - fcm: ' + fcmRespons);
});
