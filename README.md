Test project for Sky

Android Studio Flamingo | 2022.2.1 Patch 2
JDK: corretto-17

I made the design the same as suggested in the task. Of course, it could have been improved, for example, by adding 'back' buttons or a 'read more' option on the text in the list. I decided to keep the design that was suggested but filled it with my own test data. I generated articles about cats.

Edge-to-edge app.
DI - Hilt.
UI - Jetpack Compose.
For architecture, I chose Clean Architecture with the application of the MVVM pattern.

I also included a networking layer. If the server is operational, it would be possible to simply turn on real data return from the server instead of mock data. I also use Result<T> for returning data from sources, with CallAdapter and CallAdapterFactory present for this.

I use a repository, because it allows for data caching using, let's say, Room, and the repository will manage the return of data. Also, I didn't implement data caching, but I kept it in mind.

I wrote a couple of unit tests for SkyNewsViewModel and SkyStoryViewModel.

Also, I apologize for pushing this to GitHub in one commit, rather than with history. You probably would have liked to see that.

Demo video:

https://github.com/Krobys/SkyTest/assets/48987746/84969698-186f-447e-9949-0fcb292bc419

