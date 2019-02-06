# Meepo
[![Build Status](https://travis-ci.com/nekocode/Meepo.svg?branch=master)](https://travis-ci.com/nekocode/Meepo) [![codecov](https://codecov.io/gh/nekocode/Meepo/branch/master/graph/badge.svg)](https://codecov.io/gh/nekocode/Meepo)

Meepo is a router generator for android, similar to **[retrofit](https://github.com/square/retrofit)**. You can use it to create routers for Activities, Fragments and even any things.


### Integration

Replace the `${last-version}` in below code to number [![Release](https://jitpack.io/v/nekocode/Meepo.svg)](https://jitpack.io/#nekocode/Meepo) .

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation "com.github.nekocode:Meepo:${last-version}"
}
```


## Usage

Firstly, declare a router interface:

```java
public interface Router {
    @Path("user/{user_id}/detail")
    @RequestCode(1)
    boolean gotoUserDetail(Context context, @PathParam("user_id") String userId, 
                           @QueryParam("show_title") boolean showTitle);

    @Clazz(StoreActivity.class)
    @Flags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(Context context, @BundleParam("title") String title,
               @RequestCodeParam int requestCode);
}
```

If you want to open your Activity via URI, you need to add a uri intent filter to the corresponding `<activity>` element in the manifest file:

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

And then obtain an implementation of your router interface:

```java
final Meepo meepo = new Meepo.Builder()
        .config(new UriConfig().scheme("meepo").host("meepo.com"))
        .build();

final Router router = meepo.create(Router.class);
```

Now, you can invoke the router's navigation methods:

```java
boolean isSucess = router.gotoUserDetail(this, "123", true);
```


## Router Annotation

This library currently provides the following annotations:

| Method Annotation | Description |
| :----- | :------ |
| `@Clazz` | Class of target Activity  |
| `@ClazzName` | Class name of target Activity |
| `@Path` | URI path and mime type of target Activity |
| `@Action` | Action of Intent |
| `@Flags` | Flags of Intent |
| `@RequestCode` | Request code of Intent |

| Parameter Annotation | Description |
| :----- | :------ |
| `@BundleParam` | Parameter that will be put into the extra of Intent |
| `@PathParam` | Parameter that will be used to replace the URI path's corresponding replacement block |
| `@QueryParam` | Query parameter of the URI |
| `@QueryMapParam` | Map of Query parameters |
| `@RequestCodeParam` | Request code of Intent |


## Custom Parser and CallAdapter

You can create custom Parser and CallAdapter. You can reference the **[sample](sample/src/main/java/cn/nekocode/meepo/sample/custom)** module. It means that you can create router for anything.

```java
final ModuleRouter moduleRouter = new Meepo.Builder()
        .config(new ModuleConfig("TEST"))
        .parser(new ModuleParser())
        .adapter(new ModuleCallAdapter())
        .build()
        .create(ModuleRouter.class);
```
