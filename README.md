I've followed the instructions in the project's original README.txt file. I've also tried to limit
the time I spent on it to a few hours as indicated. In summary, I've done the following:

* Fixed the defects in the original project, including making the HTTP requests resilient to
  redirects
* Tidied and refactored the code for better readability and maintainability
* Hooked up the dummy Refresh menu item so it does refresh the data
* Improved the layout files and remove unnecessary nesting
* Added a progress indicator when data is loading, and an error message when the data cannot be
  retrieved

As the brief didn't ask for tests I haven't implemented any, but I have removed static methods as a
first step towards more testable, mockable code. See below for more comments on testing.

### Third-party libraries

Third-party libraries are a double-edged sword: they help us avoid reinventing the wheel, but at
the expense of increasing the app's method count and surrendering maintenance responsibility to
a third party that may not be dependable. Some also come with a steep learning curve and insufficient
documentation. I always look at the balance of benefit vs. risk before using them.

In this case, HTTP code is difficult to implement and maintain (my code still doesn't handle retries
for example), so I'd add the well-supported libraries **Retrofit**, **Gson** and **Picasso** for
doing the HTTP work.

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
