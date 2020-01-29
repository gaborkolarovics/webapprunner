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

```
