const functions = require('firebase-functions');

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
    console.log("Test body: " + request.body.key1);
    response.send("Hello from Firebase! req: " + request.body.key1);
});
