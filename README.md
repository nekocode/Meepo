![Banner](art/Banner.jpg)

# Meepo
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Release](https://jitpack.io/v/nekocode/Meepo.svg)](https://jitpack.io/#nekocode/Meepo)

Meepo 使用和 **[Retrofit](https://github.com/square/retrofit)** 类似的形式来组织跳转行为。你可以用它来实现 Activity、Fragment 甚至任意组件的跳转路由。


### 使用简介

定义你的路由接口:

```java
public interface Router {
    @TargetPath("user/{user_id}/detail")
    boolean gotoUserDetail(Context context, @Path("user_id") String userId, @Query("show_title") boolean showTitle);

    @TargetClass(StoreActivity.class)
    @TargetFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(Context context, @Bundle("title") String title);
}
```

如果你想使用 Uri 来打开你的 Activity 的话，你还需要在 `AndroidManifest.xml` 文件下为你的目标 Activity 定义对应的 `<intent-filter>`:

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

接下来使用 Meepo 来为你的路由接口生成具体实现了:

```java
final Meepo meepo = new Meepo.Builder()
        .config(new UriConfig().scheme("meepo").host("meepo.com"))
        .build();

final Router router = meepo.create(Router.class);
```

现在你就可以使用 `router` 提供的接口来取代 `startActivity()` 了:

```java
boolean isSucess = router.gotoUserDetail(this, "123", true);
```


### 拓展

Meepo 并不局限于实现 Activity 的跳转，它提供很强的可拓展能力，你可以为它创建新的路由注解、解析器、以及跳转适配器。在示例代码的 **[custom](sample/src/main/java/cn/nekocode/meepo/sample/custom)** 目录下有一个自定义跳转适配器例子。

然后你可以构建 Meepo 的时候使用 `parser()` 和 `adapter()` 来自定义解析器、以及跳转适配器:

```java
final ModuleRouter moduleRouter = new Meepo.Builder()
        .config(new ModuleConfig("TEST"))
        .adapter(new GotoModuleAdapter())
        .build()
        .create(ModuleRouter.class);
```


### 集成

- 在根目录的 `build.gradle` 下添加 Jitpack 仓库:

```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

- 添加库依赖:

```
dependencies {
    compile 'com.github.nekocode:Meepo:{lastest-version}'
}
```