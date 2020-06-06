# android-ar-keep-distance
AR library to detect pedestrian proximity walking near by

## How to integrate
1. Enable data binding in `app` module
   ```
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
