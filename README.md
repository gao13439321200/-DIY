
MVP
Dagger2
Retrofit2
ButterKnife
SwipeMenuRecyclerView

在工程中的gradle中配置
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'      //这一行

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

