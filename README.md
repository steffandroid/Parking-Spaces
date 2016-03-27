# Parking Spaces
View the nearest car parking spaces to you in real-time. In Bristol and Bath.

Have you ever found yourself scouring the streets of Bristol or Bath for that rare specimen - an empty space to park your car? Well, search no more! With Parking Spaces you can view real-time information about the availability of spaces nearest to you.

# Build Instructions
In order to build the application, you'll need to add a couple of API keys:
 * Register for a Bristol Transport API key on [the UrbanThings portal](https://portal-bristol.api.urbanthings.io/#/signup) and set [`URBAN_THINGS_API_KEY` in ApiModule](app/src/main/java/uk/co/steffandroid/parking/data/api/ApiModule.java) to the key they email to you.
 * Register for a Google Maps Android API key in [the Google developers console](https://console.developers.google.com/) and assign this key to the `android:value` attribute in [`<meta-data android:name="com.google.android.geo.API_KEY" android:value=""/>` in AndroidManifest.xml](app/src/main/AndroidManifest.xml).
  * Note that in order to register a Google Maps key you'll need to refactor the application package name from `uk.co.steffandroid.parking` to something else.
