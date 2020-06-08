# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.

-keepnames class android.tesseract.jio.covid19.ar.*
-keepclassmembers class android.tesseract.jio.covid19.ar.** { *; }
-keep class * extends androidx.fragment.app.Fragment{}
-keep class {}
-keepclassmembers class com.google.ar.core.** { *; }
-keep class org.tensorflow.** { *; }
-keep class androidx.navigation.fragment.NavHostFragment
-keep class androidx.fragment.app.FragmentContainerView