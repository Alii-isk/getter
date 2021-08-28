# Getter
Made flutter easier.


# Description

Flutter plugin you can through it get images, videos, or audios from the local storage.


[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Release Status](https://github.com/flutter/packages/actions/workflows/release.yml/badge.svg)](https://github.com/nesyou01/getter)

## Dependencies

Before using Getter you need first to add this line to your ``AndroidManifest.xml``

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

and accept app to use storage.

## Using

To use this plugin all you have to do just implement this code below

```dart
List<Media> data = await Getter.get(type: GetterType.audios);

```
change the type by what you need, you can chose between ``GetterType.videos``,``GetterType.audios``,``GetterType.images``, or ``GetterType.all``

## Issues

Please file any issues, bugs, or feature requests in the [main getter
repo](https://github.com/nesyou01/getter/issues/new).

