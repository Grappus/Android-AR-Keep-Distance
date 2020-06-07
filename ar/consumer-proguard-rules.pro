# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.

-keepclassmembers class com.google.ar.core.** { *; }
-keepclassmembers class android.tesseract.jio.covid19.ar.** { *; }
-keep class org.tensorflow.** { *; }