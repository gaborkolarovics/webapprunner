const functions = require('firebase-functions');

var admin = require("firebase-admin");

var serviceAccount = require("./webapprunner-1093-firebase-adminsdk-ftbvi-db57a6fba5.json");

var message = {
  data: {
    score: '850',
    time: '2:45'
  },
  token: 'YOUR_REGISTRATION_TOKEN'
};

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://webapprunner-1093.firebaseio.com"
});


// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
    console.log("Test body: " + request.body.key1);
    response.send("Hello from Firebase! req: " + request.body.key1);
    
    // Send a message to the device corresponding to the provided
    // registration token.
    admin.messaging().send(message)
        .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
            return "ok";
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
});
