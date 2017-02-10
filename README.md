# window-wordcount-streams

Displaying top tweeting users from sample twitter stream in fixed-tweets-number window.

![alt tag](https://github.com/bartekkalinka/window-wordcount-streams/blob/master/screenshot.png)

## Setup ##

Log in to twitter, go to https://apps.twitter.com/.

Create an application, copy consumer key and consumer secret.

Create access token for your account, copy access token and access token secret.

Copy `src/main/resources/application.conf.template` to `src/main/resources/application.conf` and fill in above keys/tokens/secrets.

Run with `sbt "run twitter web"`

Open `localhost:9000/index.html` in browser

Optionally, specify window size, like this: `sbt "run twitter web 20000"` (default is 5000)
