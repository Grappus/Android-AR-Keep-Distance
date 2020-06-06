# android-ar-keep-distance
AR library to detect pedestrian proximity walking near by

## How to add the gradle dependency
1. Add the `JitPack` repository to your root `build.gradle` at the end of repositories
   ```
   allprojects {
   		repositories {
   			...
   			maven { url 'https://jitpack.io' }
   		}
   	}
   ```
2. Add the gradle dependency to you app `build.gradle` inside `dependencies`
   ```
   dependencies {
   	    implementation 'com.github.Grappus:android-ar-keep-distance:<version>'
   }
   ```

## How to integrate
1. In your app `build.gradle`, add `java` compatibility & enable `data binding`
   ```
   compileOptions {
           sourceCompatibility JavaVersion.VERSION_1_8
           targetCompatibility JavaVersion.VERSION_1_8
   }
   viewBinding {
        enabled = true
   }
   dataBinding {
        enabled = true
   }
   ```
2. Initialize the libray `Prefs` class using it's `init` function from `app` application class.
   ```
   class SampleApplication : Application() {
     override fun onCreate() {
        super.onCreate()
        Prefs.init(applicationContext)
     }
   }
   ```
3. Start the `ARActivity` from wherever you want to launch the `AR` flow
   ```
   class SampleActivity : FragmentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        initComponents()
     }

     private fun initComponents() {
        startActivity(Intent(this, ARActivity::class.java))
        finish()
     }
   }
   ```
4. `ARActivity` should handle the rest of the flow
