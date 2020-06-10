# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.

#-keepnames class android.tesseract.jio.covid19.ar.*
#-keep class android.tesseract.jio.covid19.ar.*
#-keepclassmembers class android.tesseract.jio.covid19.ar.** { *; }

-keepclassmembers class com.google.ar.core.** { *; }
-keep class org.tensorflow.** { *; }

# Add any project specific keep options here:

##---------------Begin: settings, recommended for libraries (https://www.guardsquare.com/en/proguard/manual/examples#library):
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames, includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
##---------------End: settings, recommended for libraries
