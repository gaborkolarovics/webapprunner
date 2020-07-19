/**
 * WebAppRunner - sendPushNotification cloud function
 * 
 * @author Gábor KOLÁROVICS
 * @since 2020.07.19
 */
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const serviceAccount = require('./webapprunner-1093-firebase-adminsdk-ftbvi-db57a6fba5.json');

// Initialize Firebase admin
admin.initializeApp({ credential: admin.credential.cert(serviceAccount), databaseURL: 'https://webapprunner-1093.firebaseio.com' });

/**
 * Checks if a CharSequence is empty (""), null or whitespace only.
 * 
 * isBlank(null)      = true
 * isBlank("")        = true
 * isBlank(" ")       = true
 * isBlank("bob")     = false
 * isBlank("  bob  ") = false
 * 
 * @param {String} str 
 */
function isBlank(str) {
  if (str === null || str === undefined || str.length === 0) {
    return true;
  }
  if (str.length === 0) {
    return true;
  }
  for (let i = 0; i < str.length; i++) {
    if (str[i] !== '' && str[i] !== ' ' && str[i] !== '\n' && str[i] !== '\t') {
      return false;
    }
  }
  return true;
}

/**
 * Resolve FCM message from http request
 * 
 * @param {Request} request 
 */
function resolveNotificationFromRequest(request) {
  if (isBlank(request.body.token)) {
    throw new Error('Token is empty!');
  }
  if (isBlank(request.body.title)) {
    throw new Error('Title is empty!');
  }
  if (isBlank(request.body.message)) {
    throw new Error('Message is empty!');
  }

  return {
    notification: {
      title: request.body.title,
      body: request.body.message
    },
    token: request.body.token
  };
}

/**
 * SendPusNotification cloud function
 */
exports.sendPushNotification = functions.https.onRequest((request, response) => {
  let message;
  try {
    message = resolveNotificationFromRequest(request);
  } catch (error) {
    response.status(400).json({ message: 'Error resolve message: ' + error });
    return;
  }
  console.log('Message: ' + message);
  admin.messaging().send(message)
    .then((result) => {
      response.status(200).json({ message: 'Successfully sent message: ' + result });
      return result;
    })
    .catch((error) => {
      console.log('Error sending message:', error);
      response.status(500).json({ message: 'Error sending message: ' + error });
      return error;
    });

});
