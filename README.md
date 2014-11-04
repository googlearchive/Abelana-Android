#Abelana Android client

Abelana (means "Share" in Zulu) is a project that allows users to take photos and share them with their friends. This project is the Android client. The app is currently designed for Android API levels 17+ for use primarily on mobile phones. Supporting for Material Design is currently in development.

The Android app was written by Zafir Khan and Les Vogel. For questions and comments, please join the [Google Group](https://groups.google.com/forum/#!forum/abelana-app).

Disclaimer: This application is for educational purposes only and is not a Google product or service.

#Features
* Connects to Cloud Platform backend featuring CRUD (create/read/update/delete) capabilities.
* Displays stream of images.
* Allows users to upvote images.
* Allows users to take photos or choose photos from the gallery to send to Google [Cloud Storage](https://cloud.google.com/storage/).
* Multi-platform sign-in using Google [Identity Toolkit](https://developers.google.com/identity-toolkit/), including Facebook and Yahoo sign-in.
* Network image loading using [Picasso](http://square.github.io/picasso/) from Square.
* REST APIs using [Retrofit](http://square.github.io/retrofit/).
* Android UI elements - navigation drawer, ListViews, and GridViews, all with custom adapters.
* Custom UI elements - Bezel ImageViews, square ImageViews.
* State-based layouts.

# Project setup
* You can import this project into Android Studio.  
* To generate your own private keys to access your own servers, see [Abelana-GCP](https://github.com/GoogleCloudPlatform/Abelana-gcp).

## Contributing changes

* See [CONTRIB.md](CONTRIB.md)


## Licensing

* See [LICENSE](LICENSE)
