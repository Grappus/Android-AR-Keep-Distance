# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.

#-keepparameternames
#-renamesourcefileattribute SourceFile
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.
-keepattributes *Annotation*

# Preserve all public classes, and their public and protected fields and methods.
#-keep public class * {public protected *;}

# Preserve all .class method names.
#-keepclassmembernames class * {
#    java.lang.Class class$(java.lang.String);
#    java.lang.Class class$(java.lang.String, boolean);
#}

# Preserve all native method names and the names of their classes.
#-keepclasseswithmembernames class * {native <methods>;}

# Preserve the special static methods that are required in all enumeration classes.
#-keepclassmembers class * extends java.lang.Enum {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

# Kotlin
#-keep class kotlin.Unit { *; }
#-keep class kotlin.collections.CollectionsKt { *; }


# Your library may contain more items that need to be preserved;

#-keep class android.tesseract.jio.covid19.ar.ARActivity { *; }

#-keepnames class android.tesseract.jio.covid19.ar.*
#-keep class android.tesseract.jio.covid19.ar.*

#-keepclassmembers class android.tesseract.jio.covid19.ar.** { *; }

#-keep class * extends androidx.databinding.ViewDataBinding { *; }
#-keep class * extends androidx.databinding.DataBinderMapper { *; }
#-keep class android.tesseract.jio.covid19.ar.DataBinderMapperImpl { *; }
#-keep class android.tesseract.jio.covid19.ar.databinding.** { *; }
#-dontwarn androidx.databinding.**
#-keep class androidx.databinding.** { *; }

#-keepclassmembers class com.google.ar.core.** { *; }
#-keep class org.tensorflow.** { *; }

-keep class androidx.navigation.fragment.NavHostFragment
#-keep class androidx.fragment.app.FragmentContainerView
#-keep class * extends androidx.fragment.app.Fragment{}

-keepnames class android.tesseract.jio.covid19.ar.splash.SplashFragment