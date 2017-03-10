Team 4 - FilmFridge
Simon DeMartini & Samantha Ong

FilmFridge is an app designed to recommend movie with just limited work, and limited spoilers. When
testing this app, feel free to register as a new user so that your threshold settings can be saved
to our server and used on any device or after re-installing the app. The user stories we have
implemented are all marked with an (x) below.

Phase II Requirements
    1. Address any bugs mentioned in Phase I:
        - All methods now have javadoc

    2. User stories implemented:
        (x) As a user I can choose the thresholds for ratings of the movies suggested to me.
        (x) As a user I can choose the genre of the movies suggested to me.
        ( ) As a user I can view which movie database ratings the app will compare.
            - The Movie database we used only provided one source, and APIs like Rotten Tomatoes and
              IMDb are stupidly expensive
        ( ) As a user I want to show or hide movie trailers.
            - Similarly, the API we used did not provide links to videos
        (x) As a user I can log in.
        (x) As a user I can save a recommendation to my "movies to watch" list.
        ( ) As a user I can choose which information about the movie is shown to me such as: summary,
            genre, trailer link, etc.
            - Did not get to this
        (x) As a user I can hide/remove a recommendation.
        (x) As a user I can remove a movie from my "movies to watch"list.
        (x) As a user I can see a list of customized movie recommendations.
        (x) As a user I can view my account settings.
        (x) As a user I can log out.
        (x) As a user the settings on my account will carry to other devices.
        (x) As a user I want to see the movie posters with my recommendations
        (x) As a user I can create an account
        (x) As a user I can see the details of a specific movie recommended to me
        (x) As a user I want to share recommended movies with a friend.
        (x) As a user I want to reveal the actual score of a recommended movie

    3. Use two types of device storage
        We saved movie posters to the app's filesystem in the getCacheDir(), and we use SharePreferences
        to store details like movie thresholds, user account info, and my movie list.

    4. Your app must use web services to be functional & no dummy data
        Our movie information is sourced from TheMovieDatabase (https://www.themoviedb.org/), and we
        require a user account from our own server that stores your email, password, and syncs your
        movie recommendation thresholds.

    5. We used Content sharing when you are viewing details about a film. You can hit the share button
        on the toolbar to share the name of the movie to a friend.

    6. Custom sign-in.
        Yes, this is working and also saves the movie recommendation thresholds on the server too.

    7. Use graphics to display images
        We made a custom logo that we use as the app icon, and we also download and cache the movie
        posters.

    8. Write at least 1 JUnit class and 1 Instrumentation test.
        We added JUnit tests for out Film class, and an Instrumentation test for the RegisterFragment

