# Android Programming Test - Solution

## Countries List
An Android app which fetches list of countries and shows it's USD exchange rate.

<p align="center">
<img src="screenshots/countrieslist.gif"width=" 266" height="569"/>
</p>

---

## Instructions to run the project
- Make sure to download all the dependencies included in the project.
Since, I've used Android Studio Canary 6 to build the project, it is required to have **Android Gradle Plugin version 8.0.0** or above.

- You can directly run the app by installing the apk named "countries-list-1.0.0-debug.apk" in "output" folder.

## 📸 Screenshots ##
<div class="column">
  <img src="screenshots\countrylistnocurrencydark.png" width="266" height="569" />
  <img src="screenshots\countrylistnocurrencylight.png" width="266" height="569" />
  <img src="screenshots\countrylistnocurrencylight-de.png" width="266" height="569" />
  <img src="screenshots\countrylistnocurrencydark-de.png" width="266" height="569" />
  <img src="screenshots\countrydetailslight.png" width="266" height="569" />
  <img src="screenshots\countrydetailsdark-de.png" width="266" height="569" />
  <img src="screenshots\countrydetailsnocurrencylight.png" width="266" height="569" />
  <img src="screenshots\countrydetailsnocurrencydark-de.png" width="266" height="569" />
</div>

---

## Notes and Implementation Details
- **Architecture pattern decision.** I chose MVVM architecture with use cases and used a multi-module approach for better scalability, testability, and separation of concerns. 
- I used typical libraries such as Coil for image loading, Hilt for DI, Jetpack Navigation for navigating and passing data between screens
- Testing strategy and coverage details. Both UI and unit tests have been covered with 100% coverage for unit tests. Check folder "coverageReport" for more details.

# Trade-offs #
- Since, it's a limited list of countries, it can further be improved by using a database to cache the data.
- Used two separate viewmodels for currency and exchange. A Shared ViewModel could also be used by maintaining and passing an ID of the "Country" instead of a big Json "Country" string, which I've used in the project. Since it's a limited and fixed list of countries, I chose this approach.