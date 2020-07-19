# README

Extend your web application with android features

* Signature
* Vibration
* Location
* Barcode
* NFC

## Google cloud function usage

Start develop:

```bash

$ poco up gfunc

$ docker exec -it webapprunner_gfunc_1 /bin/bash

$ firebase login

$ cd functions

$ npm i


```

Deploy:

```bash

$ cd /app

$ firebase deploy

```

Test:

```bash

$ curl -X POST -d key1=aaa https://us-central1-webapprunner-1093.cloudfunctions.net/helloWorld

$ curl -v -X POST -d token=eCq-YabAQVSt-YKdxWLiJG:APA91bFPjaGoal3Pj4MZRTRYQnAM8UrZjPx5QHToH65idO8K-IzaVj1DiKNFhD7eJPcfynBOYrHBDUv5FtrF7kMTtXMF8JcjOAsYQrX7AdofZ4J0A5tnywTmGux6HsrrKfsvIaQGUFe8 -d title=Alma -d message=nincs https://us-central1-webapprunner-1093.cloudfunctions.net/sendPushNotification

```

Test message:

```js
var message = {
  notification: {
    title: "FCM Message",
    body: "This is an FCM Message"
  },
  token: 'eCq-YabAQVSt-YKdxWLiJG:APA91bFPjaGoal3Pj4MZRTRYQnAM8UrZjPx5QHToH65idO8K-IzaVj1DiKNFhD7eJPcfynBOYrHBDUv5FtrF7kMTtXMF8JcjOAsYQrX7AdofZ4J0A5tnywTmGux6HsrrKfsvIaQGUFe8'
};
```