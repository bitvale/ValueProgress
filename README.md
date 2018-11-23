# ValueProgress

<img src="/art/preview.gif" alt="sample" title="sample" width="320" height="600" align="right" vspace="52" />

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

Android value progress library. 

USAGE
-----

Just add ValueProgress view in your layout XML and ValueProgress library in your project via Gradle:

```gradle
dependencies {
  implementation 'com.bitvale:valueprogress:1.0.0'
}
```

XML
-----

```xml
<com.bitvale.valueprogress.ValueProgress
    android:id="@+id/pac_button"
    android:layout_width="@dimen/progress_size"
    android:layout_height="@dimen/progress_size"
    app:progress_width="@dimen/progress_width"
    app:progress_text_color="@color/text_color"
    app:progress_color="@color/progress_color"
    app:progress_max_value="@integer/max_value"
    app:progress_value_symbol="%" />
```

You must use the following properties in your XML to change your ValueProgress.


##### Properties:

* `app:progress_color`                (drawable)  -> default  #52D99F
* `app:progress_width`                (drawable)  -> default  none
* `app:progress_text_size`            (dimension) -> default  none
* `app:progress_text_color`           (color)     -> default  #535353
* `app:progress_disabled_color`       (color)     -> default  #EDEDED
* `app:progress_shadow_color`         (color)     -> default  #CCCAC8C8
* `app:progress_background_color`     (color)     -> default  #FFFFFF
* `app:progress_max_value`            (float)     -> default  100
* `app:progress_value_symbol`         (string)    -> default  %


Kotlin
-----

The Progress will be animated after you set a percent value.

```kotlin
value_progress.progressMaxValue = 100
value_progress.percent = 25 // animation starts
```

LICENCE
-----

ValueProgress by [Alexander Kolpakov](https://play.google.com/store/apps/dev?id=7044571013168957413) is licensed under an [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).