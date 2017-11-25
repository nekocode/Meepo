# Meepo
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Release](https://jitpack.io/v/nekocode/Meepo.svg)](https://jitpack.io/#nekocode/Meepo)

Meepo is a router generator for android, similar to **[retrofit](https://github.com/square/retrofit)**. You can use it to create routers for Activities, Fragments and even any things.


### Install

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.nekocode:Meepo:{lastest-version}'
}
```


## Usage

Declare the router interface first and then Meepo turns your navigation methods into a Java interface.

```java
public interface Router {
    @TargetPath("user/{user_id}/detail")
    boolean gotoUserDetail(Context context, @Path("user_id") String userId, 
                           @Query("show_title") boolean showTitle);

    @TargetClass(StoreActivity.class)
    @TargetFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(Context context, @Bundle("title") String title);
}
```

If you want to use URI to open your Activity, you need to add an `<intent-filter>` element in your manifest file to the corresponding `<activity>` element.

```xml
<activity android:name=".UserDetailActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>

        <data
            android:host="meepo.com"
            android:pathPattern="/user/.*/detail"
            android:scheme="meepo"/>
    </intent-filter>
</activity>
```

Use the `Meepo` class to build an implementation for your router interface.

```java
final Meepo meepo = new Meepo.Builder()
        .config(new UriConfig().scheme("meepo").host("meepo.com"))
        .build();

final Router router = meepo.create(Router.class);
```

Now, you can use the `router`'s methods to navigate activity instead of `startActivity()` directly.

```java
boolean isSucess = router.gotoUserDetail(this, "123", true);
```


## Router Annotation

Meepo supports below router annotations currently:

| Annotation | Description |
| :----- | :------ |
| `@TargetClass` | Declare the target Class (Such as target Activity or Fragment) |
| `@TargetClassName` | Declare the target Class name |
| `@TargetPath` | Declare the path of URI path (and MimeType) |
| `@TargetAction` | Declare the Intent action |
| `@TargetFlags` | Declare the Intent flags |
| `@Bundle` | Put data into the Intent's Bundle |
| `@Path` | Replace the URI path's corresponding replacement block with string parameter |
| `@Query` | Query parameter of the URI |
| `@QueryMap` | Map of Query parameters |
| `@RequestCode` | Request code for `startActivityForResult()` |


## Custom Parser and CallAdapter

You can create custom Parser and CallAdapter for Meepo. See the **[sample](sample/src/main/java/cn/nekocode/meepo/sample/custom)** for more details. It means that you have the ability to make router for anything.

```java
final ModuleRouter moduleRouter = new Meepo.Builder()
        .config(new ModuleConfig("TEST"))
        .parser(new ModuleParser())
        .adapter(new ModuleCallAdapter())
        .build()
        .create(ModuleRouter.class);
```
