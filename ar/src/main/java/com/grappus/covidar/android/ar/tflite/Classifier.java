package com.grappus.covidar.android.ar.tflite;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

/**
 * Generic interface for interacting with different recognition engines.
 */
public interface Classifier {
  /**
   * An immutable result returned by a Classifier describing what was recognized.
   */
  public class Recognition {
    /**
     * A unique identifier for what has been recognized. Specific to the class, not the instance of
     * the object.
     */
    private final String id;

    /**
     * Display name for the recognition.
     */
    private final String title;

    /**
     * A sortable score for how good the recognition is relative to others. Higher should be better.
     */
    private Float confidence;

    private Paint paint;

    /** Optional location within the source image for the location of the recognized object. */
    private RectF location;

    Recognition(
            final String id, final String title, final Float confidence, Paint paint, final RectF location) {
      this.id = id;
      this.title = title;
      this.confidence = confidence;
      this.paint = paint;
      this.location = location;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public Float getConfidence() {
      return confidence;
    }

    public Paint getPaint() {
      return paint;
    }

    public void setPaint(Paint paint) {
      this.paint = paint;
    }

    public RectF getLocation() {
      return new RectF(location);
    }

    public void setLocation(RectF location) {
      this.location = location;
    }

    @Override
    public String toString() {
      String resultString = "";
      if (id != null) {
        resultString += "[" + id + "] ";
      }

      if (title != null) {
        resultString += title + " ";
      }

      if (confidence != null) {
        resultString += String.format("(%.1f%%) ", confidence * 100.0f);
      }

      if (location != null) {
        resultString += location + " ";
      }

      return resultString.trim();
    }
  }

  List<Recognition> recognizeImage(Bitmap bitmap);

  void enableStatLogging(final boolean debug);

  String getStatString();

  void close();
}
