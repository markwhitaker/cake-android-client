I've followed the instructions in the project's original README.txt file. I've also tried to limit
the time I spent on it to a few hours as indicated.

Given more time and a freer rein, here are some additional changes I would consider making.

### Third-party libraries

Third-party libraries can be a double-edged sword: they help us avoid reinventing the wheel, but at
the expense of increasing the app's DEX method count and surrendering maintenance responsibility to
a third party that may not be reliable. They can also come with a steep learning curve and
insufficient documentation.

If I were free to use third-party libraries for this app I would have liked to use **Retrofit** and
**Gson** for the API layer, and **Picasso** for loading the thumbnail images.



### App architecture and testability

The current architecture is OK for such a trivial app, but it doesn't scale well and isn't very
suitable for automated testing, both of which would be a concern once the app grows to include
some more views and view transition logic.

I'd improve on this as follows:

* Move as much logic as possible out of the Android framework classes (Activities and Fragments),
  e.g. by introducing an architecture such as MVP, MVVM or VIPER.  
* Refactor non-mocked Android classes (such as `Log` and `TextUtils`) to replace them with mockable
  alternatives or wrappers.
* Add non-Android unit tests using JUnit and Mockito. 
* Optionally, add Android unit tests using Espresso.
